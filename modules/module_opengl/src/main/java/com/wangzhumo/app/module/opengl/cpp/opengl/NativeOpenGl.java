package com.wangzhumo.app.module.opengl.cpp.opengl;

import android.view.Surface;

/**
 * opengl 的 cpp 方法
 */
public class NativeOpenGl {

    static {
        System.loadLibrary("opengl-cpp");
    }

    // Surface创建
    public native void surfaceCreate(Surface surface);

    // Surface的Size改变
    public native void surfaceChange(int width,int height);

    // 设置纹理数据 - 同时支持切换
    public native void setImageData(int width,int height,byte[] imageData);

    public native void surfaceDestroy();

    public native void surfaceChangeFilter(String type);

    public native void updateYuvData(byte[] dataY,byte [] dataU,byte [] dataV,int width,int height);
}
