package com.wangzhumo.app.module.media.publisher.encodec;

import android.util.Log;
import android.view.Surface;

import com.wangzhumo.app.mdeia.CustomGLSurfaceView;
import com.wangzhumo.app.mdeia.gles.EGLCore;
import com.wangzhumo.app.mdeia.gles.WindowSurface;

import java.lang.ref.WeakReference;

import static com.wangzhumo.app.mdeia.CustomGLSurfaceView.RENDERMODE_CONTINUOUSLY;

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2020-01-19  21:01
 *
 * 使用Codec编解码.
 */
public abstract class BaseMediaCodec {


    private int mRenderMode = RENDERMODE_CONTINUOUSLY;
    private Surface mSurface;
    private EGLCore mEGLCore;

    static class MediaThread extends Thread{
        private WeakReference<BaseMediaCodec> codecWeak;
        private EGLCore eglCore;
        private boolean shouldQuit = false;
        private boolean alreadyCreate = true;
        private boolean alreadyChange = true;
        private boolean isStart;
        private Object lockObj;
        private WindowSurface mWindowSurface;

        public MediaThread(BaseMediaCodec codecWeakReference) {
            this.codecWeak = new WeakReference<>(codecWeakReference);
        }


        @Override
        public void run() {
            super.run();
            shouldQuit = false;
            isStart = false;
            eglCore = new EGLCore(codecWeak.get().mEGLCore.getEGLContext(),EGLCore.FLAG_RECORDABLE);



            while(shouldQuit){

            }
        }
    }


}
