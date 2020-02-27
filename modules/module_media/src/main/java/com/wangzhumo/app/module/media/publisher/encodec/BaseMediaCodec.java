package com.wangzhumo.app.module.media.publisher.encodec;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.view.Surface;

import com.wangzhumo.app.mdeia.gles.EGLCore;
import com.wangzhumo.app.mdeia.gles.IGLRenderer;
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
    private IGLRenderer mRenderer;


    /**
     * The renderer only renders
     */
    public final static int RENDERMODE_WHEN_DIRTY = 0;
    /**
     * The renderer is called
     * continuously to re-render the scene.
     */
    public final static int RENDERMODE_CONTINUOUSLY = 1;



    //video format
    private MediaCodec mediaCodec;
    private MediaFormat mediaFormat;
    private MediaCodec.BufferInfo bufferInfo;


    /**
     * 渲染的线程.
     */
    static class MediaThread extends Thread{
        private WeakReference<BaseMediaCodec> codecWeak;
        private EGLCore eglCore;
        private boolean shouldQuit = false;
        private boolean alreadyCreate = true;
        private boolean alreadyChange = true;
        private boolean isStart;
        private Object lockObj;
        private WindowSurface mWindowSurface;

        private int width;
        private int height;

        private int mRenderMode = RENDERMODE_CONTINUOUSLY;

        public MediaThread(BaseMediaCodec codecWeakReference) {
            this.codecWeak = new WeakReference<>(codecWeakReference);
        }


        @Override
        public void run() {
            super.run();
            shouldQuit = false;
            isStart = false;
            lockObj = new Object();
            eglCore = new EGLCore(codecWeak.get().mEGLCore.getEGLContext(),EGLCore.FLAG_RECORDABLE);

            //开启循环
            while(true){
                //判断渲染模式
                if (mRenderMode == RENDERMODE_WHEN_DIRTY) {
                    //手动刷新
                    synchronized (lockObj) {
                        try {
                            lockObj.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    //自动刷新
                    try {
                        Thread.sleep(1000 / 60);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                create();
                change();
                onDraw();

                //退出循环.
                if (shouldQuit){
                    release();
                    break;
                }
            }
        }
        /**
         * 进入 surfaceCreate
         */
        private void create() {
            if (codecWeak.get() != null && codecWeak.get().mRenderer != null) {
                alreadyCreate = true;
                codecWeak.get().mRenderer.onSurfaceCreate();
            }
        }


        /**
         * 进入 surfaceChange
         */
        private void change() {
            if (codecWeak.get() != null && codecWeak.get().mRenderer != null) {
                codecWeak.get().mRenderer.onSurfaceChange(width, height);
            }
        }

        /**
         * drawFrame
         */
        private void onDraw() {
            if (codecWeak.get() != null && codecWeak.get().mRenderer != null) {
                codecWeak.get().mRenderer.drawFrame();
                if (!isStart) {
                    codecWeak.get().mRenderer.drawFrame();
                }
                mWindowSurface.swapBuffers();
            }
        }


        /**
         * 设置渲染模式
         *
         * @param renderMode
         */
        public void setRenderMode(int renderMode) {
            if (!((RENDERMODE_WHEN_DIRTY <= renderMode) && (renderMode <= RENDERMODE_CONTINUOUSLY))) {
                throw new IllegalArgumentException("renderMode");
            }
            mRenderMode = renderMode;
        }

        /**
         * release
         */
        public void release() {
            lockObj.notifyAll();
            if (mWindowSurface != null) {
                mWindowSurface.releaseEglSurface();
                mWindowSurface.release();
            }
            if (codecWeak != null){
                codecWeak = null;
            }
        }
    }


    /**
     * 视频编码线程.
     */
    static class VideoCodecThread extends Thread{
        private WeakReference<BaseMediaCodec> codecWeak;
        //video format
        private MediaCodec mediaCodec;
        private MediaFormat mediaFormat;
        private MediaCodec.BufferInfo bufferInfo;

        public VideoCodecThread(BaseMediaCodec codecWeakReference) {
            this.codecWeak = new WeakReference<>(codecWeakReference);
            this.mediaCodec = codecWeakReference.mediaCodec;
            this.mediaFormat = codecWeakReference.mediaFormat;
            this.bufferInfo = codecWeakReference.bufferInfo;
        }
    }
}
