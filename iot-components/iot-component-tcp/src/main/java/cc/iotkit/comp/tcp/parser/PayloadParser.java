package cc.iotkit.comp.tcp.parser;

import io.vertx.core.buffer.Buffer;
import reactor.core.publisher.Flux;

/**
 * @author huangwenl
 * @date 2022-10-13
 */
public interface PayloadParser {


    PayloadParser init(Object param);

    void handle(Buffer buffer);

    /**
     * 订阅完整的数据包流,每一个元素为一个完整的数据包
     *
     * @return 完整数据包流
     */
    Flux<Buffer> handlePayload();


    /**
     * 重置规则
     */
    default void reset() {
    }

    void close();
}
