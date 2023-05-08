package cc.iotkit.message.service;

import cc.iotkit.message.notify.EventManager;

/**
 * author: 石恒
 * date: 2023-05-08 16:02
 * description:
 **/
public class MessageService {

    public static void main(String[] args) {
        EventManager em = new EventManager();
        em.notify();
    }
}
