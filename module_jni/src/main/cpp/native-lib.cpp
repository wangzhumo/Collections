#include <jni.h>
#include <string>
#include <android/log.h>


extern "C"
JNIEXPORT jstring JNICALL
Java_com_wangzhumo_app_module_jni_CppActivity_stringFromJNI(JNIEnv *env, jobject instance) {

    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}