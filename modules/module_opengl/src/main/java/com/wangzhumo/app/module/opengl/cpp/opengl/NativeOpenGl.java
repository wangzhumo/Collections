package com.wangzhumo.app.module.opengl.cpp.opengl;

import android.view.Surface;

/**
 * opengl 的 cpp 方法
 */
public class NativeOpenGl {

    static {
        System.loadLibrary("opengl-cpp");
    }

    public native void surfaceCreate(Surface surface);
}
