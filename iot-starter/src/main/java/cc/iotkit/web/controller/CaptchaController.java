package cc.iotkit.web.controller;

import cc.iotkit.common.constant.Constants;
import cc.iotkit.common.constant.GlobalConstants;
import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.redis.utils.RedisUtils;
import cc.iotkit.common.utils.ReflectUtils;
import cc.iotkit.common.utils.SpringUtils;
import cc.iotkit.common.utils.StringUtils;
import cc.iotkit.common.web.config.properties.CaptchaProperties;
import cc.iotkit.common.web.enums.CaptchaType;
import cc.iotkit.web.domain.vo.CaptchaVo;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.hutool.captcha.AbstractCaptcha;
import cn.hutool.captcha.generator.CodeGenerator;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * 验证码操作处理
 *
 * @author Lion Li
 */
@SaIgnore
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@Api(tags = "验证码")
public class CaptchaController {

    private final CaptchaProperties captchaProperties;
//    private final SmsProperties smsProperties;
//    private final MailProperties mailProperties;

    /**
     * 短信验证码
     *
     * @param phonenumber 用户手机号
     */
//    @GetMapping("/resource/sms/code")
//    public void smsCode(@NotBlank(message = "{user.phonenumber.not.blank}") String phonenumber) {
//        if (!smsProperties.getEnabled()) {
//            throw new BizException("当前系统没有开启短信功能！");
//        }
//        String key = GlobalConstants.CAPTCHA_CODE_KEY + phonenumber;
//        String code = RandomUtil.randomNumbers(4);
//        RedisUtils.setCacheObject(key, code, Duration.ofMinutes(Constants.CAPTCHA_EXPIRATION));
//        // 验证码模板id 自行处理 (查数据库或写死均可)
//        String templateId = "";
//        Map<String, String> map = new HashMap<>(1);
//        map.put("code", code);
//        SmsTemplate smsTemplate = SpringUtils.getBean(SmsTemplate.class);
//        SmsResult result = smsTemplate.send(phonenumber, templateId, map);
//        if (!result.isSuccess()) {
//            log.error("验证码短信发送异常 => {}", result);
//            throw new RuntimeException("验证码短信发送异常");
//        }
//        return ;
//    }

    /**
     * 邮箱验证码
     *
     * @param email 邮箱
     */
//    @GetMapping("/resource/email/code")
//    public void emailCode(@NotBlank(message = "{user.email.not.blank}") String email) {
//        if (!mailProperties.getEnabled()) {
//            throw new BizException("当前系统没有开启邮件功能！"));
//        }
//        String key = GlobalConstants.CAPTCHA_CODE_KEY + email;
//        String code = RandomUtil.randomNumbers(4);
//        RedisUtils.setCacheObject(key, code, Duration.ofMinutes(Constants.CAPTCHA_EXPIRATION));
//        try {
//            MailUtils.sendText(email, "登录验证码", "您本次验证码为：" + code + "，有效性为" + Constants.CAPTCHA_EXPIRATION + "分钟，请尽快填写。");
//        } catch (Exception e) {
//            log.error("验证码短信发送异常 => {}", e.getMessage());
//           throw new RuntimeException("验证码短信发送异常");
//        }
//
//    }

    /**
     * 生成验证码
     */
    @ApiOperation(value = "生成验证码")
    @GetMapping("/code")
    public CaptchaVo getCode() {
        CaptchaVo captchaVo = new CaptchaVo();
        boolean captchaEnabled = captchaProperties.getEnable();
        if (!captchaEnabled) {
            captchaVo.setCaptchaEnabled(false);
            return captchaVo;
        }
        // 保存验证码信息
        String uuid = IdUtil.simpleUUID();
        String verifyKey = GlobalConstants.CAPTCHA_CODE_KEY + uuid;
        // 生成验证码
        CaptchaType captchaType = captchaProperties.getType();
        boolean isMath = CaptchaType.MATH == captchaType;
        Integer length = isMath ? captchaProperties.getNumberLength() : captchaProperties.getCharLength();
        CodeGenerator codeGenerator = ReflectUtils.newInstance(captchaType.getClazz(), length);
        AbstractCaptcha captcha = SpringUtils.getBean(captchaProperties.getCategory().getClazz());
        captcha.setGenerator(codeGenerator);
        captcha.createCode();
        String code = captcha.getCode();
        if (isMath) {
            ExpressionParser parser = new SpelExpressionParser();
            Expression exp = parser.parseExpression(StringUtils.remove(code, "="));
            code = exp.getValue(String.class);
        }
        RedisUtils.setCacheObject(verifyKey, code, Duration.ofMinutes(Constants.CAPTCHA_EXPIRATION));
        captchaVo.setUuid(uuid);
        captchaVo.setImg(captcha.getImageBase64());
        return captchaVo;
    }

}
