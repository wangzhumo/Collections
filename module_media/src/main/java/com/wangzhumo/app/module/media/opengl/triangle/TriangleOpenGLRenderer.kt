package com.wangzhumo.app.module.media.opengl.triangle

import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-11-29  10:49
 *
 * 三角形的renderer
 */
class TriangleOpenGLRenderer : GLSurfaceView.Renderer {

    private var ratio = 0F

    override fun onDrawFrame(gl: GL10?) {

    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        gl?.apply {
            //viewport视口   也就是opengl可以显示的区域大小
            glViewport(0, 0, width, height)
            //投影矩阵 - 矩阵模式
            glMatrixMode(GL10.GL_PROJECTION)
            //加载单位矩阵 - 初始化
            glLoadIdentity()
            //计算比例，避免缩放后显示变形
            ratio = width.toFloat() / height
            //设置平截头体，为了投射到viewport上时，不超出viewport，top/bottom 按照我们viewport的比例设置
            glFrustumf(-1F, 1F, -ratio, ratio, 3F, 7F)
        }
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        gl?.apply {
            //清屏颜色设置
            glClearColor(0F, 0F, 0F, 1F)
            //开启顶点缓冲区
            glEnableClientState(GL10.GL_VERTEX_ARRAY)
        }
    }
}