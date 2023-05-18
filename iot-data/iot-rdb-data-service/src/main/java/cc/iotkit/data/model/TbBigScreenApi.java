package cc.iotkit.data.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author：tfd
 * @Date：2023/5/8 10:00
 */
@Data
@Entity
@Table(name = "big_screen_api")
public class TbBigScreenApi {

    @Id
    private String id;

    /**
     * 所属性用户id
     */
    private String uid;

    /**
     * 大屏id
     */
    private String screenId;

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
    @Column(columnDefinition = "text")
    private String script;
}
