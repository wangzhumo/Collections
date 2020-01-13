package com.wangzhumo.app.module.opengl.customgl;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.wangzhumo.app.module.opengl.gles.EGLCore;
import com.wangzhumo.app.module.opengl.gles.IGLRenderer;
import com.wangzhumo.app.module.opengl.gles.WindowSurface;

import java.lang.ref.WeakReference;


/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2020-01-07  11:38
 * <p>
 * 自定义的GLSurfaceView
 */
public class CustomGLSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private EGLCore mEglCore;
    private Surface mSurface;
    private WzmGLThread mGLThread;
    private IGLRenderer mRenderer;
    private int mRenderMode = RENDERMODE_CONTINUOUSLY;

    /**
     * The renderer only renders
     *
     * @see #setRenderMode(int)
     */
    public final static int RENDERMODE_WHEN_DIRTY = 0;
    /**
     * The renderer is called
     * continuously to re-render the scene.
     */
    public final static int RENDERMODE_CONTINUOUSLY = 1;


    public CustomGLSurfaceView(Context context) {
        this(context, null);
    }

    public CustomGLSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomGLSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        getHolder().addCallback(this);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (mSurface == null) {
            mSurface = holder.getSurface();
        }
        if (mEglCore == null) {
            mEglCore = new EGLCore(null, EGLCore.FLAG_RECORDABLE);
        }

        //开启之前,判断renderer
        mGLThread = new WzmGLThread(this);
        mGLThread.alreadyCreate = false;
        mGLThread.setRenderMode(mRenderMode);
        mGLThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (mGLThread != null) {
            mGLThread.width = width;
            mGLThread.height = height;
            mGLThread.alreadyChange = false;
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mEglCore.release();
        mGLThread.release();
    }

    /**
     * 外部提供
     *
     * @param surface Surface
     * @param eglCore EGLCore
     */
    public void setSurfaceAndCore(Surface surface, EGLCore eglCore) {
        this.mSurface = surface;
        this.mEglCore = eglCore;
    }

    /**
     * 设置Renderer
     *
     * @param renderer renderer
     */
    public void setRenderer(IGLRenderer renderer) {
        if (mGLThread != null) {
            throw new IllegalStateException("setRenderer has already been called for this instance.");
        }
        this.mRenderer = renderer;
    }

    public IGLRenderer getRenderer() {
        return mRenderer;
    }

    /**
     * 设置Renderer Mode
     *
     * @param renderMode
     */
    public void setRenderMode(int renderMode) {
        if (mRenderer == null) {
            throw new IllegalStateException("setRenderer must call first.");
        }
        this.mRenderMode = renderMode;
        if (mGLThread != null){
            mGLThread.setRenderMode(mRenderMode);
        }
    }

    /**
     * 外部调用刷新.
     */
    public void requestRender() {
        mGLThread.requestRender();
    }

    /**
     * 提供给外部使用.
     *
     * @return EGLCore
     */
    public EGLCore getEGLCore() {
        return mEglCore;
    }


    /**
     * 销毁
     */
    public void release(){
        if (mGLThread != null) {
            mGLThread.release();
        }
    }


    /**
     * 仿写GLThread
     */
    static class WzmGLThread extends Thread {
        private WeakReference<CustomGLSurfaceView> glSurfaceView;
        private WindowSurface mWindowSurface;
        private boolean shouldQuit = false;
        private boolean alreadyCreate = true;
        private boolean alreadyChange = true;

        private boolean isStartRenderer;

        private int width;
        private int height;

        private int mRenderMode = RENDERMODE_CONTINUOUSLY;


        public WzmGLThread(CustomGLSurfaceView glSurfaceView) {
            this.glSurfaceView = new WeakReference<>(glSurfaceView);
        }

        @Override
        public void run() {
            super.run();

            //初始化WzmGLThread
            mWindowSurface = new WindowSurface(
                    glSurfaceView.get().mEglCore,
                    glSurfaceView.get().mSurface,
                    true);

            while (true) {
                //判断渲染模式
                if (mRenderMode == RENDERMODE_WHEN_DIRTY) {
                    //手动刷新
                    synchronized (sGLThreadManager) {
                        try {
                            sGLThreadManager.wait();
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

                isStartRenderer = true;

                //如果需要暂停,则退出循环
                if (shouldQuit) {
                    release();
                    break;
                }
            }
        }


        /**
         * 进入 surfaceCreate
         */
        private void create() {
            if (glSurfaceView.get() != null && glSurfaceView.get().mRenderer != null) {
                alreadyCreate = true;
                // TODO: 2020-01-12 取消这个
                mWindowSurface.makeCurrent();
                glSurfaceView.get().mRenderer.onSurfaceCreate();
                // TODO: 2020-01-12 取消这个
                if (glSurfaceView.get() != null) {
                    glSurfaceView.get().mEglCore.makeNothingCurrent();
                }
            }
        }


        /**
         * 进入 surfaceChange
         */
        private void change() {
            if (glSurfaceView.get() != null && glSurfaceView.get().mRenderer != null) {
                glSurfaceView.get().mRenderer.onSurfaceChange(width, height);
            }
        }

        /**
         * drawFrame
         */
        private void onDraw() {
            if (glSurfaceView.get() != null && glSurfaceView.get().mRenderer != null) {
                // TODO: 2020-01-12 取消这个
                mWindowSurface.makeCurrent();
                glSurfaceView.get().mRenderer.drawFrame();
                if (!isStartRenderer) {
                    glSurfaceView.get().mRenderer.drawFrame();
                }
                mWindowSurface.swapBuffers();
                // TODO: 2020-01-12 取消这个
                if (glSurfaceView.get() != null) {
                    glSurfaceView.get().mEglCore.makeNothingCurrent();
                }
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
         * 开始渲染刷新
         */
        public void requestRender() {
            synchronized (sGLThreadManager) {
                sGLThreadManager.notifyAll();
            }
        }

        /**
         * release
         */
        public void release() {
            sGLThreadManager.threadExiting(this);
            if (mWindowSurface != null) {
                mWindowSurface.releaseEglSurface();
                mWindowSurface.release();
            }
            if (glSurfaceView != null) {
                glSurfaceView.clear();
            }
        }
    }


    private static class GLThreadManager {
        private static String TAG = "GLThreadManager";

        public synchronized void threadExiting(WzmGLThread thread) {
            Log.i("GLThread", "exiting tid=" + thread.getId());
            thread.shouldQuit = true;
            notifyAll();
        }

        /*
         * Releases the EGL context. Requires that we are already in the
         * sGLThreadManager monitor when this is called.
         */
        public void releaseEglContextLocked(WzmGLThread thread) {
            notifyAll();
        }
    }

    private static final GLThreadManager sGLThreadManager = new GLThreadManager();
}
