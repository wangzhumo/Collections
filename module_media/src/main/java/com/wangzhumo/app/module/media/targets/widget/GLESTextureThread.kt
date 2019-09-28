package com.wangzhumo.app.module.media.targets.widget

import android.graphics.SurfaceTexture
import android.opengl.*
import android.os.Handler
import android.os.HandlerThread
import android.os.Message
import android.util.Log
import android.view.TextureView
import com.orhanobut.logger.Logger

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-08-28  23:49
 */
class GLESTextureThread constructor(textureView : TextureView?,surface:SurfaceTexture?) : Handler.Callback {

    private var mTextureView : TextureView? = textureView
    private var mSurfaceTexture : SurfaceTexture? = surface


    private var mRendererListener: IGLESRenderer? = null
    private var mEglDisplay: EGLDisplay = EGL14.EGL_NO_DISPLAY
    private var mEglSurface: EGLSurface = EGL14.EGL_NO_SURFACE
    private var mEglContext: EGLContext = EGL14.EGL_NO_CONTEXT
    private var mOESTextureId: Int = 0
    private val mHandlerThread = HandlerThread("EGL Renderer Thread")
    private val mHandler: Handler

    init {
        mHandlerThread.start()
        mHandler = Handler(mHandlerThread.looper,this)
        Log.e("Renderer","GLESTextureThread Init")
    }


    fun setRendererListener(rendererListener: IGLESRenderer) {
        mRendererListener = rendererListener
    }

    override fun handleMessage(msg: Message?): Boolean {
        when (msg?.what) {
            //创建EGL环境
            MSG_INIT -> {
                initEGL()
                return true
            }
            MSG_INIT_SURFACE -> {
                initSurfaceTexture()
                return true
            }
            MSG_RENDER -> {
                mRendererListener?.onDrawFrame()
                return true
            }
            MSG_DETACH -> {
                deInitSurfaceTexture()
                release()
                return true
            }
            else -> return true
        }
    }


    /**
     * 创建EGL环境
     */
    private fun initEGL()  {
        Log.e("Renderer","GLESTextureThread initEGL")
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

        if (mEglDisplay === EGL14.EGL_NO_DISPLAY || mEglContext === EGL14.EGL_NO_CONTEXT) {
            throw RuntimeException("eglCreateContext fail failed! " + EGL14.eglGetError())
        }

        val surfaceAttribute = intArrayOf(
            EGL14.EGL_NONE
        )

        val surfaceTexture = mTextureView?.surfaceTexture
        mEglSurface = EGL14.eglCreateWindowSurface(
            mEglDisplay, eglConfig[0], surfaceTexture,
            surfaceAttribute, 0
        )

        val makeFlag = EGL14.eglMakeCurrent(mEglDisplay, mEglSurface, mEglSurface, mEglContext)
        if (!makeFlag) {
            throw RuntimeException("eglMakeCurrent failed! " + EGL14.eglGetError())
        }
    }

    private fun release() {
        if (mEglSurface != EGL14.EGL_NO_SURFACE) {
            EGL14.eglMakeCurrent(
                mEglDisplay,
                EGL14.EGL_NO_SURFACE,
                EGL14.EGL_NO_SURFACE,
                EGL14.EGL_NO_CONTEXT
            )
            EGL14.eglDestroySurface(mEglDisplay, mEglSurface)
            mEglSurface = EGL14.EGL_NO_SURFACE
        }
        if (mEglContext != EGL14.EGL_NO_CONTEXT) {
            EGL14.eglDestroyContext(mEglDisplay, mEglContext)
            mEglContext = EGL14.EGL_NO_CONTEXT
        }
        if (mEglDisplay != EGL14.EGL_NO_DISPLAY) {
            EGL14.eglTerminate(mEglDisplay)
            mEglDisplay = EGL14.EGL_NO_DISPLAY
        }
    }

    /**
     * 需要刷新数据了.
     */
    fun requestRender() {
        mHandler.sendEmptyMessage(MSG_RENDER)
    }

    fun sendEmptyMessage(what : Int){
        mHandler.sendEmptyMessage(what)
    }


    /**
     * 设置mOESTextureId
     */
    fun attachSurfaceId(textureId :Int){
        this.mOESTextureId = textureId
    }

    private fun initSurfaceTexture() {
        Log.e("Renderer","GLESTextureThread initSurfaceTexture")
        mSurfaceTexture?.apply {
            attachToGLContext(mOESTextureId)
            setOnFrameAvailableListener(mRendererListener)
        }
    }

    private fun deInitSurfaceTexture() {
        mSurfaceTexture?.detachFromGLContext()
    }

    companion object {

        const val MSG_INIT = 10
        const val MSG_INIT_SURFACE = 11
        const val MSG_RENDER = 2
        const val MSG_DETACH = 3
    }
}