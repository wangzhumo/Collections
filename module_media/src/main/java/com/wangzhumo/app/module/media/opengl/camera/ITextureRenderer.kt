package com.wangzhumo.app.module.media.opengl.camera

import android.graphics.SurfaceTexture


/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-11-22  20:54
 *
 * Renderer要实现的方法.
 */
interface ITextureRenderer {

    fun onSurfaceCreated()

    fun onSurfaceChanged(width: Int, height: Int)

    fun onDrawFrame(surfaceTexture: SurfaceTexture?)
}