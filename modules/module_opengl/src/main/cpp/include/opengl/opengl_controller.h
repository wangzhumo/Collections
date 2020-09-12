//
// Created by wangzhumo on 2020/8/28.
//
// OpenGL的控制类
// 用途 : 提供openGL的egl环境，控制固定的流程
// 持有 : EGLThread  ->  内部拥有EGL的环境,并且线程中一直执行循环
// 持有 : BaseOpenGl ->  不同的Filter实现,可以通过更换baseOpenGl达到更换滤镜的效果
//
#pragma once
#ifndef COLLECTIONS_OPENGL_CONTROLLER_H
#define COLLECTIONS_OPENGL_CONTROLLER_H


#include <jni.h>
#include <malloc.h>
#include <string>
#include <android/native_window.h>
#include "../egl/wzm_egl_thread.h"
#include "../filter/opengl_filter_normal.h"
#include "../filter/opengl_filter_normal_copy.h"
#include "../filter/opengl_filter_yuv.h"
#include "../utils/android_log_utils.h"


class OpenGlController{
public:
    WzmEglThread *pEglThread = nullptr;
    ANativeWindow *pNativeWindow = nullptr;
    BaseOpenGl *baseOpenGl = nullptr;

    int pixWidth{};
    int pixHeight{};
    void *pixelArr = nullptr;
    std::string *filterType{};

public:
    OpenGlController();
    ~OpenGlController();

    // 通过Jni传递过来的消息
    void onSurfaceCreate(JNIEnv *env,jobject surface);
    void onSurfaceChange(int width,int height);
    void onSurfaceChangeFilter(std::string type);

    // 销毁资源
    void onRelease();

    // 设置图片的资源
    void setPixelsData(int width,int height,int len,void *pixArr);

    // 设置YUV的数据
    void updateYuvData(jbyte *dataY, jbyte *dataU, jbyte *dataV, jint width, jint height);
};

#endif //COLLECTIONS_OPENGL_CONTROLLER_H
