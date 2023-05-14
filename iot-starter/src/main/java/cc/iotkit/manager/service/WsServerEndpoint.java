package cc.iotkit.manager.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/iotkit-ws")
@Component
@Slf4j
public class WsServerEndpoint {

    //记录连接的客户端
    public static Map<String, Session> clients = new ConcurrentHashMap<>();


    private String sid = null;




    /**
     * 连接成功后调用的方法
     * @param session
     */
    @OnOpen
    public void onOpen(Session session) {
        this.sid = UUID.randomUUID().toString();

        clients.put(this.sid, session);
        log.info(this.sid + "连接开启！");
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        log.info(this.sid + "连接断开！");
        clients.remove(this.sid);
    }

    /**
     * 判断是否连接的方法
     * @return
     */
    public static boolean isServerClose() {
        if (WsServerEndpoint.clients.values().size() == 0) {
            log.info("已断开");
            return true;
        }else {
            log.info("已连接");
            return false;
        }
    }


    /**
     * 发送给所有用户
     * @param message
     */
    public static void sendMessage(String message){
        for (Session session1 : WsServerEndpoint.clients.values()) {
            try {
                session1.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 收到客户端消息后调用的方法
     * @param message
     * @param session
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("收到来自窗口"+"的信息:"+message);
    }

    /**
     * 发生错误时的回调函数
     * @param error
     */
    @OnError
    public void onError(Throwable error) {
        log.info("错误");
        error.printStackTrace();
    }


}