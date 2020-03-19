package com.wangzhumo.app.module.media.publisher.encodec;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.view.Surface;

import com.wangzhumo.app.mdeia.gles.EGLCore;
import com.wangzhumo.app.mdeia.gles.IGLRenderer;
import com.wangzhumo.app.mdeia.gles.WindowSurface;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2020-01-19  21:01
 *
 * 使用Codec编解码.
 */
public abstract class BaseMediaCodec {


    private int mRenderMode = RENDERMODE_CONTINUOUSLY;  //渲染模式
    private Surface mSurface;
    private EGLCore mEGLCore;    //egl环境
    private IGLRenderer mRenderer;   //gl的渲染器


    /**
     * The renderer only renders
     */
    public final static int RENDERMODE_WHEN_DIRTY = 0;
    /**
     * The renderer is called
     * continuously to re-render the scene.
     */
    public final static int RENDERMODE_CONTINUOUSLY = 1;

    private int width,height;

    //video format
    private MediaCodec mediaCodec;      //mediaCodec的实例,用于解码
    private MediaFormat mediaFormat;    //媒体Format的类型信息.
    private MediaCodec.BufferInfo bufferInfo;   //媒体中的一个Buffer信息

    //解复用.
    private MediaMuxer mediaMuxer;

    //两个线程
    private MediaThread mediaThread;
    private VideoCodecThread videoCodecThread;

    /**
     * 初始化
     * @param eglCore   Egl环境
     * @param mediaPath 媒体的地址路径
     * @param mimeType  格式
     * @param width     宽度
     * @param height    高度
     */
    public void initEncoder(EGLCore eglCore, String mediaPath ,String mimeType,int width,int height){
        this.width = width;
        this.height = height;
        this.mEGLCore = eglCore;
        //初始化需要的编解码/解复用器
        initMediaEncode(mediaPath, mimeType, width, height);
    }


    /**
     * renderer的设置与获取
     * @return IGLRenderer
     */
    public IGLRenderer getRenderer() {
        return mRenderer;
    }

    public void setRenderer(IGLRenderer mRenderer) {
        this.mRenderer = mRenderer;
        //如果外部设置了render,就设置进去
    }

    /**
     * RenderMode的设置与获取
     * @return RenderMode
     */
    public int getRenderMode() {
        return mRenderMode;
    }

    public void setRenderMode(int renderMode) {
        this.mRenderMode = renderMode;
        //设置到thread
        if (mediaThread != null) {
            mediaThread.setRenderMode(mRenderMode);
        }
    }

    /**
     * 开始录制.
     * 开启线程,渲染并且录制.
     */
    public void startRecord(){
        if (mSurface != null && mEGLCore != null){
            //开始搞起.
            mediaThread = new MediaThread(this);
            videoCodecThread = new VideoCodecThread(this);

            mediaThread.isStart = true;
            mediaThread.alreadyChange = true;
            mediaThread.width = width;
            mediaThread.height = height;
            mediaThread.setRenderMode(mRenderMode);

            //启动线程
            mediaThread.start();
            videoCodecThread.start();
        }else{
            throw new IllegalStateException("pls load media info first");
        }
    }


    /**
     * 停止录制,渲染
     */
    public void stopRecord(){
        //如果是已经开始录制的.
        if (mediaThread != null){
            mediaThread.exit();
            mediaThread = null;
        }
        if (videoCodecThread != null){
            videoCodecThread.exit();
            videoCodecThread = null;
        }
    }


