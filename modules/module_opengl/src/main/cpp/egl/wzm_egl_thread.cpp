//
// Created by wangzhumo on 2020/8/15.
//
#include "../include/egl/wzm_egl_thread.h"

WzmEglThread::WzmEglThread() = default;

WzmEglThread::~WzmEglThread() = default;

void *eglThreadCallBack(void *context) {

    WzmEglThread *wzmEglThread = static_cast<WzmEglThread *>(context);
    if (wzmEglThread != nullptr) {
        // 如果不为空,就在这里创建一个EGL的环境.
        WzmEglHelper *pEglHelper = new WzmEglHelper();
        // 这里一个循环,用来处理数据
        while (true) {
            // 处理 surfaceCreate
            if (wzmEglThread->isCreate) {
                LOGE("elgThread call surfaceCreate");
                wzmEglThread->isCreate = false;
                pEglHelper->initEglEnv(wzmEglThread->pNativeWindow);
            }
            // 在这里处理 surfaceChange 的事件
            if (wzmEglThread->isChange) {
                LOGE("eglThread call surfaceChange");
                wzmEglThread->isChange = false;
                glViewport(0,0,wzmEglThread->surfaceWidth,wzmEglThread->surfaceHeight);
                wzmEglThread->canStart = true;
            }
            // 这里就开始绘制 surfaceDraw
            if (wzmEglThread->canStart){
                LOGD("eglThread draw");
                glClearColor(1.0, 1.0, 0.0, 1.0);
                glClear(GL_COLOR_BUFFER_BIT);
                pEglHelper->swapBuffers();
            }
            // 暂停 60 fps
            usleep(1000000 / 60);

            if (wzmEglThread->isExit){
                break;
            }
        }
    }
    pthread_exit(&wzmEglThread->pEglThread);
}

void WzmEglThread::onSurfaceCreate(EGLNativeWindowType windowType) {
    if (pEglThread == -1) {
        // surfaceCreate
        isCreate = true;
        // 设置窗口
        pNativeWindow = windowType;
        // 如果 == -1,说明没有创建过,开始创建
        pEglThread = pthread_create(&pEglThread, nullptr, eglThreadCallBack, this);
    }
}

void WzmEglThread::onSurfaceChange(int width, int height) {
    isChange = true;
    surfaceWidth = width;
    surfaceHeight = height;
}

