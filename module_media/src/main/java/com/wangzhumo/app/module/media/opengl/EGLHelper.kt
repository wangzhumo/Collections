package com.wangzhumo.app.module.media.opengl

import android.opengl.*
import android.util.Log
import android.view.Surface

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-12-25  19:32
 *
 * EGL环境相关的,需要在自己开线程
 */
class EGLHelper {

    lateinit var eglDisplay: EGLDisplay
    lateinit var eglSurface: EGLSurface
    lateinit var eglContext: EGLContext

    /**
     * 创建EGL的环境.
     *
     * @param surface
     * @param eglContext
     */
    fun initEGLContext(surface: Surface?, ctx: EGLContext?) {
        Log.d(TAG, "initEGL initEGLContext start")
        //需判断是否成功获取EGLDisplay
        eglDisplay = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY)
        if (eglDisplay == EGL14.EGL_NO_DISPLAY) {
            Log.d(TAG, "eglDisplay 初始化失败")
        }
        //EGL初始化
        val versions = IntArray(2)
        versions[0] = 3
        //这里的IF中初始化了EGL的环境
        if (!EGL14.eglInitialize(eglDisplay, versions, 0, versions, 1)) {
            throw RuntimeException("eglInitialize failed! " + EGL14.eglGetError())
        }

        //EGL的配置
        val eglConfigAttribute = intArrayOf(
            EGL14.EGL_BUFFER_SIZE, 32,  //颜色缓冲区所有组成颜色的位数
            EGL14.EGL_RED_SIZE, 8,      //缓冲区红色位数
            EGL14.EGL_GREEN_SIZE, 8,    //缓冲区绿色位数
            EGL14.EGL_BLUE_SIZE, 8,     //缓冲区蓝色位数
            EGL14.EGL_ALPHA_SIZE, 8,    //缓冲区透明度位数
            EGL14.EGL_RENDERABLE_TYPE, EGL14.EGL_OPENGL_ES2_BIT,   //渲染窗口支持的布局类型
            EGL14.EGL_SURFACE_TYPE, EGL14.EGL_WINDOW_BIT,       //EGL窗口支持的类型
            EGL14.EGL_NONE    //结束符
        )
        val numConfig = IntArray(1)
        val eglConfig = arrayOfNulls<EGLConfig>(1)
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
            throw RuntimeException("eglChooseConfig failed! " + EGL14.eglGetError())
        }

        //创建EGL显示的窗口
        val ctxAttribute = intArrayOf(
            EGL14.EGL_CONTEXT_CLIENT_VERSION,
            3,
            EGL14.EGL_NONE
        )
        if (ctx == null) {
            //如果外部传入的是一个NULL,说明需要创建一个EGLContext
            eglContext = EGL14.eglCreateContext(
                eglDisplay,
                eglConfig[0],
                EGL14.EGL_NO_CONTEXT,
                ctxAttribute,
                0
            )
        } else {
            //如果ctx不为空,则说明要和外部传入的这个,共享OpenGL的上下文
            eglContext = EGL14.eglCreateContext(
                eglDisplay,
                eglConfig[0],
                ctx,
                ctxAttribute,
                0
            )
        }

        //创建EGL显示的窗口
        val surfaceAttribute = intArrayOf(
            EGL14.EGL_NONE
        )
        eglSurface = EGL14.eglCreateWindowSurface(
            eglDisplay,
            eglConfig[0],
            surface,
            surfaceAttribute,
            0
        )
        if (eglSurface === EGL14.EGL_NO_SURFACE) {
            Log.d(TAG, "eglCreateWindowSurface fail.")
            return
        }
        //校验1
        if (eglDisplay === EGL14.EGL_NO_DISPLAY || eglContext === EGL14.EGL_NO_CONTEXT) {
            throw RuntimeException("eglCreateContext fail failed! " + EGL14.eglGetError())
        }

        //绑定eglContext
        val makeFlag = EGL14.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext)
        if (!makeFlag) {
            throw RuntimeException("eglMakeCurrent failed! " + EGL14.eglGetError())
        }
    }

    /**
     * 调用SwapBuffer进行双缓冲切换显示渲染画面
     */
    fun swapBuffer(): Boolean {
        return EGL14.eglSwapBuffers(eglDisplay, eglSurface)
    }


    /**
     * 销毁这些创建的资源.
     */
    fun release() {
        if (eglSurface != null) {
            EGL14.eglMakeCurrent(
                eglDisplay,
                EGL14.EGL_NO_SURFACE,
                EGL14.EGL_NO_SURFACE,
                EGL14.EGL_NO_CONTEXT
            )
            EGL14.eglDestroySurface(eglDisplay, eglSurface)
        }
        if (eglContext != null) {
            EGL14.eglDestroyContext(eglDisplay, eglContext)
        }
        if (eglDisplay != null) {
            EGL14.eglTerminate(eglDisplay)
        }
    }


    companion object {
        const val TAG = "OpenGL Record"
    }
}