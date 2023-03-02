package cc.iotkit.data.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "protocol_component")
public class TbProtocolComponent {

    @Id
    private String id;

    /**
     * 所属性用户id
     */
    private String uid;

    private String name;

    private String type;

    private String protocol;

    private String jarFile;

    @Column(columnDefinition = "text")
    private String config;

    private String converter;

    private String state;

    private Long createAt;

    private String scriptTyp;

    @Column(columnDefinition = "text")//设置映射为text类型
    private String script;

}
