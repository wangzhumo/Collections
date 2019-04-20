package com.wangzhumo.app.webrtc.func;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.wangzhumo.app.webrtc.func.CallState.INIT;

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-04-20  15:48
 */
@StringDef({INIT})
@Retention(RetentionPolicy.SOURCE)
public @interface CallState {
    String INIT = "init";
}
