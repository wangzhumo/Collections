//
// Created by wangzhumo on 2020/8/15.
//


#include "../include/egl/wzm_egl_thread.h"

WzmEglThread::WzmEglThread() {

}

WzmEglThread::~WzmEglThread() {

}

void *eglThreadCallBack(void *context) {

    auto *wzmEglThread = static_cast<WzmEglThread *>(context);
    if (wzmEglThread != nullptr) {
        // 如果不为空,就在这里创建一个EGL的环境.
        auto *pEglHelper = new WzmEglHelper();
        pEglHelper->initEglEnv(wzmEglThread->pNativeWindow);
        wzmEglThread->isExit = false;
        // 这里一个循环,用来处理数据
        while (true) {
            if (wzmEglThread->isCreate) {
                LOGE("elgThread call surfaceCreate");
                wzmEglThread->isCreate = false;
            }

            if (wzmEglThread->isChange) {
                LOGE("eglThread call surfaceChange");
                wzmEglThread->isChange = false;
            }

            // 60 fps
            usleep(1000000 / 60);

            // 这里就开始绘制
            LOGD("eglThread draw");
            if (wzmEglThread->isExit){
                break;
            }
        }
    }
    return nullptr;
}

void WzmEglThread::onSurfaceCreate(EGLNativeWindowType windowType) {
    if (pEglThread == -1) {
        // 如果 == -1,说明没有创建过,开始创建
        pEglThread = pthread_create(&pEglThread, nullptr, eglThreadCallBack, this);
    }
}


void WzmEglThread::onSurfaceDraw() {

}

void WzmEglThread::onSurfaceChange() {

}

