package cc.iotkit.comp.tcp.cilent;

import io.vertx.core.net.NetClientOptions;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author huangwenl
 * @date 2022-10-13
 */
@Data
public class TcpClinetConfig {

    private String id;

    private NetClientOptions options;

    private String host;

    private int port;

    private boolean ssl;

    private String parserType;

    // 解析参数
    private Map<String, Object> parserConfiguration = new HashMap<>();

    //服务实例数量(线程数)
    private int instance = Runtime.getRuntime().availableProcessors();


}
