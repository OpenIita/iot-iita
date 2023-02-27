/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.model.protocol;

import cc.iotkit.model.Owned;
import lombok.Data;

@Data
public class ProtocolConverter implements Owned<String> {

    public static final String SCRIPT_FILE_NAME = "converter.js";

    private String id;

    /**
     * 所属性用户id
     */
    private String uid;

    private String name;

    private String desc;

    private Long createAt;

    // 脚本类型
    private String type;
}
