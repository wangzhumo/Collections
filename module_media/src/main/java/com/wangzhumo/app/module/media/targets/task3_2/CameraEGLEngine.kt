package com.wangzhumo.app.module.media.targets.task3_2

import android.graphics.SurfaceTexture
import android.os.HandlerThread

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-10-09  12:19
 *
 * 1.GLThread
 * 2.render -> IEGLTextureRenderer
 *
 */
class CameraEGLEngine(name: String?) : HandlerThread(name), SurfaceTexture.OnFrameAvailableListener {
    override fun onFrameAvailable(surfaceTexture: SurfaceTexture?) {

    }

    init {
        //创建GLThread

    }
}