//
// Created by wangzhumo on 2020/8/7.
//

#ifndef COLLECTIONS_EGL_HELPER_H
#define COLLECTIONS_EGL_HELPER_H

#include "EGL/egl.h"
#include "../log/android_log_utils.h"

class EglHelper {

public:
    EglHelper();

    ~EglHelper();

    int initEglEnv(EGLNativeWindowType windowType);

    int swapBuffer();

    void release();

public:
    EGLDisplay mEglDisplay;
    EGLConfig mEglConfig;
    EGLSurface mEglSurface;    // 这个是后台显示的Display,主要是用来缓冲
    EGLContext mEglContext;
};


#endif //COLLECTIONS_EGL_HELPER_H
