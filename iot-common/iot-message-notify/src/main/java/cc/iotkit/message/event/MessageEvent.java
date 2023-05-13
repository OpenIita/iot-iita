package cc.iotkit.message.event;

import cc.iotkit.message.model.MessageSend;
import org.springframework.context.ApplicationEvent;

/**
 * author: 石恒
 * date: 2023-05-11 14:14
 * description:
 **/

public class MessageEvent extends ApplicationEvent {
    private MessageSend message;

    public MessageEvent(MessageSend message) {
        super(message);
        this.message = message;
    }

    public MessageSend getMessage() {
        return message;
    }

    public void setMessage(MessageSend message) {
        this.message = message;
    }
}
