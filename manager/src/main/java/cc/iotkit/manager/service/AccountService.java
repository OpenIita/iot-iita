package cc.iotkit.manager.service;

import cc.iotkit.common.Constants;
import cc.iotkit.common.utils.CodecUtil;
import cc.iotkit.dao.UserAccountRepository;
import cc.iotkit.dao.UserInfoRepository;
import cc.iotkit.model.UserAccount;
import cc.iotkit.model.UserInfo;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    @Autowired
    private UserAccountRepository accountRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;

    @SneakyThrows
    public String login(String uid, String pwd) {
        UserAccount account = accountRepository.findOne(Example.of(UserAccount.builder().uid(uid).build()))
                .orElseThrow(() -> new RuntimeException("用户名或密码错误"));
        String encodePwd = CodecUtil.aesEncrypt(uid + pwd, Constants.ACCOUNT_SECRET);
        if (!account.getPwd().equals(encodePwd)) {
            throw new RuntimeException("用户名或密码错误");
        }
        return CodecUtil.aesEncrypt(System.currentTimeMillis() + "_" + uid, Constants.ACCOUNT_SECRET);
    }

    @SneakyThrows
    public void register(String uid, String pwd) {
        if (accountRepository.exists(Example.of(UserAccount.builder().uid(uid).build()))) {
            throw new RuntimeException("用户名已存在");
        }
        accountRepository.save(UserAccount.builder().uid(uid)
                .pwd(CodecUtil.aesEncrypt(uid + pwd, Constants.ACCOUNT_SECRET))
                .build());
        userInfoRepository.save(UserInfo.builder().uid(uid).build());
    }

}
