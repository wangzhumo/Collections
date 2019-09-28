package com.wangzhumo.app.module.media.targets.task3

import com.wangzhumo.app.module.media.targets.widget.IGLESRenderer

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-09-28  16:24
 */
class CameraTextureRenderer : IGLESRenderer {


    /**
     * 程序
     */
    private val mShaderProgram = -1

    private val mOESTextureId = -1

    init {

    }


    override fun onSurfaceCreated() {
    }

    override fun onSurfaceChanged(width: Int, height: Int) {
    }

    override fun onDrawFrame() {
    }

    override fun onResume() {
    }

    override fun onPause() {
    }

    override fun onDestroy() {
    }

    override fun getTextureId(): Int {
        return mOESTextureId
    }

}