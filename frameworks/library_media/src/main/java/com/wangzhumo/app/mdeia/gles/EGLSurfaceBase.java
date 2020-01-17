package com.wangzhumo.app.mdeia.gles;

import android.opengl.EGL14;
import android.opengl.EGLSurface;
import android.util.Log;

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2020-01-07  10:53
 *
 * Common base class for EGL surfaces.
 * 可以为同一个Context关联多个Surface
 */
public class EGLSurfaceBase {

    // EglCore object we're associated with.  It may be associated with multiple surfaces.
    protected EGLCore mEglCore;

    private EGLSurface mEglSurface = EGL14.EGL_NO_SURFACE;
    private int mWidth;
    private int mHeight;


    //必须提供一个 EGLCore,使用它的 ELGContext
    public EGLSurfaceBase(EGLCore eglCore){
        this.mEglCore = eglCore;
    }

    /**
     * Creates a window surface.
     * <p>
     * @param surface May be a Surface or SurfaceTexture.
     */
    public void createWindowSurface(Object surface) {
        if (mEglSurface != EGL14.EGL_NO_SURFACE) {
            throw new IllegalStateException("surface already created");
        }
        mEglSurface = mEglCore.createWindowSurface(surface);

        // Don't cache width/height here, because the size of the underlying surface can change
        // out from under us (see e.g. HardwareScalerActivity).
        //mWidth = mEglCore.querySurface(mEGLSurface, EGL14.EGL_WIDTH);
        //mHeight = mEglCore.querySurface(mEGLSurface, EGL14.EGL_HEIGHT);
    }

    /**
     * Creates an off-screen surface.
     */
    public void createOffscreenSurface(int width, int height) {
        if (mEglSurface != EGL14.EGL_NO_SURFACE) {
            throw new IllegalStateException("surface already created");
        }
        mEglSurface = mEglCore.createOffscreenSurface(width, height);
        mWidth = width;
        mHeight = height;
    }

    /**
     * Returns the surface's width, in pixels.
     * <p>
     * If this is called on a window surface, and the underlying surface is in the process
     * of changing size, we may not see the new size right away (e.g. in the "surfaceChanged"
     * callback).  The size should match after the next buffer swap.
     */
    public int getWidth() {
        if (mWidth < 0) {
            return mEglCore.querySurface(mEglSurface, EGL14.EGL_WIDTH);
        } else {
            return mWidth;
        }
    }

    /**
     * Returns the surface's height, in pixels.
     */
    public int getHeight() {
        if (mHeight < 0) {
            return mEglCore.querySurface(mEglSurface, EGL14.EGL_HEIGHT);
        } else {
            return mHeight;
        }
    }

    /**
     * Release the EGL surface.
     */
    public void releaseEglSurface() {
        mEglCore.releaseSurface(mEglSurface);
        mEglSurface = EGL14.EGL_NO_SURFACE;
        mWidth = mHeight = -1;
    }

    /**
     * Makes our EGL context and surface current.
     */
    public void makeCurrent() {
        mEglCore.makeCurrent(mEglSurface);
    }

    /**
     * Makes our EGL context and surface current.
     */
    public void freeCurrent() {
        mEglCore.makeCurrent(null);
    }

    /**
     * Makes our EGL context and surface current for drawing, using the supplied surface
     * for reading.
     */
    public void makeCurrentReadFrom(EGLSurfaceBase readSurface) {
        mEglCore.makeCurrent(mEglSurface, readSurface.mEglSurface);
    }

    /**
     * Calls eglSwapBuffers.  Use this to "publish" the current frame.
     *
     * @return false on failure
     */
    public boolean swapBuffers() {
        boolean result = mEglCore.swapBuffers(mEglSurface);
        if (!result) {
            Log.d(EGLCore.TAG, "WARNING: swapBuffers() failed");
        }
        return result;
    }

    /**
     * Sends the presentation time stamp to EGL.
     *
     * @param nsecs Timestamp, in nanoseconds.
     */
    public void setPresentationTime(long nsecs) {
        mEglCore.setPresentationTime(mEglSurface, nsecs);
    }
}
