package com.wangzhumo.app.webrtc.signal;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.wangzhumo.app.webrtc.signal.SignalState.*;

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-04-20  14:32
 */

@IntDef({IDLE,CONNECT,RETRY,WORKING,UN_CONNECT,ERROR})
@Retention(RetentionPolicy.SOURCE)
public @interface SignalState {

    //空闲
    int IDLE = 0;

    //链接中
    int CONNECT = 10;

    //重试
    int RETRY = 15;

    //工作中
    int WORKING = 20;

    //未链接
    int UN_CONNECT = 30;
    //错误
    int ERROR = 35;

}
