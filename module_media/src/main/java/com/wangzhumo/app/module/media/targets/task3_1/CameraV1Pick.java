package com.wangzhumo.app.module.media.targets.task3_1;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.view.TextureView;

import com.wangzhumo.app.module.media.opengl.camera.TextureEGLHelper;

import com.tencent.mars.xlog.InyuLog;
import com.wangzhumo.app.module.media.targets.utils.TextureUtils;


/**
 * @anchor: andy
 * @date: 18-11-11
 */

public class CameraV1Pick implements TextureView.SurfaceTextureListener {

    private static final String TAG = "CameraV1Pick";

    private TextureView mTextureView;

    private int mCameraId;

    private ICamera mCamera;

    private TextureEGLHelper mTextureEglHelper;

    public void bindTextureView(TextureView textureView) {
        this.mTextureView = textureView;
        InyuLog.d(TAG,"com.wangzhumo.app.module.media.targets.task3_1.CameraV1Pick","bindTextureView",34,"创建EGLHelper TextureEGLHelper()");
        mTextureEglHelper = new TextureEGLHelper();
        InyuLog.d(TAG,"com.wangzhumo.app.module.media.targets.task3_1.CameraV1Pick","bindTextureView",35,"添加 setSurfaceTextureListener");
        mTextureView.setSurfaceTextureListener(this);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        //加载OES纹理ID
        final int textureId = TextureUtils.loadOESTexture();
        InyuLog.d(TAG,"com.wangzhumo.app.module.media.targets.task3_1.CameraV1Pick","onSurfaceTextureAvailable",42,"loadOESTexture textureId = %d",textureId);
        //初始化操作
        mTextureEglHelper.initEGL(mTextureView, textureId);
        //自定义的SurfaceTexture
        SurfaceTexture surfaceTexture = mTextureEglHelper.loadOESTexture();
        //前置摄像头
        mCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
        mCamera = new CameraV1((Activity) mTextureView.getContext());
        if (mCamera.openCamera(mCameraId)) {
            mCamera.setPreviewTexture(surfaceTexture);
            mCamera.enablePreview(true);
            InyuLog.d(TAG,"com.wangzhumo.app.module.media.targets.task3_1.CameraV1Pick","onSurfaceTextureAvailable",52,"开启摄像头 enablePreview");
        } else {
            InyuLog.d(TAG,"com.wangzhumo.app.module.media.targets.task3_1.CameraV1Pick","onSurfaceTextureAvailable",56,"openCamera failed");
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        mTextureEglHelper.onSurfaceChanged(width, height);
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        if (mCamera != null) {
            mCamera.enablePreview(false);
            mCamera.closeCamera();
            mCamera = null;
        }
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    public void onDestroy() {
        if (mTextureEglHelper != null) {
            mTextureEglHelper.onDestroy();
        }
    }
}
