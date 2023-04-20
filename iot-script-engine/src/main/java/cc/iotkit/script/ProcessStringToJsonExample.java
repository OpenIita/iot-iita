package cc.iotkit.script;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProcessStringToJsonExample {
    public static void main(String[] args) {
        String input = "{type: \"register\", data: {productKey: \"hbtgIA0SuVw9lxjB\", deviceName: \"TEST:GW:000003\", model: \"GW01\"}}";
//        String input = "{type: \"ack\", content: \"{\\\"productKey\\\":\\\"hbtgIA0SuVw9lxjB\\\",\\\"deviceName\\\":\\\"TEST:GW:000001\\\",\\\"mid\\\":\\\"0\\\",\\\"content\\\":{\\\"topic\\\":\\\"/sys/hbtgIA0SuVw9lxjB/TEST:GW:000001/c/register_reply\\\",\\\"payload\\\":\\\"{\\\\\\\"id\\\\\\\":\\\\\\\"0\\\\\\\",\\\\\\\"code\\\\\\\":0,\\\\\\\"data\\\\\\\":{\\\\\\\"productKey\\\\\\\":\\\\\\\"cGCrkK7Ex4FESAwe\\\\\\\",\\\\\\\"deviceName\\\\\\\":\\\\\\\"TEST_SC_000001\\\\\\\"}}\\\"}}\"}";

        // 使用正则表达式匹配所有键名，并在两端加上双引号
        Pattern pattern = Pattern.compile("[^\":/](\\b\\w+\\b)\\s*:");
        Matcher matcher = pattern.matcher(input);

        StringBuilder jsonBuilder = new StringBuilder(input);
        int offset = 0;
        while (matcher.find()) {
            int start = matcher.start(1) + offset;
            int end = matcher.end(1) + offset;
            if (jsonBuilder.charAt(start - 1) != '\"' || jsonBuilder.charAt(end) != '\"') {
                jsonBuilder.insert(start, '\"');
                jsonBuilder.insert(end + 1, '\"');
                offset += 2;
            }
        }

        String output = jsonBuilder.toString();
        System.out.println(output);
    }
}
