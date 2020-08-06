cmake_minimum_required(VERSION 3.4.1)

project(opengl-cpp)

add_library(
        opengl-cpp
        SHARED
        cpp/native-lib.cpp)


# ndk官方库  log
find_library(log-lib log)


# 将ffmpeg混合编译到native-lib中
target_link_libraries(
        opengl-cpp
        EGL
        GLESv2
        android
        ${log-lib})