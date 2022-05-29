package cc.iotkit.model.device;

import cc.iotkit.model.Owned;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * 虚拟设备
 */
@Data
@Document
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VirtualDevice implements Owned {

    public static final String STATE_STOPPED = "stopped";
    public static final String STATE_RUNNING = "running";

    /**
     * 虚拟类型-基于物模型模拟
     */
    public static final String TYPE_THING_MODEL = "thingModel";
    /**
     * 虚拟类型-基于设备协议模拟
     */
    public static final String TYPE_PROTOCOL = "protocol";

    /**
     * 触发执行-无（手动）
     */
    public static final String TRIGGER_NONE = "none";
    /**
     * 触发执行-定时执行
     */
    public static final String TRIGGER_CRON = "cron";
    /**
     * 触发执行-随机执行
     */
    public static final String TRIGGER_RANDOM = "random";

    @Id
    private String id;

    /**
     * 所属用户
     */
    private String uid;

    /**
     * 虚拟设备名称
     */
    private String name;

    /**
     * 产品key
     */
    private String productKey;

    /**
     * 虚拟的目标设备列表
     */
    private List<String> devices = new ArrayList<>();

    /**
     * 虚拟类型
     */
    private String type;

    /**
     * 设备行为脚本
     */
    private String script;

    /**
     * 触发方式执行方式
     */
    private String trigger;

    /**
     * 触发表达式
     */
    private String triggerExpression;

    /**
     * 运行状态
     */
    private String state = STATE_STOPPED;

    /**
     * 创建时间
     */
    private Long createAt = System.currentTimeMillis();

}
