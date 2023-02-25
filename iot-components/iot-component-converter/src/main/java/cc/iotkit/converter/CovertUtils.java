package cc.iotkit.converter;

import org.graalvm.polyglot.Value;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CovertUtils {

    private static final Map<Class<?>, Map<String, Field>> FIELD_CACHE = new ConcurrentHashMap<>();
    private static final Map<Class<?>, Map<String, Method>> SETTER_CACHE = new ConcurrentHashMap<>();

    public static void copyProperties(Object javaObj, Value jsObj) {
        Map<String, Field> fieldMap = FIELD_CACHE.computeIfAbsent(javaObj.getClass(), clazz -> {
            Map<String, Field> fields = new ConcurrentHashMap<>();
            for (Field field : clazz.getDeclaredFields()) {
                fields.put(field.getName(), field);
            }
            return fields;
        });
        Map<String, Method> setterMap = SETTER_CACHE.computeIfAbsent(javaObj.getClass(), clazz -> {
            Map<String, Method> setters = new ConcurrentHashMap<>();
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.getName().startsWith("set") && method.getParameterCount() == 1) {
                    String propName = method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4);
                    setters.put(propName, method);
                }
            }
            return setters;
        });
        for (String propName : jsObj.getMemberKeys()) {
            try {
                Field field = fieldMap.get(propName);
                Method setter = setterMap.get(propName);
                if (field != null && setter != null) {
                    Class<?> propType = field.getType();
                    Object propValue = jsObj.getMember(propName).as(propType);
                    setter.invoke(javaObj, propValue);
                }
            } catch (Exception e) {
                // ignore errors
            }
        }
    }
}