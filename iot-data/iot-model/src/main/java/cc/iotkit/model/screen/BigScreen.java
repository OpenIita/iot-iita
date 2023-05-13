/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.model.screen;

import cc.iotkit.model.Owned;
import lombok.Data;

import java.util.List;

/**
 * @Author：tfd
 * @Date：2023/5/6 15:42
 */
@Data
public class BigScreen implements Owned<String> {

    public static final String STATE_STOPPED = "stopped";
    public static final String STATE_RUNNING = "running";

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

    /**
     * 接口列表
     */
    private List<BigScreenApi> bigScreenApis;

}
