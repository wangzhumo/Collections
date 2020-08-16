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

    //public native void surfaceDraw(int width,int height);

}
