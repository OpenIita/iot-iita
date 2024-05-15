/*
 *
 *  * | Licensed 未经许可不能去掉「OPENIITA」相关版权
 *  * +----------------------------------------------------------------------
 *  * | Author: xw2sy@163.com
 *  * +----------------------------------------------------------------------
 *
 *  Copyright [2024] [OPENIITA]
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * /
 */

package cc.iotkit.manager.service.impl;

import cc.iotkit.common.satoken.utils.LoginHelper;
import cc.iotkit.data.manager.IHomeData;
import cc.iotkit.data.manager.ISpaceData;
import cc.iotkit.data.manager.ISpaceDeviceData;
import cc.iotkit.manager.dto.bo.space.HomeBo;
import cc.iotkit.manager.service.IHomeService;
import cc.iotkit.model.space.Home;
import cc.iotkit.model.space.Space;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author：tfd
 * @Date：2023/8/25 10:21
 */
@Service
public class HomeServiceImpl implements IHomeService {
    @Autowired
    private IHomeData homeData;

    @Autowired
    private ISpaceData spaceData;

    @Autowired
    private ISpaceDeviceData spaceDeviceData;

    @Override
    public Home save(HomeBo home) {
        return homeData.save(home.to(Home.class));
    }

    @Override
    public Home findByUserIdAndCurrent(Long userId, boolean current) {
        return homeData.findByUserIdAndCurrent(userId,current);
    }

    @Override
    public List<Home> findByUserId(Long userId) {
        return homeData.findByUserId(userId);
    }

    @Override
    public Home findById(Long id) {
        return homeData.findById(id);
    }

    @Override
    public boolean checkHomeNameUnique(HomeBo home) {
        return homeData.checkHomeNameUnique(home.to(Home.class));
    }

    @Override
    public void deleteById(Long id) {
        //先删除空间设备，再删除空间，再删除家庭
        List<Space> spaces=spaceData.findByHomeId(id);
        for(Space space:spaces){
            spaceDeviceData.deleteAllBySpaceId(space.getId());
            spaceData.deleteById(space.getId());
        }
        homeData.deleteById(id);
    }

    @Override
    public void changCurrentHome(HomeBo home) {
        Home oldHome=homeData.findByUserIdAndCurrent(LoginHelper.getUserId(), true);
        oldHome.setCurrent(false);
        homeData.save(oldHome);
        Home newHome=homeData.findById(home.getId());
        newHome.setCurrent(true);
        homeData.save(newHome);
    }
}
