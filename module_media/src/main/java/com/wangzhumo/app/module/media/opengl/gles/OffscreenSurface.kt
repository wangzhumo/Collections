package com.wangzhumo.app.module.media.opengl.gles

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-12-31  16:13
 *
 * Off-screen EGL surface (pbuffer).
 */
class OffscreenSurface constructor(eglHelper: EGLHelper,width :Int,height :Int) : EglSurfaceBase(eglHelper) {

    init {
        createOffscreenSurface(width, height)
    }

    /**
     * Releases any resources associated with the surface.
     */
    fun release() {
        releaseEglSurface()
    }

}