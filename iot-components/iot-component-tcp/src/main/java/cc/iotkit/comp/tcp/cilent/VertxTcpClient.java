package cc.iotkit.comp.tcp.cilent;

import cc.iotkit.comp.IMessageHandler;
import cc.iotkit.comp.tcp.parser.PayloadParser;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * @author huangwenl
 * @date 2022-10-13
 */
@Slf4j
public class VertxTcpClient {
    @Getter
    private String id;
    @Getter
    private String deviceName = "";
    @Setter
    @Getter
    private String parentName = "";
    @Getter
    private String productKey = "";
    // 是否是服务端的连接客户端
    private final boolean serverClient;
    volatile PayloadParser payloadParser;
    public NetSocket socket;
    private final List<Runnable> disconnectListener = new CopyOnWriteArrayList<>();
    private IMessageHandler executor;
    private Consumer<Buffer> receiveHandler;
    @Setter
    private long keepAliveTimeoutMs = Duration.ofMinutes(10).toMillis();
    private volatile long lastKeepAliveTime = System.currentTimeMillis();

    public VertxTcpClient(String id, boolean serverClient) {
        this.id = id;
        this.serverClient = serverClient;
    }


    public void keepAlive() {
        lastKeepAliveTime = System.currentTimeMillis();
    }

    public boolean isOnline() {
        return System.currentTimeMillis() - lastKeepAliveTime < keepAliveTimeoutMs;
    }

    public void setDeviceInfo(String deviceName, String productKey) {
        this.deviceName = deviceName;
        this.productKey = productKey;
    }

    public void setSocket(NetSocket socket) {
        synchronized (this) {
//            Objects.requireNonNull(payloadParser);
            if (this.socket != null && this.socket != socket) {
                this.socket.close();
            }
            this.socket = socket
                    .closeHandler(v -> shutdown())
                    .handler(buffer -> {
                        if (log.isDebugEnabled()) {
                            log.debug("handle tcp client[{}] payload:[{}]",
                                    socket.remoteAddress(),
                                    Hex.encodeHexString(buffer.getBytes()));
                        }
                        keepAlive();
                        payloadParser.handle(buffer);
                        if (this.socket != socket) {
                            log.warn("tcp client [{}] memory leak ", socket.remoteAddress());
                            socket.close();
                        }
                    });
        }
    }

    /**
     * 设置客户端消息解析器
     *
     * @param payloadParser 消息解析器
     */
    public void setRecordParser(PayloadParser payloadParser) {
        synchronized (this) {
            if (null != this.payloadParser && this.payloadParser != payloadParser) {
                this.payloadParser.close();
            }
            this.payloadParser = payloadParser;
            this.payloadParser
                    .handlePayload()
                    .onErrorContinue((err, res) -> {
                        log.error(err.getMessage(), err);
                        System.out.println(err.getMessage());
                    })
                    .subscribe(buffer -> {
                        System.out.println(buffer.toString());
                        receiveHandler.accept(buffer);
                    });
        }
    }

    public void onDisconnect(Runnable disconnected) {
        disconnectListener.add(disconnected);
    }


    /**
     * 设置消息处理器
     */
    public void setReceiveHandler(Consumer<Buffer> receiveHandler) {
        this.receiveHandler = receiveHandler;
    }

    public void shutdown() {
        log.debug("tcp client [{}] disconnect", getId());
        synchronized (this) {
            if (null != socket) {
                execute(socket::close);
                this.socket = null;
            }
            // 粘包处理器
            if (null != payloadParser) {
                execute(payloadParser::close);
                payloadParser = null;
            }
        }
        for (Runnable runnable : disconnectListener) {
            execute(runnable);
        }
        disconnectListener.clear();
    }

    public void sendMessage(Buffer buffer) {
        socket.write(buffer, r -> {
            keepAlive();
            if (r.succeeded()) {
                log.info("下行消息成功:{}", buffer.toString());
            } else {
                log.error("下行消息失败", r.cause());
            }
        });
    }

    private void execute(Runnable runnable) {
        try {
            runnable.run();
        } catch (Exception e) {
            log.warn("close tcp client error", e);
        }
    }
    /**
     * 是否有父设备
     */
    public boolean hasParent() {
        return StringUtils.isNotEmpty(parentName);
    }
}
