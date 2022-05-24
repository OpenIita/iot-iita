import cc.iotkit.utils.AuthUtil;
import org.junit.Test;

public class GenPwdSecret {

    @Test
    public void gen() throws Exception {
        //生成密码加密内容
        String secret = AuthUtil.enCryptPwd("guest123");
        System.out.println(secret);
        System.out.println(AuthUtil.checkPwd("guest123", secret));
    }

}
