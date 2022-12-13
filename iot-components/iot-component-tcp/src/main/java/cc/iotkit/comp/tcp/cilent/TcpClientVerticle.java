package cc.iotkit.comp.tcp.cilent;

import cc.iotkit.comp.IMessageHandler;
import cc.iotkit.comp.tcp.parser.ParserStrategyBuilder;
import cc.iotkit.converter.DeviceMessage;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetClientOptions;
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

    private Map<String, ClientDevice> deviceMap = new ConcurrentHashMap();

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
            options.setReconnectInterval(60000L);
            config.setOptions(options);
        }
        if (config.isSsl()) {
            // 证书
        }
    }

    private void initClient() {
        NetClient netClient = vertx.createNetClient(config.getOptions());
        tcpClient.setClient(netClient);
        tcpClient.setKeepAliveTimeoutMs(Duration.ofMinutes(10).toMillis());
        tcpClient.onDisconnect(() -> {
            // 所有设备都离线
            for (String deviceName : deviceMap.keySet()) {
                // 发送离线消息
                executor.onReceive(null, "disconnect", deviceName);
            }
        });
        netClient.connect(config.getPort(), config.getHost(), result -> {
            if (result.succeeded()) {
                log.debug("connect tcp [{}:{}] success", config.getHost(), config.getPort());
                tcpClient.setRecordParser(ParserStrategyBuilder.build(config.getParserType(), config.getParserConfiguration()));
                tcpClient.setSocket(result.result());
            } else {
                log.error("connect tcp [{}:{}] error", config.getHost(), config.getPort(), result.cause());
            }
        });
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

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class ClientDevice {
        private String deviceName = "";
        private String productKey = "";
    }
}
