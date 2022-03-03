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

    public <T extends Owned> void checkOwner(MongoRepository<T, String> repository, T data) {
        //管理员不限制
        if (AuthUtil.isAdmin()) {
            return;
        }

        String dataId = data.getId();
        //没有数据id为新数据
        if (StringUtils.isBlank(dataId)) {
            return;
        }

        T old = repository.findById(dataId).orElse(null);
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

    public <T extends Owned> void checkOwnerSave(MongoRepository<T, String> repository, T data) {
        checkOwner(repository, data);
        data.setUid(AuthUtil.getUserId());
    }

}