    /**
     * 设置要编码视频的信息,初始化mediaCodec
     * @param mimeType  格式
     * @param width     宽度
     * @param height    高度
     */
    private void initVideoEncode(String mimeType,int width,int height){
        try {
            bufferInfo = new MediaCodec.BufferInfo();

            mediaFormat = new MediaFormat();
            //设置参数.
            //输入为sufrace
            mediaFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);
            //设置码率(此处最大的  width * height = 像素的数量     * 4的原因是 ARGB)
            mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, width * height * 4);
            //设置关键帧
            mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, width * height * 4);
            //设置帧率
            mediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE, 30);
            //设置关键帧的间隔时间 (单位:  秒)
            mediaFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1);

            //创建一个MediaCodec的编码器
            mediaCodec = MediaCodec.createEncoderByType(mimeType);
            //编码不需要surface
            mediaCodec.configure(mediaFormat,null,null,MediaCodec.CONFIGURE_FLAG_ENCODE);

            //获取一个surface,用于渲染的
            mSurface = mediaCodec.createInputSurface();
        } catch (IOException e) {
            bufferInfo = null;
            mediaFormat = null;
            mediaCodec = null;
            e.printStackTrace();
        }
    }

    /**
     * 初始化mediaMuxer
     * @param mediaPath 媒体的地址路径
     * @param mimeType  格式
     * @param width     宽度
     * @param height    高度
     */
    private void initMediaEncode(String mediaPath ,String mimeType,int width,int height){
        try {
            mediaMuxer = new MediaMuxer(mediaPath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
            initVideoEncode(mimeType, width, height);
        } catch (IOException e) {
            mediaMuxer = null;
            e.printStackTrace();
        }
    }



    /**
     * 渲染的线程.
     */
    static class MediaThread extends Thread{

        //外部的全局变量信息.
        private WeakReference<BaseMediaCodec> codecWeak;
        private EGLCore eglCore;

        //当前的变量
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

                if (!alreadyCreate) {
                    create();
                    alreadyCreate = true;
                }

                if (!alreadyChange) {
                    change();
                    alreadyChange = true;
                }

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
            // TODO: 2020/3/19  初始化WzmGLThread
            mWindowSurface = new WindowSurface(
                    codecWeak.get().mEGLCore,
                    codecWeak.get().mSurface,
                    true);
            mWindowSurface.makeCurrent();

            //回调onSurfaceCreate
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
         * 退出线程.
         */
        public void exit(){
            shouldQuit = true;
            if (lockObj != null) {
                synchronized (lockObj){
                    lockObj.notifyAll();
                }
            }
        }

        /**
         * 通知渲染.
         */
        public void requestRender(){
            if (lockObj != null) {
                synchronized (lockObj){
                    lockObj.notifyAll();
                }
            }
        }

        /**
         * release
         */
        public void release() {
            if (lockObj != null) {
                lockObj.notifyAll();
            }
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

        //退出.
        private boolean shouldExit;


        public VideoCodecThread(BaseMediaCodec codecWeakReference) {
            this.codecWeak = new WeakReference<>(codecWeakReference);
            this.mediaCodec = codecWeakReference.mediaCodec;
            this.mediaFormat = codecWeakReference.mediaFormat;
            this.bufferInfo = codecWeakReference.bufferInfo;
        }


        @Override
        public void run() {
            super.run();
            shouldExit = false;
            //开始编解码
            mediaCodec.start();

            while (true) {
                //结束.
                if (shouldExit) {
                    mediaCodec.stop();
                    mediaCodec.release();
                    //停止循环
                    break;
                }

                //立即返回一个outputBuffer的信息.
                //Returns the index of an output buffer that has been successfully
                int outputBufferIndex = mediaCodec.dequeueOutputBuffer(bufferInfo, 0);
                //获取到这个buffer中的所有信息,有可能一次取不完
                while (outputBufferIndex >= 0){
                    //获取一个buffer
                    //Returns a read-only ByteBuffer for a dequeued output buffer index
                    ByteBuffer outputBuffer = mediaCodec.getOutputBuffer(outputBufferIndex);

                    //根据bufferInfo中的信息操作.
                    outputBuffer.position(bufferInfo.offset);   //设置偏移的量.
                    outputBuffer.limit(bufferInfo.offset + bufferInfo.size);

                    // TODO: 2020/3/19 编码

                    //释放资源
                    mediaCodec.releaseOutputBuffer(outputBufferIndex, false);

                    //重新获取output中的信息.
                    outputBufferIndex = mediaCodec.dequeueOutputBuffer(bufferInfo, 0);
                }
            }
        }

        public void exit(){
            this.shouldExit = true;
        }
    }
}
