package com.wangzhumo.app.gles;

import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Locale;

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2020-01-08  16:41
 *
 * GL工具类
 * 1.创建一个floatBuffer
 * 2.编译着色器程序
 * 3.链接程序 (顶点着色器 + 片段着色器) 获取GL的Program
 * 4.提供一个MATRIX
 */
public class GLUtils {

    public static float[] IDENTITY_MATRIX = new float[16];

    static {
        Matrix.setIdentityM(IDENTITY_MATRIX, 0);
    }


    /**
     * 链接程序
     *
     * @param vertexSource   顶点着色器
     * @param fragmentSource 片段着色器
     * @return program
     */
    public static int linkProgram(String vertexSource,String fragmentSource){
        int vertexShaderId = compileShader(GLES20.GL_VERTEX_SHADER, vertexSource);
        if (vertexShaderId == 0){
            return 0;
        }

        int fragmentShaderId = compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource);
        if (fragmentShaderId == 0){
            return 0;
        }

        int programID = GLES20.glCreateProgram();
        if (programID != 0){
            //顶点着色器加入
            GLES20.glAttachShader(programID, vertexShaderId);
            //片元着色器加入
            GLES20.glAttachShader(programID, fragmentShaderId);
            //创建 program
            GLES20.glLinkProgram(programID);

            int[] linkState = new int[1];
            GLES20.glGetProgramiv(programID, GLES20.GL_LINK_STATUS, linkState, 0);
            if (linkState[0] != GLES20.GL_TRUE){
                String logInfo = GLES20.glGetProgramInfoLog(programID);
                GLES20.glDeleteProgram(programID);
                throw new RuntimeException(logInfo);
            }
        }
        return programID;
    }

    /**
     * 加载OES Texture
     *
     * @return OESTextureId
     */
    public static int createOESTexture(){
        int[] textureIds = new int[1];

        //获取一个可用的
        GLES20.glGenTextures(1, textureIds, 0);
        //当调用glBindTexture
        //如果是第一次调用这个函数textureIds[0] ，会创建一个新的纹理对象
        //如果textureIds[0]已经创建过了，把这个纹理置为 活动
        //如果texture为0，就停止使用这个纹理对象，并返回无名称的默认纹理
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureIds[0]);

        //设置参数
        GLES20.glTexParameterf(
                GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GLES20.GL_TEXTURE_MIN_FILTER,
                GLES20.GL_NEAREST
        );
        GLES20.glTexParameterf(
                GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GLES20.GL_TEXTURE_MAG_FILTER,
                GLES20.GL_LINEAR
        );
        GLES20.glTexParameterf(
                GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GLES20.GL_TEXTURE_WRAP_S,
                GLES20.GL_CLAMP_TO_EDGE
        );
        GLES20.glTexParameterf(
                GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GLES20.GL_TEXTURE_WRAP_T,
                GLES20.GL_CLAMP_TO_EDGE
        );

        //取消绑定纹理
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, 0);
        return textureIds[0];
    }



    /**
     * 验证程序片段是否有效
     *
     * @param programObjectId
     * @return
     */
    public static boolean validProgram(int programObjectId) {
        GLES20.glValidateProgram(programObjectId);
        int[] programStatus = new int[1];
        GLES20.glGetProgramiv(programObjectId, GLES20.GL_VALIDATE_STATUS, programStatus, 0);
        return programStatus[0] != 0;
    }


    /**
     * 创建一个floatBuffer
     * @param vertexData 数据
     */
    public static FloatBuffer createFloatBuffer(float[] vertexData){
        FloatBuffer buffer = ByteBuffer.allocateDirect(vertexData.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        buffer.put(vertexData, 0, vertexData.length).position(0);
        return buffer;
    }

    /**
     * 创建一个顶点Buffer
     *
     * @param byteBuffer
     * @param data
     * @return FloatBuffer
     */
    public static FloatBuffer createFloatBuffer(ByteBuffer byteBuffer,float[] data){
        FloatBuffer buffer = byteBuffer.order(ByteOrder.nativeOrder()).asFloatBuffer();
        buffer.put(data);
        return buffer;
    }


    /**
     * Checks to see if a GLES error has been raised.
     */
    public static void checkGLError(String op){
        int error = GLES20.glGetError();
        if (error != GLES20.GL_NO_ERROR){
            String msg = String.format(Locale.CHINA,"%s : glError 0x %s", op, Integer.toHexString(error));
            throw new RuntimeException(msg);
        }
    }


    /**
     * Checks to see if the location we obtained is valid.  GLES returns -1 if a label
     * could not be found, but does not set the GL error.
     *
     *
     * Throws a RuntimeException if the location is invalid.
     */
    public static void checkLocation(int location,String label) {
        if (location < 0) {
            throw new RuntimeException(String.format("Unable to locate %s in program", label));
        }
    }


    /**
     * 编译
     *
     * @param type       顶点着色器:GLES30.GL_VERTEX_SHADER
     *                   片段着色器:GLES30.GL_FRAGMENT_SHADER
     * @param shaderSource source
     * @return int
     */
    private static int compileShader(int type,String shaderSource){
        int shaderId = GLES20.glCreateShader(type);
        if (shaderId != 0) {
            GLES20.glShaderSource(shaderId, shaderSource);
            GLES20.glCompileShader(shaderId);

            //检查这个Shader状态
            int[] compileState = new int[1];
            GLES20.glGetShaderiv(shaderId, GLES20.GL_COMPILE_STATUS, compileState, 0);
            if (compileState[0] == 0){
                String logInfo = GLES20.glGetShaderInfoLog(shaderId);
                GLES20.glDeleteShader(shaderId);
                throw new RuntimeException(logInfo);
            }
        }
        return shaderId;
    }


}
