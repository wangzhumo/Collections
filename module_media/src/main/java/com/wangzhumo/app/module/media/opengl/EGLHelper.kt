package com.wangzhumo.app.module.media.opengl

import android.graphics.SurfaceTexture
import android.opengl.*
import android.util.Log
import android.view.Surface

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-12-25  19:32
 *
 * EGL环境相关的,需要在自己开线程
 * 1.创建EGLContext
 * 2.创建EGLSurface
 * 3.swapBuffers
 * 4.makeCurrent
 */
class EGLHelper {

    private var eglDisplay: EGLDisplay = EGL14.EGL_NO_DISPLAY
    private var eglContext: EGLContext = EGL14.EGL_NO_CONTEXT
    private var eglConfig: EGLConfig? = null


    /**
     * 创建EGL的环境.
     *
     * @param surface
     * @param sharedContext  sharedContext The context to share, or null if sharing is not desired.
     */
    constructor (flags: Int = 0, sharedContext: EGLContext = EGL14.EGL_NO_CONTEXT) {
        Log.d(TAG, "initEGL initEGLContext start")

        //需判断是否成功获取EGLDisplay
        eglDisplay = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY)
        if (eglDisplay == EGL14.EGL_NO_DISPLAY) {
            throw RuntimeException("unable to get EGL14 display")
        }
        //EGL初始化
        val versions = IntArray(2)
        versions[0] = OPENGL_ES_VERSION
        //这里的IF中初始化了EGL的环境
        if (!EGL14.eglInitialize(eglDisplay, versions, 0, versions, 1)) {
            throw RuntimeException("eglInitialize failed! " + EGL14.eglGetError())
        }
        //Log.d(TAG, "Trying GLES 3");
        val config = getConfig(flags, OPENGL_ES_VERSION)
        if (config == null) {
            throw RuntimeException("Unable to find a suitable EGLConfig, " + EGL14.eglGetError())
        }

        //创建EGL显示的窗口
        val ctxAttribute = intArrayOf(
            EGL14.EGL_CONTEXT_CLIENT_VERSION, OPENGL_ES_VERSION,
            EGL14.EGL_NONE
        )
        if (sharedContext == EGL14.EGL_NO_CONTEXT) {
            //如果外部传入的是一个NULL,说明需要创建一个EGLContext
            eglContext = EGL14.eglCreateContext(
                eglDisplay,
                config,
                EGL14.EGL_NO_CONTEXT,
                ctxAttribute,
                0
            )
            eglConfig = config
        }

