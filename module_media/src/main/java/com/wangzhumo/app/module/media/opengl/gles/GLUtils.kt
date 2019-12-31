package com.wangzhumo.app.module.media.opengl.gles

import android.opengl.GLES11Ext
import android.opengl.GLES20
import android.opengl.Matrix
import android.util.Log
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer


/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-09-28  16:16
 *
 * GL工具类
 * 1.创建一个floatBuffer
 * 2.编译着色器程序
 * 3.链接程序 (顶点着色器 + 片段着色器) 获取GL的Program
 * 4.提供一个MATRIX
 */
object GLUtils {

    val IDENTITY_MATRIX: FloatArray = FloatArray(16)

    init {
        Matrix.setIdentityM(IDENTITY_MATRIX, 0)
    }


    /**
     * 编译
     *
     * @param type       顶点着色器:GLES30.GL_VERTEX_SHADER
     * 片段着色器:GLES30.GL_FRAGMENT_SHADER
     * @param shaderCode
     * @return
     */
    @JvmStatic
    private fun compileShader(type: Int, shaderCode: String): Int {
        //创建一个着色器
        val shaderId = GLES20.glCreateShader(type)
        if (shaderId != 0) {
            GLES20.glShaderSource(shaderId, shaderCode)
            GLES20.glCompileShader(shaderId)
            //检测状态
            val compileStatus = IntArray(1)
            GLES20.glGetShaderiv(shaderId, GLES20.GL_COMPILE_STATUS, compileStatus, 0)
            if (compileStatus[0] == 0) {
                val logInfo = GLES20.glGetShaderInfoLog(shaderId)
                System.err.println(logInfo)
                //创建失败
                GLES20.glDeleteShader(shaderId)
                return 0
            }
            return shaderId
        } else {
            //创建失败
            return 0
        }
    }

    /**
     * 链接程序
     *
     * @param vertexShader   顶点着色器
     * @param fragmentShader 片段着色器
     * @return
     */
    @JvmStatic
    fun linkProgram(vertexShader: String, fragmentShader: String): Int {
        val vertexShaderId =
            compileShader(
                GLES20.GL_VERTEX_SHADER,
                vertexShader
            )
        if (vertexShaderId == 0) {
            return 0
        }
        val fragmentShaderId =
            compileShader(
                GLES20.GL_FRAGMENT_SHADER,
                fragmentShader
            )
        if (fragmentShaderId == 0) {
            return 0
        }

        val programId = GLES20.glCreateProgram()
        if (programId != 0) {
            //将顶点着色器加入到程序
            GLES20.glAttachShader(programId, vertexShaderId)
            //将片元着色器加入到程序中
            GLES20.glAttachShader(programId, fragmentShaderId)
            //链接着色器程序
            GLES20.glLinkProgram(programId)

            val linkStatus = IntArray(1)
            GLES20.glGetProgramiv(programId, GLES20.GL_LINK_STATUS, linkStatus, 0)
            if (linkStatus[0] == 0) {
                val logInfo = GLES20.glGetProgramInfoLog(programId)
                System.err.println(logInfo)
                Log.e("OpenGL", "Could not link program: ")
                GLES20.glDeleteProgram(programId)
                return 0
            }
            return programId
        } else {
            //创建失败
            return 0
        }
    }


    /**
     * 验证程序片段是否有效
     *
     * @param programObjectId
     * @return
     */
    @JvmStatic
    fun validProgram(programObjectId: Int): Boolean {
        GLES20.glValidateProgram(programObjectId)
        val programStatus = IntArray(1)
        GLES20.glGetProgramiv(programObjectId, GLES20.GL_VALIDATE_STATUS, programStatus, 0)
        return programStatus[0] != 0
    }

