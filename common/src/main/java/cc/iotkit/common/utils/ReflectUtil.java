package cc.iotkit.common.utils;


import lombok.SneakyThrows;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.BeanUtils;

import java.util.HashMap;
import java.util.Map;

public class ReflectUtil {

    @SneakyThrows
    public static <T> T copyNoNulls(T from, T to) {
        Map<String, Object> map = new HashMap<>();
        new BeanMap(from).forEach((key, value) -> {
            if (value == null) {
                return;
            }
            map.put(key.toString(), value);
        });
        BeanUtils.populate(to, map);
        return to;
    }

}
