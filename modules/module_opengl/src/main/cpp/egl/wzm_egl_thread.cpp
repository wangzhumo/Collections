//
// Created by wangzhumo on 2020/8/15.
//
#include "../include/egl/wzm_egl_thread.h"

WzmEglThread::WzmEglThread(){
    // 初始化线程锁
    pthread_mutex_init(&pThreadMutex, nullptr);
    // 初始化信号量
    pthread_cond_init(&pThreadCond, nullptr);

}

WzmEglThread::~WzmEglThread(){
    // 把锁打开,释放
    pthread_mutex_destroy(&pThreadMutex);
    pthread_cond_destroy(&pThreadCond);
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
            // 处理 surfaceCreate
            if (wzmEglThread->isCreate) {
                LOGE("OpenGL elgThread call surfaceCreate");
                wzmEglThread->isCreate = false;
                // 回调给外部.
                wzmEglThread->onCreateCall(wzmEglThread->onCreateCtx);

            }
            // 在这里处理 surfaceChange 的事件
            if (wzmEglThread->isChange) {
                LOGE("OpenGL elgThread  call surfaceChange");
                wzmEglThread->isChange = false;
                // 这里执行回调.
                wzmEglThread->onChangeCall(wzmEglThread->surfaceWidth,wzmEglThread->surfaceHeight,wzmEglThread->onChangeCtx);
                // 告知执行完毕
                wzmEglThread->canStart = true;
            }

            // 这里切换ChangeFilter的逻辑，交给外部处理具体切换的东西
            if (wzmEglThread->isChangeFilter){
                wzmEglThread->isChangeFilter = false;
                LOGE("OpenGL elgThread  call onChangeFilter");
                wzmEglThread->onChangeFilter(wzmEglThread->surfaceWidth,wzmEglThread->surfaceHeight,wzmEglThread->onChangeFilterCtx);
            }

            // 这里就开始绘制 surfaceDraw
            if (wzmEglThread->canStart){
                LOGD("OpenGL eglThread onDrawCall");
                // 这里执行回调.
                wzmEglThread->onDrawCall(wzmEglThread->onDrawCtx);
                pEglHelper->swapBuffers();
            }

            // 休眠根据模式来搞起.
            if (wzmEglThread->renderMode == OPENGL_RENDER_AUTO){
                // 暂停 60 fps
                usleep(1000000 / 60);
            }else{
                // 加上锁
                pthread_mutex_lock(&(wzmEglThread->pThreadMutex));
                // 阻塞起来
                pthread_cond_wait(&(wzmEglThread->pThreadCond),&(wzmEglThread->pThreadMutex));
                // 此时打开锁，线程已经是在阻塞状态了.
                pthread_mutex_unlock(&(wzmEglThread->pThreadMutex));
            }


            if (wzmEglThread->isExit){
                // 回调给外部，自行销毁资源
                LOGD("OpenGL eglThread onReleaseCall");
                wzmEglThread->onReleaseCall(wzmEglThread->onReleaseCtx);

                // 销毁EGL
                pEglHelper->release();
                delete pEglHelper;
                pEglHelper = nullptr;
                break;
            }
        }
    }
    //也可以调用这个
    //pthread_exit(&(wzmEglThread->pEglThread));
    return 0;
}

void WzmEglThread::onSurfaceCreate(EGLNativeWindowType windowType) {
    if (pEglThread == -1) {
        // surfaceCreate
        isCreate = true;
        // 设置窗口
        pNativeWindow = windowType;
        // 如果 == -1,说明没有创建过,开始创建
        LOGD("OpenGLCpp eglThread onSurfaceCreate");
        pEglThread = pthread_create(&pEglThread, nullptr, eglThreadCallBack, this);
    }
}

void WzmEglThread::onSurfaceChange(int width, int height) {
    isChange = true;
    surfaceWidth = width;
    surfaceHeight = height;
    // 渲染一次,避免线程锁住无法更新的问题
    LOGD("OpenGLCpp eglThread onSurfaceChange");
    notifyRender();
}

// 切换filter
void WzmEglThread::onSurfaceChangeFilter() {
    this->isChangeFilter = true;
    this->notifyRender();
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

void WzmEglThread::setFilterChangeCallBack(WzmEglThread::OnChangeFilter onFilter, void *context) {
    this->onChangeFilter = onFilter;
    this->onChangeFilterCtx = context;
}

void WzmEglThread::setRenderMode(int mode) {
    this->renderMode = mode;
}

void WzmEglThread::setReleaseCallBack(OnReleaseCall releaseCall,void *context) {
    this->onReleaseCall = releaseCall;
    this->onReleaseCtx = context;
}

void WzmEglThread::notifyRender() {
    // 加上锁
    pthread_mutex_lock(&pThreadMutex);
    // 通知之前设置的阻塞 - 应该打开,继续去运行
    pthread_cond_signal(&pThreadCond);
    // 操作结束，此时打开锁
    pthread_mutex_unlock(&pThreadMutex);
}

void WzmEglThread::release() {
    this->isExit = true;
    LOGD("OpenGLCpp eglThread release");
    // 渲染一次,避免线程锁住无法更新的问题
    notifyRender();
    // 去掉pEglThread,会等待EGL线程销毁才继续执行
    pthread_join(pEglThread, nullptr);
    pEglThread = -1;
    // 由于NativeWindow,这里只是引用,我们仅仅把指针去掉即可
    pNativeWindow = nullptr;
}




