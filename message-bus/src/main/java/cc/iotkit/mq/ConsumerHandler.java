package cc.iotkit.mq;

public interface ConsumerHandler<T> {

    void handler(T msg);

}
