//
// Created by wangzhumo on 2020/9/3.
//

#ifndef COLLECTIONS_JNI_UTILS_H
#define COLLECTIONS_JNI_UTILS_H

#include <string>
#include <jni.h>

static std::string jstring2string(JNIEnv *env,jstring jstr){
    if (!jstr){
        return "";
    }
}

#endif //COLLECTIONS_JNI_UTILS_H
