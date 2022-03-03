package cc.iotkit.common.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class DeviceUtil {

    public static PkDn getPkDn(String pkDn) {
        String[] arr = pkDn.split("/");
        return new PkDn(arr[0], arr[1]);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PkDn {
        private String productKey;
        private String deviceName;
    }

}
