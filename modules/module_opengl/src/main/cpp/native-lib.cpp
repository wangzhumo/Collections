#include <jni.h>
#include <string>


#include "android/native_window.h"
#include "android/native_window_jni.h"
#include "include/egl/wzm_egl_thread.h"
#include "include/utils/shader_utils.h"
#include "include/utils/shader_glsl.h"
#include "include/utils/matrix_utils.h"


WzmEglThread *pEglThread = nullptr;
ANativeWindow *pNativeWindow = nullptr;


GLuint programId = 0;
GLint vPosition = 0;
GLint fPosition = 0;
GLint samplerId = 0;
GLint uMatrix = 0;
GLuint textureId = 0;

int width = 0;
int height = 0;

int iwidth = 0;
int iheight = 0;

void *pixelsArr = nullptr;

float matrixArr[16];


template <class T>
int getArrSize(T& arr){
    return sizeof(arr) / sizeof(arr[0]);
}

void onSurfaceCreateCall(void *) {
    LOGD("onSurfaceCreateCall");
    // 测试opengl初始化 shader
    programId = createProgram(vertexMatrix, fragmentSurfaceSource);
    LOGD("onSurfaceCreateCall createProgram programId = %d", programId);

    // 获取参数
    vPosition = glGetAttribLocation(programId, "vPosition");  //顶点的坐标
    fPosition = glGetAttribLocation(programId, "fPosition");  //这个纹理的坐标
    samplerId = glGetUniformLocation(programId, "sTexture");  //2d纹理
    uMatrix = glGetUniformLocation(programId,"uMatrix");

    // 创建一个原始的矩阵
    initMatrix(matrixArr);
    // 给他旋转一些角度
    //rotateMatrix(90,matrixArr);
    // 缩放
    //scaleMatrix(1.5,matrixArr);
    // 平移
    //translateMatrix(0.5,0,matrixArr);
    // 修改投影矩阵
    //orthoM(-1,1,1,-1,matrixArr);

    // 创建一个texture，并且赋值到 textureId
    glGenTextures(1, &textureId);
    // 绑定纹理
    glBindTexture(GL_TEXTURE_2D, textureId);

    // 设置环绕方式
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
    // 设置过滤方式
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

    //设置图片，加载资源 - 纹理资源
    if (pixelsArr != nullptr) {
        // GLenum target,      目标
        // GLint level,        层级?
        // GLint internalformat,  格式
        // GLsizei width, GLsizei height,   宽,高
        // GLint border,
        // GLenum format,
        // GLenum type,
        // const void *pixels
        glTexImage2D(GL_TEXTURE_2D,
                     0,
                     GL_RGBA,
                     iwidth,
                     iheight,
                     0,
                     GL_RGBA,
                     GL_UNSIGNED_BYTE,
                     pixelsArr);
        LOGD("onSurfaceCreateCall glTexImage2D pixelsArr");
    }

    // 设置完毕之后,解除绑定纹理
    glBindTexture(GL_TEXTURE_2D, 0);

}

void onSurfaceChangeCall(int width, int height, void *ctx) {
    //WzmEglThread *wzmEglThread = static_cast<WzmEglThread *>(ctx);
    glViewport(0, 0, width, height);
    LOGD("onSurfaceChangeCall width = %d ,height = %d", width, height);
    float screenR = 1.0 * width / height;
    float sourceR = 1.0 * iwidth / iheight;

    // 计算他们的比值
    if (screenR > sourceR) {
        // 图片的宽度的缩放
        float r = width / (1.0 * height / iheight * iwidth);
        LOGD("onSurfaceChangeCall 图片的高度缩放 r = %f", r);
        orthoM(-r,1,r,-1,matrixArr);
    }else{
        // 图片的高度缩放
        float r = height / (1.0 * width / iwidth * iheight);
        LOGD("onSurfaceChangeCall 图片的高度缩放 r = %f", r);
        orthoM(-1,r,1,-r,matrixArr);
    }
}

void onSurfaceDrawCall(void *ctx) {
    LOGD("onSurfaceDrawCall");
    auto *wzmEglThread = static_cast<WzmEglThread *>(ctx);
    glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    glClear(GL_COLOR_BUFFER_BIT);

    // 使用程序
    glUseProgram(programId);

    // 启用矩阵
    // count 表示要传递几个矩阵过去.
    // GL_FALSE 表示不需要交换行 与 列
    glUniformMatrix4fv(uMatrix,1,GL_FALSE, matrixArr);

    // 激活这个samplerId
    glActiveTexture(GL_TEXTURE5);
    glUniform1i(samplerId, 5);

    // 绑定textureID
    glBindTexture(GL_TEXTURE_2D, textureId);

    // 设置顶点数组可用 - 顶点坐标
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
            VERTEX_ARR
    );


    // 设置数组可用 - 给纹理坐标
    glEnableVertexAttribArray(fPosition);
    glVertexAttribPointer(
            fPosition,
            2,
            GL_FLOAT,
            false,
            8,
            TEXTURE_TURN);


    // 绘制这个三角形,共有3个点
    //glDrawArrays(GL_TRIANGLES,0,6);
    glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);


    // 解除绑定textureID
    glBindTexture(GL_TEXTURE_2D, 0);
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
    jbyte *data = env->GetByteArrayElements(image_data, nullptr);
    int length = env->GetArrayLength(image_data);
    LOGD("setImageData length = %d", length);
    // 开辟数据内存
    pixelsArr = malloc(length);
    memcpy(pixelsArr, data, length);

    // 设置 w, h
    iwidth = jwidth;
    iheight = jheight;

    // 回收空间 - 使用之前拷贝的数据即可
    env->ReleaseByteArrayElements(image_data, data, 0);
}