package com.wangzhumo.app.module.media.publisher.encodec;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.wangzhumo.app.mdeia.gles.Drawable2d;
import com.wangzhumo.app.mdeia.gles.GLUtils;
import com.wangzhumo.app.mdeia.gles.IGLRenderer;
import com.wangzhumo.app.mdeia.gles.Texture2dProgram;
import com.wangzhumo.app.module.media.publisher.camera.FboRenderer;
import com.wangzhumo.app.module.media.publisher.camera.GLCameraRenderer;
import com.wangzhumo.app.origin.utils.AppUtils;
import com.wangzhumo.app.origin.utils.DensityUtils;

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2020/3/19  6:22 PM
 *
 * 录制的Renderer,可以显示录制中的画面.
 */
public class RecordGlRenderer implements IGLRenderer {

    private Drawable2d drawable2d;

    private int mTextureOESId;

    private Texture2dProgram mTexture2dProgram;
    private SurfaceTexture mCameraTexture;
    private GLCameraRenderer.OnSurfaceCreateListener createListener;

    private FboRenderer mFboRender;

    private int screenWidth, screenHeight;
    private int showWidth, showHeight;
    private int mRotationValue = 0;
    private int mCameraId;

    public RecordGlRenderer() {
        //this.mFboRender = new FboRenderer();
        this.drawable2d = new Drawable2d(Drawable2d.Prefab.FULL_RECTANGLE);
        this.screenWidth = DensityUtils.getScreenWidth(AppUtils.getContext());
        this.screenHeight = DensityUtils.getScreenHeight(AppUtils.getContext());
    }

    @Override
    public void onSurfaceCreate() {
        //mFboRender.onCreate();
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
        //mCameraTexture.setOnFrameAvailableListener(this);
        //解除绑定
        GLES20.glBindTexture(mTexture2dProgram.mTextureTarget, 0);
        if (createListener != null) {
            createListener.onSurfaceTexture(mCameraTexture);
        }
    }

    @Override
    public void onSurfaceChange(int width, int height) {
        //mFboRender.onChange(width, height);
        GLES20.glViewport(0, 0, width, height);
        //applyMatrix();

    }

    @Override
    public void drawFrame() {
        mCameraTexture.updateTexImage();

        //清屏
        GLES20.glClearColor(0F, 0F, 1F, 0.4F);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        //使用程序
        GLES20.glUseProgram(mTexture2dProgram.mProgramHandle);

        //设置矩阵
        GLES20.glUniformMatrix4fv(mTexture2dProgram.uMVPMatrix, 1, false, GLUtils.IDENTITY_MATRIX, 0);

        //绑定纹理
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureOESId);

        //绑定FBO
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, drawable2d.fboId);
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



    /**
     * 当前屏幕的旋转状态.
     */
    public void setRotation(int rotation, int cameraId) {
        this.mRotationValue = rotation;
        this.mCameraId = cameraId;
        applyMatrix();
    }

    /**
     * 设置opengl 的旋转
     */
    private void applyMatrix() {
        Matrix.setIdentityM(GLUtils.IDENTITY_MATRIX, 0);
        if (mRotationValue == 0) {
            if (mCameraId == Camera.CameraInfo.CAMERA_FACING_FRONT){
                Matrix.rotateM(GLUtils.IDENTITY_MATRIX,0,-90F,0F,0F,1F);
            }else {
                Matrix.rotateM(GLUtils.IDENTITY_MATRIX,0,180F,1F,0F,0F);
                Matrix.rotateM(GLUtils.IDENTITY_MATRIX,0,90F,0F,0F,1F);
            }
        } else if (mRotationValue == 90) {
            if (mCameraId == Camera.CameraInfo.CAMERA_FACING_FRONT){
                Matrix.rotateM(GLUtils.IDENTITY_MATRIX,0,180F,1F,0F,0F);
                Matrix.rotateM(GLUtils.IDENTITY_MATRIX,0,180F,0F,1F,0F);
            }else {
                Matrix.rotateM(GLUtils.IDENTITY_MATRIX,0,180F,1F,0F,0F);
            }
        } else if (mRotationValue == 180) {
            if (mCameraId == Camera.CameraInfo.CAMERA_FACING_FRONT){

            }else {
                Matrix.rotateM(GLUtils.IDENTITY_MATRIX,0,180F,1F,0F,0F);
            }
        } else if (mRotationValue == 270) {
            if (mCameraId == Camera.CameraInfo.CAMERA_FACING_FRONT){

            }else {
                Matrix.rotateM(GLUtils.IDENTITY_MATRIX,0,180F,0F,1F,0F);
            }
        }
    }
}
