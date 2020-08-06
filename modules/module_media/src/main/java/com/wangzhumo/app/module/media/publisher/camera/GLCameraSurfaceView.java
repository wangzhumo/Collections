package com.wangzhumo.app.module.media.publisher.camera;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.display.DisplayManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;

import com.wangzhumo.app.mdeia.CameraManager;
import com.wangzhumo.app.mdeia.CustomGLSurfaceView;
import com.wangzhumo.app.mdeia.gles.EGLCore;

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2020-01-17  11:51
 * <p>
 * 相机预览
 */
public class GLCameraSurfaceView extends CustomGLSurfaceView implements GLCameraRenderer.OnSurfaceCreateListener,
        DisplayManager.DisplayListener {

    private GLCameraRenderer mRenderer;
    private CameraManager cameraManager;
    private DisplayManager displayManager;
    private int mRotation = 0;
    private int textureId = -1;

    public GLCameraSurfaceView(Context context) {
        this(context, null);
    }

    public GLCameraSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GLCameraSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mRenderer = new GLCameraRenderer();
        cameraManager = new CameraManager(getContext());
        setRenderer(mRenderer);
        mRenderer.setSurfaceCreateListener(this);

        displayManager = (DisplayManager) context.getSystemService(Context.DISPLAY_SERVICE);
        displayManager.registerDisplayListener(this, null);
        for (Display display : displayManager.getDisplays()) {
            if (display.getDisplayId() == Display.DEFAULT_DISPLAY){
                //获取了一个本地显示ID.
                mRenderer.setRotation(getDisplaySurfaceRotation(display),cameraManager.getCameraId());
                break;
            }
        }
    }

    @Override
    public void onSurfaceTexture(SurfaceTexture surfaceTexture,int textureId) {
        Log.d(EGLCore.TAG, "onSurfaceTextureCreate   打开相机预览");
        cameraManager.startCamera(surfaceTexture);
        this.textureId = textureId;
    }

    @Override
    public void release() {
        super.release();
        cameraManager.stopCamera();
        if (displayManager != null) {
            displayManager.unregisterDisplayListener(this);
        }
    }

    public void switchCamera() {
        if (mRenderer != null && cameraManager != null) {
            //通知渲染
            if (cameraManager.getCameraId() == Camera.CameraInfo.CAMERA_FACING_BACK){
                mRenderer.setRotation(getSurfaceRotation(),Camera.CameraInfo.CAMERA_FACING_FRONT);
            }else {
                mRenderer.setRotation(getSurfaceRotation(),Camera.CameraInfo.CAMERA_FACING_BACK);
            }
            cameraManager.switchCamera();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        super.surfaceDestroyed(holder);
        if (displayManager != null) {
            displayManager.unregisterDisplayListener(this);
        }
    }

    @Override
    public void onDisplayAdded(int displayId) {

    }

    @Override
    public void onDisplayRemoved(int displayId) {

    }

    @Override
    public void onDisplayChanged(int displayId) {
        //改变了状态.
        Display display = displayManager.getDisplay(displayId);
        mRotation = getDisplaySurfaceRotation(display);
        if (mRenderer != null && cameraManager != null) {
            mRenderer.setRotation(mRotation,cameraManager.getCameraId());
        }
    }

    /**
     * 获取当前屏幕的旋转.
     * @return int
     */
    public int getSurfaceRotation() {
        return mRotation;
    }

    private int getDisplaySurfaceRotation(Display display) {
        switch (display.getRotation()) {
            case Surface.ROTATION_0:
                Log.e(EGLCore.TAG, "翻转 0");
                return 0;
            case Surface.ROTATION_90:
                Log.e(EGLCore.TAG, "翻转 90");
                return 90;
            case Surface.ROTATION_180:
                Log.e(EGLCore.TAG, "翻转 180");
                return 180;
            case Surface.ROTATION_270:
                Log.e(EGLCore.TAG, "翻转 270");
                return 270;
            default:
                return 0;
        }
    }

    public int getTextureId() {
        return textureId;
    }
}



