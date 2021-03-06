//
// Created by wangzhumo on 2020/8/28.
//
// 作为使用opengl的基类，定义必须的参数,以及固定的渲染流程方法
// 内部保存了program,shader,source,屏幕尺寸,渲染的数据等等
//
#pragma once
#ifndef COLLECTIONS_BASE_OPENGL_H
#define COLLECTIONS_BASE_OPENGL_H


#include <cstdlib>
#include <cstring>
#include <GLES2/gl2.h>
#include "../utils/matrix_utils.h"
#include "../utils/shader_utils.h"
#include "../utils/android_log_utils.h"

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
    GLuint vertexShaderId;
    GLuint fragmentShaderId;


public:
    BaseOpenGl();
    ~BaseOpenGl();

    // 需要实现一个onCreate
    virtual void onSurfaceCreate();

    // onChange
    virtual void onSurfaceChange(int width,int height);

    // 开始绘制
    virtual void onSurfaceDraw();

    // 销毁GL资源
    virtual void onRelease();

    // 设置图片的像素数据
    virtual void setPixelsData(int width, int height, void *pixArr);

    // 销毁图片等资源数据
    virtual void onDestroyResource();

    // 设置YUV的数据源
    virtual void updateYuvData(void *y,void *u,void *v,int width,int height);
};


#endif //COLLECTIONS_BASE_OPENGL_H
