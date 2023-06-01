package cc.iotkit.data.service;

import cc.iotkit.common.utils.MapstructUtils;
import cc.iotkit.data.dao.IJPACommData;
import cc.iotkit.data.dao.SysUserPostRepository;
import cc.iotkit.data.model.TbSysUserPost;
import cc.iotkit.data.system.ISysUserPostData;
import cc.iotkit.model.system.SysUserPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author：tfd
 * @Date：2023/5/30 17:04
 */
public class SysUserPostDataImpl implements ISysUserPostData, IJPACommData<SysUserPost, Long> {

    @Autowired
    private SysUserPostRepository sysUserPostRepository;

    @Override
    public int deleteByUserId(Long userId) {
        return sysUserPostRepository.deleteAllByUserId(userId);
    }

    @Override
    public JpaRepository getBaseRepository() {
        return sysUserPostRepository;
    }

    @Override
    public Class getJpaRepositoryClass() {
        return TbSysUserPost.class;
    }

    @Override
    public void batchSave(List<SysUserPost> data) {
        sysUserPostRepository.saveAll(MapstructUtils.convert(data, TbSysUserPost.class));
    }
}
