package cc.iotkit.data.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.util.Date;

/**
 * Entity基类
 *
 * @author Lion Li
 */
@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    /**
     * 创建部门
     */
    private Long createDept;

    /**
     * 创建者
     */
    @CreatedBy
    @Column(name = "create_by", updatable = false)
    private Long createBy;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = "create_time", updatable = false)
    private Date createTime;

    /**
     * 更新者
     */
    @LastModifiedBy
    @Column(name = "update_by")
    private Long updateBy;

    /**
     * 更新时间
     */
    @LastModifiedDate
    @Column(name = "update_time")
    private Date updateTime;

}
