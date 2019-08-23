package com.wangzhumo.app.webrtc.signal;


import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@StringDef({SignalType.OFFER,SignalType.ANSWER,SignalType.CANDIDATE})
@Retention(RetentionPolicy.SOURCE)
public @interface SignalType {

    String OFFER = "offer";
    String ANSWER = "answer";
    String CANDIDATE = "candidate";
}
