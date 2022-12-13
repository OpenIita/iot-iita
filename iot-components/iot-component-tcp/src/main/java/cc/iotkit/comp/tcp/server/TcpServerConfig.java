package cc.iotkit.comp.tcp.server;

import io.vertx.core.net.NetServerOptions;
import io.vertx.core.net.SocketAddress;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author huangwenl
 * @date 2022-10-13
 */
@Data
public class TcpServerConfig {

    private String id;

    private NetServerOptions options;

    private String host;

    private int port;

    private boolean ssl;

    private String parserType;

    // 解析参数
    private Map<String, Object> parserConfiguration = new HashMap<>();

    //服务实例数量(线程数)
    private int instance = Runtime.getRuntime().availableProcessors();

    public SocketAddress createSocketAddress() {
        if (StringUtils.isEmpty(host)) {
            host = "localhost";
        }
        return SocketAddress.inetSocketAddress(port, host);
    }

}
