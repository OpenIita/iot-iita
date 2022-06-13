package cc.iotkit.mq;

public interface MqConsumer<T> {

    void consume(String topic, ConsumerHandler<T> handler);

}
