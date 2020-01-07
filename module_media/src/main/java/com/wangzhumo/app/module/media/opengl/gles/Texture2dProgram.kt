package com.wangzhumo.app.module.media.opengl.gles

import android.opengl.GLES11Ext
import android.opengl.GLES20
import android.util.Log
import com.wangzhumo.app.module.media.R
import com.wangzhumo.app.base.utils.RawUtils
import java.nio.FloatBuffer

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-12-30  17:24
 *
 * GL program的一些源文件
 */
class Texture2dProgram {

    lateinit var mProgramType : ProgramType
    private var mProgramHandle = 0
    private var mTextureTarget = 0

    private var muMVPMatrixLoc = 0
    private var muTexMatrixLoc = 0
    private var muKernelLoc = 0
    private var muTexOffsetLoc = 0
    private var muColorAdjustLoc = 0
    private var maPositionLoc = 0
    private var maTextureCoordLoc = 0

    constructor(programType: ProgramType){
        //在这里,判断不同的加载类型,提供不同的程序
        mProgramType = programType
        when(mProgramType){
            ProgramType.TEXTURE_2D -> {
                mTextureTarget = GLES20.GL_TEXTURE_2D
                mProgramHandle = GLUtils.linkProgram(
                    RawUtils.readResource(R.raw.grafika_all_vertex_shader),
                    RawUtils.readResource(R.raw.grafika_normal_fragment_shader)
                )
            }
            ProgramType.TEXTURE_EXT -> {
                mTextureTarget = GLES11Ext.GL_TEXTURE_EXTERNAL_OES
                mProgramHandle = GLUtils.linkProgram(
                    RawUtils.readResource(R.raw.grafika_all_vertex_shader),
                    RawUtils.readResource(R.raw.grafika_oes_ext_fragment_shader)
                )
            }
        }
        if (mProgramHandle == 0) {
            throw RuntimeException("Unable to create program")
        }
        Log.d(TAG,"Create program $mProgramHandle ($programType)")

        //获取一些,在glsl中定义的变量,用来赋值
        muMVPMatrixLoc = GLES20.glGetUniformLocation(mProgramHandle, "uMVPMatrix")
        GLUtils.checkLocation(muMVPMatrixLoc,"uMVPMatrix")

        muTexMatrixLoc = GLES20.glGetUniformLocation(mProgramHandle, "uTexMatrix")
        GLUtils.checkLocation(muTexMatrixLoc,"uTexMatrix")

        maPositionLoc = GLES20.glGetAttribLocation(mProgramHandle, "aPosition")
        GLUtils.checkLocation(maPositionLoc, "aPosition")

        maTextureCoordLoc = GLES20.glGetAttribLocation(mProgramHandle, "aTextureCoord")
        GLUtils.checkLocation(maTextureCoordLoc, "aTextureCoord")


    }


    /**
     * Releases the program.
     *
     *
     * The appropriate EGL context must be current (i.e. the one that was used to create
     * the program).
     */
    fun release() {
        Log.d(TAG, "deleting program $mProgramHandle")
        GLES20.glDeleteProgram(mProgramHandle)
        mProgramHandle = -1
    }

    /**
     * Returns the program type.
     */
    fun getProgramType(): ProgramType? {
        return mProgramType
    }

    /**
     * Creates a texture object suitable for use with this program.
     *
     *
     * On exit, the texture will be bound.
     */
    fun createTextureObject(): Int {
        val textures = IntArray(1)
        GLES20.glGenTextures(1, textures, 0)
        GLUtils.checkGlError("glGenTextures")

        val texId = textures[0]
        GLES20.glBindTexture(mTextureTarget, texId)
        GLUtils.checkGlError("glBindTexture $texId")
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
        GLES20.glTexParameteri(
            GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
            GLES20.GL_TEXTURE_WRAP_S,
            GLES20.GL_CLAMP_TO_EDGE
        )
        GLES20.glTexParameteri(
            GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
            GLES20.GL_TEXTURE_WRAP_T,
            GLES20.GL_CLAMP_TO_EDGE
        )
        GLUtils.checkGlError("glTexParameter")
        return texId
    }

