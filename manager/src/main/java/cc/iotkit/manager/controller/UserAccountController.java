package cc.iotkit.manager.controller;

import cc.iotkit.dao.UserAccountRepository;
import cc.iotkit.model.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/userAccount")
public class UserAccountController extends DbBaseController<UserAccountRepository, UserAccount> {

    @Autowired
    public UserAccountController(UserAccountRepository userAccountRepository) {
        super(userAccountRepository);
    }

}
