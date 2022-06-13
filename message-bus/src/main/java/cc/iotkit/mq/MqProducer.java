package cc.iotkit.mq;

public interface MqProducer<T> {

    void publish(String topic, T msg);

}
