import cc.iotkit.common.Constants;
import cc.iotkit.common.utils.CodecUtil;
import org.junit.Test;

public class SysMqttAuth {

    @Test
    public void createSyPwd() throws Exception {
        System.out.println(CodecUtil.aesEncrypt("sy_gateway_dev", Constants.ACCOUNT_SECRET));
        System.out.println(CodecUtil.aesDecryptHex("4B6272544E59324C596562686173494A696E764E69673D3D", Constants.ACCOUNT_SECRET));
    }

}
