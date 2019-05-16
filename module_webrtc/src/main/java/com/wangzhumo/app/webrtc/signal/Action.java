package com.wangzhumo.app.webrtc.signal;

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-04-20  14:22
 */
public interface Action {

    /**
     * 请求加入
     */
    String REQUEST_JOIN = "join";

    /**
     * 请求退出
     */
    String REQUEST_EXIT = "leave";


    /**
     * 已经加入
     */
    String RECEIVE_JOINED = "joined";
    String RECEIVE_OTHER_JOIN = "otherjoin";

    /**
     * 已经离开
     */
    String RECEIVE_LEAVED = "leaved";
    String RECEIVE_OTHER_LEAVE = "bye";


    /**
     * 房间满
     */
    String RECEIVE_FULL = "full";

    /**
     * 收到消息
     */
    String RECEIVE_MESSAGE = "message";


}
