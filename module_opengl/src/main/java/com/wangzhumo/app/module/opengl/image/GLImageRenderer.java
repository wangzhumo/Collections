package com.wangzhumo.app.module.opengl.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;

import com.wangzhumo.app.module.opengl.R;
import com.wangzhumo.app.module.opengl.gles.Drawable2d;
import com.wangzhumo.app.module.opengl.gles.GLUtils;
import com.wangzhumo.app.module.opengl.gles.IGLRenderer;
import com.wangzhumo.app.module.opengl.gles.Texture2dProgram;
import com.wangzhumo.app.module.opengl.gles.Transformation;


/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2020-01-08  17:57
 */
public class GLImageRenderer implements IGLRenderer {


    private Context mContext;

    private Drawable2d drawable2d;

    private int bitmapTextureId;

    private final Transformation mTransformation;
    private Texture2dProgram mTexture2dProgram;

    public GLImageRenderer(Context context) {
        this.mContext = context;
        this.drawable2d = new Drawable2d(Drawable2d.Prefab.FULL_RECTANGLE);
        this.mTransformation = new Transformation();
    }

    @Override
    public void onSurfaceCreate() {
        mTransformation.setRotation(Transformation.ROTATION_180);
        drawable2d.setTransformation(mTransformation);

        mTexture2dProgram = new Texture2dProgram(Texture2dProgram.ProgramType.TEXTURE_2D);
        bitmapTextureId = mTexture2dProgram.createTextureObject();

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

        GLES20.glUseProgram(mTexture2dProgram.mProgramHandle);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, bitmapTextureId);
        GLES20.glUniform1i(mTexture2dProgram.sTexture, 0);

        GLES20.glEnableVertexAttribArray(mTexture2dProgram.aPosition);
        GLES20.glVertexAttribPointer(
                mTexture2dProgram.aPosition,
                drawable2d.getCoordsPerVertex(),
                GLES20.GL_FLOAT,
                false,
                drawable2d.getVertexStride(),
                drawable2d.getVertexArray()
        );

        GLES20.glEnableVertexAttribArray(mTexture2dProgram.aTextureCoord);
        GLES20.glVertexAttribPointer(
                mTexture2dProgram.aTextureCoord,
                drawable2d.getCoordsPerVertex(),
                GLES20.GL_FLOAT,
                false,
                drawable2d.getTexCoordStride(),
                drawable2d.getTexCoordArray()
        );
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, drawable2d.getVertexCount());
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
