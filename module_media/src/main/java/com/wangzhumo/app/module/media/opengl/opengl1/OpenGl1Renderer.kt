package com.wangzhumo.app.module.media.opengl.opengl1

import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-09-17  16:44
 *
 * opengl1的自定义渲染器.
 */
class OpenGl1Renderer : GLSurfaceView.Renderer{

    //surface创建成功时
    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        //设置一个清屏的颜色
        gl?.glClearColor(0F, 0F, 0F, 1F)
    }

    //size改变时回调
    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {

    }

    //绘制资源
    override fun onDrawFrame(gl: GL10?) {

    }





}