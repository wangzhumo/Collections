package com.wangzhumo.app.webrtc.func;

import org.webrtc.MediaConstraints;

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-04-20  15:45
 */
public class StreamFormatConfig {

    public static final VideoFormat VIDEO_NORMAL = new VideoFormat(1280, 720, 30);

    public static MediaConstraints getAudioConstraint(){
        return new MediaConstraints();
    }
}
