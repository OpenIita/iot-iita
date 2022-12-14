package cc.iotkit.comp.tcp.parser;

import io.vertx.core.buffer.Buffer;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;

import java.util.function.Function;

/**
 * 不处理
 *
 * @author huangwenl
 * @date 2022-10-13
 */
public class DirectPayloadParser implements PayloadParser {


    EmitterProcessor<Buffer> processor = EmitterProcessor.create(false);


    @Override
    public PayloadParser init(Object param) {
        return this;
    }

    @Override
    public void handle(Buffer buffer) {
        processor.onNext(buffer);
    }

    @Override
    public Flux<Buffer> handlePayload() {
        return processor.map(Function.identity());
    }


    @Override
    public void close() {
        processor.onComplete();
    }
}