        // Confirm with query.
        val values = IntArray(1)
        EGL14.eglQueryContext(eglDisplay, eglContext, EGL14.EGL_CONTEXT_CLIENT_VERSION, values, 0)
        Log.d(TAG, "EGLContext created, client version " + values[0])
    }

    /**
     * Creates an EGL surface associated with a Surface.
     * @param surface 可以是 Surface or SurfaceTexture
     */
    fun createWindowSurface(surface: Any): EGLSurface {
        if (surface !is Surface && surface !is SurfaceTexture) {
            throw IllegalArgumentException("invalid surface: $surface")
        }

        //创建EGL显示的窗口
        val surfaceAttribute = intArrayOf(
            EGL14.EGL_NONE
        )
        val eglSurface = EGL14.eglCreateWindowSurface(
            eglDisplay,
            eglConfig,
            surface,
            surfaceAttribute,
            0
        )
        if (eglSurface == null) {
            throw RuntimeException("surface was null")
        }
        return eglSurface
    }


    /**
     * 创建一个离屏渲染EGLSurface
     */
    fun createOffsetSurface(width: Int, height: Int): EGLSurface {
        val surfaceAttribs = intArrayOf(
            EGL14.EGL_WIDTH, width,
            EGL14.EGL_HEIGHT, height,
            EGL14.EGL_NONE
        )
        val eglSurface = EGL14.eglCreatePbufferSurface(
            eglDisplay,
            eglConfig,
            surfaceAttribs,
            0
        )
        if (eglSurface == null) {
            throw RuntimeException("surface was null")
        }
        return eglSurface
    }

    /**
     * Makes our EGL context current, using the supplied surface for both "draw" and "read".
     * 传入的这个EglSurface 可读可写
     */
    fun makeCurrent(eglSurface: EGLSurface?) {
        if (eglDisplay == EGL14.EGL_NO_DISPLAY) { // called makeCurrent() before create?
            Log.d(TAG, "NOTE: makeCurrent w/o display")
        }
        if (!EGL14.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext)) {
            throw java.lang.RuntimeException("eglMakeCurrent failed")
        }
    }

    /**
     * Makes our EGL context current, using the supplied "draw" and "read" surfaces.
     * 传入的EGLSurface 一个是读取用,一个是写入的
     */
    fun makeCurrent(drawSurface: EGLSurface?, readSurface: EGLSurface?) {
        if (eglDisplay == EGL14.EGL_NO_DISPLAY) { // called makeCurrent() before create?
            Log.d(TAG, "NOTE: makeCurrent w/o display")
        }
        if (!EGL14.eglMakeCurrent(eglDisplay, drawSurface, readSurface, eglContext)) {
            throw java.lang.RuntimeException("eglMakeCurrent(draw,read) failed")
        }
    }

    /**
     * Makes no context current.
     */
    fun makeNothingCurrent() {
        if (!EGL14.eglMakeCurrent(
                eglDisplay,
                EGL14.EGL_NO_SURFACE,
                EGL14.EGL_NO_SURFACE,
                EGL14.EGL_NO_CONTEXT
            )
        ) {
            throw java.lang.RuntimeException("eglMakeCurrent failed")
        }
    }

    /**
     * 调用SwapBuffer进行双缓冲切换显示渲染画面
     */
    fun swapBuffer(eglSurface: EGLSurface): Boolean {
        return EGL14.eglSwapBuffers(this.eglDisplay, eglSurface)
    }

    /**
     * Returns true if our context and the specified surface are current.
     */
    fun isCurrent(eglSurface: EGLSurface): Boolean {
        return eglContext.equals(EGL14.eglGetCurrentContext()) && eglSurface.equals(EGL14.eglGetCurrentSurface(EGL14.EGL_DRAW))
    }

    /**
     * 销毁这些创建的资源.
     */
    fun release() {
        if (eglContext != EGL14.EGL_NO_CONTEXT) {
            EGL14.eglDestroyContext(eglDisplay, eglContext)
            eglContext = EGL14.EGL_NO_CONTEXT
        }
        if (eglDisplay != EGL14.EGL_NO_DISPLAY) {
            // Android is unusual in that it uses a reference-counted EGLDisplay.  So for
            // every eglInitialize() we need an eglTerminate().
            EGL14.eglMakeCurrent(
                eglDisplay,
                EGL14.EGL_NO_SURFACE,
                EGL14.EGL_NO_SURFACE,
                EGL14.EGL_NO_CONTEXT
            )
            EGL14.eglDestroyContext(eglDisplay, eglContext)
            EGL14.eglReleaseThread()
            EGL14.eglTerminate(eglDisplay)
        }
        eglConfig = null
    }

    /**
     * Destroys the specified surface
     */
    fun releaseSurface(eglSurface: EGLSurface?) {
        if (eglSurface != null) {
            EGL14.eglMakeCurrent(
                eglDisplay,
                EGL14.EGL_NO_SURFACE,
                EGL14.EGL_NO_SURFACE,
                EGL14.EGL_NO_CONTEXT
            )
            EGL14.eglDestroySurface(eglDisplay, eglSurface)
        }
    }

    /**
     * Finds a suitable EGLConfig.
     *
     * @param flags Bit flags from constructor.
     */
    private fun getConfig(flags: Int, version: Int = OPENGL_ES_VERSION): EGLConfig? {
        //EGL的配置
        val eglConfigAttribute = intArrayOf(
            EGL14.EGL_RED_SIZE, 8,      //缓冲区红色位数
            EGL14.EGL_GREEN_SIZE, 8,    //缓冲区绿色位数
            EGL14.EGL_BLUE_SIZE, 8,     //缓冲区蓝色位数
            EGL14.EGL_ALPHA_SIZE, 8,    //缓冲区透明度位数
            EGL14.EGL_RENDERABLE_TYPE, EGL14.EGL_OPENGL_ES2_BIT,   //渲染窗口支持的布局类型
            EGL14.EGL_SURFACE_TYPE, EGL14.EGL_WINDOW_BIT,       //EGL窗口支持的类型
            EGL14.EGL_NONE, 0,          // placeholder for recordable [@-3]
            EGL14.EGL_NONE    //结束符
        )

        //添加FLAG_RECORDABLE 的标签
        if (flags and FLAG_RECORDABLE != 0) {
            eglConfigAttribute[eglConfigAttribute.size - 3] = EGL_RECORDABLE_ANDROID
            eglConfigAttribute[eglConfigAttribute.size - 2] = 1
        }

        val eglConfig = arrayOfNulls<EGLConfig>(1)
        val numConfig = IntArray(1)

        val eglChooseFlag = EGL14.eglChooseConfig(
            eglDisplay,
            eglConfigAttribute,
            0,
            eglConfig,
            0,
            eglConfig.size,
            numConfig,
            0
        )

        if (!eglChooseFlag) {
            Log.d(TAG, "unable to find RGB8888 / $version EGLConfig")
            return null
        }
        return eglConfig[0]
    }

    companion object {
        const val TAG = "OpenGL Record"

        const val OPENGL_ES_VERSION = 2

        /**
         * Constructor flag: surface must be recordable.  This discourages EGL from using a
         * pixel format that cannot be converted efficiently to something usable by the video
         * encoder.
         */
        const val FLAG_RECORDABLE = 0x01

        // Android-specific extension.
        private const val EGL_RECORDABLE_ANDROID = 0x3142
    }
}