    /**
     * Issues the draw call.  Does the full setup on every call.
     *
     * @param mvpMatrix The 4x4 projection matrix.
     * @param vertexBuffer Buffer with vertex position data.
     * @param firstVertex Index of first vertex to use in vertexBuffer.
     * @param vertexCount Number of vertices in vertexBuffer.
     * @param coordsPerVertex The number of coordinates per vertex (e.g. x,y is 2).
     * @param vertexStride Width, in bytes, of the position data for each vertex (often
     * vertexCount * sizeof(float)).
     * @param texMatrix A 4x4 transformation matrix for texture coords.  (Primarily intended
     * for use with SurfaceTexture.)
     * @param texBuffer Buffer with vertex texture data.
     * @param texStride Width, in bytes, of the texture data for each vertex.
     */
    fun draw(
        mvpMatrix: FloatArray,
        vertexBuffer: FloatBuffer,
        firstVertex: Int,
        vertexCount: Int,
        coordsPerVertex: Int,
        vertexStride: Int,
        texMatrix: FloatArray,
        texBuffer: FloatBuffer,
        textureId: Int,
        texStride: Int
    ) {
        GLUtils.checkGlError("draw start")
        // 使用指定的 program.
        GLES20.glUseProgram(mProgramHandle)
        GLUtils.checkGlError("glUseProgram")
        // Set the texture.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(mTextureTarget, textureId)

        // Copy the model / view / projection matrix over.
        GLES20.glUniformMatrix4fv(muMVPMatrixLoc, 1, false, mvpMatrix, 0)
        GLUtils.checkGlError("glUniformMatrix4fv")

        // Copy the texture transformation matrix over.
        GLES20.glUniformMatrix4fv(muTexMatrixLoc, 1, false, texMatrix, 0)
        GLUtils.checkGlError("glUniformMatrix4fv")

        // Enable the "aPosition" vertex attribute.
        GLES20.glEnableVertexAttribArray(maPositionLoc)
        GLUtils.checkGlError("glEnableVertexAttribArray")

        // Connect vertexBuffer to "aPosition".
        GLES20.glVertexAttribPointer(
            maPositionLoc, coordsPerVertex,
            GLES20.GL_FLOAT, false, vertexStride, vertexBuffer
        )
        GLUtils.checkGlError("glVertexAttribPointer")

        // Enable the "aTextureCoord" vertex attribute.
        GLES20.glEnableVertexAttribArray(maTextureCoordLoc)
        GLUtils.checkGlError("glEnableVertexAttribArray")

        // Connect texBuffer to "aTextureCoord".
        GLES20.glVertexAttribPointer(
            maTextureCoordLoc, 2,
            GLES20.GL_FLOAT, false, texStride, texBuffer
        )
        GLUtils.checkGlError("glVertexAttribPointer")

        // Draw the rect.
        GLES20.glDrawArrays(
            GLES20.GL_TRIANGLE_STRIP,
            firstVertex,
            vertexCount
        )
        GLUtils.checkGlError("glDrawArrays")

        // Done -- disable vertex array, texture, and program.
        GLES20.glDisableVertexAttribArray(maPositionLoc)
        GLES20.glDisableVertexAttribArray(maTextureCoordLoc)
        GLES20.glBindTexture(mTextureTarget, 0)
        GLES20.glUseProgram(0)
    }

    enum class ProgramType {
        TEXTURE_2D,TEXTURE_EXT
    }

