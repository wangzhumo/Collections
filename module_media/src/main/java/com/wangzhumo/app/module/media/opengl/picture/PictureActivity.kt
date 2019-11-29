package com.wangzhumo.app.module.media.opengl.picture

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wangzhumo.app.module.media.R
import com.wangzhumo.app.origin.BaseActivity
import kotlinx.android.synthetic.main.activity_picture.*

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-11-29  15:03
 *
 * 把图片作为纹理显示
 */
class PictureActivity : BaseActivity() {

    override fun getLayoutId(): Int = R.layout.activity_picture

    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        gl_surface_view.setRenderer(PictureRenderer())
    }
}
