//
// Created by wangzhumo on 2020/8/28.
//
// 作为使用opengl的基类，定义必须的参数
//

#ifndef COLLECTIONS_BASE_OPENGL_H
#define COLLECTIONS_BASE_OPENGL_H


#include <GLES2/gl2.h>
#include <cstring>
#include "../utils/matrix_utils.h"
#include "../utils/shader_utils.h"
#include "../log/android_log_utils.h"

class  BaseOpenGl{
public:
    // surface的尺寸
    int baseSurfaceWidth;
    int baseSurfaceHeight;

    // vertex 以及 fragment 着色器的源码
    const char *pBaseVertexSource;
    const char *pBaseFragmentSource;

    // 顶点以及片元的顶点坐标
    float *pBaseVertexArr;
    float *pBaseSurfaceArr;

    // 一些glsl中的变量
    GLuint baseProgramId;

public:
    BaseOpenGl();
    ~BaseOpenGl();

    // 需要实现一个onCreate
    virtual void onSurfaceCreate();

    // onChange
    virtual void onSurfaceChange(int width,int height);

    // 开始绘制
    virtual void onSurfaceDraw();

    // 销毁资源
    virtual void onRelease();
};


#endif //COLLECTIONS_BASE_OPENGL_H
