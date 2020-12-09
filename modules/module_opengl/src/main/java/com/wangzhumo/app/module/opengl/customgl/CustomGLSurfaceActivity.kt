package com.wangzhumo.app.module.opengl.customgl

import android.opengl.GLES20
import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.alibaba.android.arouter.facade.annotation.Route
import com.wangzhumo.app.base.IRoute
import com.wangzhumo.app.mdeia.CustomGLSurfaceView
import com.wangzhumo.app.mdeia.gles.IGLRenderer
import com.wangzhumo.app.module.opengl.databinding.ActivityCustomGlsurfaceBinding
import com.wangzhumo.app.origin.BaseActivity

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2020-01-07  11:36
 *
 * 使用自定义的GLSurfaceView
 */
@Route(path = IRoute.OPENGL.CUSTOM_GL_SURFACE)
class CustomGLSurfaceActivity : BaseActivity<ActivityCustomGlsurfaceBinding>() {


    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        vBinding.customSurface.setZOrderOnTop(true)
        vBinding.customSurface.setRenderer(MyRenderer())
        vBinding.customSurface.setRenderMode(CustomGLSurfaceView.RENDERMODE_CONTINUOUSLY)
    }


    inner class MyRenderer : IGLRenderer {
        override fun onSurfaceCreate() {

        }

        override fun onSurfaceChange(width: Int, height: Int) {
            GLES20.glViewport(0, 0, width, height)
        }

        override fun drawFrame() {
            GLES20.glClearColor(0F, 0F, 1F, 0.3F)
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        }

    }
}
