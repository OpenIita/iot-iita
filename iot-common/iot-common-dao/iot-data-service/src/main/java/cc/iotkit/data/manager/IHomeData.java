/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.data.manager;

import cc.iotkit.data.IOwnedData;
import cc.iotkit.model.space.Home;


public interface IHomeData extends IOwnedData<Home, String> {

    default Home findByUidAndCurrent(String uid, boolean current) {
        return null;

    }

    default Home findByUidAndId(String uid, String id) {
        return null;

    }

}
