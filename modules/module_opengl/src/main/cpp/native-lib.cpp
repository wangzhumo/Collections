#include <jni.h>
#include <string>



#include "android/native_window.h"
#include "android/native_window_jni.h"
#include "include/egl/wzm_egl_thread.h"
#include "include/utils/shader_utils.h"


WzmEglThread *pEglThread = nullptr;
ANativeWindow *pNativeWindow = nullptr;

// 顶点着色器
const char *vertexSource = "attribute vec4 vPosition;    \n"
                           "void main(){                 \n"
                           "    gl_Position = vPosition; \n"
                           "}";

// 片着色器
const char *fragmentSource = "precision mediump float;              \n"
                             "void main(){                          \n"
                             "    gl_FragColor = vec4(1f,0f,0f,1f); \n"
                             "}";



GLuint  programId = 0;
GLuint  vPosition = 0;
float vertexArr[] = {
     -1,-1,
     0,1,
     1,-1
};

void onSurfaceCreateCall(void *) {
    LOGD("onSurfaceCreateCall");

    // 测试opengl初始化 shader
    programId = createProgram(vertexSource,fragmentSource);
    LOGD("createProgram programId = %d",programId);
    // 获取参数
    vPosition = glGetAttribLocation(programId,"vPosition");
}

void onSurfaceChangeCall(int width, int height, void *ctx) {
    //WzmEglThread *wzmEglThread = static_cast<WzmEglThread *>(ctx);
    glViewport(0, 0, width, height);
    LOGD("onSurfaceChangeCall width = %d ,height = %d",width,height);
}

void onSurfaceDrawCall(void *ctx) {
    LOGD("onSurfaceDrawCall");
    WzmEglThread *wzmEglThread = static_cast<WzmEglThread *>(ctx);
    glClearColor(0.0, 1.0, 1.0, 1.0);
    glClear(GL_COLOR_BUFFER_BIT);

    // 使用程序
    glUseProgram(programId);
    // 设置顶点数组可用
    glEnableVertexAttribArray(vPosition);
    // 设置参数
    // vPosition 参数的指向
    // 2         每一个顶点由几个数据组成，这里（1,1）两个float 2
    // GL_FLOAT  参数的数据类型
    // false     是否归一化坐标
    // 8         每一个顶点的长度   float 4 * 2 = 8
    glVertexAttribPointer(
        vPosition,
        2,
        GL_FLOAT,
    false,
    8,
    vertexArr
            );
}

// 创建一个Surface - EGL 的环境
extern "C"
JNIEXPORT void JNICALL
Java_com_wangzhumo_app_module_opengl_cpp_opengl_NativeOpenGl_surfaceCreate(
        JNIEnv *env, jobject thiz, jobject surface) {
    // 获取一个 ANativeWindow
    pNativeWindow = ANativeWindow_fromSurface(env, surface);
    if (pNativeWindow == nullptr) {
        LOGE("ANativeWindow_fromSurface pNativeWindow = nullptr");
        return;
    }

    // 1.创建ELG的环境
    pEglThread = new WzmEglThread();
    // 1.1 create_callback
    pEglThread->setCreateCallBack(onSurfaceCreateCall, pEglThread);
    // 1.2 change_callback
    pEglThread->setChangeCallBack(onSurfaceChangeCall, pEglThread);
    // 1.3 draw_callback
    pEglThread->setDrawCallBack(onSurfaceDrawCall, pEglThread);

    // 2.启动eglThread
    pEglThread->setRenderMode(OPENGL_RENDER_DIRTY);
    pEglThread->onSurfaceCreate(pNativeWindow);


}


// surfaceChange调用
extern "C"
JNIEXPORT void JNICALL
Java_com_wangzhumo_app_module_opengl_cpp_opengl_NativeOpenGl_surfaceChange(
        JNIEnv *env, jobject thiz, jint width, jint height) {
    // surfaceChange 的调用
    if (pEglThread != nullptr) {
        pEglThread->onSurfaceChange(width, height);

        // 手动调用一次 notify
        usleep(1000000);

        // 查看确实已经draw了两次
        pEglThread->notifyRender();
    }
}