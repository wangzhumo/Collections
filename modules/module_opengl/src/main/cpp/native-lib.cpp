#include <jni.h>
#include <string>


#include "GLES2/gl2.h"
#include "android/native_window.h"
#include "android/native_window_jni.h"
#include "include/egl/wzm_egl_thread.h"


WzmEglThread *pEglThread = nullptr;
ANativeWindow *pNativeWindow = nullptr;

// 创建一个Surface - EGL 的环境
extern "C"
JNIEXPORT void JNICALL
Java_com_wangzhumo_app_module_opengl_cpp_opengl_NativeOpenGl_surfaceCreate(
        JNIEnv *env,jobject thiz,jobject surface) {
    // 获取一个 ANativeWindow
    pNativeWindow = ANativeWindow_fromSurface(env, surface);
    if (pNativeWindow == nullptr) {
        LOGE("ANativeWindow_fromSurface pNativeWindow = nullptr");
        return;
    }

    // 创建ELG的环境,启动eglThread
    pEglThread = new WzmEglThread();
    pEglThread->onSurfaceCreate(pNativeWindow);
}


// surfaceChange调用
extern "C"
JNIEXPORT void JNICALL
Java_com_wangzhumo_app_module_opengl_cpp_opengl_NativeOpenGl_surfaceChange(
        JNIEnv *env,jobject thiz, jint width,jint height) {
    // surfaceChange 的调用
    if (pEglThread != nullptr){
        pEglThread->onSurfaceChange(width,height);
    }
}