import cc.iotkit.common.Constants;
import cc.iotkit.common.utils.CodecUtil;
import cc.iotkit.mqttauth.controller.MqttAuthController;
import org.junit.Assert;
import org.junit.Test;

public class SupperUser {

    @Test
    public void createSuperuser() throws Exception {
        //mqtt生成超级用户，作为mqtt-server连接mqtt的clientId
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
