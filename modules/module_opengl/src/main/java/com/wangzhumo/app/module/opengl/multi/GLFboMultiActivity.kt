package com.wangzhumo.app.module.opengl.multi

import android.os.Bundle
import android.view.ViewGroup
import android.widget.FrameLayout
import com.alibaba.android.arouter.facade.annotation.Route
import com.wangzhumo.app.base.IRoute
import com.wangzhumo.app.module.opengl.databinding.ActivityGlfboMultiBinding
import com.wangzhumo.app.origin.BaseActivity
import com.wangzhumo.app.origin.utils.DensityUtils


/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2020-01-08  17:53
 *
 * 显示一个图片纹理到OpenGL
 */
@Route(path = IRoute.OPENGL.IMAGE_TEXTURE_MULTI)
class GLFboMultiActivity : BaseActivity<ActivityGlfboMultiBinding>() {


    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        //这里是渲染的FBO纹理ID
        vBinding.imageTextureView.renderer = GLFboMultiRenderer(this)
        vBinding.imageTextureView.fboMultiRenderer.setTextureListener {
            //可以动态添加一个
            runOnUiThread {
                addTextureView(it)
            }
        }
    }

    /**
     * 动态的添加一个TextureView
     */
    private fun addTextureView(textureId: Int) {
        vBinding.containerFf.removeAllViews()

        for (index in 0..2) {
            val multiTextureView = GLFboMultiTextureView(this)

            //添加TextureID
            val fboMultiRenderer = FboMultiRenderer()
            multiTextureView.setRenderer(fboMultiRenderer)
            fboMultiRenderer.textureId = textureId

            multiTextureView.setSurfaceAndCore(null, vBinding.imageTextureView.eglCore)
            val frameParam = FrameLayout.LayoutParams(
                DensityUtils.dp2px(this, 120F),
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            vBinding.containerFf.addView(multiTextureView, frameParam)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        vBinding.imageTextureView.release()
    }
}
