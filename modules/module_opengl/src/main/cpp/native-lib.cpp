#include <jni.h>
#include <string>


#include "GLES2/gl2.h"
#include "android/native_window.h"
#include "android/native_window_jni.h"
#include "include/log/android_log_utils.h"
#include "include/egl/wzm_egl_helper.h"


WzmEglHelper *pEglHelper = NULL;
ANativeWindow  *pNativeWindow = NULL;


extern "C"
JNIEXPORT void JNICALL
Java_com_wangzhumo_app_module_opengl_cpp_opengl_NativeOpenGl_surfaceCreate(JNIEnv *env,jobject thiz,jobject surface)
{
    // 获取一个 ANativeWindow
    pNativeWindow = ANativeWindow_fromSurface(env,surface);
    if (pNativeWindow == nullptr){
        LOGE("ANativeWindow_fromSurface pNativeWindow = nullptr");
        return;
    }

    // 创建ELG的环境
    pEglHelper = new WzmEglHelper();
    pEglHelper->initEglEnv(pNativeWindow);

    // opengl 绘制
    glViewport(0,0,720,1080);
    glClearColor(0.0, 1.0, 0.0, 1.0);
    glClear(GL_COLOR_BUFFER_BIT);

    // 绘制
    pEglHelper->swapBuffers();
}