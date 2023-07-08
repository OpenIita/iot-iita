package cc.iotkit.web.service;

import cc.iotkit.common.constant.Constants;
import cc.iotkit.common.constant.GlobalConstants;
import cc.iotkit.common.enums.DeviceType;
import cc.iotkit.common.enums.LoginType;
import cc.iotkit.common.enums.UserStatus;
import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.exception.user.UserException;
import cc.iotkit.common.log.event.LogininforEvent;
import cc.iotkit.common.redis.utils.RedisUtils;
import cc.iotkit.common.satoken.utils.LoginHelper;
import cc.iotkit.common.tenant.helper.TenantHelper;
import cc.iotkit.common.undefined.LoginUser;
import cc.iotkit.common.undefined.RoleDTO;
import cc.iotkit.common.undefined.XcxLoginUser;
import cc.iotkit.common.utils.*;
import cc.iotkit.common.web.config.properties.CaptchaProperties;
import cc.iotkit.common.web.utils.ServletUtils;
import cc.iotkit.data.system.ISysUserData;
import cc.iotkit.model.system.SysUser;
import cc.iotkit.system.dto.vo.SysUserVo;
import cc.iotkit.system.service.ISysPermissionService;
import cc.iotkit.system.service.ISysTenantService;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.function.Supplier;

