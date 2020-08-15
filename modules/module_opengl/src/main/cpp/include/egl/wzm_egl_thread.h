//
// Created by wangzhumo on 2020/8/15.
//

#ifndef COLLECTIONS_WZM_EGL_THREAD_H
#define COLLECTIONS_WZM_EGL_THREAD_H

#include <pthread.h>
#include <EGL/egl.h>
#include <unistd.h>
#include <android/native_window.h>
#include <android/native_window_jni.h>
#include "../include/egl/wzm_egl_helper.h"
#include "../include/log/android_log_utils.h"

class WzmEglThread {

public:
    WzmEglThread();
    ~WzmEglThread();

    void onSurfaceCreate(EGLNativeWindowType windowType);

    void onSurfaceChange();

    void onSurfaceDraw();


public:
    pthread_t pEglThread = -1;
    ANativeWindow *pNativeWindow = nullptr;

    bool isCreate = false;
    bool isChange = false;
    bool isExit = false;

};


#endif //COLLECTIONS_WZM_EGL_THREAD_H
