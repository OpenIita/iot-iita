package cc.iotkit.comp.tcp.parser;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.parsetools.RecordParser;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.function.Function;

/**
 * 固定长度
 *
 * @author huangwenl
 * @date 2022-10-13
 */
public class FixPayloadParser implements PayloadParser {

    private int size;
    private final EmitterProcessor<Buffer> processor = EmitterProcessor.create(false);
    private final FluxSink<Buffer> sink = processor.sink(FluxSink.OverflowStrategy.BUFFER);

    private RecordParser recordParser;


    public PayloadParser init(Object size) {
        this.size = (int) size;
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
        this.recordParser = RecordParser.newFixed(size);
        this.recordParser.handler(sink::next);
    }

    @Override
    public void close() {
        processor.onComplete();
    }
}
