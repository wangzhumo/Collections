package com.wangzhumo.app.module.media.targets.utils

import android.opengl.GLES11Ext
import android.opengl.GLES20
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import android.graphics.SurfaceTexture


/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-08-23$  23:31$
 */
object TextureUtils {

    /**
     * 加载OES Texture
     *
     * @return
     */
    @JvmStatic
    fun loadOESTexture(): Int {
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
     * 创建一个顶点Buffer
     */
    fun loadVertexBuffer(vertexData: FloatArray): FloatBuffer {
        val buffer = ByteBuffer.allocateDirect(vertexData.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
        buffer.put(vertexData, 0, vertexData.size).position(0)
        return buffer
    }

    /**
     * 创建一个顶点Buffer
     */
    fun loadVertexBuffer(byteBuffer: ByteBuffer,vertexData: FloatArray): FloatBuffer {
        val buffer = byteBuffer.order(ByteOrder.nativeOrder()).asFloatBuffer()
        buffer.put(vertexData, 0, vertexData.size).position(0)
        return buffer
    }


    /**
     * 创建一个新的SurfaceTexture
     */
    fun loadOESTexture(mOESTextureId: Int): SurfaceTexture {
        return SurfaceTexture(mOESTextureId)
    }
}

