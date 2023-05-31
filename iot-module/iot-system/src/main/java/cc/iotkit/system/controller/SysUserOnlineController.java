package cc.iotkit.system.controller;

import cc.iotkit.common.api.Paging;
import cc.iotkit.common.constant.CacheConstants;
import cc.iotkit.common.log.annotation.Log;
import cc.iotkit.common.log.enums.BusinessType;
import cc.iotkit.common.redis.utils.RedisUtils;
import cc.iotkit.common.undefined.UserOnlineDTO;
import cc.iotkit.common.utils.StreamUtils;
import cc.iotkit.common.utils.StringUtils;
import cc.iotkit.common.web.core.BaseController;
import cc.iotkit.system.dto.SysUserOnline;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Author：tfd
 * @Date：2023/5/31 16:08
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/monitor/online")
@Api(tags = "用户在线监控")
public class SysUserOnlineController extends BaseController {

    /**
     * 获取在线用户监控列表
     *
     * @param ipaddr   IP地址
     * @param userName 用户名
     */
    @ApiOperation("获取在线用户监控列表")
    @SaCheckPermission("monitor:online:list")
    @GetMapping("/list")
    public Paging<SysUserOnline> list(String ipaddr, String userName) {
        // 获取所有未过期的 token
        List<String> keys = StpUtil.searchTokenValue("", 0, -1, false);
        List<UserOnlineDTO> userOnlineDTOList = new ArrayList<>();
        for (String key : keys) {
            String token = StringUtils.substringAfterLast(key, ":");
            // 如果已经过期则跳过
            if (StpUtil.stpLogic.getTokenActivityTimeoutByToken(token) < -1) {
                continue;
            }
            userOnlineDTOList.add(RedisUtils.getCacheObject(CacheConstants.ONLINE_TOKEN_KEY + token));
        }
        if (StringUtils.isNotEmpty(ipaddr) && StringUtils.isNotEmpty(userName)) {
            userOnlineDTOList = StreamUtils.filter(userOnlineDTOList, userOnline ->
                    StringUtils.equals(ipaddr, userOnline.getIpaddr()) &&
                            StringUtils.equals(userName, userOnline.getUserName())
            );
        } else if (StringUtils.isNotEmpty(ipaddr)) {
            userOnlineDTOList = StreamUtils.filter(userOnlineDTOList, userOnline ->
                    StringUtils.equals(ipaddr, userOnline.getIpaddr())
            );
        } else if (StringUtils.isNotEmpty(userName)) {
            userOnlineDTOList = StreamUtils.filter(userOnlineDTOList, userOnline ->
                    StringUtils.equals(userName, userOnline.getUserName())
            );
        }
        Collections.reverse(userOnlineDTOList);
        userOnlineDTOList.removeAll(Collections.singleton(null));
        List<SysUserOnline> userOnlineList = BeanUtil.copyToList(userOnlineDTOList, SysUserOnline.class);
        return null;
    }

    /**
     * 强退用户
     *
     * @param tokenId token值
     */
    @ApiOperation("强退用户")
    @SaCheckPermission("monitor:online:forceLogout")
    @Log(title = "在线用户", businessType = BusinessType.FORCE)
    @DeleteMapping("/{tokenId}")
    public void forceLogout(@PathVariable String tokenId) {
        try {
            StpUtil.kickoutByTokenValue(tokenId);
        } catch (NotLoginException ignored) {
        }
    }
}
