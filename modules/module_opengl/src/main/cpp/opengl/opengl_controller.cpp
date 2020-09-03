//
// Created by wangzhumo on 2020/8/28.
//



#include "../include/opengl/opengl_controller.h"


void onSurfaceCreateCall(void *ctx) {
    auto *pGlController = static_cast<OpenGlController *>(ctx);
    if (pGlController != nullptr && pGlController->baseOpenGl != nullptr) {
        // 如果不为空,就可以使用
        if (pGlController->baseOpenGl != nullptr) {
            // 如果不为空,就可以调用到baseOpenGL中自己的具体实现
            pGlController->baseOpenGl->onSurfaceCreate();
        }
    }
}

void onSurfaceChangeCall(int width, int height, void *ctx) {
    auto *pGlController = static_cast<OpenGlController *>(ctx);
    if (pGlController != nullptr) {
        // 如果不为空,就可以使用
        if (pGlController->baseOpenGl != nullptr) {
            pGlController->baseOpenGl->onSurfaceChange(width, height);
        }
    }
}

void onSurfaceDrawCall(void *ctx) {
    auto *pGlController = static_cast<OpenGlController *>(ctx);
    if (pGlController != nullptr) {
        // 如果不为空,就可以使用
        if (pGlController->baseOpenGl != nullptr) {
            pGlController->baseOpenGl->onSurfaceDraw();
        }
    }
}

// 这里可以添加参数，用于切换不同的滤镜
void onSurfaceFilterCall(int width, int height, void *ctx){
    auto *pGlController = static_cast<OpenGlController *>(ctx);
    if (pGlController != nullptr) {
        // 如果不为空,就可以使用
        if (pGlController->baseOpenGl != nullptr) {
            //1.销毁之前的filter
            pGlController->baseOpenGl->onRelease();
            pGlController->baseOpenGl = nullptr;
        }
        //2.创建新的filter并赋值
        pGlController->baseOpenGl = new OpenGLFilterNormalCopy();
        //3.滤镜自己的生命周期
        pGlController->baseOpenGl->onSurfaceCreate();
        pGlController->baseOpenGl->onSurfaceChange(width,height);
        //4.更新一下线程，调用render方法
        pGlController->pEglThread->notifyRender();
    }
}

// 创建了EGL的环境， 启动线程 , 获取原生的数据
// 为什么要用回调?
// 内部的实现,都是流程代码,不会对业务有任何操作,所以可以更方便的替换
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
    pEglThread->setCreateCallBack(onSurfaceCreateCall, this);
    // 1.2 change_callback
    pEglThread->setChangeCallBack(onSurfaceChangeCall, this);
    // 1.3 draw_callback
    pEglThread->setDrawCallBack(onSurfaceDrawCall, this);
    // 1.4 filter_callback
    pEglThread->setFilterChangeCallBack(onSurfaceFilterCall,this);

    // 创建baseOpengl
    // 这里可以创建不同的BaseOpenGL达到更换渲染的
    baseOpenGl = new OpenGLFilterNormal();

    // 2.启动eglThread
    pEglThread->setRenderMode(OPENGL_RENDER_DIRTY);
    pEglThread->onSurfaceCreate(pNativeWindow);

    // 3.调用
    baseOpenGl->onSurfaceCreate();
}

void OpenGlController::onSurfaceChange(int width, int height) {
    // surfaceChange 的调用
    if (pEglThread != nullptr) {
        // 保存一下子
        if (baseOpenGl != nullptr) {
            baseOpenGl->baseSurfaceWidth = width;
            baseOpenGl->baseSurfaceHeight = height;
        }
        pEglThread->onSurfaceChange(width, height);

        // 查看确实已经draw了两次
        pEglThread->notifyRender();
    }
}

void OpenGlController::onRelease() {
    // 停止线程之前先
    pEglThread->release();
    //调用baseOpenGL的方法.
    if (baseOpenGl != nullptr) {
        baseOpenGl->onRelease();
        delete baseOpenGl;
        baseOpenGl = nullptr;
    }

    // 销毁资源
    ANativeWindow_release(pNativeWindow);
    pNativeWindow = nullptr;

}


// 数据被保存到自己的controller.
void OpenGlController::setPixelsData(int width, int height, int len, void *pixArr) {
    pixWidth = width;
    pixHeight = height;

    // 开辟数组
    pixelArr = malloc(len);
    // 拷贝
    memcpy(pixelArr, pixArr, len);

    // 刷新
    if (baseOpenGl != nullptr) {
        baseOpenGl->setPixelsData(width, height, len, pixelArr);
    }
    if (pEglThread != nullptr) {
        pEglThread->notifyRender();
    }
}

OpenGlController::OpenGlController() {

}

OpenGlController::~OpenGlController() {

}




