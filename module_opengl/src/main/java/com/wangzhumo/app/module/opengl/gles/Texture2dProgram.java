package com.wangzhumo.app.module.opengl.gles;

import android.opengl.GLES20;
import android.util.Log;

import com.wangzhumo.app.base.utils.RawUtils;
import com.wangzhumo.app.module.opengl.R;

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2020-01-10  15:51
 * <p>
 * GL program and supporting functions for textured 2D shapes
 */
public class Texture2dProgram {


    private ProgramType mProgramType;

    public int mTextureTarget;
    // Handles to the GL program and various components of it.
    public int mProgramHandle;

    public int aPosition;
    public int aTextureCoord;
    public int sTexture;



    public enum ProgramType {
        TEXTURE_2D, TEXTURE_EXT, TEXTURE_EXT_BW, TEXTURE_EXT_FILT
    }


    public Texture2dProgram(ProgramType programType) {
        mProgramType = programType;
        switch (programType) {
            case TEXTURE_2D:
                mTextureTarget = GLES20.GL_TEXTURE_2D;
                mProgramHandle = GLUtils.linkProgram(
                        RawUtils.readResource(R.raw.gles_vertex_shader),
                        RawUtils.readResource(R.raw.gles_fragment_shader)
                );
                break;
        }
        if (mProgramHandle == 0) {
            throw new RuntimeException("Unable to create program");
        }

        aPosition = GLES20.glGetAttribLocation(mProgramHandle, "aPosition");
        aTextureCoord = GLES20.glGetAttribLocation(mProgramHandle, "aTextureCoord");
        sTexture = GLES20.glGetUniformLocation(mProgramHandle, "sTexture");
    }

    /**
     * Releases the program.
     * The appropriate EGL context must be current (i.e. the one that was used to create
     * the program).
     */
    public void release() {
        Log.d(EGLCore.TAG, "deleting program " + mProgramHandle);
        GLES20.glDeleteProgram(mProgramHandle);
        mProgramHandle = -1;
    }


    /**
     * Creates a texture object suitable for use with this program.
     * <p>
     * On exit, the texture will be bound.
     */
    public int createTextureObject() {
        int[] textures = new int[1];
        GLES20.glGenTextures(1, textures, 0);
        GLUtils.checkGLError("glGenTextures");

        int texId = textures[0];
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);
        GLUtils.checkGLError("glBindTexture " + texId);

        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,
                GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,
                GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
                GLES20.GL_REPEAT);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
                GLES20.GL_REPEAT);
        GLUtils.checkGLError("glTexParameter");

        return texId;
    }
}
