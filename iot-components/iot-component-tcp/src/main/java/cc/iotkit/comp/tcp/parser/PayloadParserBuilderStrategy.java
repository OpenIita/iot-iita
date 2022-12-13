package cc.iotkit.comp.tcp.parser;



import cc.iotkit.comp.tcp.parser.enums.PayloadParserType;

import java.util.Map;

/**
 * @author huangwenl
 * @date 2022-10-13
 */
public interface PayloadParserBuilderStrategy {
    PayloadParserType getType();

    PayloadParser build(Map<String, Object> parserConfiguration);
}
