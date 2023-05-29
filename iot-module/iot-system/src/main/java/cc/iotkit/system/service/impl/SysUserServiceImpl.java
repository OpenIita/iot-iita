package cc.iotkit.system.service.impl;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.api.Paging;
import cc.iotkit.common.service.UserService;
import cc.iotkit.system.dto.bo.SysUserBo;
import cc.iotkit.system.dto.vo.SysUserVo;
import cc.iotkit.system.service.ISysUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户 业务层处理
 *
 * @author Lion Li
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SysUserServiceImpl implements ISysUserService, UserService {

    @Override
    public String selectUserNameById(Long userId) {
        return null;
    }

    @Override
    public Paging<SysUserVo> selectPageUserList(SysUserBo user, PageRequest<?> query) {
        return null;
    }

    @Override
    public List<SysUserVo> selectUserList(SysUserBo user) {
        return null;
    }

    @Override
    public Paging<SysUserVo> selectAllocatedList(SysUserBo user, PageRequest<?> query) {
        return null;
    }

    @Override
    public Paging<SysUserVo> selectUnallocatedList(SysUserBo user, PageRequest<?> query) {
        return null;
    }

    @Override
    public SysUserVo selectUserByUserName(String userName) {
        return null;
    }

    @Override
    public SysUserVo selectUserByPhonenumber(String phonenumber) {
        return null;
    }

    @Override
    public SysUserVo selectUserById(Long userId) {
        return null;
    }

    @Override
    public String selectUserRoleGroup(String userName) {
        return null;
    }

    @Override
    public String selectUserPostGroup(String userName) {
        return null;
    }

    @Override
    public boolean checkUserNameUnique(SysUserBo user) {
        return false;
    }

    @Override
    public boolean checkPhoneUnique(SysUserBo user) {
        return false;
    }

    @Override
    public boolean checkEmailUnique(SysUserBo user) {
        return false;
    }

    @Override
    public void checkUserAllowed(Long userId) {

    }

    @Override
    public void checkUserDataScope(Long userId) {

    }

    @Override
    public int insertUser(SysUserBo user) {
        return 0;
    }

    @Override
    public boolean registerUser(SysUserBo user, String tenantId) {
        return false;
    }

    @Override
    public int updateUser(SysUserBo user) {
        return 0;
    }

    @Override
    public void insertUserAuth(Long userId, Long[] roleIds) {

    }

    @Override
    public int updateUserStatus(Long userId, String status) {
        return 0;
    }

    @Override
    public int updateUserProfile(SysUserBo user) {
        return 0;
    }

    @Override
    public boolean updateUserAvatar(Long userId, Long avatar) {
        return false;
    }

    @Override
    public int resetUserPwd(Long userId, String password) {
        return 0;
    }

    @Override
    public int deleteUserById(Long userId) {
        return 0;
    }

    @Override
    public int deleteUserByIds(Long[] userIds) {
        return 0;
    }
}
