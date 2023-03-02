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
public class ProtocolComponent implements Owned<String> {

    public static final String STATE_STOPPED = "stopped";
    public static final String STATE_RUNNING = "running";
    public static final String TYPE_DEVICE = "device";
    public static final String TYPE_BIZ = "biz";

    public static final String SCRIPT_FILE_NAME = "component.js";

    private String id;

    /**
     * 所属性用户id
     */
    private String uid;

    private String name;

    private String type = TYPE_DEVICE;

    private String protocol;

    private String jarFile;

    private String config;

    private String converter;

    private String state;

    private Long createAt;

    private String scriptTyp;

    private String script;

}
