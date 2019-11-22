package com.wangzhumo.app.module.media.opengl.camera

import android.graphics.SurfaceTexture
import android.opengl.GLES11Ext
import android.opengl.GLES20
import android.opengl.GLES30
import com.wangzhumo.app.module.media.R
import com.wangzhumo.app.module.media.targets.utils.RawUtils
import com.wangzhumo.app.module.media.targets.utils.ShaderUtils
import com.wangzhumo.app.module.media.targets.utils.TextureUtils
import java.nio.FloatBuffer


/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-11-22  21:31
 *
 * 渲染实现
 */
class TextureEGLRenderer constructor(private val textureId:Int) : ITextureRenderer{

    private var mVertexBuffer: FloatBuffer? = null

    /**
     * 顶点数组
     */
    private val vertexData = floatArrayOf(
        1.0f, 1.0f, 1.0f, 1.0f,
        -1.0f, 1.0f, 0.0f, 1.0f,
        -1.0f, -1f, 0.0f, 0.0f,
        1.0f, 1.0f, 1.0f, 1.0f,
        -1.0f, -1.0f, 0f, 0.0f,
        1.0f, -1.0f, 1.0f, 0.0f
    )

    /**
     * 变换矩阵
     */
    private val transformMatrix = FloatArray(16)

    private var aPositionLocation = -1
    private var aTextureCoordLocation = -1
    private var uTextureMatrixLocation = -1
    private var uTextureSamplerLocation = -1

    /**
     * 程序
     */
    private var mShaderProgram = -1

    init {
        mVertexBuffer = TextureUtils.loadVertexBuffer(vertexData)
    }

    override fun onSurfaceCreated() {
        //加载GL的一些东西
        val vertexShader = ShaderUtils.compileVertexShader(RawUtils.readResource(R.raw.vertex_texture_shader))
        val fragmentShader = ShaderUtils.compileFragmentShader(RawUtils.readResource(R.raw.fragment_texture_shader))
        mShaderProgram = ShaderUtils.linkProgram(vertexShader, fragmentShader)

        aPositionLocation = GLES30.glGetAttribLocation(mShaderProgram,POSITION_ATTRIBUTE)
        aTextureCoordLocation = GLES30.glGetAttribLocation(mShaderProgram,TEXTURE_COORD_ATTRIBUTE)
        uTextureMatrixLocation = GLES30.glGetUniformLocation(mShaderProgram, TEXTURE_MATRIX_UNIFORM)
        uTextureSamplerLocation = GLES30.glGetUniformLocation(mShaderProgram, TEXTURE_SAMPLER_UNIFORM)
    }

    override fun onSurfaceChanged(width: Int, height: Int) {
        GLES30.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(surfaceTexture: SurfaceTexture?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        //开始使用程序
        GLES30.glUseProgram(mShaderProgram)

        surfaceTexture!!.updateTexImage()
        surfaceTexture.getTransformMatrix(transformMatrix)

        GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
        GLES30.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureId)
        GLES30.glUniform1i(uTextureSamplerLocation, 0)
        GLES30.glUniformMatrix4fv(uTextureMatrixLocation,
            1,
            false,
            transformMatrix,
            0)

        mVertexBuffer?.position(0)
        GLES30.glEnableVertexAttribArray(aPositionLocation)
        GLES30.glVertexAttribPointer(
            aPositionLocation,
            2,
            GLES30.GL_FLOAT,
            false,
            STRIDE,
            mVertexBuffer
        )

        mVertexBuffer?.position(2)
        GLES30.glEnableVertexAttribArray(aTextureCoordLocation)
        GLES30.glVertexAttribPointer(
            aTextureCoordLocation,
            2,
            GLES30.GL_FLOAT,
            false,
            STRIDE,
            mVertexBuffer
        )

        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 6)
    }

    companion object {
        private const val POSITION_ATTRIBUTE = "aPosition"
        private const val TEXTURE_COORD_ATTRIBUTE = "aTextureCoordinate"
        private const val TEXTURE_MATRIX_UNIFORM = "uTextureMatrix"
        private const val TEXTURE_SAMPLER_UNIFORM = "uTextureSampler"

        private const val POSITION_SIZE = 2
        private const val TEXTURE_SIZE = 2
        private const val STRIDE = (POSITION_SIZE + TEXTURE_SIZE) * 4
    }
}