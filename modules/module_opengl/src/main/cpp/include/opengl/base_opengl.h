//
// Created by wangzhumo on 2020/8/28.
//
// 作为使用opengl的基类，定义必须的参数
//

#ifndef COLLECTIONS_BASE_OPENGL_H
#define COLLECTIONS_BASE_OPENGL_H


#include <GLES2/gl2.h>
#include <cstring>


class  BaseOpenGl{
public:
    // surface的尺寸
    int surfaceWidth;
    int surfaceHeight;

    // vertex 以及 fragment 着色器的源码
    char *vertexSource;
    char *fragmentSource;

    // 顶点以及片元的顶点坐标
    float *vertexArr;
    float *surfaceArr;

    // 一些glsl中的变量
    GLuint programId;

public:
    BaseOpenGl();
    ~BaseOpenGl();

    // 需要实现一个onCreate
    virtual void onSurfaceCreate();

    // onChange
    virtual void onSurfaceChange(int width,int height);

    // 开始绘制
    virtual void onDraw();
};


#endif //COLLECTIONS_BASE_OPENGL_H
