//
// Created by wangzhumo on 2020/8/7.
//
#pragma once
#ifndef COLLECTIONS_WZM_EGL_HELPER_H
#define COLLECTIONS_WZM_EGL_HELPER_H

#include <EGL/egl.h>
#include <EGL/eglext.h>
#include "../log/android_log_utils.h"

class WzmEglHelper {

public:
    WzmEglHelper();

    ~WzmEglHelper();

    int initEglEnv(EGLNativeWindowType windowType);

    int swapBuffers();

    void release();

public:
    EGLDisplay mEglDisplay;
    EGLConfig mEglConfig;
    EGLSurface mEglSurface;    // 这个是后台显示的Display,主要是用来缓冲
    EGLContext mEglContext;
};


#endif //COLLECTIONS_WZM_EGL_HELPER_H
