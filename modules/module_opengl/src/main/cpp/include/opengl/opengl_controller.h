//
// Created by wangzhumo on 2020/8/28.
//
// 这负责提供opengl的egl环境，控制固定的流程
//
#pragma once
#ifndef COLLECTIONS_OPENGL_CONTROLLER_H
#define COLLECTIONS_OPENGL_CONTROLLER_H

#include <android/native_window.h>
#include <jni.h>
#include "../egl/wzm_egl_thread.h"
#include "opengl_filter_normal.h"



class OpenGlController{
public:
    WzmEglThread *pEglThread = nullptr;
    ANativeWindow *pNativeWindow = nullptr;
    BaseOpenGl *baseOpenGl = nullptr;

public:
    OpenGlController();
    ~OpenGlController();

    // 通过Jni传递过来的消息
    void onSurfaceCreate(JNIEnv *env,jobject surface);
    void onSurfaceChange(int width,int height);

    // 销毁资源
    void onRelease();
};


#endif //COLLECTIONS_OPENGL_CONTROLLER_H
