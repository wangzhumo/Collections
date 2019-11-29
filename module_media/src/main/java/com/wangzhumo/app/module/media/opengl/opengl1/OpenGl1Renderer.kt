package com.wangzhumo.app.module.media.opengl.opengl1

import android.opengl.GLSurfaceView
import android.opengl.GLU
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-09-17  16:44
 *
 * opengl1的自定义渲染器.
 */
class OpenGl1Renderer : GLSurfaceView.Renderer {

    //surface创建成功时
    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        //设置一个清屏的颜色
        gl?.glClearColor(0F, 0F, 0F, 1F)
        //开启顶点缓冲区
        gl?.glEnableClientState(GL10.GL_VERTEX_ARRAY)
    }

    //size改变时回调
    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        gl?.apply {
            glViewport(0, 0, width, height)   //viewport视口   也就是opengl可以显示的区域大小
            val ratio = width.toFloat() / height   //计算比例，避免缩放后显示变形
            //投影矩阵 - 矩阵模式
            glMatrixMode(GL10.GL_PROJECTION)
            //加载单位矩阵 - 初始化
            glLoadIdentity()
            //此处,  bottom = -left * ratio    top = left * ratio
            //zNear 近平面距离
            //zFar  远平面距离
            glFrustumf(-1F, 1F, -ratio, ratio, 3F, 7F)  //平截头体
        }
    }


    //绘制资源
    override fun onDrawFrame(gl: GL10?) {
        //切换到模型矩阵
        gl?.apply {
            //清屏
            glClear(GL10.GL_COLOR_BUFFER_BIT) //清除颜色缓冲区
            //gluLookAt 需要操作的是模型矩阵
            glMatrixMode(GL10.GL_MODELVIEW)
            //加载单位矩阵 - 初始化
            glLoadIdentity()
        }
        /*
         * @param gl a GL10 interface   gl对象
         *
         * @param eyeX eye point X
         * @param eyeY eye point Y      观察者的位置，最好是与平截头体在一个原点上
         * @param eyeZ eye point Z
         *
         * @param centerX center of view X
         * @param centerY center of view Y   观察者的观察方向（朝向0，0，0 原点）
         * @param centerZ center of view Z
         *
         * @param upX up vector X
         * @param upY up vector Y    观察者向上的位置
         * @param upZ up vector Z
         */
        //确定观察的空间坐标
        GLU.gluLookAt(gl, 0F, 0F, 5F, 0F, 0F, 0F, 0F, 1F, 0F)

        //确定三角形位置
        val trianglePoint = floatArrayOf(
            0F, 0.25F, 0F,
            -0.75F, -0.25F, 0F,
            0.75F, -0.25F, 0F
        )

        //创建顶点缓冲区
        //@param  capacity   The new buffer's capacity, in bytes
        val pointBuffer: ByteBuffer = ByteBuffer.allocateDirect(trianglePoint.size * 4)

        //存放到缓冲区
        pointBuffer.order(ByteOrder.nativeOrder())   //使用系统的字节序
        val floatBuffer: FloatBuffer = pointBuffer.asFloatBuffer()
        floatBuffer.put(trianglePoint)

        //为了native中，开始读取的指针位置 在第一个可用元素
        floatBuffer.position(0)

        //设置绘图的颜色
        gl?.glColor4f(0F,0F,1F,1F)
        //指定缓冲区
        //int size  标识使用3个坐标标识一个点
        //int type  点的数据类型
        //int stride  跨度
        gl?.glVertexPointer(3, GL10.GL_FLOAT, 0, pointBuffer)
        //画一个三角
        //mode  绘制的类型
        //first 第一个
        //count 有几个点
        gl?.glDrawArrays(GL10.GL_TRIANGLES,0,3)
    }


}