package com.wangzhumo.app.module.media.targets.task3_2;

import android.graphics.SurfaceTexture;

/**
 * @anchor: andy
 * @date: 2018-11-12
 * @description:
 */
public interface ICamera {

    boolean openCamera(int cameraId);

    void enablePreview(boolean enable);

    void setPreviewTexture(SurfaceTexture surfaceTexture);

    void closeCamera();
}
