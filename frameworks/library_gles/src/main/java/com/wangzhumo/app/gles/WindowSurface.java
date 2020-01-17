package com.wangzhumo.app.gles;

import android.graphics.SurfaceTexture;
import android.view.Surface;

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2020-01-07  10:58
 *
 * Recordable EGL window surface.
 */
public class WindowSurface extends EGLSurfaceBase {

    private Surface mSurface;
    private boolean mReleaseSurface;


    /**
     * 创建一个屏幕上的Surface,用于渲染显示
     * @param eglCore eglContext
     * @param surface surface
     * @param releaseSurface bool Set releaseSurface to true if you want the Surface to be released when release() is called
     */
    public WindowSurface(EGLCore eglCore,Surface surface, boolean releaseSurface) {
        super(eglCore);
        //创建一个windowSurface
        createWindowSurface(surface);
        //保存变量
        this.mSurface = surface;
        this.mReleaseSurface = releaseSurface;
    }

    /**
     * Associates an EGL surface with the SurfaceTexture.
     */
    public WindowSurface(EGLCore eglCore, SurfaceTexture surfaceTexture) {
        super(eglCore);
        createWindowSurface(surfaceTexture);
    }

    /**
     * Releases any resources associated with the EGL surface (and, if configured to do so,
     * with the Surface as well).
     * <p>
     * Does not require that the surface's EGL context be current.
     */
    public void release() {
        releaseEglSurface();
        if (mSurface != null) {
            if (mReleaseSurface) {
                mSurface.release();
            }
            mSurface = null;
        }
    }


    /**
     *
     * 使用新的EglCore - EglContext重新创建一个EGLSurface,必须在releaseEglSurface之后调用
     *
     * <p>
     * This is useful when we want to update the EGLSurface associated with a Surface.
     * For example, if we want to share with a different EGLContext, which can only
     * be done by tearing down and recreating the context.  (That's handled by the caller;
     * this just creates a new EGLSurface for the Surface we were handed earlier.)
     * <p>
     * If the previous EGLSurface isn't fully destroyed, e.g. it's still current on a
     * context somewhere, the create call will fail with complaints from the Surface
     * about already being connected.
     */
    public void recreate(EGLCore newEglCore) {
        if (mSurface == null) {
            throw new RuntimeException("not yet implemented for SurfaceTexture");
        }
        mEglCore = newEglCore;          // switch to new context
        createWindowSurface(mSurface);  // create new surface
    }
}
