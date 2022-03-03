package cc.iotkit.tppa.xiaodu.handler;

public interface IRequestHandler<T, R> {

    String getName();

    Class getRequestType();

    R handle(T request);

}
