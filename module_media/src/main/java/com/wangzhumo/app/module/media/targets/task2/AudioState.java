package com.wangzhumo.app.module.media.targets.task2;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-08-22$  23:35$
 */

@IntDef({AudioState.DISABLE, AudioState.INIT,
        AudioState.RECORDING,AudioState.PLAYING})
@Retention(RetentionPolicy.RUNTIME)
public @interface AudioState {
    int DISABLE = 0;
    int INIT = 1;
    int RECORDING = 20;
    int PLAYING = 30;
}
