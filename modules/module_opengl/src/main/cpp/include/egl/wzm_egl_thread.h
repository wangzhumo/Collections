//
// Created by wangzhumo on 2020/8/15.
//

#ifndef COLLECTIONS_WZM_EGL_THREAD_H
#define COLLECTIONS_WZM_EGL_THREAD_H

#include <pthread.h>
#include <EGL/egl.h>
#include <GLES2/gl2.h>
#include <unistd.h>
#include <android/native_window.h>
#include <android/native_window_jni.h>
#include "wzm_egl_helper.h"
#include "../log/android_log_utils.h"


class WzmEglThread {

public:
    WzmEglThread();
    ~WzmEglThread();

    void onSurfaceCreate(EGLNativeWindowType windowType);

    void onSurfaceChange(int width, int height);


public:
    pthread_t pEglThread = -1;
    ANativeWindow *pNativeWindow = nullptr;

    bool isCreate = false;
    bool isChange = false;
    bool isExit = false;
    bool canStart = false;

    int surfaceWidth = 0;
    int surfaceHeight = 0;
};


#endif //COLLECTIONS_WZM_EGL_THREAD_H
