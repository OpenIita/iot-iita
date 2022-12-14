package cc.iotkit.comp.tcp.cilent;

import cc.iotkit.comp.IMessageHandler;
import cc.iotkit.comp.tcp.parser.ParserStrategyBuilder;
import cc.iotkit.converter.DeviceMessage;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetClientOptions;
import io.vertx.core.net.NetSocket;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author huangwenlong
 * @version 1.0
 * @date 2022/10/23 13:08
 */
@Slf4j
public class TcpClientVerticle extends AbstractVerticle {

    private TcpClinetConfig config;

    private IMessageHandler executor;

    private VertxTcpClient tcpClient;

    private NetClient netClient;

    private Map<String, ClientDevice> deviceMap = new ConcurrentHashMap();

    private boolean stopAction = false;

    public TcpClientVerticle(TcpClinetConfig config) {
        this.config = config;
    }

    public void setExecutor(IMessageHandler executor) {
        this.executor = executor;
    }

    @Override
    public void start() {
        tcpClient = new VertxTcpClient(UUID.randomUUID().toString(), false);
        initConfig();
        initClient();
    }

    @Override
    public void stop() {
        if (null != tcpClient) {
            stopAction = true;
            tcpClient.shutdown();
        }
    }

    public void sendMsg(DeviceMessage msg) {
        if (tcpClient != null) {
            tcpClient.sendMessage(Buffer.buffer(msg.getContent().toString()));
        }
    }

    public void offlineDevice(String deviceName) {
        ClientDevice remove = deviceMap.remove(deviceName);
    }

    /**
     * 创建配置文件
     * 未链接成功就一直重连（每隔1分钟）
     */
    public void initConfig() {
        if (config.getOptions() == null) {
            NetClientOptions options = new NetClientOptions();
            options.setReconnectAttempts(Integer.MAX_VALUE);
            options.setReconnectInterval(20000L);
            config.setOptions(options);
        }
        if (config.isSsl()) {
            // 证书
        }
    }

    private void initClient() {
        netClient = vertx.createNetClient(config.getOptions());
        tcpClient.setKeepAliveTimeoutMs(Duration.ofMinutes(10).toMillis());
        tcpClient.onDisconnect(() -> {
            // 所有设备都离线
            for (String deviceName : deviceMap.keySet()) {
                // 发送离线消息
                executor.onReceive(null, "disconnect", deviceName);
            }
        });
        // 连接
        toConnection();
        // 设置收到消息处理
        tcpClient.setReceiveHandler(buffer -> {
            try {
                executor.onReceive(null, "", buffer.toString(),
                        result -> {
                            if (!deviceMap.containsKey(result.getDeviceName())) {
                                deviceMap.put(result.getDeviceName(), new ClientDevice(result.getDeviceName(), result.getProductKey()));
                                // 有些设备并没有连接时报文，所以模拟一次 online
                                HashMap<String, Object> map = new HashMap<>();
                                map.put("deviceName", result.getDeviceName());
                                executor.onReceive(map, "connect", buffer.toString());
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void toConnection() {
        netClient.connect(config.getPort(), config.getHost(), result -> {
            if (result.succeeded()) {
                log.debug("connect tcp [{}:{}] success", config.getHost(), config.getPort());
                tcpClient.setRecordParser(ParserStrategyBuilder.build(config.getParserType(), config.getParserConfiguration()));
                NetSocket socket = result.result();
                tcpClient.setSocket(socket);
                socket.closeHandler((nil) -> {
                    tcpClient.shutdown();
                    // 重连自动断开重连，收到停止组件不重连
                    try {
                        if (!stopAction) {
                            Thread.sleep(5000L);
                            toConnection();
                        }else{
                            netClient.close();
                            netClient = null;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            } else {
                log.error("connect tcp [{}:{}] error", config.getHost(), config.getPort(), result.cause());
            }
        });
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class ClientDevice {
        private String deviceName = "";
        private String productKey = "";
    }
}
