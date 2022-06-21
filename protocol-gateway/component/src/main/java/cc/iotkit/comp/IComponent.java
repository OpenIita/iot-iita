/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.comp;

public interface IComponent {

    String getId();

    void create(CompConfig config);

    void start();

    void stop();

    void destroy();

    CompConfig getConfig();

    void setScript(String script);

    String getScript();

    /**
     * 添加脚本环境变量
     */
    void putScriptEnv(String key, Object value);
}
