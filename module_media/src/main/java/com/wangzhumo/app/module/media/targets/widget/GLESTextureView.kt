package com.wangzhumo.app.module.media.targets.widget

import android.content.Context
import android.graphics.SurfaceTexture
import android.os.Message
import android.util.AttributeSet
import android.view.TextureView


/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-08-28  23:39
 *
 * TextureView + GL 的简单封装.
 */
class GLESTextureView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : TextureView(context, attrs, defStyleAttr), TextureView.SurfaceTextureListener {


    private var mGLThread: GLESTextureThread? = null
    private var mRenderer: IGLESRenderer? = null
    private var mRendererMode = RENDERMODE_CONTINUOUSLY

    //给自己设置监听.
    init {
        surfaceTextureListener = this
    }

    /**
     * 模仿GLSurfaceView的setRenderer
     */
    fun setRenderer(renderer: IGLESRenderer) {
        mRenderer = renderer
    }

    /**
     * 模仿GLSurfaceView的setRenderMode
     * 渲染模式
     *   1.循环刷新，
     *   2.请求的时候刷新
     */
    fun setRenderMode(mode: Int) {
        mRendererMode = mode
    }

    /**
     * Request that the renderer render a frame. This method is typically used when the render mode has been set to [.RENDERMODE_WHEN_DIRTY], so
     * that frames are only rendered on demand. May be called from any thread. Must not be called before a renderer has been set.
     */
    fun requestRender() {
        if (mRendererMode != RENDERMODE_WHEN_DIRTY) {
            return
        }
        mGLThread?.requestRender()
    }

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
        //创建GLESTextureThread
        mGLThread = GLESTextureThread(surface, mRenderer)
        mGLThread?.apply {
            //通过发消息的形式开始初始化,后期封装.
            handleMessage(Message.obtain()?.apply {
                what = GLESTextureThread.MSG_INIT
            })
            //设置渲染模式
            setRenderMode(mRendererMode)
            //设置surface的大小信息
            onSurfaceChange(width,height)
        }
    }


    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {
        mGLThread?.onSurfaceChange(width, height)
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
        return false
    }




    companion object {
        const val RENDERMODE_WHEN_DIRTY = 0
        const val RENDERMODE_CONTINUOUSLY = 1
    }
}