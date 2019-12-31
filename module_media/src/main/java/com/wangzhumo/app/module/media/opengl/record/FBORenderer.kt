package com.wangzhumo.app.module.media.opengl.record

import android.content.Context
import android.graphics.SurfaceTexture
import android.opengl.GLES11Ext
import android.opengl.GLES20
import android.opengl.GLES30
import android.opengl.Matrix
import android.util.Log
import com.wangzhumo.app.base.utils.DensityUtils
import com.wangzhumo.app.module.media.R
import com.wangzhumo.app.module.media.opengl.IRenderer
import com.wangzhumo.app.module.media.opengl.OnFBOSurfaceListener
import com.wangzhumo.app.module.media.opengl.RawUtils
import com.wangzhumo.app.module.media.opengl.gles.GLUtils
import java.nio.FloatBuffer

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-12-25  15:01
 *
 * FBOSurfaceView 的 Render,离屏渲染
 */
class FBORenderer(ctx: Context) : IRenderer, SurfaceTexture.OnFrameAvailableListener {


    //顶点数据
    private var mVertexBuffer: FloatBuffer
    //纹理数据
    private var mTextureBuffer: FloatBuffer
    //程序地址
    private var mShaderProgram = -1
    private var mOESTextureId = -1
    //屏幕尺寸
    private var screenWidth = 0
    private var screenHeight = 0

    //顶点的位置
    private var aPositionLocation = -1
    //纹理的位置
    private var aTextureCoordLocation = -1
    //矩阵变换
    private var uTextureMatrixLocation = -1


    //变换矩阵
    private val matrix = FloatArray(16)

    //vbo的Id
    private var vboId = 0
    //fbo的Id
    private var fboId = 0
    //和fboId绑定的texture的ID
    private var fboTextureId = 0

    //用显示相机的
    private var surfaceTexture: SurfaceTexture? = null

    //回调
    private var listener: OnFBOSurfaceListener? = null
    private var cameraRender: FBOCameraRenderer? = null


    init {
        cameraRender = FBOCameraRenderer()
        //创建用于native的数据,并把游标位置指向第一个字节
        mVertexBuffer = GLUtils.createFloatBuffer(VERTEX_POINT)
        mTextureBuffer = GLUtils.createFloatBuffer(TEXTURE_POINT)
        //获取屏幕尺寸
        screenHeight = DensityUtils.getScreenHeight(ctx)
        screenWidth = DensityUtils.getScreenWidth(ctx)
        Log.d(TAG, "FBORenderer Init.")
    }


    override fun onSurfaceCreated() {
        Log.d(TAG, "FBORenderer onSurfaceCreated.")
        //设置背景颜色 - 白色
        GLES30.glClearColor(0f, 0f, 0f, 0f)
        //获取Program
        mShaderProgram = GLUtils.linkProgram(RawUtils.readResource(R.raw.vertex_shader), RawUtils.readResource(R.raw.fragment_shader))
        if (mShaderProgram > 0) {
            //为着色器程序传递参数 - 必须先拿到句柄才能够传递数据
            aPositionLocation = GLES30.glGetAttribLocation(mShaderProgram, "a_Position")
            aTextureCoordLocation = GLES30.glGetAttribLocation(mShaderProgram, "a_TexCoord")
            uTextureMatrixLocation = GLES30.glGetUniformLocation(mShaderProgram, "u_Matrix")
            //创建一个vbo
            Log.d(TAG, "FBORenderer createVBOInstance.")
            createVBOInstance()
            //创建一个fbo
            Log.d(TAG, "FBORenderer createFBOInstance.")
            createFBOInstance()
            //创建相机扩展的纹理
            Log.d(TAG, "FBORenderer createCameraTexture.")
            createCameraTexture()
        }
        cameraRender?.onSurfaceCreated()
    }


    override fun onSurfaceChanged(width: Int, height: Int) {
        //修改视口的大小
        GLES30.glViewport(0, 0, width, height)
        cameraRender?.onSurfaceChanged(width, height)
    }

