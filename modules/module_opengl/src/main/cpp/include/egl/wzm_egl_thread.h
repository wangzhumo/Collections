//
// Created by wangzhumo on 2020/8/15.
//
#pragma once
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

// 自动的每秒60帧
#define OPENGL_RENDER_AUTO 1
// 手动调用渲染
#define OPENGL_RENDER_DIRTY 2


class WzmEglThread {


public:
    pthread_t pEglThread = -1;
    ANativeWindow *pNativeWindow = nullptr;

    bool isCreate = false;
    bool isChange = false;
    bool isExit = false;
    bool canStart = false;

    int surfaceWidth = 0;
    int surfaceHeight = 0;

    // 定义一个回调 surfaceCreate
    typedef void(*OnCreateCall)(void *);
    OnCreateCall onCreateCall;

    // 保存create参数
    void *onCreateCtx;

    // 定义一个回调 surfaceChange
    typedef void(*OnChangeCall)(int width,int height,void *);
    OnChangeCall onChangeCall;

    // 保持change的参数
    void *onChangeCtx;

    // 定义一个回调 surfaceDraw
    typedef void(*OnDrawCall)(void *);
    OnDrawCall onDrawCall;

    // 保持draw的参数
    void *onDrawCtx;

    // 渲染的模式.
    int renderMode = OPENGL_RENDER_AUTO;

    // 添加一个线程锁,实现模式的切换功能
    pthread_mutex_t pThreadMutex;
    // 信号量
    pthread_cond_t pThreadCond;

public:
    WzmEglThread();
    ~WzmEglThread();

    void onSurfaceCreate(EGLNativeWindowType windowType);

    void onSurfaceChange(int width, int height);

    void setCreateCallBack(OnCreateCall onCreate,void *context);

    void setChangeCallBack(OnChangeCall onChange,void *context);

    void setDrawCallBack(OnDrawCall onDraw,void *context);

    void setRenderMode(int mode);

    void notifyRender();

    void release();
};


#endif //COLLECTIONS_WZM_EGL_THREAD_H
