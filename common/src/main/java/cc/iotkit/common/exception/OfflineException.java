package cc.iotkit.common.exception;


public class OfflineException extends BizException {

    public OfflineException() {
    }

    public OfflineException(String message) {
        super(message);
    }

    public OfflineException(String message, Throwable cause) {
        super(message, cause);
    }

    public OfflineException(Throwable cause) {
        super(cause);
    }
}
