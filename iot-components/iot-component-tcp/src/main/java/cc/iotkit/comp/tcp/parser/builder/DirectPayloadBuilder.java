package cc.iotkit.comp.tcp.parser.builder;

import cc.iotkit.comp.tcp.parser.DirectPayloadParser;
import cc.iotkit.comp.tcp.parser.PayloadParser;
import cc.iotkit.comp.tcp.parser.PayloadParserBuilderStrategy;
import cc.iotkit.comp.tcp.parser.enums.PayloadParserType;

import java.util.Map;

/**
 * @author huangwenl
 * @date 2022-10-13
 */
public class DirectPayloadBuilder implements PayloadParserBuilderStrategy {
    @Override
    public PayloadParserType getType() {
        return PayloadParserType.DIRECT;
    }

    @Override
    public PayloadParser build(Map<String, Object> parserConfiguration) {
        return new DirectPayloadParser().init(null);
    }
}
