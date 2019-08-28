package com.wangzhumo.app.module.media.targets.widget

import android.graphics.SurfaceTexture
import android.opengl.*

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-08-28  23:49
 */
class GLESTextureThread : Thread() {

    private lateinit var mSurfaceTexture :SurfaceTexture
    private lateinit var mEgl : EGL14
    private var mEGLDisplay: EGLDisplay = EGL14.EGL_NO_DISPLAY
    private var mEglConfig: EGLSurface = EGL14.EGL_NO_SURFACE
    private var mEglContext: EGLContext = EGL14.EGL_NO_CONTEXT




    companion object {
        val TAG = "GLESTextureThread"
        val EGL_CONTEXT_CLIENT_VERSION = 0x3098
        val EGL_OPENGL_ES2_BIT = 4

    }
}