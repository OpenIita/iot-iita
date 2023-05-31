package cc.iotkit.data.service;

import cc.iotkit.common.utils.MapstructUtils;
import cc.iotkit.data.dao.SysUserPostRepository;
import cc.iotkit.data.model.TbSysUserPost;
import cc.iotkit.data.system.ISysUserPostData;
import cc.iotkit.model.system.SysUserPost;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Author：tfd
 * @Date：2023/5/30 17:04
 */
public class SysUserPostDataImpl implements ISysUserPostData {

    @Autowired
    private SysUserPostRepository sysUserPostRepository;

    @Override
    public int deleteByUserId(Long userId) {
        return sysUserPostRepository.deleteAllByUserId(userId);
    }

    @Override
    public void batchSave(List<SysUserPost> data) {
        sysUserPostRepository.saveAll(MapstructUtils.convert(data, TbSysUserPost.class));
    }
}
