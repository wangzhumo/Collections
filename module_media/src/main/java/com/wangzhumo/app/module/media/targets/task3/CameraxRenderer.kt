package com.wangzhumo.app.module.media.targets.task3

import android.graphics.SurfaceTexture
import android.opengl.*
import android.os.Handler
import android.view.TextureView
import android.os.HandlerThread
import android.os.Message


/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-08-23  16:22
 *
 * Camera的Renderer
 */
class CameraxRenderer : SurfaceTexture.OnFrameAvailableListener {

    var eglSurface: EGLSurface = EGL14.EGL_NO_SURFACE
    var eglContext: EGLContext = EGL14.EGL_NO_CONTEXT
    var eglDisplay: EGLDisplay = EGL14.EGL_NO_DISPLAY

    private var mOESTextureId: Int = 0
    private lateinit var mTextureView: TextureView
    private lateinit var mSurfaceTexture: SurfaceTexture
    private lateinit var mHandler: Handler

    fun init(textureView: TextureView, oesTextureId: Int) {
        mTextureView = textureView
        mOESTextureId = oesTextureId
        val mHandlerThread = HandlerThread("EGL Renderer Thread")
        mHandlerThread.start()
        mHandler = object : Handler(mHandlerThread.looper) {
            override fun handleMessage(msg: Message?) {
                when (msg?.what) {
                    //创建EGL环境
                    MSG_INIT -> initEGL()
                    MSG_RENDER -> drawFrame(msg.obj as SurfaceTexture?)
                    MSG_ATTACH -> attachSurfaceTexture()
                    MSG_DETACH -> detachSurfaceTexture()
                    else -> return
                }
            }
        }
    }

    fun initTexture(surfacetexture: SurfaceTexture) {
        mSurfaceTexture = surfacetexture
        mHandler.sendEmptyMessage(MSG_ATTACH)
    }

    /**
     * 绘制图像.
     * @param surfaceTexture SurfaceTexture
     */
    private fun drawFrame(surfaceTexture: SurfaceTexture?) {
//        EGL14.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext);
        mSurfaceTexture.updateTexImage()
//        EGL14.eglSwapBuffers(eglDisplay, eglSurface);
    }


    override fun onFrameAvailable(surfaceTexture: SurfaceTexture?) {
        mHandler.sendEmptyMessage(MSG_RENDER)
    }


    /**
     * 创建EGL环境
     */
    private fun initEGL() {
        eglDisplay = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY)
        //需判断是否成功获取EGLDisplay
        if (eglDisplay == EGL14.EGL_NO_DISPLAY) {
            throw  RuntimeException("Unable to get EGL14 display")
        }
        val versions = IntArray(2)
        versions[0] = 3
        if (!EGL14.eglInitialize(eglDisplay, versions, 0, versions, 1)) {
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
            eglDisplay,
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

        eglContext = EGL14.eglCreateContext(
            eglDisplay,
            eglConfig[0],
            EGL14.EGL_NO_CONTEXT,
            ctxAttribute, 0
        )

        if (eglDisplay === EGL14.EGL_NO_DISPLAY || eglContext === EGL14.EGL_NO_CONTEXT) {
            throw RuntimeException("eglCreateContext fail failed! " + EGL14.eglGetError())
        }

        val surfaceAttribute = intArrayOf(
            EGL14.EGL_NONE
        )

        eglSurface = EGL14.eglCreateWindowSurface(
            eglDisplay, eglConfig[0], mTextureView.surfaceTexture,
            surfaceAttribute, 0
        )

        val makeFlag = EGL14.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext)
        if (!makeFlag) {
            throw RuntimeException("eglMakeCurrent failed! " + EGL14.eglGetError())
        }
    }

    private fun detachSurfaceTexture() {
        mSurfaceTexture.detachFromGLContext()
    }

    private fun attachSurfaceTexture() {
        //mSurfaceTexture.attachToGLContext(mOESTextureId)
        mSurfaceTexture.setOnFrameAvailableListener(this)
    }


    fun release() {
        if (eglSurface != EGL14.EGL_NO_SURFACE) {
            EGL14.eglMakeCurrent(
                eglDisplay,
                EGL14.EGL_NO_SURFACE,
                EGL14.EGL_NO_SURFACE,
                EGL14.EGL_NO_CONTEXT
            )
            EGL14.eglDestroySurface(eglDisplay, eglSurface)
            eglSurface = EGL14.EGL_NO_SURFACE
        }
        if (eglContext != EGL14.EGL_NO_CONTEXT) {
            EGL14.eglDestroyContext(eglDisplay, eglContext)
            eglContext = EGL14.EGL_NO_CONTEXT
        }
        if (eglDisplay != EGL14.EGL_NO_DISPLAY) {
            EGL14.eglTerminate(eglDisplay)
            eglDisplay = EGL14.EGL_NO_DISPLAY
        }
    }


    companion object {
        private const val MSG_INIT = 1
        private const val MSG_RENDER = 2
        private const val MSG_DETACH = 3
        private const val MSG_ATTACH = 4
    }
}