package com.wangzhumo.app.module.media.targets.task3

import com.wangzhumo.app.base.AppUtils
import com.wangzhumo.app.base.UIUtils
import com.wangzhumo.app.module.media.R
import com.wangzhumo.app.module.media.targets.utils.ShaderUtils
import com.wangzhumo.app.module.media.targets.utils.TextureUtils
import com.wangzhumo.app.module.media.targets.widget.IGLESRenderer
import java.nio.FloatBuffer

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-09-28  16:24
 */
class CameraTextureRenderer : IGLESRenderer {


    /**
     * 顶点数组
     */
    val vertexData = floatArrayOf(
        1.0f, 1.0f, 1.0f, 1.0f,
        -1.0f, 1.0f, 0.0f, 1.0f,
        -1.0f, -1f, 0.0f, 0.0f,
        1.0f, 1.0f, 1.0f, 1.0f,
        -1.0f, -1.0f, 0f, 0.0f,
        1.0f, -1.0f, 1.0f, 0.0f
    )


    /**
     * 程序
     */
    private var mShaderProgram = -1
    private val mOESTextureId  = TextureUtils.loadOESTexture()
    private val mVertexBuffer = TextureUtils.loadVertexBuffer(vertexData)



    override fun onSurfaceCreated() {
        val vertexShader = ShaderUtils.compileVertexShader(UIUtils.readRaw(R.raw.vertex_texture_shader));
        val fragmentShader = ShaderUtils.compileFragmentShader(UIUtils.readRaw(R.raw.fragment_texture_shader));
        mShaderProgram = ShaderUtils.linkProgram(vertexShader, fragmentShader);
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