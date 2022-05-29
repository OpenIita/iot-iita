package cc.iotkit.common.utils;


import lombok.SneakyThrows;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.BeanUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReflectUtil {

    @SneakyThrows
    public static <T> T copyNoNulls(T from, T to, String... fields) {
        List<String> fieldList = Arrays.asList(fields);

        Map<String, Object> map = new HashMap<>();
        new BeanMap(from).forEach((key, value) -> {
            if (value == null) {
                return;
            }
            String field = key.toString();
            if (fields.length == 0 || fieldList.contains(field)) {
                map.put(field, value);
            }
        });
        BeanUtils.populate(to, map);
        return to;
    }

}
