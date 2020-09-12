#include <jni.h>
#include <string>
#include "include/opengl/opengl_controller.h"
#include "include/utils/jni_utils.h"


OpenGlController * pGlController = nullptr;


// 创建一个Surface - EGL 的环境
extern "C"
JNIEXPORT void JNICALL
Java_com_wangzhumo_app_module_opengl_cpp_opengl_NativeOpenGl_surfaceCreate(
        JNIEnv *env, jobject thiz, jobject surface) {
    LOGD("OpenGLCPP  native-lib  surfaceCreate");
    // 获取一个 OpenGlController
    if (pGlController == nullptr){
        pGlController = new OpenGlController();
    }
    // 通知gl中的 create 事件触发
    pGlController->onSurfaceCreate(env, surface);
}


// surfaceChange调用
extern "C"
JNIEXPORT void JNICALL
Java_com_wangzhumo_app_module_opengl_cpp_opengl_NativeOpenGl_surfaceChange(
        JNIEnv *env, jobject thiz, jint width, jint height) {
    LOGD("OpenGLCPP  native-lib  surfaceChange");
    // surfaceChange 的调用
    if (pGlController != nullptr){
        pGlController->onSurfaceChange(width,height);
    }
}

// 切换不同的Filter使用
extern "C"
JNIEXPORT void JNICALL
Java_com_wangzhumo_app_module_opengl_cpp_opengl_NativeOpenGl_surfaceChangeFilter(JNIEnv *env,jobject thiz,jstring type) {
    LOGD("OpenGLCPP  native-lib  surfaceChangeFilter");
    if (pGlController != nullptr){
        pGlController->onSurfaceChangeFilter(jstring2string(env,type));
    }
}



// 外部传递过来的imageData
extern "C"
JNIEXPORT void JNICALL
Java_com_wangzhumo_app_module_opengl_cpp_opengl_NativeOpenGl_setImageData(JNIEnv *env, jobject thiz,
                                                                          jint jwidth, jint jheight,
                                                                          jbyteArray image_data) {
    LOGD("OpenGLCPP  native-lib  setImageData");
    // 获取数据
    jbyte *data = env->GetByteArrayElements(image_data, nullptr);
    int length = env->GetArrayLength(image_data);
    LOGD("setImageData length = %d", length);

    if(pGlController != nullptr){
        pGlController->setPixelsData(jwidth,jheight,length,data);
    }
    // 回收空间 - 使用之前拷贝的数据即可
    env->ReleaseByteArrayElements(image_data, data, 0);
}



extern "C"
JNIEXPORT void JNICALL
Java_com_wangzhumo_app_module_opengl_cpp_opengl_NativeOpenGl_surfaceDestroy(JNIEnv *env,
                                                                            jobject thiz) {
    LOGD("OpenGLCPP  native-lib  surfaceDestroy");
    // 调用通知
    if (pGlController != nullptr){
        pGlController->onRelease();
        // 回收自己的内存
        delete pGlController;
        pGlController = nullptr;
    }
}


extern "C"
JNIEXPORT void JNICALL
Java_com_wangzhumo_app_module_opengl_cpp_opengl_NativeOpenGl_updateYuvData(JNIEnv *env,
                                                                           jobject thiz,
                                                                           jbyteArray data_y,
                                                                           jbyteArray data_u,
                                                                           jbyteArray data_v,
                                                                           jint width,
                                                                           jint height) {

    jbyte *dataY = env->GetByteArrayElements(data_y, nullptr);
    jbyte *dataU = env->GetByteArrayElements(data_u, nullptr);
    jbyte *dataV = env->GetByteArrayElements(data_v, nullptr);
    LOGD("OpenGLCPP  native-lib  updateYuvData  y = %p, u = %p, v = %p",dataY,dataU,dataV);

    if(pGlController != nullptr){
        pGlController->updateYuvData(dataY,dataU,dataV,width,height);
    }

    // 回收空间 - 使用之前拷贝的数据即可
    env->ReleaseByteArrayElements(data_y, dataY, 0);
    env->ReleaseByteArrayElements(data_u, dataU, 0);
    env->ReleaseByteArrayElements(data_v, dataV, 0);
}