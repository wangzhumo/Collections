//
// Created by wangzhumo on 2020/8/28.
//

#include "../include/opengl/opengl_controller.h"


void onSurfaceCreateCall(void *) {

}

void onSurfaceChangeCall(int width, int height, void *ctx) {

}

void onSurfaceDrawCall(void *ctx) {

}

// 创建了EGL的环境， 启动线程 , 获取原生的数据
void OpenGlController::onSurfaceCreate(JNIEnv *env, jobject surface) {
    // 获取一个 ANativeWindow
    pNativeWindow = ANativeWindow_fromSurface(env, surface);
    if (pNativeWindow == nullptr) {
        LOGE("ANativeWindow_fromSurface pNativeWindow = nullptr");
        return;
    }

    // 1.创建ELG的环境
    pEglThread = new WzmEglThread();
    // 1.1 create_callback
    pEglThread->setCreateCallBack(onSurfaceCreateCall, pEglThread);
    // 1.2 change_callback
    pEglThread->setChangeCallBack(onSurfaceChangeCall, pEglThread);
    // 1.3 draw_callback
    pEglThread->setDrawCallBack(onSurfaceDrawCall, pEglThread);

    // 2.启动eglThread
    pEglThread->setRenderMode(OPENGL_RENDER_DIRTY);
    pEglThread->onSurfaceCreate(pNativeWindow);
}

void OpenGlController::onSurfaceChange(int width, int height) {
    // surfaceChange 的调用
    if (pEglThread != nullptr) {
        // 保存一下子
        if(baseOpenGl != nullptr){
            baseOpenGl->surfaceWidth = width;
            baseOpenGl->surfaceHeight = height;
        }
        pEglThread->onSurfaceChange(width, height);

        // 查看确实已经draw了两次
        pEglThread->notifyRender();
    }
}

void OpenGlController::onRelease() {

}


