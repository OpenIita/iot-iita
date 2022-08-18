/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.ruleengine.expression;


public class NeqComparator extends BaseComparator {

    @Override
    public String getName() {
        return "!=";
    }

    @Override
    public String getScript() {
        return "a!=b";
    }

}
