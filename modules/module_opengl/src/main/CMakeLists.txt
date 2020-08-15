cmake_minimum_required(VERSION 3.4.1)

# PROJECT NAME & VERSION
project(opengl-cpp VERSION 1.0.1 LANGUAGES C CXX)

#不加这句不能用std命名空间的函数
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11 -Wall")


# Add all binary files
aux_source_directory(cpp SRC_DIR)
aux_source_directory(cpp/egl SRC_EGL)


# Set all source
set(ALL_SOURCE ${SRC_DIR} ${SRC_EGL})

add_library(
        opengl-cpp
        SHARED
        ${ALL_SOURCE}
)

# ndk官方库  include.log
find_library(log-lib log)

# Include dir
# ${EVENT_INCLUDE_DIR}
set(INCLUDE_DIR ${CMAKE_SOURCE_DIR}/main/cpp/include)
set(INCLUDES  ${INCLUDE_DIR}/egl ${INCLUDE_DIR}/log)
include_directories(${INCLUDES})




# 将ffmpeg混合编译到native-lib中
target_link_libraries(
        opengl-cpp
        android
        EGL
        GLESv2

        ${log-lib})