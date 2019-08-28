package com.wangzhumo.app.module.media.targets.widget

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-08-28$  23:36$
 *
 * 指定Renderer需要实现的方法
 */
interface IGLESRenderer {

    /**
     * Surface创建好之后
     */
    fun onSurfaceCreated()

    /**
     * 界面大小有更改
     * @param width
     * @param height
     */
    fun onSurfaceChanged(width: Int, height: Int)

    /**
     * 绘制每一帧
     */
    fun onDrawFrame()

    /**
     * onResume操作
     */
    fun onResume()

    /**
     * onPause操作
     */
    fun onPause()

    /**
     * onDestroy操作
     */
    fun onDestroy()

}