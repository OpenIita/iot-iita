package cc.iotkit.comp.DLT645;


import cc.iotkit.common.utils.JsonUtils;
import cc.iotkit.comp.DLT645.analysis.*;
import cc.iotkit.comp.DLT645.utils.ByteUtils;
import cc.iotkit.comp.DLT645.utils.ContainerUtils;
import cc.iotkit.comp.IMessageHandler;
import cc.iotkit.comp.model.ReceiveResult;
import cc.iotkit.converter.DeviceMessage;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetServerOptions;
import io.vertx.core.net.NetSocket;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author tfd
 * @date 2023-04-07
 */
@Slf4j
public class DLT645Verticle extends AbstractVerticle {

    private IMessageHandler executor;

    private final DLT645Config config;

    private Map<String, NetSocket> clientMap = new ConcurrentHashMap();

    private NetServer netServer ;

    public DLT645Verticle(DLT645Config config) {
        this.config = config;
    }

    public void setExecutor(IMessageHandler executor) {
        this.executor = executor;
    }

    private List<DLT645Data> entityList;

    private Map<String, DLT645Data> dinMap;

    /**
     * DL/T 645-1997 自定义协议-67开头，DLT645-68开头
     * 注册上行：67+动作标识2B(注册：7267、心跳：6862)+设备唯一值6B
     * 注册下行：67+动作标识2B(注册：7267、心跳：6862)+状态码1B(00:成功、99：失败)
     * 心跳上行：67+动作标识2B(注册：7267、心跳：6862)
     * 心跳下行：67+动作标识2B(注册：7267、心跳：6862)+00
     */
    @Override
    public void start() {
        NetServerOptions options=new NetServerOptions().setPort(config.getPort());
        netServer=vertx.createNetServer(options);
        netServer.connectHandler(socket -> {
            log.info("TCP client connect address:{}", socket.remoteAddress());
            AtomicReference<String> clientKey = new AtomicReference<>();
            Map<String, Object> dataMap= new HashMap<>();
            // 设置连接超时时间
            long timeoutId = vertx.setTimer(10000, id -> {
                 socket.close();
            });
            // 处理连接
            socket.handler(data -> {
                String hexStr=ByteUtils.byteArrayToHexString(data.getBytes(),false);
                log.info("Received message:{}", hexStr);
                if(hexStr.startsWith("67")){//67打头为网关自定义协议
                    String funCode=hexStr.substring(2,6);
                    log.info("收到自定义消息，行为码为：" + funCode + "，数据为：" + hexStr);
                    if("7267".equals(funCode)){
                        String mac=hexStr.substring(6,18);
                        dataMap.put("mac",mac);
                        executor.onReceive(dataMap, "register", "",r ->{
                            if(r!=null){
                                //注册成功
                                clientKey.set(getClientKey(r));
                                if(!clientMap.containsKey(clientKey.get())){
                                    clientMap.put(clientKey.get(),socket);
                                }
                                vertx.cancelTimer(timeoutId);
                                executor.onReceive(dataMap, "online", "");
                                socket.write(hexStr+"00");
                                return;
                            }
                            socket.write(hexStr+"99");
                            return;
                        });
                    }else if("6862".equals(funCode)){//心跳
                        socket.write(hexStr+"00");
                        return;
                    }
                }else{//其他为电表协议
                    Map<String, Object> result = DLT645Analysis.unPackCmd2Map(ByteUtils.hexStringToByteArray(hexStr));
                    //获取功能码
                    Object func = result.get(DLT645Analysis.FUN);
                    DLT645FunCode funCode = DLT645FunCode.decodeEntity((byte) func);
                    if(funCode.isError()){
                        log.error("message erroe:{}", hexStr);
                        return;
                    }
                    //获取设备地址
                    byte[] adrrTmp = (byte[]) result.get(DLT645Analysis.ADR);
                    byte[] addr = new byte[6];
                    ByteUtils.byteInvertedOrder(adrrTmp,addr);
                    //获取数据
                    byte[] dat = (byte[]) result.get(DLT645Analysis.DAT);
                    DLT645V1997Data dataEntity = new DLT645V1997Data();
                    dataEntity.decodeValue(dat, dinMap);
                    Map<String, Object> unPack = new HashMap<>();
                    unPack.put("deviceAddress",ByteUtils.byteArrayToHexString(addr,false));
                    unPack.put("funCode",funCode.getCode());
                    unPack.put("identify",dataEntity.getKey());//数据标识
                    unPack.put("data",dataEntity.getValue()+dataEntity.getUnit());//数据+单位
                    executor.onReceive(new HashMap<>(), "dlt", JsonUtils.toJsonString(unPack));
                }
            });
            socket.closeHandler(res->{
                log.warn("TCP connection closed!");
                if(clientMap.containsKey(clientKey.get())){
                    executor.onReceive(dataMap, "offline", "");
                    clientMap.remove(clientKey.get());
                }
            });
            socket.exceptionHandler(res->{
                log.warn("TCP connection exception!");
                if(clientMap.containsKey(clientKey)){
                    executor.onReceive(dataMap, "offline", "");
                    clientMap.remove(clientKey.get());
                }
            });
        });
        netServer.listen(res -> {
            if (res.succeeded()) {
                log.info("TCP server start success!");
                DLT645v1997CsvLoader template = new DLT645v1997CsvLoader();
                entityList = template.loadCsvFile();
                dinMap = ContainerUtils.buildMapByKey(entityList, DLT645V1997Data::getKey);
            } else {
                log.error("TCP server start fail: " + res.cause());
            }
        });
    }

    @Override
    public void stop() {
        for (String clientKey : clientMap.keySet()) {
            Map<String,Object> dataMap=new HashMap<>();
            dataMap.put("mac",clientKey.split("_")[1]);
            executor.onReceive(dataMap, "offline", "");
        }
        if (!entityList.isEmpty()) {
            entityList.clear();
        }
        if (!dinMap.isEmpty()) {
            dinMap.clear();
        }
        clientMap.clear();
        netServer.close(voidAsyncResult -> log.info("close tcp server..."));
    }

    private String getClientKey(ReceiveResult result) {
        return getClientKey(result.getProductKey(), result.getDeviceName());
    }

    private String getClientKey(String productKey, String deviceName) {
        return String.format("%s_%s", productKey, deviceName);
    }

    public DeviceMessage sendMsg(DeviceMessage msg) {
        NetSocket client = clientMap.get(getClientKey(msg.getProductKey(),msg.getDeviceName()));
        log.info("send msg payload:{}", msg.getContent().toString());
        Future<Void> result = client.write(msg.getContent().toString());
        result.onFailure(e -> log.error("DLT645 server send msg failed", e));
        return msg;
    }

}
