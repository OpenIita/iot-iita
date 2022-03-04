package cc.iotkit.manager.controller;

import cc.iotkit.common.Constants;
import cc.iotkit.common.utils.CodecUtil;
import cc.iotkit.dao.UserAccountRepository;
import cc.iotkit.model.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SystemController {

    @Autowired
    private UserAccountRepository userAccountRepository;

    @PostMapping("/user/login")
    public String login(String uid, String pwd) throws Exception {
        UserAccount account = userAccountRepository.findById(uid).orElse(new UserAccount());
        String encodePwd = CodecUtil.aesEncrypt(uid + pwd, Constants.ACCOUNT_SECRET);
        if (encodePwd.equals(account.getPwd())) {
            return CodecUtil.aesEncrypt(System.currentTimeMillis() + "_" + uid, Constants.ACCOUNT_SECRET);
        }
        throw new RuntimeException("用户名或密码错误");
    }

    public static void main(String[] args) throws Exception {
        System.out.println(CodecUtil.aesEncrypt("aaa123", Constants.ACCOUNT_SECRET));
    }
}