    companion object{

        const val TAG = "OpenGL"

        // 所有程序的 顶点着色器.
        private const val VERTEX_SHADER = "uniform mat4 uMVPMatrix;\n" +
                "uniform mat4 uTexMatrix;\n" +
                "attribute vec4 aPosition;\n" +
                "attribute vec4 aTextureCoord;\n" +
                "varying vec2 vTextureCoord;\n" +
                "void main() {\n" +
                "    gl_Position = uMVPMatrix * aPosition;\n" +
                "    vTextureCoord = (uTexMatrix * aTextureCoord).xy;\n" +
                "}\n"

        // Simple fragment shader for use with "normal" 2D textures.
        private const val FRAGMENT_SHADER_2D = "precision mediump float;\n" +
                "varying vec2 vTextureCoord;\n" +
                "uniform sampler2D sTexture;\n" +
                "void main() {\n" +
                "    gl_FragColor = texture2D(sTexture, vTextureCoord);\n" +
                "}\n"


        // Simple fragment shader for use with external 2D textures (e.g. what we get from
        // SurfaceTexture).
        private const val FRAGMENT_SHADER_EXT =
            "#extension GL_OES_EGL_image_external : require\n" +
                    "precision mediump float;\n" +
                    "varying vec2 vTextureCoord;\n" +
                    "uniform samplerExternalOES sTexture;\n" +
                    "void main() {\n" +
                    "    gl_FragColor = texture2D(sTexture, vTextureCoord);\n" +
                    "}\n"


        // Fragment shader that converts color to black & white with a simple transformation.
        private const val FRAGMENT_SHADER_EXT_BW =
            "#extension GL_OES_EGL_image_external : require\n" +
                    "precision mediump float;\n" +
                    "varying vec2 vTextureCoord;\n" +
                    "uniform samplerExternalOES sTexture;\n" +
                    "void main() {\n" +
                    "    vec4 tc = texture2D(sTexture, vTextureCoord);\n" +
                    "    float color = tc.r * 0.3 + tc.g * 0.59 + tc.b * 0.11;\n" +
                    "    gl_FragColor = vec4(color, color, color, 1.0);\n" +
                    "}\n"


        // Fragment shader with a convolution filter.  The upper-left half will be drawn normally,
        // the lower-right half will have the filter applied, and a thin red line will be drawn
        // at the border.
        //
        // This is not optimized for performance.  Some things that might make this faster:
        // - Remove the conditionals.  They're used to present a half & half view with a red
        //   stripe across the middle, but that's only useful for a demo.
        // - Unroll the loop.  Ideally the compiler does this for you when it's beneficial.
        // - Bake the filter kernel into the shader, instead of passing it through a uniform
        //   array.  That, combined with loop unrolling, should reduce memory accesses.
        const val KERNEL_SIZE = 9
        private const val FRAGMENT_SHADER_EXT_FILT =
            "#extension GL_OES_EGL_image_external : require\n" +
                    "#define KERNEL_SIZE " + KERNEL_SIZE + "\n" +
                    "precision highp float;\n" +
                    "varying vec2 vTextureCoord;\n" +
                    "uniform samplerExternalOES sTexture;\n" +
                    "uniform float uKernel[KERNEL_SIZE];\n" +
                    "uniform vec2 uTexOffset[KERNEL_SIZE];\n" +
                    "uniform float uColorAdjust;\n" +
                    "void main() {\n" +
                    "    int i = 0;\n" +
                    "    vec4 sum = vec4(0.0);\n" +
                    "    if (vTextureCoord.x < vTextureCoord.y - 0.005) {\n" +
                    "        for (i = 0; i < KERNEL_SIZE; i++) {\n" +
                    "            vec4 texc = texture2D(sTexture, vTextureCoord + uTexOffset[i]);\n" +
                    "            sum += texc * uKernel[i];\n" +
                    "        }\n" +
                    "    sum += uColorAdjust;\n" +
                    "    } else if (vTextureCoord.x > vTextureCoord.y + 0.005) {\n" +
                    "        sum = texture2D(sTexture, vTextureCoord);\n" +
                    "    } else {\n" +
                    "        sum.r = 1.0;\n" +
                    "    }\n" +
                    "    gl_FragColor = sum;\n" +
                    "}\n"


    }
}