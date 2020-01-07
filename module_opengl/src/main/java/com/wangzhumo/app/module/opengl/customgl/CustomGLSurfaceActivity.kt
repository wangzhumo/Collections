package com.wangzhumo.app.module.opengl.customgl

import android.opengl.GLES20
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.wangzhumo.app.base.IRoute
import com.wangzhumo.app.module.opengl.R
import com.wangzhumo.app.module.opengl.gles.IGLRenderer
import com.wangzhumo.app.origin.BaseActivity
import kotlinx.android.synthetic.main.activity_custom_glsurface.*

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2020-01-07  11:36
 *
 * 使用自定义的GLSurfaceView
 */
@Route(path = IRoute.OPENGL.CUSTOM_GL_SURFACE)
class CustomGLSurfaceActivity : BaseActivity() {

    override fun getLayoutId(): Int = R.layout.activity_custom_glsurface


    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        custom_surface.setZOrderOnTop(true)
        custom_surface.setRenderer(MyRenderer())
        custom_surface.setRenderMode(CustomGLSurfaceView.RENDERMODE_CONTINUOUSLY)
    }


    inner class MyRenderer : IGLRenderer{
        override fun onSurfaceCreate() {

        }

        override fun onSurfaceChange(width: Int, height: Int) {
            GLES20.glViewport(0,0,width, height)
        }

        override fun drawFrame() {
            GLES20.glClearColor(0F,0F,1F,0.3F)
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        }

    }
}
