#include <jni.h>
#include <string>
#include "include/log/android_log_utils.h"



//#include "include/opengl/opengl_controller.h"


int iwidth = 0;
int iheight = 0;

void *pixelsArr = nullptr;


// 创建一个Surface - EGL 的环境
extern "C"
JNIEXPORT void JNICALL
Java_com_wangzhumo_app_module_opengl_cpp_opengl_NativeOpenGl_surfaceCreate(
        JNIEnv *env, jobject thiz, jobject surface) {
    // 获取一个 ANativeWindow

}


// surfaceChange调用
extern "C"
JNIEXPORT void JNICALL
Java_com_wangzhumo_app_module_opengl_cpp_opengl_NativeOpenGl_surfaceChange(
        JNIEnv *env, jobject thiz, jint width, jint height) {
    // surfaceChange 的调用

}

// 外部传递过来的imageData
extern "C"
JNIEXPORT void JNICALL
Java_com_wangzhumo_app_module_opengl_cpp_opengl_NativeOpenGl_setImageData(JNIEnv *env, jobject thiz,
                                                                          jint jwidth, jint jheight,
                                                                          jbyteArray image_data) {
    // 获取数据
    jbyte *data = env->GetByteArrayElements(image_data, nullptr);
    int length = env->GetArrayLength(image_data);
    LOGD("setImageData length = %d", length);
    // 开辟数据内存
    pixelsArr = malloc(length);
    memcpy(pixelsArr, data, length);

    // 设置 w, h
    iwidth = jwidth;
    iheight = jheight;

    // 回收空间 - 使用之前拷贝的数据即可
    env->ReleaseByteArrayElements(image_data, data, 0);
}