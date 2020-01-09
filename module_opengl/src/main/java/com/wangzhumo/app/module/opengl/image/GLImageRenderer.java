package com.wangzhumo.app.module.opengl.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
    private int sTexture;

    private int bitmapTextureId;

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
        sTexture = GLES20.glGetUniformLocation(programId, "sTexture");

        bitmapTextureId = createTextureObject();
        //设置图片到纹理上
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_city_night);
        android.opengl.GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();

        //解除绑定
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
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

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, bitmapTextureId);
        GLES20.glUniform1i(sTexture, 0);

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
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
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
