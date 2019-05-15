package com.wangzhumo.app.webrtc.signal;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-04-20  13:37
 */
public class Signaling {

    private Socket mSocket;
    private String mRoomName;
    private int mState;
    private List<SignalEventListener> mSignalListener;

    private Signaling() {
        mSignalListener = new ArrayList<>();
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
        mState = SignalState.CONNECT;
        //开始链接
        try {
            mSocket = IO.socket(address);
            mSocket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            mState = SignalState.IDLE;
            return false;
        }
        //链接后更新this.mRoomName = room;
        this.mRoomName = room;
        mState = SignalState.WORKING;


        handleSignalEvent(mSocket);

        //发送进入房间的请求
        mSocket.emit(Action.REQUEST_JOIN, mRoomName);
        //注册监听
        return true;
    }


    /**
     * 设置一系列的监听
     *
     * @param socket Socket
     */
    private void handleSignalEvent(final Socket socket) {
        socket.on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if (mSignalListener != null && !mSignalListener.isEmpty()) {
                    for (SignalEventListener signalEventListener : mSignalListener) {
                        signalEventListener.onError(new RuntimeException("Connect Error."));
                    }
                }
            }
        });

        socket.on(Socket.EVENT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if (mSignalListener != null && !mSignalListener.isEmpty()) {
                    for (SignalEventListener signalEventListener : mSignalListener) {
                        if (args != null && args.length > 0){
                            signalEventListener.onError(new RuntimeException(args[0].toString()));
                        }else{
                            signalEventListener.onError(new RuntimeException("Create Connection Error"));
                        }
                    }
                }
            }
        });

        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if (mSignalListener != null && !mSignalListener.isEmpty()) {
                    for (SignalEventListener signalEventListener : mSignalListener) {
                        signalEventListener.onConnected();
                    }
                }
            }
        });

        socket.on(Socket.EVENT_CONNECTING, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if (mSignalListener != null && !mSignalListener.isEmpty()) {
                    for (SignalEventListener signalEventListener : mSignalListener) {
                        signalEventListener.onConnecting();
                    }
                }
            }
        });

        socket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if (mSignalListener != null && !mSignalListener.isEmpty()) {
                    for (SignalEventListener signalEventListener : mSignalListener) {
                        signalEventListener.onDisconnect();
                    }
                }
            }
        });

        socket.on(Action.RECEIVE_JOINED, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String room_name = (String) args[0];
                String user_id = (String) args[1];
                if (mSignalListener != null && !mSignalListener.isEmpty()) {
                    for (SignalEventListener signalEventListener : mSignalListener) {
                        signalEventListener.onUserJoined(room_name, user_id);
                    }
                }
            }
        });

        socket.on(Action.RECEIVE_LEAVED, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String room_name = (String) args[0];
                String user_id = (String) args[1];
                if (mSignalListener != null && !mSignalListener.isEmpty()) {
                    for (SignalEventListener signalEventListener : mSignalListener) {
                        signalEventListener.onUserLeaved(room_name, user_id);
                    }
                }
                onDestroy();
            }
        });

        socket.on(Action.RECEIVE_OTHER_JOIN, new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                String roomName = (String) args[0];
                String userId = (String) args[1];
                if (mSignalListener != null && !mSignalListener.isEmpty()) {
                    for (SignalEventListener signalEventListener : mSignalListener) {
                        signalEventListener.onRemoteUserJoin(roomName, userId);
                    }
                }
            }
        });

        socket.on(Action.RECEIVE_OTHER_LEAVE, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String roomName = (String) args[0];
                String userId = (String) args[1];
                if (mSignalListener != null && !mSignalListener.isEmpty()) {
                    for (SignalEventListener signalEventListener : mSignalListener) {
                        signalEventListener.onRemoteUserLeave(roomName, userId);
                    }
                }

            }
        });

        socket.on(Action.RECEIVE_FULL, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                //释放资源
                onDestroy();

                String roomName = (String) args[0];
                String userId = (String) args[1];
                if (mSignalListener != null && !mSignalListener.isEmpty()) {
                    for (SignalEventListener signalEventListener : mSignalListener) {
                        signalEventListener.onJoinError(roomName, userId);
                    }
                }
            }
        });

        socket.on(Action.RECEIVE_MESSAGE, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String roomName = (String) args[0];
                JSONObject msg = (JSONObject) args[1];
                if (mSignalListener != null && !mSignalListener.isEmpty()) {
                    for (SignalEventListener signalEventListener : mSignalListener) {
                        signalEventListener.onMessage(msg);
                    }
                }

            }
        });
    }

    /**
     * 发送消息
     * @param args  key-value
     */
    public void sendMessage(String ...args) {
        if (args != null && args.length > 0){
            try {
                JSONObject jsonObject = new JSONObject();
                for (int i = 0; i < args.length; i+=2) {
                    jsonObject.put(args[i],args[i+1]);
                }
                if (mSocket != null){
                    mSocket.send(jsonObject);
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (mSignalListener != null && !mSignalListener.isEmpty()) {
                    for (SignalEventListener signalEventListener : mSignalListener) {
                        signalEventListener.onError(e);
                    }
                }
            }


        }
    }

    /**
     * 离开房间
     */
    public boolean leaveRoom() {
        if (mSocket == null) {
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
        if (mSignalListener != null) {
            mSignalListener.clear();
            mSignalListener = null;
        }
    }


    /**
     * 获取状态
     *
     * @return SignalState
     */
    public int getState() {
        return mState;
    }


    /**
     * 添加一个回调
     *
     * @param listener SignalEventListener
     */
    public void addSignalListener(SignalEventListener listener) {
        if (mSignalListener != null) {
            mSignalListener.add(listener);
        }
    }

    /**
     * 添加一个回调
     *
     * @param listener SignalEventListener
     */
    public void removeSignalListener(SignalEventListener listener) {
        if (mSignalListener != null && !mSignalListener.isEmpty()) {
            mSignalListener.remove(listener);
        }
    }

    public void clearSignalListener() {
        if (mSignalListener != null && !mSignalListener.isEmpty()) {
            mSignalListener.clear();
        }
    }
}
