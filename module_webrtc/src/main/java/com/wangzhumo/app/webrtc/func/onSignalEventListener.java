package com.wangzhumo.app.webrtc.func;

import org.json.JSONObject;

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-04-20  16:39
 *
 * 信令服务器的一系列回调
 */
public interface onSignalEventListener {

    void onConnected(); //链接成功
    void onConnecting(); //连接中
    void onDisconnect(); //断开链接
    void onUserJoined(String room,String uid);  //用户成功加入
    void onUserLeaved(String room,String uid);  //用户离开
    void onRemoteUserJoin(String room,String uid);  //远端用户加入
    void onRemoteUserLeave(String room,String uid);  //远端用户离开
    void onMessage(JSONObject message);   //发送来消息
}
