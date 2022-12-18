package cc.iotkit.comp.tcp.parser;

import cc.iotkit.comp.tcp.parser.builder.DelimitedPayloadBuilder;
import cc.iotkit.comp.tcp.parser.builder.DirectPayloadBuilder;
import cc.iotkit.comp.tcp.parser.builder.FixPayloadBuilder;
import cc.iotkit.comp.tcp.parser.builder.ScriptPayloadBuilder;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author huangwenl
 * @date 2022-10-13
 */
public class ParserStrategyBuilder {
    private static Map<String, PayloadParserBuilderStrategy> strategyMap = new ConcurrentHashMap<>();

    static  {
        register(new DelimitedPayloadBuilder());
        register(new DirectPayloadBuilder());
        register(new FixPayloadBuilder());
        register(new ScriptPayloadBuilder());
    }

    public static PayloadParser build(String type, Map<String, Object> configuration) {
        return Optional.ofNullable(strategyMap.get(type))
                .map(builder -> builder.build(configuration))
                .orElseThrow(() -> new UnsupportedOperationException("unsupported parser:" + type));
    }

    private static void register(PayloadParserBuilderStrategy strategy) {
        strategyMap.put(strategy.getType().getText(), strategy);
    }
}
