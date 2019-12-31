package com.wangzhumo.app.module.media.opengl.gles

import android.graphics.SurfaceTexture
import android.view.Surface

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-12-31  16:16
 *
 * Recordable EGL window surface.
 */
class WindowSurface : EglSurfaceBase {

    private var mSurface: Surface? = null
    private var mReleaseSurface = false


    constructor(eglHelper: EGLHelper, surface: Surface, releaseSurface: Boolean) : super(eglHelper){
        createWindowSurface(surface)
        this.mSurface = surface
        this.mReleaseSurface = releaseSurface
    }

    constructor(eglHelper: EGLHelper, surface: SurfaceTexture) : super(eglHelper){
        createWindowSurface(surface)
    }


    /**
     * Releases any resources associated with the EGL surface
     */
    fun release(){
        releaseEglSurface()
        mSurface?.release()
        mSurface = null
    }

    /**
     * Recreate the EGLSurface, using the new EglBase.
     */
    fun recreate(newEGLHelper: EGLHelper){
        if (mSurface == null) {
            throw RuntimeException("not yet implemented for SurfaceTexture")
        }
        eglHelper = newEGLHelper // switch to new context
        createWindowSurface(mSurface!!) // create new surface
    }

}