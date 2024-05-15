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

package cc.iotkit.system.service.impl;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.api.Paging;
import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.utils.MapstructUtils;
import cc.iotkit.data.system.ISysAppData;
import cc.iotkit.model.system.SysApp;
import cc.iotkit.system.dto.bo.SysAppBo;
import cc.iotkit.system.dto.vo.SysAppVo;
import cc.iotkit.system.service.ISysAppService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 应用信息Service业务层处理
 *
 * @author tfd
 * @date 2023-08-10
 */
@RequiredArgsConstructor
@Service
public class SysAppServiceImpl implements ISysAppService {

    private final ISysAppData baseData;

    /**
     * 查询应用信息
     */
    @Override
    public SysAppVo queryById(Long id){
        return MapstructUtils.convert(baseData.findById(id), SysAppVo.class);
    }

    @Override
    public SysAppVo queryByAppId(String appId) {
        SysApp ret=baseData.findByAppId(appId);
        return ret!=null?MapstructUtils.convert(ret,SysAppVo.class):null;
    }

    @Override
    public boolean checkAppIdUnique(String appId) {
        return baseData.findByAppId(appId)!=null;
    }

    /**
     * 查询应用信息列表
     */
    @Override
    public Paging<SysAppVo> queryPageList(PageRequest<SysAppBo> pageQuery) {
        Paging<SysAppVo> result = baseData.findAll(pageQuery.to(SysApp.class)).to(SysAppVo.class);
        return result;
    }

    /**
     * 查询应用信息列表
     */
    @Override
    public List<SysAppVo> queryList(SysAppBo bo) {

        return MapstructUtils.convert(baseData.findAllByCondition(bo.to(SysApp.class)), SysAppVo.class);
    }

    /**
     * 新增应用信息
     */
    @Override
    public Long insertByBo(SysAppBo bo) {
        SysApp add = MapstructUtils.convert(bo, SysApp.class);
        baseData.save(add);
        if (add == null) {
            throw new BizException("新增失败");
        }
        return add.getId();
    }

    /**
     * 修改应用信息
     */
    @Override
    public Boolean updateByBo(SysAppBo bo) {
        SysApp update = MapstructUtils.convert(bo, SysApp.class);
        validEntityBeforeSave(update);
        SysApp ret = baseData.save(update);
        if(ret == null){
            return false;
        }
        return true;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(SysApp entity){
    }

    /**
     * 批量删除应用信息
     */
    @Override
    public Boolean deleteById(Long id) {
        baseData.deleteById(id);
        return true;
    }
}
