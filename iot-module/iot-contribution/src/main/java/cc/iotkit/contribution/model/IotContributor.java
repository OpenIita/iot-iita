package cc.iotkit.contribution.model;

import cc.iotkit.model.Id;
import cc.iotkit.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;


/**
 * 贡献者对象 iot_contributor
 *
 * @author Lion Li
 * @date 2023-07-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class IotContributor extends BaseModel implements Id<Long>, Serializable{

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 贡献者名称
     */
    private String contributor;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 岗位(1前端开发,2后端开发,3全栈开发,4测试,5ui设计师,6产品经理,7架构师)
     */
    private Integer post;

    /**
     * 简介
     */
    private String intro;

    /**
     * tag列表(为了简单,逗号隔开)
     */
    private String tags;

    /**
     * 详情标题
     */
    private String title;

    /**
     * 详情
     */
    private String context;

    /**
     * 帐号状态（0正常 1停用）
     */
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;


}
