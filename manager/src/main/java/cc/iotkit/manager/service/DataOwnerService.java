package cc.iotkit.manager.service;

import cc.iotkit.common.exception.BizException;
import cc.iotkit.manager.utils.AuthUtil;
import cc.iotkit.model.Owned;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

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
        if (AuthUtil.getUserId().equals(data.getUid())) {
            return data;
        }

        throw new BizException("无权限操作");
    }

    /**
     * 从库中取对应数据Id的数据中的uid是否与当前登录用户一致
     */
    public <T extends Owned> void checkOwner(MongoRepository<T, String> repository, String id) {
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
    public <T extends Owned> void checkOwnerSave(MongoRepository<T, String> repository, T data) {
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
