package com.wangzhumo.app.webrtc.signal;

import android.util.Log;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import java.net.URISyntaxException;

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-04-20  13:37
 */
public class Signaling {

    private static final String TAG = "Signaling";

    private Socket mSocket;
    private String mRoomName;
    private int mState;

    private Signaling() {
        mState = SignalState.IDLE;
    }

    private static class Singleton {
        private static Signaling instance = new Signaling();
    }


    public static Signaling getInstance() {
        return Singleton.instance;
    }


    /**
     * 加入房间
     *
     * @param address 地址
     * @param room    房间名字
     */
    public boolean joinRoom(String address, String room) {
        Log.d(TAG, "joinRoom() called with: address = [" + address + "], room = [" + room + "]");
        mState = SignalState.CONNECT;
        //开始链接
        try {
            mSocket = IO.socket(address);
            mSocket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            Log.e(TAG, "joinRoom: connect", e);
            mState = SignalState.IDLE;
            return false;
        }
        //链接后更新this.mRoomName = room;
        this.mRoomName = room;
        mState = SignalState.WORKING;


        mSocket.on(Action.RECEIVE_JOINED, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String room_name = (String) args[0];
                String user_id = (String) args[1];
                Log.d(TAG, "RECEIVE_JOINED: roomName = [" + room_name + "]   userId = [" + user_id + "]");
            }
        });

        mSocket.on(Action.RECEIVE_LEAVED, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String room_name = (String) args[0];
                String user_id = (String) args[1];
                Log.d(TAG, "RECEIVE_LEAVED: roomName = [" + room_name + "]   userId = [" + user_id + "]");
                onDestroy();
            }
        });

        //发送进入房间的请求
        mSocket.emit(Action.REQUEST_JOIN, mRoomName);
        //注册监听
        return true;
    }


    /**
     * 离开房间
     */
    public boolean leaveRoom() {
        if (mSocket == null) {
            Log.e(TAG, "leaveRoom: Socket is Null");
            mState = SignalState.ERROR;
            return false;
        }

        //发送退出房间的请求
        mSocket.emit(Action.REQUEST_EXIT, mRoomName);
        //注册监听
        return true;
    }


    /**
     * 注销
     */
    public void onDestroy() {
        if (mSocket != null) {
            mSocket.disconnect();
            mSocket.close();
            mState = SignalState.IDLE;
            mSocket = null;
        }
    }


    /**
     * 获取状态
     * @return SignalState
     */
    public int getState() {
        return mState;
    }
}
