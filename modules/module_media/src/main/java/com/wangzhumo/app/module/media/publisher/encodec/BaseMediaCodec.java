package com.wangzhumo.app.module.media.publisher.encodec;

import com.wangzhumo.app.mdeia.gles.EGLCore;

import java.lang.ref.WeakReference;

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2020-01-19  21:01
 *
 * 使用Codec编解码.
 */
public abstract class BaseMediaCodec {



    static class MediaThread extends Thread{
        private WeakReference<BaseMediaCodec> codecWeakReference;
        private EGLCore eglCore;


    }
}
