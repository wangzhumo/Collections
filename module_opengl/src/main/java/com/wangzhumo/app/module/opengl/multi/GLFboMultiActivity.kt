package com.wangzhumo.app.module.opengl.multi

import android.os.Bundle
import android.view.ViewGroup
import android.widget.FrameLayout
import com.alibaba.android.arouter.facade.annotation.Route
import com.wangzhumo.app.base.IRoute
import com.wangzhumo.app.base.utils.DensityUtils
import com.wangzhumo.app.module.opengl.R
import com.wangzhumo.app.origin.BaseActivity
import kotlinx.android.synthetic.main.activity_glfbo_multi.*


/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2020-01-08  17:53
 *
 * 显示一个图片纹理到OpenGL
 */
@Route(path = IRoute.OPENGL.IMAGE_TEXTURE_MULTI)
class GLFboMultiActivity : BaseActivity() {

    override fun getLayoutId(): Int = R.layout.activity_glfbo_multi


    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        //这里是渲染的FBO纹理ID
        imageTextureView.renderer = GLFboMultiRenderer(this)
        imageTextureView.fboMultiRenderer.setTextureListener {
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
        container_ff.removeAllViews()

        for (index in 0..2){
            val multiTextureView = GLFboMultiTextureView(this)

            //添加TextureID
            val fboMultiRenderer = FboMultiRenderer()
            multiTextureView.setRenderer(fboMultiRenderer)
            fboMultiRenderer.textureId = textureId

            multiTextureView.setSurfaceAndCore(null, imageTextureView.eglCore)
            val frameParam = FrameLayout.LayoutParams(
                DensityUtils.dp2px(this,120F),
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            container_ff.addView(multiTextureView, frameParam)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        imageTextureView.release()
    }
}
