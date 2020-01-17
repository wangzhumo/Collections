package com.wangzhumo.app.module.opengl.multi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.wangzhumo.app.gles.Drawable2d;
import com.wangzhumo.app.gles.GLUtils;
import com.wangzhumo.app.gles.IGLRenderer;
import com.wangzhumo.app.gles.Texture2dProgram;
import com.wangzhumo.app.module.opengl.R;
import com.wangzhumo.app.origin.utils.DensityUtils;


/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2020-01-08  17:57
 */
public class GLFboMultiRenderer implements IGLRenderer {


    private Context mContext;

    private Drawable2d drawable2d;

    private int bitmapTextureId;

    private Texture2dProgram mTexture2dProgram;
    private FboMultiRenderer mFboRender;
    private OnTextureListener textureListener;

    private int screenWidth, screenHeight;
    private int showWidth, showHeight;

    public GLFboMultiRenderer(Context context) {
        this.mContext = context;
        this.drawable2d = new Drawable2d(Drawable2d.Prefab.FULL_RECTANGLE);
        this.mFboRender = new FboMultiRenderer();
        this.screenWidth = DensityUtils.getScreenWidth(context);
        this.screenHeight = DensityUtils.getScreenHeight(context);
    }

    @Override
    public void onSurfaceCreate() {
        mFboRender.onSurfaceCreate();
        drawable2d.createVboBuffer();
        drawable2d.createFboBuffer(screenWidth, screenHeight);
        mTexture2dProgram = new Texture2dProgram(Texture2dProgram.ProgramType.TEXTURE_MATRIX);
        if (textureListener != null){
            textureListener.onCreate(drawable2d.fboTextureId);
        }
        createImageTexture();
    }

    private void createImageTexture() {
        bitmapTextureId = mTexture2dProgram.createTextureObject();

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, bitmapTextureId);

        //设置图片到纹理上
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_city_night);
        showWidth = bitmap.getWidth();
        showHeight = bitmap.getHeight();
        android.opengl.GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();

        //解除绑定
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
    }

    @Override
    public void onSurfaceChange(int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        mFboRender.onSurfaceChange(width, height);
        if (width > height) {
            Matrix.setIdentityM(GLUtils.IDENTITY_MATRIX,0);
            Matrix.orthoM(
                    GLUtils.IDENTITY_MATRIX,
                    0,
                    -width / ((height / showHeight) * showWidth),
                    width / ((height / showHeight) * showWidth),
                    -1F, 1F,
                    -1F, 1F);
        } else {
            Matrix.setIdentityM(GLUtils.IDENTITY_MATRIX,0);
            Matrix.orthoM(
                    GLUtils.IDENTITY_MATRIX,
                    0,
                    -1F, 1F,
                    -height / ((width / showWidth) * showHeight),
                    height / ((width / showWidth) * showHeight),
                    -1F, 3F);
        }

        //1.矩阵
        //2.mOffset
        //3.   a  旋转的角度
        //4. x,y,z围绕选装的轴
        Matrix.rotateM(GLUtils.IDENTITY_MATRIX, 0, 180, 1, 0, 0);
    }

    @Override
    public void drawFrame() {
        //绑定FBO
        GLES20.glBindFramebuffer(GLES20.GL_RENDERBUFFER, drawable2d.fboId);

        //清屏
        GLES20.glClearColor(0F, 0F, 1F, 0.4F);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        //使用程序
        GLES20.glUseProgram(mTexture2dProgram.mProgramHandle);

        //设置矩阵
        GLES20.glUniformMatrix4fv(mTexture2dProgram.uMVPMatrix, 1, false, GLUtils.IDENTITY_MATRIX, 0);

        //绑定纹理
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
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
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);

        mFboRender.setTextureId(drawable2d.fboTextureId);
        mFboRender.drawFrame();
    }

    public void setTextureListener(OnTextureListener textureListener) {
        this.textureListener = textureListener;
    }

    interface OnTextureListener{
        void onCreate(int textureId);
    }
}
