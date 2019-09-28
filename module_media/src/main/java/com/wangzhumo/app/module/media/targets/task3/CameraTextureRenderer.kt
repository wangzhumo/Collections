package com.wangzhumo.app.module.media.targets.task3

import android.graphics.SurfaceTexture
import android.opengl.GLES11Ext
import android.opengl.GLES20
import android.opengl.GLES30
import android.util.Log
import android.view.TextureView
import com.orhanobut.logger.Logger
import com.wangzhumo.app.base.utils.UIUtils
import com.wangzhumo.app.module.media.R
import com.wangzhumo.app.module.media.targets.utils.RawUtils
import com.wangzhumo.app.module.media.targets.utils.ShaderUtils
import com.wangzhumo.app.module.media.targets.utils.TextureUtils
import com.wangzhumo.app.module.media.targets.widget.GLESTextureThread
import com.wangzhumo.app.module.media.targets.widget.GLESTextureView
import com.wangzhumo.app.module.media.targets.widget.IGLESRenderer


/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-09-28  16:24
 */
class CameraTextureRenderer(surfaceTexture: SurfaceTexture?,textureView: TextureView?) : IGLESRenderer {

    /**
     * GL Thread
     */
    lateinit var mGLThread : GLESTextureThread
    //xml里面的TextureView
    private var mTextureView: TextureView? = textureView
    private var mSurfaceTexture: SurfaceTexture? = surfaceTexture

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
    private var mRendererMode = GLESTextureView.RENDERMODE_WHEN_DIRTY
    private val mOESTextureId = TextureUtils.loadOESTexture()
    private val mVertexBuffer = TextureUtils.loadVertexBuffer(vertexData)


    init {
        Log.e("Renderer","CameraTextureRenderer Init")
    }

    override fun onSurfaceCreated() {
        Log.e("Renderer","CameraTextureRenderer onSurfaceCreated")
        //主要是创建线程.
        mGLThread = GLESTextureThread(mTextureView,mSurfaceTexture)
        mGLThread.sendEmptyMessage(GLESTextureThread.MSG_INIT)
        mGLThread.setRendererListener(this)
        mGLThread.attachSurfaceId(mOESTextureId)

        //加载GL的一些东西
        val vertexShader = ShaderUtils.compileVertexShader(RawUtils.readResource(R.raw.vertex_texture_shader))
        val fragmentShader = ShaderUtils.compileFragmentShader(RawUtils.readResource(R.raw.fragment_texture_shader))
        mShaderProgram = ShaderUtils.linkProgram(vertexShader, fragmentShader)

        aPositionLocation = GLES30.glGetAttribLocation(mShaderProgram, POSITION_ATTRIBUTE);
        aTextureCoordLocation = GLES30.glGetAttribLocation(mShaderProgram, TEXTURE_COORD_ATTRIBUTE);
        uTextureMatrixLocation = GLES30.glGetUniformLocation(mShaderProgram, TEXTURE_MATRIX_UNIFORM);
        uTextureSamplerLocation = GLES30.glGetUniformLocation(mShaderProgram, TEXTURE_SAMPLER_UNIFORM);


        mGLThread.sendEmptyMessage(GLESTextureThread.MSG_INIT_SURFACE)
    }

    override fun onSurfaceChanged(width: Int, height: Int) {
        GLES30.glViewport(0, 0, width, height)
    }

    override fun onFrameAvailable(surfaceTexture: SurfaceTexture?) {
        Log.e("Renderer","onFrameAvailable")
        mGLThread.requestRender()
    }

    override fun onDrawFrame() {
        Log.e("Renderer","onDrawFrame")
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        GLES30.glUseProgram(mShaderProgram)

        mSurfaceTexture?.apply {
            updateTexImage()
            getTransformMatrix(transformMatrix)
        }

        GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
        GLES30.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, mOESTextureId)
        GLES30.glUniform1i(uTextureSamplerLocation, 0)
        GLES30.glUniformMatrix4fv(uTextureMatrixLocation, 1, false, transformMatrix, 0)

        mVertexBuffer.position(0);
        GLES30.glEnableVertexAttribArray(aPositionLocation);
        GLES30.glVertexAttribPointer(
            aPositionLocation,
            2,
            GLES30.GL_FLOAT,
            false,
            STRIDE,
            mVertexBuffer
        )

        mVertexBuffer.position(2);
        GLES30.glEnableVertexAttribArray(aTextureCoordLocation);
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

    override fun onResume() {

    }

    override fun onPause() {

    }

    override fun onDestroy() {
        mGLThread.sendEmptyMessage(GLESTextureThread.MSG_DETACH)
    }

    override fun getTextureId(): Int {
        return mOESTextureId
    }

    override fun setRenderMode(rendererMode: Int) {
        mRendererMode = rendererMode
    }

    override fun setTextureView(textureView: TextureView) {
        mTextureView = textureView
    }

    override fun setSurfaceTexture(surfaceTexture: SurfaceTexture) {
        mSurfaceTexture = surfaceTexture
    }


    companion object {
        private val POSITION_ATTRIBUTE = "aPosition"
        private val TEXTURE_COORD_ATTRIBUTE = "aTextureCoordinate"
        private val TEXTURE_MATRIX_UNIFORM = "uTextureMatrix"
        private val TEXTURE_SAMPLER_UNIFORM = "uTextureSampler"

        private val POSITION_SIZE = 2
        private val TEXTURE_SIZE = 2
        private val STRIDE = (POSITION_SIZE + TEXTURE_SIZE) * 4
    }
}