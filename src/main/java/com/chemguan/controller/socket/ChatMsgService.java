package com.chemguan.controller.socket;

import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
@ServerEndpoint(value ="/websocket/{userParam}")
@Component
public class ChatMsgService {
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    private static CopyOnWriteArraySet<ChatMsgService> webSocketSet=new CopyOnWriteArraySet<ChatMsgService>();

    private Session wsSession;

    private static ConcurrentHashMap<String,Session> webSocketMap = new ConcurrentHashMap<String,Session>();//建立连接的方法

    private static synchronized void addOnlineCount(){
        onlineCount++;
    }

    private static synchronized void subOnlineCount(){
        onlineCount--;
    }

    private static synchronized int getOnlineCount(){
        return onlineCount;
    }

    //send message to client
    private void sendMessage(Session session,String message){
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //broadcast message
    private void broadcast(String message){
        try {
            if (webSocketSet.size() != 0) {
                for (ChatMsgService s : webSocketSet) {
                    if (s != null) {
                        s.wsSession.getBasicRemote().sendText(message);
                    }
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



    @OnOpen
    public void onOpen(Session session, @PathParam("userParam")String  userParam){
        webSocketSet.add(this);
        webSocketMap.put(userParam, session);
    	addOnlineCount(); //在线数加
        this.wsSession=session;
    }
    
    /**
    * 连接关闭调用的方法
    */
    @OnClose
    public void onClose(Session session){
        webSocketSet.remove(this);
    	subOnlineCount(); //在线数减
    }
    
    /**
    * 收到客户端消息后调用的方法
    * @param message 客户端发送过来的消息
    * @param session 可选的参数
    */
    @OnMessage
    public void onMessage(String message, Session session) {
        ClientMessage cm= ClientMessage.praseMessage(message);
        if(cm.getType().equals(MessageType.TYPE_USER_INFO)){
            //message=new ServerMessage(MessageType.TYPE_SERVER, cm.getUserid(), "add").toJson();
            //broadcast(message);
        }
        if(cm.getType().equals(MessageType.TYPE_USER_CHAT)){
            //判断是否在聊天室
            for(String userid:webSocketMap.keySet()){
                if(cm.getUserid().equals(userid)||cm.getTouserid().equals(userid)){
                    sendMessage(webSocketMap.get(userid),message);
                }
            }
        }
    }
    
    /**
    * 发生错误时调用
    * @param session
    * @param error
    */
    @OnError
    public void onError(Session session, Throwable error){
    	error.printStackTrace();
    }
}
