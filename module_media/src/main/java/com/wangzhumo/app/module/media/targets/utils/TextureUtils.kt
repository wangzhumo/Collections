package com.wangzhumo.app.module.media.targets.utils;

import android.opengl.GLES11Ext
import android.opengl.GLES20
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-08-23$  23:31$
 */
class TextureUtils {


    /**
     * 创建一个顶点Buffer
     */
    private fun loadVertexBuffer(vertexData: FloatArray): FloatBuffer {
        val buffer = ByteBuffer.allocateDirect(vertexData.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
        buffer.put(vertexData, 0, vertexData.size).position(0)
        return buffer
    }


    companion object {

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


        private val POSITION_ATTRIBUTE = "aPosition"
        private val TEXTURE_COORD_ATTRIBUTE = "aTextureCoordinate"
        private val TEXTURE_MATRIX_UNIFORM = "uTextureMatrix"
        private val TEXTURE_SAMPLER_UNIFORM = "uTextureSampler"


        private val VERTEX_SHADER = "attribute vec4 aPosition;\n" +
                "uniform mat4 uTextureMatrix;\n" +
                "attribute vec4 aTextureCoordinate;\n" +
                "varying vec2 vTextureCoord;\n" +
                "void main()\n" +
                "{\n" +
                "  vTextureCoord = (uTextureMatrix * aTextureCoordinate).xy;\n" +
                "  gl_Position = aPosition;\n" +
                "}"

        private val FRAGMENT_SHADER = "#extension GL_OES_EGL_image_external : require\n" +
                "precision mediump float;\n" +
                "uniform samplerExternalOES uTextureSampler;\n" +
                "varying vec2 vTextureCoord;\n" +
                "void main()\n" +
                "{\n" +
                "  vec4 vCameraColor = texture2D(uTextureSampler, vTextureCoord);\n" +
                "  float fGrayColor = (0.3*vCameraColor.r + 0.59*vCameraColor.g + 0.11*vCameraColor.b);\n" +
                "  gl_FragColor = vec4(fGrayColor, fGrayColor, fGrayColor, 1.0);\n" +
                "}\n"


        /**
         * 加载OES Texture
         *
         * @return
         */
        fun loadOESTexture(): Int {
            val textureIds = IntArray(1)
            GLES20.glGenTextures(1, textureIds, 0)
            GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureIds[0])
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
            GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, 0)
            return textureIds[0]
        }


        /**
         * 创建一个顶点Buffer
         */
        fun loadVertexBuffer(vertexData: FloatArray): FloatBuffer {
            val buffer = ByteBuffer.allocateDirect(vertexData.size * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
            buffer.put(vertexData, 0, vertexData.size).position(0)
            return buffer
        }
    }
}
