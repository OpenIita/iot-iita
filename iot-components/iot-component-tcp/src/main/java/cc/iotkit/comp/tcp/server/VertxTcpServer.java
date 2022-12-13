package cc.iotkit.comp.tcp.server;//package cc.iotkit.comp.tcp.server;
//
//import cc.iotkit.comp.IMessageHandler;
//import cc.iotkit.comp.tcp.cilent.VertxTcpClient;
//import cc.iotkit.comp.tcp.parser.PayloadParser;
//import cc.iotkit.converter.DeviceMessage;
//import io.vertx.core.buffer.Buffer;
//import io.vertx.core.net.NetServer;
//import io.vertx.core.net.NetSocket;
//import lombok.Setter;
//import lombok.extern.slf4j.Slf4j;
//
//import java.time.Duration;
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.function.Supplier;
//import java.util.stream.Collectors;
//
///**
// * @author huangwenl
// * @date 2022-10-13
// */
//@Slf4j
//public class VertxTcpServer {
//
//    private String id;
//
//    private Supplier<PayloadParser> parserSupplier;
//
//    private Map<String, VertxTcpClient> clientMap = new ConcurrentHashMap();
//    private IMessageHandler executor;
//    private Collection<NetServer> tcpServers;
//    @Setter
//    private long keepAliveTimeout = Duration.ofMinutes(10).toMillis();
//
//    public VertxTcpServer(String id) {
//        this.id = id;
//    }
//
//    /**
//     * 为每个NetServer添加connectHandler
//     *
//     * @param servers 创建的所有NetServer
//     */
//    public void setServer(Collection<NetServer> servers) {
//        if (this.tcpServers != null && !this.tcpServers.isEmpty()) {
//            shutdown();
//        }
//        this.tcpServers = servers;
//
//        for (NetServer tcpServer : this.tcpServers) {
//            tcpServer.connectHandler(this::acceptTcpConnection);
//        }
//    }
//
//    /**
//     * TCP连接处理逻辑
//     *
//     * @param socket socket
//     */
//    protected void acceptTcpConnection(NetSocket socket) {
//        // 客户端连接处理
//        String clientId = id + "_" + socket.remoteAddress();
//        VertxTcpClient client = new VertxTcpClient(clientId, true);
//        client.setKeepAliveTimeoutMs(keepAliveTimeout);
//        try {
//            // TCP异常和关闭处理
//            socket.exceptionHandler(err -> {
//                log.error("tcp server client [{}] error", socket.remoteAddress(), err);
//            }).closeHandler((nil) -> {
//                log.debug("tcp server client [{}] closed", socket.remoteAddress());
//                client.shutdown();
//            });
//            // 这个地方是在TCP服务初始化的时候设置的 parserSupplier
//            client.setRecordParser(parserSupplier.get());
//            client.setSocket(socket);
//            client.onDisconnect(() -> {
//                clientDisconnect(client.getDeviceName());
//            });
//            // 设置收到消息处理
//            client.setReceiveHandler(buffer -> {
//                executor.onReceive(null, "", buffer.toString(),
//                        result -> {
//                            if (!clientMap.containsKey(result.getDeviceName())) {
//                                client.setDeviceInfo(result.getDeviceName(), result.getData().getParent().getDeviceName(),
//                                        result.getProductKey());
//                                clientMap.put(result.getDeviceName(), client);
//                                // 有些设备并没有连接时报文，所以模拟一次 online
//                                HashMap<String, Object> map = new HashMap<>();
//                                map.put("deviceName", result.getDeviceName());
//                                executor.onReceive(map, "online", "");
//                            }
//                        });
//            });
////            clientMap.put(clientId, client);
//            log.debug("accept tcp client [{}] connection", socket.remoteAddress());
//        } catch (Exception e) {
//            log.error("create tcp server client error", e);
//            client.shutdown();
//        }
//    }
//
//    public void setParserSupplier(Supplier<PayloadParser> parserSupplier) {
//        this.parserSupplier = parserSupplier;
//    }
//
//    public void shutdown() {
//        if (null != tcpServers) {
//            for (NetServer tcpServer : tcpServers) {
//                execute(tcpServer::close);
//            }
//            tcpServers = null;
//        }
//    }
//
//    private void execute(Runnable runnable) {
//        try {
//            runnable.run();
//        } catch (Exception e) {
//            log.warn("close tcp server error", e);
//        }
//    }
//
//
//    public void sendMsg(DeviceMessage message) {
//        VertxTcpClient client = clientMap.get(message.getDeviceName());
//        if (client != null) {
//            client.sendMessage(Buffer.buffer(message.getContent().toString()));
//        }
//    }
//
//    /**
//     * 递归断开连接
//     */
//    private void clientDisconnect(String deviceName) {
//        VertxTcpClient remove = clientMap.remove(deviceName);
//        if (null != remove) {
//            executor.onReceive(null, "disconnect", deviceName);
//            if (remove.hasParent()) {
//                List<VertxTcpClient> childClients = clientMap.values().stream().filter(cl -> cl.hasParent() && cl.getParentName()
//                        .equals(remove.getParentName())).collect(Collectors.toList());
//                childClients.forEach(child -> clientDisconnect(child.getDeviceName()));
//            }
//        }
//    }
//
//    public void offlineDev(String deviceName) {
//        VertxTcpClient remove = clientMap.remove(deviceName);
//        if (null != remove) {
//            if (remove.hasParent()) {
//                List<VertxTcpClient> childClients = clientMap.values().stream().filter(cl -> cl.hasParent() && cl.getParentName()
//                        .equals(remove.getParentName())).collect(Collectors.toList());
//                childClients.forEach(child -> offlineDev(child.getDeviceName()));
//            }
//        }
//    }
//}
