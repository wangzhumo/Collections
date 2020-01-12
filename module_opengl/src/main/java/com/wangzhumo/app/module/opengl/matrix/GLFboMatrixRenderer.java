package com.wangzhumo.app.module.opengl.matrix;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;

import com.wangzhumo.app.base.utils.DensityUtils;
import com.wangzhumo.app.module.opengl.R;
import com.wangzhumo.app.module.opengl.fbo.FboRenderer;
import com.wangzhumo.app.module.opengl.gles.Drawable2d;
import com.wangzhumo.app.module.opengl.gles.EGLCore;
import com.wangzhumo.app.module.opengl.gles.GLUtils;
import com.wangzhumo.app.module.opengl.gles.IGLRenderer;
import com.wangzhumo.app.module.opengl.gles.Texture2dProgram;
import com.wangzhumo.app.module.opengl.gles.Transformation;


/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2020-01-08  17:57
 */
public class GLFboMatrixRenderer implements IGLRenderer {


    private Context mContext;

    private Drawable2d drawable2d;

    private int bitmapTextureId;

    private final Transformation mTransformation;
    private Texture2dProgram mTexture2dProgram;
    private com.wangzhumo.app.module.opengl.fbo.FboRenderer mFboRender;

    private int screenWidth,screenHeight;

    public GLFboMatrixRenderer(Context context) {
        this.mContext = context;
        this.drawable2d = new Drawable2d(Drawable2d.Prefab.FULL_RECTANGLE);
        this.mTransformation = new Transformation();
        this.mFboRender = new FboRenderer();
        this.screenWidth = DensityUtils.getScreenWidth(context);
        this.screenHeight = DensityUtils.getScreenHeight(context);
    }

    @Override
    public void onSurfaceCreate() {
        mFboRender.onCreate();
        mTransformation.setRotation(Transformation.ROTATION_180);
        drawable2d.setTransformation(mTransformation);
        drawable2d.createVboBuffer();
        drawable2d.createFboBuffer(screenWidth, screenHeight);
        mTexture2dProgram = new Texture2dProgram(Texture2dProgram.ProgramType.TEXTURE_2D);
        createImageTexture();
    }

    private void createImageTexture() {
        bitmapTextureId = mTexture2dProgram.createTextureObject();

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, bitmapTextureId);

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
        mFboRender.onChange(width,height);
    }

    @Override
    public void drawFrame() {
        //绑定FBO
//        GLES20.glBindFramebuffer(GLES20.GL_RENDERBUFFER, drawable2d.fboId);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);

        //清屏
        GLES20.glClearColor(0F, 0F, 1F, 0.4F);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        //使用程序
        GLES20.glUseProgram(mTexture2dProgram.mProgramHandle);

        //绑定纹理
        //GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, bitmapTextureId);

        //绑定使用vbo
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, drawable2d.vboId);

        //取用vbo信息
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

        //绘制
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, drawable2d.getVertexCount());

        //取消绑定
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
//        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
//
//        mFboRender.onDraw(drawable2d.fboTextureId);
    }
}
