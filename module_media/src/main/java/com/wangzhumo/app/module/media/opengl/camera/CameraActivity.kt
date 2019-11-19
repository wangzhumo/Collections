package com.wangzhumo.app.module.media.opengl.camera

import android.os.Bundle
import android.view.TextureView
import android.view.ViewGroup
import android.widget.FrameLayout
import com.wangzhumo.app.module.media.R
import com.wangzhumo.app.origin.BaseActivity
import kotlinx.android.synthetic.main.activity_camera.*

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-11-19  17:07
 *
 * 相机预览显示.
 */
class CameraActivity : BaseActivity() {

    override fun getLayoutId(): Int = R.layout.activity_camera


    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        createTextureView()
    }

    private fun createTextureView() {
        val textureView = TextureView(this)
        val layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        container_ff.addView(textureView, layoutParams)

        //创建相机控制器
    }

}
