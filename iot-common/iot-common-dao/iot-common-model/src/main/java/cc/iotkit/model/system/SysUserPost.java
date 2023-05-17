package cc.iotkit.model.system;

import cc.iotkit.model.BaseEntity;
import cc.iotkit.model.Id;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 用户和岗位关联 sys_user_post
 *
 * @author Michelle.Chung
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysUserPost extends BaseEntity implements Id<Long>, Serializable {
    private static final long serialVersionUID = 1L;


    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 岗位ID
     */
    private Long postId;
}