/**
 * 登录校验方法
 *
 * @author Lion Li
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class SysLoginService {

    private final ISysUserData userData;
    private final CaptchaProperties captchaProperties;
    private final ISysPermissionService permissionService;
    private final ISysTenantService tenantService;

    @Value("${user.password.maxRetryCount}")
    private Integer maxRetryCount;

    @Value("${user.password.lockTime}")
    private Integer lockTime;

    /**
     * 登录验证
     *
     * @param username 用户名
     * @param password 密码
     * @param code     验证码
     * @param uuid     唯一标识
     * @return 结果
     */
    public String login(String tenantId, String username, String password, String code, String uuid) {
        boolean captchaEnabled = captchaProperties.getEnable();
        // 验证码开关
        if (captchaEnabled) {
            validateCaptcha(tenantId, username, code, uuid);
        }
        // 校验租户
        checkTenant(tenantId);

        SysUserVo user = loadUserByUsername(tenantId, username);
        checkLogin(LoginType.PASSWORD, tenantId, username, () -> !BCrypt.checkpw(password, user.getPassword()));
        // 此处可根据登录用户的数据不同 自行创建 loginUser
        LoginUser loginUser = buildLoginUser(user);
        // 生成token
        LoginHelper.loginByDevice(loginUser, DeviceType.PC);

        recordLoginInfo(loginUser.getTenantId(), username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success"));
        recordLoginInfo(user.getId());
        return StpUtil.getTokenValue();
    }

    public String smsLogin(String tenantId, String phonenumber, String smsCode) {
        // 校验租户
        checkTenant(tenantId);
        // 通过手机号查找用户
        SysUserVo user = loadUserByPhonenumber(tenantId, phonenumber);

        checkLogin(LoginType.SMS, tenantId, user.getUserName(), () -> !validateSmsCode(tenantId, phonenumber, smsCode));
        // 此处可根据登录用户的数据不同 自行创建 loginUser
        LoginUser loginUser = buildLoginUser(user);
        // 生成token
        LoginHelper.loginByDevice(loginUser, DeviceType.APP);

        recordLoginInfo(loginUser.getTenantId(), user.getUserName(), Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success"));
        recordLoginInfo(user.getId());
        return StpUtil.getTokenValue();
    }

    public String emailLogin(String tenantId, String email, String emailCode) {
        // 校验租户
        checkTenant(tenantId);
        // 通过手机号查找用户
        SysUserVo user = loadUserByEmail(tenantId, email);

        checkLogin(LoginType.EMAIL, tenantId, user.getUserName(), () -> !validateEmailCode(tenantId, email, emailCode));
        // 此处可根据登录用户的数据不同 自行创建 loginUser
        LoginUser loginUser = buildLoginUser(user);
        // 生成token
        LoginHelper.loginByDevice(loginUser, DeviceType.APP);

        recordLoginInfo(loginUser.getTenantId(), user.getUserName(), Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success"));
        recordLoginInfo(user.getId());
        return StpUtil.getTokenValue();
    }


    public String xcxLogin(String xcxCode) {
        // xcxCode 为 小程序调用 wx.login 授权后获取
        // todo 以下自行实现
        // 校验 appid + appsrcret + xcxCode 调用登录凭证校验接口 获取 session_key 与 openid
        String openid = "";
        SysUserVo user = loadUserByOpenid(openid);
        // 校验租户
        checkTenant(user.getTenantId());

        // 此处可根据登录用户的数据不同 自行创建 loginUser
        XcxLoginUser loginUser = new XcxLoginUser();
        loginUser.setTenantId(user.getTenantId());
        loginUser.setUserId(user.getId());
        loginUser.setUsername(user.getUserName());
        loginUser.setUserType(user.getUserType());
        loginUser.setOpenid(openid);
        // 生成token
        LoginHelper.loginByDevice(loginUser, DeviceType.XCX);

        recordLoginInfo(loginUser.getTenantId(), user.getUserName(), Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success"));
        recordLoginInfo(user.getId());
        return StpUtil.getTokenValue();
    }

    /**
     * 退出登录
     */
    public void logout() {
        try {
            LoginUser loginUser = LoginHelper.getLoginUser();
            if (TenantHelper.isEnable() && LoginHelper.isSuperAdmin()) {
                // 超级管理员 登出清除动态租户
                TenantHelper.clearDynamic();
            }
            StpUtil.logout();
            recordLoginInfo(loginUser.getTenantId(), loginUser.getUsername(), Constants.LOGOUT, MessageUtils.message("user.logout.success"));
        } catch (NotLoginException ignored) {
        }
    }

    /**
     * 记录登录信息
     *
     * @param tenantId 租户ID
     * @param username 用户名
     * @param status   状态
     * @param message  消息内容
     */
    private void recordLoginInfo(String tenantId, String username, String status, String message) {
        LogininforEvent logininforEvent = new LogininforEvent();
        logininforEvent.setTenantId(tenantId);
        logininforEvent.setUsername(username);
        logininforEvent.setStatus(status);
        logininforEvent.setMessage(message);
        logininforEvent.setRequest(ServletUtils.getRequest());
        SpringUtils.context().publishEvent(logininforEvent);
    }

    /**
     * 校验短信验证码
     */
    private boolean validateSmsCode(String tenantId, String phonenumber, String smsCode) {
        String code = RedisUtils.getCacheObject(GlobalConstants.CAPTCHA_CODE_KEY + phonenumber);
        if (StringUtils.isBlank(code)) {
            recordLoginInfo(tenantId, phonenumber, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire"));
            throw new BizException(MessageUtils.message("user.jcaptcha.expire"));
        }
        return code.equals(smsCode);
    }

    /**
     * 校验邮箱验证码
     */
    private boolean validateEmailCode(String tenantId, String email, String emailCode) {
        String code = RedisUtils.getCacheObject(GlobalConstants.CAPTCHA_CODE_KEY + email);
        if (StringUtils.isBlank(code)) {
            recordLoginInfo(tenantId, email, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire"));
            throw new BizException("验证码过期");
        }
        return code.equals(emailCode);
    }

    /**
     * 校验验证码
     *
     * @param username 用户名
     * @param code     验证码
     * @param uuid     唯一标识
     */
    public void validateCaptcha(String tenantId, String username, String code, String uuid) {
        String verifyKey = GlobalConstants.CAPTCHA_CODE_KEY + StringUtils.defaultString(uuid, "");
        String captcha = RedisUtils.getCacheObject(verifyKey);
        RedisUtils.deleteObject(verifyKey);
        if (captcha == null) {
            recordLoginInfo(tenantId, username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire"));
            throw new BizException("验证码过期");
        }
        if (!code.equalsIgnoreCase(captcha)) {
            recordLoginInfo(tenantId, username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error"));
            throw new BizException("验证码错误");
        }
    }

    private SysUserVo loadUserByUsername(String tenantId, String username) {
        SysUser query = new SysUser();
        query.setUserName(username);
        if(TenantHelper.isEnable()){
            query.setTenantId(tenantId);
        }

        SysUser user = userData.findOneByCondition(query);

        if (ObjectUtil.isNull(user)) {
            log.info("登录用户：{} 不存在.", username);
            throw new UserException("登录用户不存在");
        } else if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            log.info("登录用户：{} 已被停用.", username);
            throw new UserException("用户被停用");
        }
        if (TenantHelper.isEnable()) {
            SysUser sysUser = userData.selectTenantUserByUserName(username, tenantId);
            return MapstructUtils.convert(sysUser, SysUserVo.class);

        }
        SysUser sysUser = userData.selectUserByUserName(username);
        return MapstructUtils.convert(sysUser, SysUserVo.class);
    }

    private SysUserVo loadUserByPhonenumber(String tenantId, String phonenumber) {
        SysUser query = new SysUser();
        query.setPhonenumber(phonenumber);
        if(TenantHelper.isEnable()){
            query.setTenantId(tenantId);
        }

        SysUser user = userData.findOneByCondition(query);
        if (ObjectUtil.isNull(user)) {
            log.info("登录用户：{} 不存在.", phonenumber);
            throw new UserException("登录用户不存在");
        } else if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            log.info("登录用户：{} 已被停用.", phonenumber);
            throw new UserException("用户被停用");
        }

        if (TenantHelper.isEnable()) {
            SysUser sysUser = userData.selectTenantUserByPhonenumber(phonenumber, tenantId);
            return MapstructUtils.convert(sysUser, SysUserVo.class);
        }
        SysUser userFind =  userData.selectByPhonenumber(phonenumber);
        return MapstructUtils.convert(userFind, SysUserVo.class);
    }

    private SysUserVo loadUserByEmail(String tenantId, String email) {
        SysUser query = new SysUser();
        query.setEmail(email);
        if(TenantHelper.isEnable()){
            query.setTenantId(tenantId);
        }

        SysUser user = userData.findOneByCondition(query);

        if (ObjectUtil.isNull(user)) {
            log.info("登录用户：{} 不存在.", email);
            throw new UserException("用户不存在");
        } else if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            log.info("登录用户：{} 已被停用.", email);
            throw new UserException("用户被停用");
        }
        if (TenantHelper.isEnable()) {
            SysUser sysUser = userData.selectTenantUserByEmail(email, tenantId);
            return MapstructUtils.convert(sysUser, SysUserVo.class);
        }
        SysUser sysUser = userData.selectUserByEmail(email);
        return MapstructUtils.convert(sysUser, SysUserVo.class);

    }

    private SysUserVo loadUserByOpenid(String openid) {
        // 使用 openid 查询绑定用户 如未绑定用户 则根据业务自行处理 例如 创建默认用户
        // todo 自行实现 userService.selectUserByOpenid(openid);
        SysUserVo user = new SysUserVo();
        if (ObjectUtil.isNull(user)) {
            log.info("登录用户：{} 不存在.", openid);
            // todo 用户不存在 业务逻辑自行实现
        } else if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            log.info("登录用户：{} 已被停用.", openid);
            // todo 用户已被停用 业务逻辑自行实现
        }
        return user;
    }

    /**
     * 构建登录用户
     */
    private LoginUser buildLoginUser(SysUserVo user) {
        LoginUser loginUser = new LoginUser();
        loginUser.setTenantId(user.getTenantId());
        loginUser.setUserId(user.getId());
        loginUser.setDeptId(user.getDeptId());
        loginUser.setUsername(user.getUserName());
        loginUser.setUserType(user.getUserType());
        loginUser.setMenuPermission(permissionService.getMenuPermission(user.getId()));
        loginUser.setRolePermission(permissionService.getRolePermission(user.getId()));
        loginUser.setDeptName(ObjectUtil.isNull(user.getDept()) ? "" : user.getDept().getDeptName());
        List<RoleDTO> roles = BeanUtil.copyToList(user.getRoles(), RoleDTO.class);
        loginUser.setRoles(roles);
        return loginUser;
    }

    /**
     * 记录登录信息
     *
     * @param userId 用户ID
     */
    public void recordLoginInfo(Long userId) {
        SysUser sysUser = userData.findById(userId);
        sysUser.setLoginIp(ServletUtils.getClientIP());
        sysUser.setLoginDate(DateUtils.getNowDate());
        sysUser.setUpdateBy(userId);
        userData.save(sysUser);
    }

    /**
     * 登录校验
     */
    private void checkLogin(LoginType loginType, String tenantId, String username, Supplier<Boolean> supplier) {
        String errorKey = GlobalConstants.PWD_ERR_CNT_KEY + username;
        String loginFail = Constants.LOGIN_FAIL;

        // 获取用户登录错误次数(可自定义限制策略 例如: key + username + ip)
        Integer errorNumber = RedisUtils.getCacheObject(errorKey);
        // 锁定时间内登录 则踢出
        if (ObjectUtil.isNotNull(errorNumber) && errorNumber.equals(maxRetryCount)) {
            recordLoginInfo(tenantId, username, loginFail, MessageUtils.message(loginType.getRetryLimitExceed(), maxRetryCount, lockTime));
            throw new UserException("重试达到最大限制" );
        }

        if (supplier.get()) {
            // 是否第一次
            errorNumber = ObjectUtil.isNull(errorNumber) ? 1 : errorNumber + 1;
            // 达到规定错误次数 则锁定登录
            if (errorNumber.equals(maxRetryCount)) {
                RedisUtils.setCacheObject(errorKey, errorNumber, Duration.ofMinutes(lockTime));
                recordLoginInfo(tenantId, username, loginFail, MessageUtils.message(loginType.getRetryLimitExceed(), maxRetryCount, lockTime));
                throw new UserException( "重试达到最大限制" );

            } else {
                // 未达到规定错误次数 则递增
                RedisUtils.setCacheObject(errorKey, errorNumber);
                recordLoginInfo(tenantId, username, loginFail, MessageUtils.message(loginType.getRetryLimitCount(), errorNumber));
                throw new UserException( String.format("错误次数:%s", errorNumber) );

            }
        }

        // 登录成功 清空错误次数
        RedisUtils.deleteObject(errorKey);
    }

    private void checkTenant(String tenantId) {
//        if (!TenantHelper.isEnable()) {
//            return;
//        }
//        if (TenantConstants.DEFAULT_TENANT_ID.equals(tenantId)) {
//            return;
//        }
//        SysTenantVo tenant = tenantService.queryByTenantId(tenantId);
//        if (ObjectUtil.isNull(tenant)) {
//            log.info("登录租户：{} 不存在.", tenantId);
//            throw new TenantException("tenant.not.exists");
//        } else if (TenantStatus.DISABLE.getCode().equals(tenant.getStatus())) {
//            log.info("登录租户：{} 已被停用.", tenantId);
//            throw new TenantException("tenant.blocked");
//        } else if (ObjectUtil.isNotNull(tenant.getExpireTime())
//                && new Date().after(tenant.getExpireTime())) {
//            log.info("登录租户：{} 已超过有效期.", tenantId);
//            throw new TenantException("tenant.expired");
//        }
    }

}
