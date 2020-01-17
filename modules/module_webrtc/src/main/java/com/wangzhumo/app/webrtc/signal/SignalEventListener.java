package com.wangzhumo.app.webrtc.signal;

import org.json.JSONObject;

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-04-20  16:39
 *
 * 信令服务器的一系列回调
 */
public interface SignalEventListener {

    void onConnected(); //链接成功
    void onConnecting(); //连接中
    void onDisconnect(); //断开链接
    void onUserJoined(String room,String uid);  //用户成功加入
    void onUserLeaved(String room,String uid);  //用户离开
    void onRemoteUserJoin(String room,String uid);  //远端用户加入
    void onRemoteUserLeave(String room,String uid);  //远端用户离开
    void onMessage(JSONObject message);   //发送来消息
    void onJoinError(String room,String uid);   //房间加入失败
    void onError(Exception e);   //建立连接失败
    void onSend(String type); //发送消息
}
