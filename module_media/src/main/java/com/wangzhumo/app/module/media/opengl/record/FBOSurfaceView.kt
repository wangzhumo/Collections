package com.wangzhumo.app.module.media.opengl.record

import android.content.Context
import android.opengl.EGLContext
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.wangzhumo.app.module.media.opengl.EGLHelper
import com.wangzhumo.app.module.media.opengl.IRenderer

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-12-25  14:53
 *
 * 为了使用FBO录制,自定的提供EGL环境的View,用于预览相机采集的数据
 */
open class FBOSurfaceView @JvmOverloads constructor(
    ctx: Context,
    attrs: AttributeSet?,
    defaultAttr: Int = 0
) : SurfaceView(ctx, attrs, defaultAttr), SurfaceHolder.Callback {

    /**
     * 自己内部的Surface
     */
    private var surface: Surface? = null

    /**
     * 渲染模式
     */
    var renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY

    /**
     * Renderer的实例
     */
    var renderer: IRenderer? = null

    /**
     * EGLHelper的实例,建立EGLContext
     */
    lateinit var eglHelper: EGLHelper

    /**
     * EGLContext的对象
     */
    private var eglContext: EGLContext? = null
    private var eglRenderer:EglRenderer? = null
    private var eglThread:Thread? = null

    init {
        //添加一个监听.
        holder.addCallback(this)
    }


    override fun surfaceCreated(holder: SurfaceHolder?) {
        //初始化好,surface就可用了
        this.surface = holder?.surface
        this.eglRenderer = EglRenderer()
        this.eglThread = Thread(eglRenderer)
        eglRenderer?.isSurfaceCreate = true
    }


    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        eglRenderer?.also {
            it.width = width
            it.height = height
            it.isSurfaceChange = true

        }
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        //停止线程内部的死循环
        eglRenderer?.isStop = true
        eglRenderer?.requestRender()
        eglThread?.isInterrupted
        surface = null
        eglContext = null
    }

    inner class EglRenderer : Runnable {
        //控制符
        var isSurfaceCreate: Boolean = false
        var isSurfaceChange = false
        var isStart = false
        var isStop = false
        var width = 0
        var height = 0

        //锁
        private lateinit var localObject: Object

        override fun run() {
            isStart = false
            isStop = false
            localObject = Object()
            eglHelper = EGLHelper()
            eglHelper.initEGLContext(surface, eglContext)

            //内部死循环.
            while (true) {
                //停止渲染,直接销毁资源
                if (isStop) {
                    release()
                    break
                }
                //开始阶段,依据不同的渲染模式阻塞即可
                if (isStart) {
                    if (renderMode == GLSurfaceView.RENDERMODE_WHEN_DIRTY) {
                        synchronized(localObject) {
                            try {
                                localObject.wait()
                            } catch (e: InterruptedException) {
                                e.printStackTrace()
                            }
                        }
                    } else if (renderMode == GLSurfaceView.RENDERMODE_CONTINUOUSLY) {
                        try {
                            Thread.sleep(1000 / 60)
                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                        }
                    }
                }

                //Surface创建完成,renderer不为空,就开始渲染
                if (isSurfaceCreate && renderer != null) {
                    renderer?.onSurfaceCreated()
                    isSurfaceCreate = false
                }

                //SurfaceTexture有大小变换
                if (isSurfaceChange && renderer != null) {
                    renderer?.onSurfaceChanged(width, height)
                    isSurfaceChange = false
                }

                //最后,渲染这个新的数据
                if (renderer != null) {
                    renderer?.onDrawFrame(null)
                    //但是如果首次调用
                    if (!isStart) {
                        renderer?.onDrawFrame(null)
                    }
                }

                eglHelper.swapBuffer()
                isStart = true
            }
        }


        /**
         * 释放资源
         */
        private fun release() {
            eglHelper.release()
        }


        /**
         * 重新绘制,停止阻塞
         */
        fun requestRender() {
            synchronized(localObject) {
                localObject.notifyAll()
            }
        }
    }
}