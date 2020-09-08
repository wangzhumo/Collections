//
// Created by wangzhumo on 2020/8/6.
//

#ifndef COLLECTIONS_ANDROID_LOG_UTILS_H
#define COLLECTIONS_ANDROID_LOG_UTILS_H

#include <android/log.h>

#define TAG "cpp-opengl" // 这个是自定义的LOG的标识
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,TAG ,__VA_ARGS__) // 定义LOGD类型
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,TAG ,__VA_ARGS__) // 定义LOGI类型
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,TAG ,__VA_ARGS__) // 定义LOGE类型


#endif //COLLECTIONS_ANDROID_LOG_UTILS_H
