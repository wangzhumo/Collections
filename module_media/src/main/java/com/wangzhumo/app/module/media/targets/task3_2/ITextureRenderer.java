package com.wangzhumo.app.module.media.targets.task3_2;

import android.graphics.SurfaceTexture;

/**
 * @anchor: andy
 * @date: 2018-11-11
 * @description:
 */
public interface ITextureRenderer {

    void onSurfaceCreated();

    void onSurfaceChanged(int width, int height);

    void onDrawFrame(SurfaceTexture surfaceTexture);

}
