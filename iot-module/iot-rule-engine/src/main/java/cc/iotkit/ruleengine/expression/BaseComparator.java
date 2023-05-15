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


import java.util.HashMap;
import java.util.Map;

public abstract class BaseComparator implements Comparator {

    @Override
    public Map<String, Object> getData(Object left, Object right) {
        Map<String, Object> data = new HashMap<>();
        data.put("a", left);
        data.put("b", right);
        return data;
    }
}
