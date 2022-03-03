package cc.iotkit.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.BeanMap;
import org.springframework.data.mongodb.core.query.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DaoTool {

    public static void update(Update update, List<Prop> props) {
        for (Prop pro : props) {
            update.set(pro.getName(), pro.getValue());
        }
    }

    public static List<Prop> getProp(String key, Object value) {
        List<Prop> props = new ArrayList<>();
        if (value instanceof Map) {
            Set<Map.Entry<String, Object>> entrySet = ((Map) value).entrySet();
            for (Map.Entry<String, Object> entry : entrySet) {
                props.addAll(getProp(key + "." + entry.getKey(), entry.getValue()));
            }
        } else if (value != null && !(value instanceof Class)) {
            props.add(new Prop(key, value));
        }
        return props;
    }

    @SneakyThrows
    public static <T> Update update(T obj) {
        Map<Object, Object> pros = new BeanMap(obj);
        Update update = new Update();
        for (Map.Entry<Object, Object> entry : pros.entrySet()) {
            update(update, DaoTool.getProp(entry.getKey().toString(), entry.getValue()));
        }
        return update;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class Prop {
        private String name;
        private Object value;
    }
}
