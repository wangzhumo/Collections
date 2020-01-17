package com.wangzhumo.app.module.opengl.multi;

import android.opengl.GLES20;

import com.wangzhumo.app.mdeia.gles.Drawable2d;
import com.wangzhumo.app.mdeia.gles.IGLRenderer;
import com.wangzhumo.app.mdeia.gles.Texture2dProgram;

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2020-01-12  13:19
 *
 * FOB渲染
 */
public class FboMultiRenderer implements IGLRenderer {

    private int textureId;
    private Drawable2d drawable2d;
    private Texture2dProgram mTexture2dProgram;

    public FboMultiRenderer() {
        this.drawable2d = new Drawable2d(Drawable2d.Prefab.FULL_RECTANGLE);
    }


    @Override
    public void onSurfaceCreate() {
        drawable2d.createVboBuffer();
        mTexture2dProgram = new Texture2dProgram(Texture2dProgram.ProgramType.TEXTURE_2D);
    }

    @Override
    public void onSurfaceChange(int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void drawFrame() {
        //清屏
        GLES20.glClearColor(0F, 0F, 1F, 0.4F);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        //使用程序
        GLES20.glUseProgram(mTexture2dProgram.mProgramHandle);

        //显示一个2D
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, drawable2d.vboId);

        GLES20.glEnableVertexAttribArray(mTexture2dProgram.aPosition);
        GLES20.glVertexAttribPointer(
                mTexture2dProgram.aPosition,
                drawable2d.getCoordsPerVertex(),
                GLES20.GL_FLOAT,
                false,
                drawable2d.getVertexStride(),
                0
        );

        GLES20.glEnableVertexAttribArray(mTexture2dProgram.aTextureCoord);
        GLES20.glVertexAttribPointer(
                mTexture2dProgram.aTextureCoord,
                drawable2d.getCoordsPerVertex(),
                GLES20.GL_FLOAT,
                false,
                drawable2d.getTexCoordStride(),
                drawable2d.getVertexLength() * drawable2d.getSizeofFloat()
        );


        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, drawable2d.getVertexCount());

        //解绑
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
    }

    public int getTextureId() {
        return textureId;
    }

    public void setTextureId(int textureId) {
        this.textureId = textureId;
    }
}
