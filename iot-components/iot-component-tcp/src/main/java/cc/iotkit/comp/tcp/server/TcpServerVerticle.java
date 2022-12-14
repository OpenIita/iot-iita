package cc.iotkit.comp.tcp.server;


import cc.iotkit.comp.IMessageHandler;
import cc.iotkit.comp.tcp.cilent.VertxTcpClient;
import cc.iotkit.comp.tcp.parser.ParserStrategyBuilder;
import cc.iotkit.comp.tcp.parser.PayloadParser;
import cc.iotkit.converter.DeviceMessage;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetServerOptions;
import io.vertx.core.net.NetSocket;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author huangwenl
 * @date 2022-10-13
 */
@Slf4j
public class TcpServerVerticle extends AbstractVerticle {

    @Getter
    private TcpServerConfig config;

    private IMessageHandler executor;

    private VertxTcpServer tcpServer;

    private String id;

    private Map<String, VertxTcpClient> clientMap = new ConcurrentHashMap();

    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;

    @Setter
    private long keepAliveTimeout = Duration.ofMinutes(10).toMillis();

    private Collection<NetServer> tcpServers;

    public TcpServerVerticle(TcpServerConfig config) {
        this.config = config;
    }

    public void setExecutor(IMessageHandler executor) {
        this.executor = executor;
    }

    @Override
    public void start() {
        tcpServer = new VertxTcpServer();
        initConfig();
        initTcpServer();
        keepClientTask();
    }

    @Override
    public void stop() {
        tcpServer.shutdown();
        scheduledThreadPoolExecutor.shutdown();
    }

    /**
     * 创建配置文件
     */
    public void initConfig() {
        if (config.getOptions() == null) {
            config.setOptions(new NetServerOptions());
        }
        if (config.isSsl()) {
            // 证书
        }
    }


    /**
     * 初始TCP服务
     */
    private void initTcpServer() {
        int instance = Math.max(2, config.getInstance());
        List<NetServer> instances = new ArrayList<>(instance);
        for (int i = 0; i < instance; i++) {
            instances.add(vertx.createNetServer(config.getOptions()));
        }
        // 根据解析类型配置数据解析器
        tcpServer.setParserSupplier(() -> ParserStrategyBuilder.build(config.getParserType(), config.getParserConfiguration()));
        tcpServer.setServer(instances);
        // 针对JVM做的多路复用优化
        // 多个server listen同一个端口，每个client连接的时候vertx会分配
        // 一个connection只能在一个server中处理
        for (NetServer netServer : instances) {
            netServer.listen(config.createSocketAddress(), result -> {
                if (result.succeeded()) {
                    log.info("tcp server startup on {}", result.result().actualPort());
                } else {
                    log.error("startup tcp server error", result.cause());
                }
            });
        }
    }

    public void offlineDevice(String deviceName) {
        VertxTcpClient client = clientMap.get(deviceName);
        if (client != null) {
            client.shutdown();
        }
    }

    public void onlineDevice(String deviceName, String parentName) {
        VertxTcpClient client = clientMap.get(deviceName);
        if (client != null) {
            client.setParentName(parentName);
        }
    }

    public void sendMsg(DeviceMessage msg) {
        VertxTcpClient tcpClient = clientMap.get(msg.getDeviceName());
        if (tcpClient != null) {
            tcpClient.sendMessage(Buffer.buffer(msg.getContent().toString()));
        }
    }

    /**
     * 保活定时任务
     */
    public void keepClientTask() {
        scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(2);

        scheduledThreadPoolExecutor.scheduleAtFixedRate(() -> {
            log.info("保活任务开始！");
            Set<String> clients = new HashSet(clientMap.keySet());
            for (String key : clients) {
                VertxTcpClient client = clientMap.get(key);
                if (!client.isOnline()) {
                    client.shutdown();
                }
            }
        }, 1000, keepAliveTimeout, TimeUnit.MILLISECONDS);
    }


    class VertxTcpServer {

        private Supplier<PayloadParser> parserSupplier;

        /**
         * 为每个NetServer添加connectHandler
         *
         * @param servers 创建的所有NetServer
         */
        public void setServer(Collection<NetServer> servers) {
            if (tcpServers != null && !tcpServers.isEmpty()) {
                shutdown();
            }
            tcpServers = servers;
            for (NetServer tcpServer : tcpServers) {
                tcpServer.connectHandler(this::acceptTcpConnection);
            }
        }

        /**
         * TCP连接处理逻辑
         *
         * @param socket socket
         */
        protected void acceptTcpConnection(NetSocket socket) {
            // 客户端连接处理
            String clientId = id + "_" + socket.remoteAddress();
            VertxTcpClient client = new VertxTcpClient(clientId, true);
            client.setKeepAliveTimeoutMs(keepAliveTimeout);
            try {
                // TCP异常和关闭处理
                socket.exceptionHandler(err -> {
                    log.error("tcp server client [{}] error", socket.remoteAddress(), err);
                }).closeHandler((nil) -> {
                    log.debug("tcp server client [{}] closed", socket.remoteAddress());
                    client.shutdown();
                });
                // 这个地方是在TCP服务初始化的时候设置的 parserSupplier
                client.setKeepAliveTimeoutMs(keepAliveTimeout);
                client.setRecordParser(parserSupplier.get());
                client.setSocket(socket);
                client.onDisconnect(() -> {
                    clientDisconnect(client.getDeviceName());
                });
                // 设置收到消息处理
                client.setReceiveHandler(buffer -> {
                    System.out.println(buffer.toString());
                    try {
                        executor.onReceive(null, "", buffer.toString(),
                                result -> {
                                    if (result != null && !clientMap.containsKey(result.getDeviceName())) {
                                        client.setDeviceInfo(result.getDeviceName(), result.getProductKey());
                                        clientMap.put(result.getDeviceName(), client);
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
                log.debug("accept tcp client [{}] connection", socket.remoteAddress());
            } catch (Exception e) {
                log.error("create tcp server client error", e);
                client.shutdown();
            }
        }

        public void setParserSupplier(Supplier<PayloadParser> parserSupplier) {
            this.parserSupplier = parserSupplier;
        }

        public void shutdown() {
            if (null != tcpServers) {
                for (NetServer tcpServer : tcpServers) {
                    execute(tcpServer::close);
                }
                tcpServers = null;
            }
        }

        private void execute(Runnable runnable) {
            try {
                runnable.run();
            } catch (Exception e) {
                log.warn("close tcp server error", e);
            }
        }

        /**
         * 断开连接,并移除子设备
         */
        private void clientDisconnect(String deviceName) {
            VertxTcpClient remove = clientMap.remove(deviceName);
            if (null != remove) {
                // 发送离线消息
                executor.onReceive(null, "disconnect", deviceName);
                // 移除子设备
                if (remove.hasParent()) {
                    List<VertxTcpClient> childClients = clientMap.values().stream().filter(cl -> cl.hasParent() && cl.getParentName()
                            .equals(remove.getParentName())).collect(Collectors.toList());
                    childClients.forEach(child -> clientMap.remove(child.getDeviceName()));
                }
            }
        }
    }
}