    override fun onDrawFrame(surfaceTexture: SurfaceTexture?) {
        Log.d(TAG, "FBORenderer  onDrawFrame")
        //调用触发onFrameAvailable，更新预览图像
        surfaceTexture?.updateTexImage()

        //清空颜色
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)

        //使用已经创建的程序
        GLES30.glUseProgram(mShaderProgram)

        //绑定FBO
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, fboId)

        //给摄像头的扩展纹理赋值
        GLES30.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, mOESTextureId)
        GLES30.glEnableVertexAttribArray(aPositionLocation)
        GLES30.glEnableVertexAttribArray(aTextureCoordLocation)

        //变换矩阵.
        GLES30.glUniformMatrix4fv(uTextureMatrixLocation, 1, false, matrix, 0)

        //使用VBO设置纹理和顶点值
        //TODO  什么意思啊?
        useVboSetVertext()

        //绘制 GLES30.GL_TRIANGLE_STRIP:复用坐标
        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_STRIP, 0, 4)
        GLES30.glEnableVertexAttribArray(aPositionLocation)
        GLES30.glEnableVertexAttribArray(aTextureCoordLocation)

        //解除绑定FBO
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, 0)

        //渲染显示
        cameraRender?.draw(fboTextureId)
    }

    override fun onFrameAvailable(surfaceTexture: SurfaceTexture?) {
        Log.d(TAG, "FBORenderer  onSurfaceChanged")
        listener?.onFrameAvailable(surfaceTexture);
    }


    /**
     * 创建摄像头预览的扩展纹理
     */
    private fun createCameraTexture() {
        //1.创建一个纹理
        val textureArr = IntArray(1)
        //创建纹理
        GLES30.glGenTextures(1, textureArr, 0)
        mOESTextureId = textureArr[0]
        //绑定纹理
        GLES30.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, mOESTextureId)
        //环绕（超出纹理坐标范围）  （s==x t==y GL_REPEAT 重复）
        GLES30.glTexParameteri(
            GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
            GLES30.GL_TEXTURE_WRAP_S,
            GLES30.GL_REPEAT
        )
        GLES30.glTexParameteri(
            GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
            GLES30.GL_TEXTURE_WRAP_T,
            GLES30.GL_REPEAT
        )
        //过滤（纹理像素映射到坐标点）  （缩小、放大：GL_LINEAR线性）
        GLES30.glTexParameteri(
            GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
            GLES30.GL_TEXTURE_MIN_FILTER,
            GLES30.GL_LINEAR
        )
        GLES30.glTexParameteri(
            GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
            GLES30.GL_TEXTURE_MAG_FILTER,
            GLES30.GL_LINEAR
        )

        //2.创建一个SurfaceTexture
        surfaceTexture = SurfaceTexture(mOESTextureId)
        surfaceTexture?.setOnFrameAvailableListener(this)

        //3.回调给其他人的
        Log.d(TAG, "FBORenderer listener?.onSurfaceCreate(surfaceTexture, fboTextureId).")
        listener?.onSurfaceCreate(surfaceTexture, fboTextureId)

        //4.解绑扩展纹理
        GLES30.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, 0)
    }

    /**
     * 创建FBO
     */
    private fun createFBOInstance() {
        //1.创建FBO
        val fboArr = IntArray(1)
        GLES30.glGenFramebuffers(1, fboArr, 0)
        fboId = fboArr[0]
        //2.绑定FBO
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, fboId)

        //3.创建一个纹理
        val textureArr = IntArray(1)
        //创建纹理
        GLES30.glGenTextures(1, textureArr, 0)
        fboTextureId = textureArr[0]
        //绑定纹理
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, fboTextureId)
        //环绕（超出纹理坐标范围）  （s==x t==y GL_REPEAT 重复）
        GLES30.glTexParameteri(
            GLES30.GL_TEXTURE_2D,
            GLES30.GL_TEXTURE_WRAP_S,
            GLES30.GL_REPEAT
        )
        GLES30.glTexParameteri(
            GLES30.GL_TEXTURE_2D,
            GLES30.GL_TEXTURE_WRAP_T,
            GLES30.GL_REPEAT
        )
        //过滤（纹理像素映射到坐标点）  （缩小、放大：GL_LINEAR线性）
        GLES30.glTexParameteri(
            GLES30.GL_TEXTURE_2D,
            GLES30.GL_TEXTURE_MIN_FILTER,
            GLES30.GL_LINEAR
        )
        GLES30.glTexParameteri(
            GLES30.GL_TEXTURE_2D,
            GLES30.GL_TEXTURE_MAG_FILTER,
            GLES30.GL_LINEAR
        )

        //4.把这个纹理和之前创建的 FBO 绑定
        GLES20.glFramebufferTexture2D(
            GLES30.GL_FRAMEBUFFER,
            GLES30.GL_COLOR_ATTACHMENT0,
            GLES30.GL_TEXTURE_2D,
            fboTextureId,
            0
        )

        //5.FBO需要自己管理在,这里我们给FBO的内存分配空间
        GLES30.glTexImage2D(
            GLES30.GL_TEXTURE_2D,
            0,
            GLES30.GL_RGBA,
            screenWidth,
            screenHeight,
            0,
            GLES30.GL_RGBA,
            GLES30.GL_UNSIGNED_BYTE,
            null
        )

        //6.检查FBO与Texture是否绑定成功
        if (GLES30.glCheckFramebufferStatus(GLES30.GL_FRAMEBUFFER) != GLES30.GL_FRAMEBUFFER_COMPLETE) {
            Log.d(TAG, "createFBOInstance 绑定FBO和Texture失败")
        }

        //7.解除纹理和FBO
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0)
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, 0)
    }


    /**
     * 创建VBO
     */
    private fun createVBOInstance() {
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

    /**
     * 使用vbo设置顶点位置
     */
    private fun useVboSetVertext() {
        //1. 绑定VBO
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboId)
        //2. 设置顶点数据
        GLES20.glVertexAttribPointer(
            aPositionLocation,
            3,
            GLES20.GL_FLOAT,
            false,
            12,
            0
        )
        GLES20.glVertexAttribPointer(
            aTextureCoordLocation, 3,
            GLES20.GL_FLOAT,
            false,
            12,
            VERTEX_POINT.size * 4
        )
        //3. 解绑VBO
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0)
    }


    /**
     * 绑定一个监听回调
     * @param OnFBOSurfaceListener listener
     */
    fun setFBOListener(listener: OnFBOSurfaceListener) {
        this.listener = listener
    }

    /**
     * 初始化矩阵
     */
    fun resetMatrix() {
        //初始化
        Matrix.setIdentityM(matrix, 0)
    }


    /**
     * 旋转
     *
     * @param angle
     * @param x
     * @param y
     * @param z
     */
    fun setAngle(angle: Float, x: Float, y: Float, z: Float) {
        //旋转
        Matrix.rotateM(matrix, 0, angle, x, y, z)
    }

    companion object {
        const val TAG = "OpenGL Record"


        private const val POSITION_ATTRIBUTE = "aPosition"
        private const val TEXTURE_COORD_ATTRIBUTE = "aTextureCoord"
        private const val TEXTURE_MATRIX_UNIFORM = "uTextureMatrix"
        private const val TEXTURE_SAMPLER_UNIFORM = "uTextureSampler"

        //纹理
        private val TEXTURE_POINT = floatArrayOf(
            0f, 1f, 0f,
            1f, 1f, 0f,
            0f, 0f, 0f,
            1f, 0f, 0f
        )

        //顶点
        private val VERTEX_POINT = floatArrayOf(
            -1f, -1f, 0f,
            1f, -1f, 0f,
            -1f, 1f, 0f,
            1f, 1f, 0f
        )
    }

}
