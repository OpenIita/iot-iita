package cc.iotkit.comps.config;

public class CacheKey {

    private static final String KEY_CMD_MID = "str:cmd:mid:%s:%s";

    public static String getKeyCmdMid(String deviceName, String downMid) {
        return String.format(KEY_CMD_MID, deviceName, downMid);
    }

}
