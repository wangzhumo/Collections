//
// Created by wangzhumo on 2020/8/15.
//
#include "../include/egl/wzm_egl_thread.h"

WzmEglThread::WzmEglThread() = default;

WzmEglThread::~WzmEglThread() = default;

void *eglThreadCallBack(void *context) {

    auto *wzmEglThread = static_cast<WzmEglThread *>(context);
    if (wzmEglThread != nullptr) {
        // 如果不为空,就在这里创建一个EGL的环境.
        auto *pEglHelper = new WzmEglHelper();
        // 这里一个循环,用来处理数据
        while (true) {
            // 处理 surfaceCreate
            if (wzmEglThread->isCreate) {
                LOGE("elgThread call surfaceCreate");
                wzmEglThread->isCreate = false;
                pEglHelper->initEglEnv(wzmEglThread->pNativeWindow);
                // 回调给外部.
                wzmEglThread->onCreateCall(wzmEglThread->onCreateCtx);

            }
            // 在这里处理 surfaceChange 的事件
            if (wzmEglThread->isChange) {
                LOGE("eglThread call surfaceChange");
                wzmEglThread->isChange = false;
                // 这里执行回调.
                wzmEglThread->onChangeCall(wzmEglThread->surfaceWidth,wzmEglThread->surfaceHeight,wzmEglThread->onChangeCtx);
                // 告知执行完毕
                wzmEglThread->canStart = true;
            }
            // 这里就开始绘制 surfaceDraw
            if (wzmEglThread->canStart){
                LOGD("eglThread draw");
                // 这里执行回调.
                wzmEglThread->onDrawCall(wzmEglThread->onDrawCtx);
                pEglHelper->swapBuffers();
            }
            // 暂停 60 fps
            usleep(1000000 / 60);

            if (wzmEglThread->isExit){
                break;
            }
        }
    }
    //也可以调用这个
    //pthread_exit(&(wzmEglThread->pEglThread));
    return nullptr;
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

void WzmEglThread::setCreateCallBack(OnCreateCall onCreate, void *context) {
    this->onCreateCall = onCreate;
    this->onCreateCtx = context;
}

void WzmEglThread::setChangeCallBack(OnChangeCall onChange, void *context) {
    this->onChangeCall = onChange;
    this->onChangeCtx = context;
}


void WzmEglThread::setDrawCallBack(OnDrawCall drawCall, void *context) {
    this->onDrawCall = drawCall;
    this->onDrawCtx = context;
}

