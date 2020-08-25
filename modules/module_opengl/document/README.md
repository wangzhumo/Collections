- call to OpenGL ES API with no current context
```c++

extern "C"
JNIEXPORT void JNICALL
Java_com_wangzhumo_app_module_opengl_cpp_opengl_NativeOpenGl_surfaceCreate(
        JNIEnv *env, jobject thiz, jobject surface) {
    ...

    // 测试opengl初始化 shader
    int programId = createProgram(vertexSource,fragmentSource);
    LOGD("createProgram programId = %d",programId);
}

```
在create中去加载,此时的EGL环境还没有建立,加载失败.

```c++

void onSurfaceCreateCall(void *) {
    LOGD("onSurfaceCreateCall");

    // 测试opengl初始化 shader
    int programId = createProgram(vertexSource,fragmentSource);
    LOGD("createProgram programId = %d",programId);
}

```


- 使用STRIP的方式绘制三角形

```gles

float vertexArrStrip[] = {
        -1, 1,
        1, -1,
        -1, -1,
        1, 1,
};


# 此时的三角形是有去缺口的, 1,-1   -1,-1 中间的两个点是公用边
# 这样不能组成一个四边形,而会有缺口


float vertexArrStrip[] = {
        -1, -1,
        1, -1,
        -1, 1,
        1, 1,
};
#这样，公用的边就是  -1,1   1,-1 完美


```


https://learnopengl-cn.github.io/01%20Getting%20started/07%20Transformations/#_10

