package com.wangzhumo.app.module.media.targets.widget

import android.content.Context
import android.graphics.SurfaceTexture
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


    lateinit var mRenderer: IGLESRenderer
    private var mRendererMode = RENDERMODE_CONTINUOUSLY



    /**
     * 模仿GLSurfaceView的setRenderer
     */
    fun setRenderer(renderer: IGLESRenderer) {
        mRenderer = renderer
        //给自己设置监听.
        surfaceTextureListener = this@GLESTextureView
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


    override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
        requireNotNull(mRenderer) { "No Renderer." }
        //创建GLESTextureThread
        mRenderer.setRenderMode(mRendererMode)
        mRenderer.onSurfaceCreated()
    }


    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {
        mRenderer.onSurfaceChanged(width, height)
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {

    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
        mRenderer.onDestroy()
        return true
    }


    companion object {
        const val RENDERMODE_WHEN_DIRTY = 0
        const val RENDERMODE_CONTINUOUSLY = 1
    }
}