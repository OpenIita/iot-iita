package cc.iotkit.system.domain;

import lombok.Data;

/**
 * 用户和岗位关联
 *
 * @author Lion Li
 */

@Data
public class SysUserPost {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 岗位ID
     */
    private Long postId;

}
