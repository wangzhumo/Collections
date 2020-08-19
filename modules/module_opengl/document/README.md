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