package cc.iotkit.model.screen;

import cc.iotkit.model.Owned;
import lombok.Data;

/**
 * @Author：tfd
 * @Date：2023/5/8 9:48
 */
@Data
public class BigScreenApi implements Owned<String> {

    public static final String DATA_SOURCE_DEVICE = "device";
    public static final String DATA_SOURCE_INSIDE_API = "inside";
    public static final String DATA_SOURCE_TPI = "tpi";
    public static final String DATA_SOURCE_STATIC = "static";

    private String id;

    /**
     * 所属性用户id
     */
    private String uid;

    /**
     * 接口路径
     */
    private String apiPath;

    /**
     * 接口参数
     */
    private String apiParams;

    /**
     * 请求方法
     */
    private String httpMethod;

    /**
     * 数据源
     */
    private String dataSource;

    /**
     * 创建时间
     */
    private Long createAt;

    /**
     * 转换脚本
     */
    private String script;
}
