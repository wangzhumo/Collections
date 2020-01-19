package com.wangzhumo.app.module.media.publisher.camera;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;
import android.util.Log;

import com.wangzhumo.app.mdeia.CameraManager;
import com.wangzhumo.app.mdeia.CustomGLSurfaceView;
import com.wangzhumo.app.mdeia.gles.EGLCore;

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2020-01-17  11:51
 *
 * 相机预览
 */
public class GLCameraSurfaceView extends CustomGLSurfaceView  implements GLCameraRenderer.OnSurfaceCreateListener {

    private GLCameraRenderer mRenderer;
    private CameraManager cameraManager;

    public GLCameraSurfaceView(Context context) {
        this(context,null);
    }

    public GLCameraSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GLCameraSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        cameraManager = new CameraManager(getContext());
        mRenderer = new GLCameraRenderer();
        mRenderer.setSurfaceCreateListener(this);
        setRenderer(mRenderer);
    }

    @Override
    public void onSurfaceTexture(SurfaceTexture surfaceTexture) {
        Log.d(EGLCore.TAG, "onSurfaceTextureCreate   打开相机预览");
        cameraManager.startCamera(surfaceTexture);
    }

    @Override
    public void release() {
        super.release();
        cameraManager.stopCamera();
    }

    public void switchCamera(){
        cameraManager.switchCamera();
    }
}
