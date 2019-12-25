package com.wangzhumo.app.module.media.opengl

import android.graphics.SurfaceTexture

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-12-25  16:20
 *
 * FBO的回调,让其他程序知道,SurfaceTexture的生命周期
 */
interface OnFBOSurfaceListener {

    /**
     * SurfaceTexture已经创建成功
     * fboTextureId  与FBO绑定的Texture
     */
    fun onSurfaceCreate(surfaceTexture: SurfaceTexture?, fboTextureId: Int)

    /**
     * Frame有新数据,可以更新了
     */
    fun onFrameAvailable(surfaceTexture: SurfaceTexture?)
}