    /**
     * 加载OES Texture
     *
     * @return
     */
    @JvmStatic
    fun createOESTexture(): Int {
        val textureIds = IntArray(1)

        //textureIds 中存放一个textures中的可用值
        GLES20.glGenTextures(1, textureIds, 0)

        //当调用glBindTexture
        //如果是第一次调用这个函数textureIds[0] ，会创建一个新的纹理对象
        //如果textureIds[0]已经创建过了，把这个纹理置为 活动
        //如果texture为0，就停止使用这个纹理对象，并返回无名称的默认纹理
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureIds[0])

        //设置参数
        GLES20.glTexParameterf(
            GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
            GLES20.GL_TEXTURE_MIN_FILTER,
            GLES20.GL_NEAREST.toFloat()
        )
        GLES20.glTexParameterf(
            GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
            GLES20.GL_TEXTURE_MAG_FILTER,
            GLES20.GL_LINEAR.toFloat()
        )
        GLES20.glTexParameterf(
            GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
            GLES20.GL_TEXTURE_WRAP_S,
            GLES20.GL_CLAMP_TO_EDGE.toFloat()
        )
        GLES20.glTexParameterf(
            GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
            GLES20.GL_TEXTURE_WRAP_T,
            GLES20.GL_CLAMP_TO_EDGE.toFloat()
        )

        //取消绑定纹理，并且返回一个默认的纹理
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, 0)
        return textureIds[0]
    }

    /**
     * Creates a texture from raw data.
     *
     * @param data Image data, in a "direct" ByteBuffer.
     * @param width Texture width, in pixels (not bytes).
     * @param height Texture height, in pixels.
     * @param format Image data format (use constant appropriate for glTexImage2D(), e.g. GL_RGBA).
     * @return Handle to texture.
     */
    fun createImageTexture(data: ByteBuffer?, width: Int, height: Int, format: Int): Int {
        val textureHandles = IntArray(1)
        val textureHandle: Int
        GLES20.glGenTextures(1, textureHandles, 0)
        textureHandle = textureHandles[0]
        // Bind the texture handle to the 2D texture target.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle)
        // Configure min/mag filtering, i.e. what scaling method do we use if what we're rendering
        // is smaller or larger than the source image.
        GLES20.glTexParameteri(
            GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,
            GLES20.GL_LINEAR
        )
        GLES20.glTexParameteri(
            GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,
            GLES20.GL_LINEAR
        )
        // Load the data from the buffer into the texture handle.
        GLES20.glTexImage2D(
            GLES20.GL_TEXTURE_2D, 0, format,
            width, height, 0, format, GLES20.GL_UNSIGNED_BYTE, data
        )
        return textureHandle
    }


    /**
     * 创建一个floatBuffer
     */
    @JvmStatic
    fun createFloatBuffer(vertexData: FloatArray): FloatBuffer {
        val buffer = ByteBuffer.allocateDirect(vertexData.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
        buffer.put(vertexData, 0, vertexData.size).position(0)
        return buffer
    }

    /**
     * 创建一个顶点Buffer
     */
    @JvmStatic
    fun createFloatBuffer(byteBuffer: ByteBuffer, vertexData: FloatArray): FloatBuffer {
        val buffer = byteBuffer.order(ByteOrder.nativeOrder()).asFloatBuffer()
        buffer.put(vertexData, 0, vertexData.size).position(0)
        return buffer
    }

    /**
     * Checks to see if the location we obtained is valid.  GLES returns -1 if a label
     * could not be found, but does not set the GL error.
     *
     *
     * Throws a RuntimeException if the location is invalid.
     */
    @JvmStatic
    fun checkLocation(location: Int, label: String) {
        if (location < 0) {
            throw RuntimeException("Unable to locate '$label' in program")
        }
    }

    /**
     * Checks to see if a GLES error has been raised.
     */
    @JvmStatic
    fun checkGlError(op: String) {
        val error = GLES20.glGetError()
        if (error != GLES20.GL_NO_ERROR) {
            val msg = op + ": glError 0x" + Integer.toHexString(error)
            throw RuntimeException(msg)
        }
    }

}