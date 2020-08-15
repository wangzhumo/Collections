cmake_minimum_required(VERSION 3.4.1)

project(opengl-cpp)

add_library(
        opengl-cpp
        SHARED
        cpp/native-lib.cpp)


# ndk官方库  include.log
find_library(log-lib log)

# Include dir
# ${EVENT_INCLUDE_DIR}
set(INCLUDE_DIR ${CMAKE_SOURCE_DIR}/main/cpp/include)
set(INCLUDES  ${INCLUDE_DIR}/egl/wzm_egl_helper.h ${INCLUDE_DIR}/log/android_log_utils.h)
include_directories(${INCLUDES})




# 将ffmpeg混合编译到native-lib中
target_link_libraries(
        opengl-cpp
        EGL
        GLESv2
        android
        ${log-lib})