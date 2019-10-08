package com.wangzhumo.app.module.media.targets.task3_2

import android.graphics.SurfaceTexture
import android.view.TextureView
import androidx.lifecycle.LifecycleOwner

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-10-08  16:12
 */
class CameraEGLHelper : TextureView.SurfaceTextureListener{

    fun bindToLifecycle(lifecycleOwner : LifecycleOwner, textureView: TextureView?) {
        //创建Renderer. ->  HandlerThread
        //添加监听到这里
    }


    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {

    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
        return true
    }

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {}


}