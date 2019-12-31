package com.wangzhumo.app.module.media.opengl.capture

import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-12-31  17:37
 *
 * Renderer object for our GLSurfaceView.
 */
class CameraCaptureRenderer : GLSurfaceView.Renderer{



    override fun onDrawFrame(gl: GL10?) {
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
    }


    companion object{
        const val TAG = "OpenGL Record"
    }
}