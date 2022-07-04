/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.manager.service;

import cc.iotkit.common.exception.BizException;
import cc.iotkit.utils.AuthUtil;
import cc.iotkit.model.Owned;
import cc.iotkit.model.device.DeviceInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataOwnerService {

    public <T extends Owned> T wrapExample(T data) {
        if (AuthUtil.isAdmin()) {
            return data;
        }
        data.setUid(AuthUtil.getUserId());
        return data;
    }

    /**
     * 检查数据中的uid与当前登录用户是否一致
     */
    public <T extends Owned> T checkOwner(T data) {
        //管理员不限制
        if (AuthUtil.isAdmin()) {
            return data;
        }

        if (data == null) {
            return null;
        }
        if (StringUtils.isBlank(data.getUid())) {
            return data;
        }

        String uid = AuthUtil.getUserId();
        if (uid.equals(data.getUid())) {
            return data;
        }

        if (data instanceof DeviceInfo) {
            DeviceInfo device = (DeviceInfo) data;
            //设备子用户验证
            List<String> subUid = device.getSubUid();
            if (subUid != null && subUid.contains(uid)) {
                return data;
            }
        }

        throw new BizException("无权限操作");
    }

    /**
     * 从库中取对应数据Id的数据中的uid是否与当前登录用户一致
     */
    public <T extends Owned> void checkOwner(ElasticsearchRepository<T, String> repository, String id) {
        //管理员不限制
        if (AuthUtil.isAdmin()) {
            return;
        }

        //数据id为空的新数据
        if (StringUtils.isBlank(id)) {
            return;
        }

        T old = repository.findById(id).orElse(null);
        //新数据
        if (old == null) {
            return;
        }

        String currUid = AuthUtil.getUserId();
        if (currUid.equals(old.getUid())) {
            return;
        }

        throw new BizException("无权限操作");
    }

    /**
     * 从库中取对应数据Id的数据中的uid是否与当前登录用户一致，并把当前用户id设置到数据中
     */
    public <T extends Owned> void checkOwnerSave(ElasticsearchRepository<T, String> repository, T data) {
        checkOwner(repository, data.getId());
        data.setUid(AuthUtil.getUserId());
    }

    public void checkWriteRole() {
        if (AuthUtil.isAdmin()) {
            return;
        }

        if (!AuthUtil.hasWriteRole()) {
            throw new BizException("无权操作");
        }
    }
}