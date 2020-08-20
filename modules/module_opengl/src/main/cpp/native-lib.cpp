#include <jni.h>
#include <string>


#include "android/native_window.h"
#include "android/native_window_jni.h"
#include "include/egl/wzm_egl_thread.h"
#include "include/utils/shader_utils.h"
#include "include/utils/shader_glsl.h"


WzmEglThread *pEglThread = nullptr;
ANativeWindow *pNativeWindow = nullptr;



GLuint programId = 0;
GLint vPosition = 0;
GLint fPosition = 0;
GLint samplerId = 0;
GLuint textureId = 0;

int width = 0;
int height = 0;
void *pixelsArr = nullptr;

float vertexArrStrip[] = {
        -1, -1,
        1, -1,
        -1, 1,
        1, 1,
};

float textureArr[] = {
    0,1,
    1,1,
    0,0,
    1,0
};

void onSurfaceCreateCall(void *) {
    LOGD("onSurfaceCreateCall");
    // 测试opengl初始化 shader
    programId = createProgram(vertexSource, fragmentSource);
    LOGD("createProgram programId = %d", programId);

    // 获取参数
    vPosition = glGetAttribLocation(programId, "vPosition");  //顶点的坐标
    fPosition = glGetAttribLocation(programId, "fPosition");  //这个纹理的坐标
    samplerId = glGetUniformLocation(programId, "sTexture");  //2d纹理

    // 创建一个texture，并且赋值到 textureId
    glGenTextures(1,&textureId);
    // 绑定纹理
    glBindTexture(GL_TEXTURE_2D,textureId);

    // 设置环绕方式
    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_REPEAT);
    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_REPEAT);
    // 设置过滤方式
    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_LINEAR);
    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);

    //设置图片，加载资源
    if (pixelsArr != nullptr){
        //    glTexImage2D(EGL_SURFACE_TYPE)
    }


    // 设置完毕之后,解除绑定纹理
    glBindTexture(GL_TEXTURE_2D,0);

}

void onSurfaceChangeCall(int width, int height, void *ctx) {
    //WzmEglThread *wzmEglThread = static_cast<WzmEglThread *>(ctx);
    glViewport(0, 0, width, height);
    LOGD("onSurfaceChangeCall width = %d ,height = %d", width, height);
}

void onSurfaceDrawCall(void *ctx) {
    LOGD("onSurfaceDrawCall");
    auto *wzmEglThread = static_cast<WzmEglThread *>(ctx);
    glClearColor(0.0, 0.0, 1.0, 1.0);
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
    // vertexArr 要赋值的数值数组
    glVertexAttribPointer(
            vPosition,
            2,
            GL_FLOAT,
            false,
            8,
            vertexArrStrip
    );


    // 设置数组可用
    glEnableVertexAttribArray(fPosition);
    glVertexAttribPointer(fPosition,2,GL_FLOAT, false,8,textureArr);


    // 绘制这个三角形,共有3个点
    //glDrawArrays(GL_TRIANGLES,0,6);
    glDrawArrays(GL_TRIANGLE_STRIP,0,4);

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

// 外部传递过来的imageData
extern "C"
JNIEXPORT void JNICALL
Java_com_wangzhumo_app_module_opengl_cpp_opengl_NativeOpenGl_setImageData(JNIEnv *env, jobject thiz,
                                                                          jint jwidth, jint jheight,
                                                                          jbyteArray image_data) {

    // 获取数据
    jbyte *data = env->GetByteArrayElements(image_data,nullptr);
    int length = env->GetArrayLength(image_data);

    // 开辟数据内存
    pixelsArr = malloc(length);
    memcpy(pixelsArr,data,length);

    // 设置 w, h
    width = jwidth;
    height = jheight;

    // 回收空间
    env->ReleaseByteArrayElements(image_data,data,0);
}