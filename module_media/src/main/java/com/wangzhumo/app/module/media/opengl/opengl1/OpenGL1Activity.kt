package com.wangzhumo.app.module.media.opengl.opengl1

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.wangzhumo.app.base.IRoute
import com.wangzhumo.app.module.media.R
import com.wangzhumo.app.origin.BaseActivity
import kotlinx.android.synthetic.main.activity_open_gl1.*

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-09-17  16:37
 */
@Route(path = IRoute.MEDIA_OPENGL_1)
class OpenGL1Activity : BaseActivity() {

    override fun getLayoutId(): Int = R.layout.activity_open_gl1

    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        gl_surface_view.setRenderer(OpenGl1Renderer())
    }


}
