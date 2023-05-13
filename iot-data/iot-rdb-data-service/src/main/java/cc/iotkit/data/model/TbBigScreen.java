package cc.iotkit.data.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author：tfd
 * @Date：2023/5/6 15:34
 */
@Data
@Entity
@Table(name = "big_screen")
public class TbBigScreen {
    @Id
    private String id;

    /**
     * 所属性用户id
     */
    private String uid;

    /**
     * 大屏名称
     */
    private String name;

    /**
     * 资源文件
     */
    private String resourceFile;

    /**
     * 端口
     */
    private Integer port;

    /**
     * 发布状态
     */
    private String state;

    /**
     * 创建时间
     */
    private Long createAt;

    /**
     * 是否为默认大屏
     */
    private Boolean isDefault;
}
