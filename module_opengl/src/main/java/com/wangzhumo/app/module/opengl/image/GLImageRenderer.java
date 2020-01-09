package com.wangzhumo.app.module.opengl.image;

import android.content.Context;
import android.opengl.GLES20;

import com.wangzhumo.app.base.utils.RawUtils;
import com.wangzhumo.app.module.opengl.R;
import com.wangzhumo.app.module.opengl.gles.GLUtils;
import com.wangzhumo.app.module.opengl.gles.IGLRenderer;

import java.nio.FloatBuffer;

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2020-01-08  17:57
 */
public class GLImageRenderer implements IGLRenderer {


    private Context mContext;

    //不指定点的顺序,就要自己排好
    private final float[] VERTEX_DATA = {
            -1F, -1F,
            1F, -1F,
            -1F, 1F,
            1F, 1F
    };

    private final float[] TEXTURE_DATA = {
            0F, 1F,
            1F, 1F,
            0F, 0F,
            1F, 0F
    };

    private FloatBuffer vertexBuffer;
    private FloatBuffer fragmentBuffer;

    private int programId;
    private int aPosition;
    private int aTextureCoord;




    public GLImageRenderer(Context context) {
        this.mContext = context;
        vertexBuffer = GLUtils.createFloatBuffer(VERTEX_DATA);
        fragmentBuffer = GLUtils.createFloatBuffer(TEXTURE_DATA);
    }

    @Override
    public void onSurfaceCreate() {
        programId = GLUtils.linkProgram(
                RawUtils.readResource(R.raw.gles_vertex_shader),
                RawUtils.readResource(R.raw.gles_fragment_shader)
        );

        aPosition = GLES20.glGetAttribLocation(programId, "aPosition");
        aTextureCoord = GLES20.glGetAttribLocation(programId, "aTextureCoord");
    }

    @Override
    public void onSurfaceChange(int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void drawFrame() {
        GLES20.glClearColor(0F, 0F, 1F, 0.4F);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);


        GLES20.glUseProgram(programId);
        GLES20.glEnableVertexAttribArray(aPosition);
        GLES20.glVertexAttribPointer(
                aPosition,
                2,
                GLES20.GL_FLOAT,
                false,
                8,
                vertexBuffer
        );

        GLES20.glEnableVertexAttribArray(aTextureCoord);
        GLES20.glVertexAttribPointer(
                aTextureCoord,
                2,
                GLES20.GL_FLOAT,
                false,
                8,
                fragmentBuffer
        );
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
    }
}
