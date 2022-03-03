package cc.iotkit.common.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.SneakyThrows;

public final class JsonUtil {

    private final static ObjectMapper MAPPER = new ObjectMapper()
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    @SneakyThrows
    public static String toJsonString(Object obj) {
        return MAPPER.writeValueAsString(obj);
    }

    @SneakyThrows
    public static <T> T parse(String json, Class<T> cls) {
        return MAPPER.readValue(json, cls);
    }

    @SneakyThrows
    public static <T> T parse(String json, TypeReference<T> type) {
        return MAPPER.readValue(json, type);
    }

    @SneakyThrows
    public static JsonNode parse(String json) {
        return MAPPER.readTree(json);
    }
}
