package cc.iotkit.comp.tcp.parser.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author huangwenl
 * @date 2022-10-13
 */
@Getter
@AllArgsConstructor
public enum PayloadParserType {
    DIRECT("不处理"),

    FIXED_LENGTH("固定长度"),

    DELIMITED("分隔符"),

    SCRIPT("自定义脚本")
    ;

    private String text;
}
