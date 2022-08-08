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

import cc.iotkit.comp.model.ReceiveResult;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

public interface IMessageHandler {

    void onReceive(Map<String, Object> head, String type, String msg);

    void onReceive(Map<String, Object> head, String type, String msg, Consumer<ReceiveResult> onResult);

    /**
     * 添加脚本环境变量
     */
    void putScriptEnv(String key, Object value);
}
