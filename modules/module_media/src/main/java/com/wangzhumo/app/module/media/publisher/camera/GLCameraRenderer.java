package com.wangzhumo.app.module.media.publisher.camera;

import android.graphics.SurfaceTexture;
import android.opengl.GLES20;

import com.wangzhumo.app.mdeia.gles.Drawable2d;
import com.wangzhumo.app.mdeia.gles.IGLRenderer;
import com.wangzhumo.app.mdeia.gles.Texture2dProgram;
import com.wangzhumo.app.origin.utils.AppUtils;
import com.wangzhumo.app.origin.utils.DensityUtils;

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2020-01-17  14:30
 */
public class GLCameraRenderer implements IGLRenderer , SurfaceTexture.OnFrameAvailableListener {

    private Drawable2d drawable2d;

    private int mTextureOESId;

    private Texture2dProgram mTexture2dProgram;
    private SurfaceTexture mCameraTexture;
    private OnSurfaceCreateListener createListener;

    private FboRenderer mFboRender;

    private int screenWidth, screenHeight;
    private int showWidth, showHeight;

    public GLCameraRenderer() {
        this.drawable2d = new Drawable2d(Drawable2d.Prefab.FULL_RECTANGLE);
        this.mFboRender = new FboRenderer();
        this.screenWidth = DensityUtils.getScreenWidth(AppUtils.getContext());
        this.screenHeight = DensityUtils.getScreenHeight(AppUtils.getContext());
    }

    @Override
    public void onSurfaceCreate() {
        mFboRender.onCreate();
        //加载vbo/fbo
        drawable2d.createVboBuffer();
        drawable2d.createFboBuffer(screenWidth, screenHeight);
        //创建程序
        mTexture2dProgram = new Texture2dProgram(Texture2dProgram.ProgramType.TEXTURE_EXT);
        //创建一个OES纹理
        mTextureOESId = mTexture2dProgram.createTextureObject();
        GLES20.glBindTexture(mTexture2dProgram.mTextureTarget, mTextureOESId);
        //绑定这个 OES纹理与 SurfaceTexture
        mCameraTexture = new SurfaceTexture(mTextureOESId);
        //添加纹理可用的监听
        mCameraTexture.setOnFrameAvailableListener(this);
        if (createListener != null){
            createListener.onSurfaceTexture(mCameraTexture);
        }
        //解除绑定
        GLES20.glBindTexture(mTexture2dProgram.mTextureTarget, 0);
    }

    @Override
    public void onSurfaceChange(int width, int height) {
        mFboRender.onChange(width, height);
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void drawFrame() {
        if (mCameraTexture != null) {
            mCameraTexture.updateTexImage();
        }
        //清屏
        GLES20.glClearColor(0F, 0F, 1F, 0.4F);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        //使用程序
        GLES20.glUseProgram(mTexture2dProgram.mProgramHandle);


        //设置矩阵
        //GLES20.glUniformMatrix4fv(mTexture2dProgram.uMVPMatrix, 1, false, GLUtils.IDENTITY_MATRIX, 0);

        //绑定纹理
        //GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        //GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureOESId);

        //绑定FBO
        GLES20.glBindFramebuffer(GLES20.GL_RENDERBUFFER, drawable2d.fboId);
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

        mFboRender.onDraw(drawable2d.fboTextureId);
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {

    }


    public void setSurfaceCreateListener(OnSurfaceCreateListener createListener) {
        this.createListener = createListener;
    }

    public interface OnSurfaceCreateListener{
        //内部创建的SurfaceTexture可用
        void onSurfaceTexture(SurfaceTexture surfaceTexture);
    }
}
