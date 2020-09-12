//
// Created by wangzhumo on 2020/8/28.
//
//

#include "../include/opengl/opengl_controller.h"


void onSurfaceCreateCall(void *ctx) {
    auto *pGlController = static_cast<OpenGlController *>(ctx);
    if (pGlController != nullptr) {
        // 如果不为空,就可以使用
        if (pGlController->baseOpenGl != nullptr) {
            // 如果不为空,就可以调用到baseOpenGL中自己的具体实现
            pGlController->baseOpenGl->onSurfaceCreate();
        }
    }
}

void onSurfaceChangeCall(int width, int height, void *ctx) {
    auto *pGlController = static_cast<OpenGlController *>(ctx);
    LOGD("OpenGLController  onSurfaceChangeCall width = %d  , height = %d", width, height);
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
void onSurfaceFilterCall(int width, int height, void *ctx) {
    auto *pGlController = static_cast<OpenGlController *>(ctx);
    if (pGlController != nullptr) {
        // 如果不为空,就可以使用
        if (pGlController->baseOpenGl != nullptr) {
            //1.销毁之前的filter
            pGlController->baseOpenGl->onRelease();
            //由于这里不会停止EGL线程（不是外部停止，不会调用jni中的onSurfaceRelease），
            // 所以手动的调用一下onDestroyResource
            pGlController->baseOpenGl->onDestroyResource();
            delete pGlController->baseOpenGl;
            pGlController->baseOpenGl = nullptr;
        }
        // 这里可以通过 pGlController->filterType 来指定不同的滤镜效果
        //2.创建新的filter并赋值
        pGlController->baseOpenGl = new OpenGLFilterNormalCopy();
        //3.滤镜自己的生命周期
        pGlController->baseOpenGl->onSurfaceCreate();
        LOGD("OpenGLController  onSurfaceFilterCall width = %d  , height = %d", width, height);
        pGlController->baseOpenGl->onSurfaceChange(width, height);
        //4.设置图像数据
        LOGD("OpenGLController  onSurfaceFilterCall pixWidth = %d  , pixHeight = %d",
             pGlController->pixWidth, pGlController->pixHeight);
        pGlController->baseOpenGl->setPixelsData(pGlController->pixWidth, pGlController->pixHeight,
                                                 pGlController->pixelArr);
        //4.更新一下线程，调用render方法
        pGlController->pEglThread->notifyRender();
    }
}

// 销毁，因为是egl中的回调，可以方便的销毁资源
void onReleaseCall(void *ctx) {
    auto *pGlController = static_cast<OpenGlController *>(ctx);
    if (pGlController != nullptr) {
        // 如果不为空,就可以使用
        if (pGlController->baseOpenGl != nullptr) {
            pGlController->baseOpenGl->onRelease();
        }
    }
}

// 创建了EGL的环境， 启动线程 , 获取原生的数据
// 为什么要用回调?
// 内部的实现,都是流程代码,不会对业务有任何操作,所以可以更方便的替换
void OpenGlController::onSurfaceCreate(JNIEnv *env, jobject surface) {
    // 获取一个 ANativeWindow
    LOGD("OpenGLCPP OpenGlController::onCreateSurface");
    pNativeWindow = ANativeWindow_fromSurface(env, surface);
    if (pNativeWindow == nullptr) {
        LOGE("ANativeWindow_fromSurface pNativeWindow = nullptr");
        return;
    }

    // 1.创建ELG的环境
    pEglThread = new WzmEglThread();
    pEglThread->setRenderMode(OPENGL_RENDER_DIRTY);
    // 1.1 create_callback
    pEglThread->setCreateCallBack(onSurfaceCreateCall, this);
    // 1.2 change_callback
    pEglThread->setChangeCallBack(onSurfaceChangeCall, this);
    // 1.3 draw_callback
    pEglThread->setDrawCallBack(onSurfaceDrawCall, this);
    // 1.4 filter_callback
    pEglThread->setFilterChangeCallBack(onSurfaceFilterCall, this);
    // 1.5 release_callback
    pEglThread->setReleaseCallBack(onReleaseCall, this);

    // 创建baseOpengl
    // 这里可以创建不同的BaseOpenGL达到更换渲染的
    baseOpenGl = new OpenGLFilterYuv();

    // 2.启动eglThread
    pEglThread->onSurfaceCreate(pNativeWindow);
}

void OpenGlController::onSurfaceChange(int width, int height) {
    // surfaceChange 的调用
    if (pEglThread != nullptr) {
        LOGE("OpenGLCPP OpenGlController::onChangeSurface surfaceWidth = %d surfaceHeight = %d", width, height);
        if (baseOpenGl != nullptr){
            baseOpenGl->baseSurfaceWidth = width;
            baseOpenGl->baseSurfaceHeight = height;
        }
        pEglThread->onSurfaceChange(width, height);
    }
}

void OpenGlController::onSurfaceChangeFilter(std::string type) {
    // SurfaceChangeFilter 的调用
    // 保存要切换的filter类型。
    if (pEglThread != nullptr) {
        pEglThread->onSurfaceChangeFilter();
    }
    filterType = &type;
}


void OpenGlController::onRelease() {
    // 停止线程之前先
    if (pEglThread != nullptr) {
        pEglThread->release();
    }
    //调用baseOpenGL的方法.
    if (baseOpenGl != nullptr) {
        baseOpenGl->onDestroyResource();
        delete baseOpenGl;
        baseOpenGl = nullptr;
    }
    // 销毁Window资源
    if (pNativeWindow != nullptr) {
        ANativeWindow_release(pNativeWindow);
        pNativeWindow = nullptr;
    }
    // 销毁图片的数组
    if (pixelArr != nullptr) {
        free(pixelArr);
        pixelArr = nullptr;
    }

}


// 数据被保存到自己的controller.
void OpenGlController::setPixelsData(int width, int height, int len, void *pixArr) {
    pixWidth = width;
    pixHeight = height;
    LOGD("OpenGLController  setPixelsData pixWidth = %d  , pixHeight = %d", pixWidth, pixHeight);
    // 支持动态切换纹理
    void *tempPixAar = nullptr;
    if (pixelArr != nullptr) {
        tempPixAar = pixelArr;
        pixelArr = nullptr;
    }

    // 开辟数组,设置新的数据进来
    pixelArr = malloc(len);
    // 拷贝
    memcpy(pixelArr, pixArr, len);

    // 刷新
    if (baseOpenGl != nullptr) {
        baseOpenGl->setPixelsData(width, height, pixelArr);
    }
    if (pEglThread != nullptr) {
        pEglThread->notifyRender();
    }
    // 完成后释放tempPixAar中的旧数据
    if (tempPixAar != nullptr) {
        free(tempPixAar);
        tempPixAar = nullptr;
    }
}

void OpenGlController::updateYuvData(jbyte *dataY, jbyte *dataU, jbyte *dataV, jint width, jint height) {
    LOGD("OpenGLCPP OpenGlController::setYuvData y = %p u = %p v = %p , width = %d height = %d", dataY, dataU,dataV,width,height);
    if (baseOpenGl != nullptr) {
        baseOpenGl->updateYuvData(dataY, dataU, dataV, width, height);
    }
    if (pEglThread != nullptr){
        pEglThread->notifyRender();
    }
}

OpenGlController::OpenGlController() = default;

OpenGlController::~OpenGlController() = default;







