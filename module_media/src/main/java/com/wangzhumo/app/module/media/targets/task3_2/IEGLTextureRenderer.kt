package com.wangzhumo.app.module.media.targets.task3_2

import android.graphics.SurfaceTexture

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-10-09  12:15
 *
 * Renderer必须要的一些能力.
 */
interface IEGLTextureRenderer {

    fun onSurfaceCreated()

    fun onSurfaceChanged(width: Int, height: Int)

    fun onDrawFrame(surfaceTexture: SurfaceTexture)
}