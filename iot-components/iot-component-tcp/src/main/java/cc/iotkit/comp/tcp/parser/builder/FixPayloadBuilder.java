package cc.iotkit.comp.tcp.parser.builder;

import cc.iotkit.comp.tcp.parser.FixPayloadParser;
import cc.iotkit.comp.tcp.parser.PayloadParser;
import cc.iotkit.comp.tcp.parser.PayloadParserBuilderStrategy;
import cc.iotkit.comp.tcp.parser.enums.PayloadParserType;

import java.util.Map;

/**
 * @author huangwenl
 * @date 2022-10-13
 */
public class FixPayloadBuilder implements PayloadParserBuilderStrategy {
    @Override
    public PayloadParserType getType() {
        return PayloadParserType.FIXED_LENGTH;
    }

    @Override
    public PayloadParser build(Map<String, Object> parserConfiguration) {
        return new FixPayloadParser().init(parserConfiguration.get("fix"));
    }
}
