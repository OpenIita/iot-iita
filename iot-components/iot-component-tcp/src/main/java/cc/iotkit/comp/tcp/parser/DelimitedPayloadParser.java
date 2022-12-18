package cc.iotkit.comp.tcp.parser;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.parsetools.RecordParser;
import org.apache.commons.lang3.StringEscapeUtils;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.function.Function;

/**
 * 分隔符
 *
 * @author huangwenl
 * @date 2022-10-13
 */
public class DelimitedPayloadParser implements PayloadParser {

    private String delimited;
    private final EmitterProcessor<Buffer> processor = EmitterProcessor.create(false);
    private final FluxSink<Buffer> sink = processor.sink(FluxSink.OverflowStrategy.BUFFER);

    private RecordParser recordParser;


    public PayloadParser init(Object delimited) {
        this.delimited = StringEscapeUtils.unescapeJava(delimited.toString());
        this.reset();
        return this;
    }


    @Override
    public void handle(Buffer buffer) {
        recordParser.handle(buffer);
    }

    @Override
    public Flux<Buffer> handlePayload() {
        return processor.map(Function.identity());
    }

    @Override
    public void reset() {
        this.recordParser = RecordParser.newDelimited(delimited);
        // 塞入 skin pusher
        this.recordParser.handler(sink::next);
    }

    @Override
    public void close() {
        processor.onComplete();
    }
}
