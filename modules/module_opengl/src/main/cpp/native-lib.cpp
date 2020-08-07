#include <jni.h>
#include <string>

#include "EGL/egl.h"
#include "GLES2/gl2.h"
#include "android/native_window.h"
#include "android/native_window_jni.h"
#include "include/log/android_log_utils.h"

#include "include/egl/egl_helper.h"

extern "C"
JNIEXPORT jint JNICALL
Java_com_wangzhumo_app_module_opengl_cpp_OpenGLCppActivity_printAndroidLog(JNIEnv *env,
                                                                           jobject thiz,
                                                                           jstring message) {
    const char *msg = env->GetStringUTFChars(message,NULL);

    LOGD("Message from Android : %s",msg);

    char *cha = NULL;

    return 0;
}extern "C"
JNIEXPORT void JNICALL
Java_com_wangzhumo_app_module_opengl_cpp_opengl_NativeOpenGl_surfaceCreate(JNIEnv *env,
                                                                           jobject thiz,
                                                                           jobject surface) {

}