package com.wangzhumo.app.module.media.opengl.gles

import android.opengl.EGL14
import android.util.Log

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-12-31  15:59
 *
 * Common base class for EGL surfaces.
 * 可以为同一个Context关联多个Surface
 */
class EglSurfaceBase constructor(val eglHelper :EGLHelper) {

    private var mEGLSurface = EGL14.EGL_NO_SURFACE
    private var mWidth = -1
    private var mHeight = -1

    /**
     * 创建一个用于显示的EGLSurface
     * @param surface Surface SurfaceTexture
     */
    fun createWindowSurface(surface : Any){
        check(!(mEGLSurface !== EGL14.EGL_NO_SURFACE)) { "surface already created" }
        mEGLSurface = eglHelper.createWindowSurface(surface)
    }

    /**
     * 创建一个离屏渲染的EGLSurface
     * @param width 宽度
     * @param height 高度
     */
    fun createOffscreenSurface(width: Int, height: Int) {
        check(!(mEGLSurface !== EGL14.EGL_NO_SURFACE)) { "surface already created" }
        mEGLSurface = eglHelper.createOffsetSurface(width, height)
        mWidth = width
        mHeight = height
    }

    /**
     * Returns the surface's width, in pixels.
     *
     */
    fun getWidth(): Int {
        return if (mWidth < 0) {
            eglHelper.querySurface(mEGLSurface, EGL14.EGL_WIDTH)
        } else {
            mWidth
        }
    }

    /**
     * Returns the surface's height, in pixels.
     */
    fun getHeight(): Int {
        return if (mHeight < 0) {
            eglHelper.querySurface(mEGLSurface, EGL14.EGL_HEIGHT)
        } else {
            mHeight
        }
    }


    /**
     * Release the EGL surface.
     */
    fun releaseEglSurface() {
        eglHelper.releaseSurface(mEGLSurface)
        mEGLSurface = EGL14.EGL_NO_SURFACE
        mHeight = -1
        mWidth = -1
    }


    /**
     * Makes our EGL context and surface current.
     */
    fun makeCurrent() {
        eglHelper.makeCurrent(mEGLSurface)
    }

    /**
     * Makes our EGL context and surface current for drawing, using the supplied surface
     * for reading.
     */
    fun makeCurrentReadFrom(readSurface: EglSurfaceBase) {
        eglHelper.makeCurrent(mEGLSurface, readSurface.mEGLSurface)
    }

    /**
     * Calls eglSwapBuffers.  Use this to "publish" the current frame.
     *
     * @return false on failure
     */
    fun swapBuffers(): Boolean {
        val result: Boolean = eglHelper.swapBuffer(mEGLSurface)
        if (!result) {
            Log.d(TAG, "WARNING: swapBuffers() failed")
        }
        return result
    }

    /**
     * Sends the presentation time stamp to EGL.
     *
     * @param nsecs Timestamp, in nanoseconds.
     */
    fun setPresentationTime(nsecs: Long) {
        eglHelper.setPresentationTime(mEGLSurface, nsecs)
    }

    companion object{

        const val TAG = "OpenGL Record"
    }
}