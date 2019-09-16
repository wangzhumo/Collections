# 指定了CMake的最低版本为3.4.1
cmake_minimum_required(VERSION 3.4.1)

# 添加一个库
add_library(
        # 这里设置so库的名称为native-lib
        native-lib

        # 设置该库为共享
        SHARED

        # 源文件的路径，可以有多个，最终这个源文件将被编译并打包进native-lib库
        native-lib.cpp)


# 查找一个库，并设置到路径变量中去
find_library(
        # 设置路径变量
        log-lib

        # 你希望CMake编译器加载的NDK函数库
        log)

# 关联指定的库到目标函数库中
target_link_libraries(
        # 目标函数库
        native-lib

        # 需要在目标函数库中使用的函数库，${路径变量}
        ${log-lib})