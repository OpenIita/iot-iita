import cc.iotkit.common.utils.CodecUtil;
import cc.iotkit.mqttauth.config.Constants;
import cc.iotkit.mqttauth.controller.MqttAuthController;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Assert;
import org.junit.Test;

public class SupperUser {

    public static void main(String[] args) throws Exception {
        System.out.println("clientId:su_" + CodecUtil.aesEncrypt("admin_produce_dev", Constants.MQTT_SECRET));

        String hmac = DigestUtils.md5Hex(Constants.MQTT_SECRET + "2P6MDKr8cB7y8EmM_ABC123DEF456");
        System.out.println(hmac);

    }

    @Test
    public void createSuperuser() throws Exception {
        String clientId = "mqtt-server-producer-dev";
        System.out.println("clientId:su_" + CodecUtil.aesEncrypt("admin_" + clientId, Constants.MQTT_SECRET));
    }

    @Test
    public void isSupperUser() {
        String clientId = "su_344A6E61654F567A30536E59646A306659664A75625A374D35484756776D457977374653684B306B414E513D";
//        String clientId = "su_tng1t408QrZDEoM7CxiDueP++4FmXIxS7x35YbpuNf8=";
        MqttAuthController authController = new MqttAuthController();
        Assert.assertTrue(authController.isSupperUser(clientId));
    }
}
