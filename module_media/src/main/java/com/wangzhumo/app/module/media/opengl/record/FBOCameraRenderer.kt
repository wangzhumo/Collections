package com.wangzhumo.app.module.media.opengl.record

import android.graphics.SurfaceTexture
import android.opengl.GLES20
import android.opengl.GLES30
import android.util.Log
import com.wangzhumo.app.module.media.R
import com.wangzhumo.app.module.media.opengl.IRenderer
import com.wangzhumo.app.module.media.utils.RawUtils
import com.wangzhumo.app.module.media.utils.ShaderUtils
import com.wangzhumo.app.module.media.utils.TextureUtils
import java.nio.FloatBuffer

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-12-25  15:01
 *
 * FBOSurfaceView 的 Render, 预览FBO处理后的数据
 */
class FBOCameraRenderer : IRenderer {


    //顶点数据
    private var mVertexBuffer: FloatBuffer
    //纹理数据
    private var mTextureBuffer: FloatBuffer
    //程序地址
    private var mShaderProgram = -1
    //纹理
    private var mOESTextureId = -1
    //顶点的位置
    private var aPositionLocation = -1
    //纹理的位置
    private var aTextureCoordLocation = -1
    //颜色类型切换
    private var changeType = 0
    //颜色值切换
    private var changeColor = 0
    //vbo 的 id
    private var vboId = -1
    private var fboTextureId = -1


    //滤镜传入类型
    private var type = 0
    //滤镜传入颜色
    private val color = floatArrayOf(0.0f, 0.0f, 0.0f)


    init {
        //创建用于native的数据,并把游标位置指向第一个字节
        mVertexBuffer = TextureUtils.loadVertexBuffer(VERTEX_POINT)
        mTextureBuffer = TextureUtils.loadVertexBuffer(TEXTURE_POINT)
        Log.d(TAG, "FBOCameraRenderer Init.")
    }


    override fun onSurfaceCreated() {
        Log.d(TAG, "FBOCameraRenderer onSurfaceCreated.")
        //启用透明
        GLES30.glEnable(GLES30.GL_BLEND)
        GLES30.glBlendFunc(GLES30.GL_SRC_ALPHA, GLES30.GL_ONE_MINUS_SRC_ALPHA)

        //设置背景颜色
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)

        //创建并编译顶点着色器，vertexShader 是编译后顶底着色器的句柄
        val vertexShader =
            ShaderUtils.compileVertexShader(RawUtils.readResource(R.raw.vertex_shader_screen))
        //创建并编译片段着色器
        val fragmentShader =
            ShaderUtils.compileFragmentShader(RawUtils.readResource(R.raw.fragment_shader_screen))
        //获取Program
        mShaderProgram = ShaderUtils.linkProgram(vertexShader, fragmentShader)

        if (mShaderProgram > 0) {
            //为着色器程序传递参数 - 必须先拿到句柄才能够传递数据
            aPositionLocation = GLES30.glGetAttribLocation(mShaderProgram, "a_Position")
            aTextureCoordLocation = GLES30.glGetAttribLocation(mShaderProgram, "a_TexCoord")
            mOESTextureId = GLES30.glGetUniformLocation(mShaderProgram, "sTexture")
            changeType = GLES30.glGetUniformLocation(mShaderProgram, "vChangeType")
            changeColor = GLES30.glGetUniformLocation(mShaderProgram, "vChangeColor")

            //创建一个vbo
            createVBOInstance()

        }
    }

    private fun createVBOInstance() {
        Log.d(TAG, "FBOCameraRenderer createVBOInstance start.")
        //1. 创建VBO
        val vbos = IntArray(1)
        GLES30.glGenBuffers(vbos.size, vbos, 0)
        vboId = vbos[0]
        //2. 绑定VBO
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vboId)
        //3. 分配VBO需要的缓存大小
        GLES30.glBufferData(
            GLES30.GL_ARRAY_BUFFER,
            VERTEX_POINT.size * 4 + TEXTURE_POINT.size * 4,
            null,
            GLES30.GL_STATIC_DRAW
        )
        //4. 为VBO设置顶点数据的值
        GLES30.glBufferSubData(
            GLES30.GL_ARRAY_BUFFER,
            0,
            VERTEX_POINT.size * 4,
            mVertexBuffer
        )
        GLES30.glBufferSubData(
            GLES30.GL_ARRAY_BUFFER,
            VERTEX_POINT.size * 4,
            TEXTURE_POINT.size * 4,
            mTextureBuffer
        )
        //5. 解绑VBO
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0)
    }

    override fun onSurfaceChanged(width: Int, height: Int) {
        GLES30.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(surfaceTexture: SurfaceTexture?) {
        //清空颜色
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)

        //使用已经创建的程序
        GLES30.glUseProgram(mShaderProgram)
        //设置滤镜
        GLES20.glUniform1i(changeType, type)
        GLES20.glUniform3fv(changeColor, 1, color, 0)

        //绑定纹理
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, fboTextureId)
        GLES20.glEnableVertexAttribArray(aPositionLocation)
        GLES20.glEnableVertexAttribArray(aTextureCoordLocation)

        //使用VBO设置纹理和顶点值
        useVboSetVertext()

        //绘制 GLES30.GL_TRIANGLE_STRIP:复用坐标
        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_STRIP, 0, 4)
        GLES30.glEnableVertexAttribArray(aPositionLocation)
        GLES30.glEnableVertexAttribArray(aPositionLocation)

        //解除绑定FBO
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, 0)
    }

    /**
     * 使用vbo设置顶点位置
     */
    private fun useVboSetVertext() {
        //1. 绑定VBO
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboId)
        //2. 设置顶点数据
        GLES20.glVertexAttribPointer(
            aPositionLocation,
            COORDS_PER_VERTEX,
            GLES20.GL_FLOAT,
            false,
            VERTEX_STRIDE,
            0
        )
        GLES20.glVertexAttribPointer(
            aTextureCoordLocation,
            COORDS_PER_VERTEX,
            GLES20.GL_FLOAT,
            false,
            VERTEX_STRIDE,
            VERTEX_POINT.size * 4
        )
        //3. 解绑VBO
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0)
    }

    fun draw(fboTextureId:Int){
        this.fboTextureId = fboTextureId
    }


    companion object{

        const val TAG = "OpenGL Record"

        private const val COORDS_PER_VERTEX = 3
        private const val VERTEX_STRIDE = COORDS_PER_VERTEX * 4

        //纹理
        private val TEXTURE_POINT = floatArrayOf(
            0f, 1f, 0f,
            1f, 1f, 0f,
            0f, 0f, 0f,
            1f, 0f, 0f
        )

        //顶点,后面的 4个点是水印预留位置
        private val VERTEX_POINT = floatArrayOf(
            -1f, -1f, 0f,
            1f, -1f, 0f,
            -1f, 1f, 0f,
            1f, 1f, 0f,

            0f, 0f, 0f,
            0f, 0f, 0f,
            0f, 0f, 0f,
            0f, 0f, 0f
        )
    }

}
