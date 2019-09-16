package com.wangzhumo.app.module.media.targets.utils

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-09-16  17:49
 */
object TriangleUtils {

    // Used to load the 'native-lib' library on application startup.
    init {
        System.loadLibrary("opengl-learn-lib")

    }

    //初始化GLES
    external fun init(): Boolean

    //为GLES设置宽和高
    external fun resize(width: Int, height: Int)

    //绘制图形
    external fun drawTriangle()

}