package com.wangzhumo.app.mdeia.gles;

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2020-01-07  11:03
 *
 * Off-screen EGL surface (pbuffer).
 */
public class OffscreenSurface extends EGLSurfaceBase {


    /**
     * Creates an off-screen surface with the specified width and height.
     */
    public OffscreenSurface(EGLCore eglCore,int width, int height) {
        super(eglCore);
        createOffscreenSurface(width, height);
    }

    /**
     * Releases any resources associated with the surface.
     */
    public void release() {
        releaseEglSurface();
    }
}
