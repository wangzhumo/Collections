package com.wangzhumo.app.webrtc.func;

import android.support.annotation.StringDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.wangzhumo.app.webrtc.func.CallState.*;

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-04-20  15:48
 */
@StringDef({INIT,JOINED_UNBIND,LEAVED,JOINED})
@Retention(RetentionPolicy.SOURCE)
public @interface CallState {
    String INIT = "init";
    String JOINED = "joined";
    String JOINED_UNBIND = "joined_unbind";
    String JOINED_CONN = "joined_conn";
    String LEAVED = "leaved";
}
