package com.wangzhumo.app.module.media.targets.widget

import android.graphics.SurfaceTexture
import android.opengl.*
import android.os.Handler
import android.os.HandlerThread
import android.os.Message

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-08-28  23:49
 */
class GLESTextureThread constructor(surface:SurfaceTexture,rendererListener: IGLESRenderer) : Handler.Callback {


    private val mSurfaceTexture :SurfaceTexture = surface
    private val mRendererListener: IGLESRenderer = rendererListener

    private lateinit var mEgl : EGL14
    private var mEglDisplay: EGLDisplay = EGL14.EGL_NO_DISPLAY
    private var mEglSurface: EGLSurface = EGL14.EGL_NO_SURFACE
    private var mEglContext: EGLContext = EGL14.EGL_NO_CONTEXT
    private val mHandlerThread = HandlerThread("EGL Renderer Thread")
    private val mHandler: Handler

    init {
        mHandler = Handler(mHandlerThread.looper,this)
    }


    override fun handleMessage(msg: Message?): Boolean {
        return when (msg?.what) {
            //创建EGL环境
            GLESTextureThread.MSG_INIT -> initEGL()
            GLESTextureThread.MSG_RENDER -> drawFrame()
            GLESTextureThread.MSG_ATTACH -> attachSurfaceTexture()
            GLESTextureThread.MSG_DETACH -> detachSurfaceTexture()
            else -> return true
        }
    }


    /**
     * 创建EGL环境
     */
    private fun initEGL() : Boolean {
        mEglDisplay = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY)
        //需判断是否成功获取EGLDisplay
        if (mEglDisplay == EGL14.EGL_NO_DISPLAY) {
            throw  RuntimeException("Unable to get EGL14 display")
        }
        val versions = IntArray(2)
        versions[0] = 3
        if (!EGL14.eglInitialize(mEglDisplay, versions, 0, versions, 1)) {
            throw RuntimeException("eglInitialize failed! " + EGL14.eglGetError())
        }

        //egl的一些配置
        val eglConfigAttribute = intArrayOf(
            EGL14.EGL_BUFFER_SIZE, 32,
            EGL14.EGL_RED_SIZE, 8,
            EGL14.EGL_GREEN_SIZE, 8,
            EGL14.EGL_BLUE_SIZE, 8,
            EGL14.EGL_ALPHA_SIZE, 8,
            EGL14.EGL_RENDERABLE_TYPE, 4,
            EGL14.EGL_SURFACE_TYPE, EGL14.EGL_WINDOW_BIT,
            EGL14.EGL_NONE
        )
        val numConfig = IntArray(1)
        val eglConfig = arrayOfNulls<EGLConfig>(1)

        val eglChooseFlag = EGL14.eglChooseConfig(
            mEglDisplay,
            eglConfigAttribute, 0,
            eglConfig, 0, 1,
            numConfig, 0
        )

        if (!eglChooseFlag) {
            throw RuntimeException("eglChooseConfig failed! " + EGL14.eglGetError())
        }

        val ctxAttribute = intArrayOf(
            EGL14.EGL_CONTEXT_CLIENT_VERSION, 2,
            EGL14.EGL_NONE
        )

        mEglContext = EGL14.eglCreateContext(
            mEglDisplay,
            eglConfig[0],
            EGL14.EGL_NO_CONTEXT,
            ctxAttribute, 0
        )

        if (mEglDisplay === android.opengl.EGL14.EGL_NO_DISPLAY || mEglContext === android.opengl.EGL14.EGL_NO_CONTEXT) {
            throw RuntimeException("eglCreateContext fail failed! " + EGL14.eglGetError())
        }

        val surfaceAttribute = intArrayOf(
            EGL14.EGL_NONE
        )

        mEglSurface = EGL14.eglCreateWindowSurface(
            mEglDisplay, eglConfig[0], mSurfaceTexture,
            surfaceAttribute, 0
        )

        val makeFlag = EGL14.eglMakeCurrent(mEglDisplay, mEglSurface, mEglSurface, mEglContext)
        if (!makeFlag) {
            throw RuntimeException("eglMakeCurrent failed! " + EGL14.eglGetError())
        }
        return true
    }


    companion object {
        const val TAG = "GLESTextureThread"
        const val EGL_CONTEXT_CLIENT_VERSION = 0x3098
        const val EGL_OPENGL_ES2_BIT = 4

        private const val MSG_INIT = 1
        private const val MSG_RENDER = 2
        private const val MSG_DETACH = 3
        private const val MSG_ATTACH = 4
    }
}