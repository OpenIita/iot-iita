package cc.iotkit.comp.tcp.parser.builder;

import cc.iotkit.comp.tcp.parser.PayloadParser;
import cc.iotkit.comp.tcp.parser.PayloadParserBuilderStrategy;
import cc.iotkit.comp.tcp.parser.ScriptPayloadParser;
import cc.iotkit.comp.tcp.parser.enums.PayloadParserType;

import java.util.Map;

/**
 * @author huangwenl
 * @date 2022-10-13
 */
public class ScriptPayloadBuilder implements PayloadParserBuilderStrategy {
    @Override
    public PayloadParserType getType() {
        return PayloadParserType.SCRIPT;
    }

    @Override
    public PayloadParser build(Map<String, Object> parserConfiguration) {
        return new ScriptPayloadParser().init(parserConfiguration.get("script"));
    }
}
