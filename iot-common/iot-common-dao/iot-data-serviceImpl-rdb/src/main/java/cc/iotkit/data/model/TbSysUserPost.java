package cc.iotkit.data.model;

import cc.iotkit.data.model.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用户和岗位关联 sys_user_post
 *
 * @author Lion Li
 */

@Data
@Entity
@Table(name = "sys_user_post")
public class TbSysUserPost extends BaseEntity {

    @Id
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 岗位ID
     */
    private Long postId;

}
