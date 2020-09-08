//
// Created by wangzhumo on 9/8/20.
//
#pragma once
#ifndef COLLECTIONS_OPENGL_FILTER_YUV_H
#define COLLECTIONS_OPENGL_FILTER_YUV_H

#include "../opengl/base_opengl.h"
#include "../utils/shader_glsl.h"

class OpenGLFilterYuv: public BaseOpenGl{
public:
    GLint vPosition = 0;
    GLint fPosition = 0;
    GLint sampler_y = 0;
    GLint sampler_u = 0;
    GLint sampler_v = 0;
    GLint uMatrix = 0;
    GLuint textureIds[3];


    int yuvWidth = 0;
    int yuvHeight = 0;

    void *pDataY = nullptr;
    void *pDataU = nullptr;
    void *pDataV = nullptr;

    float matrixArr[16]{};


public:
    OpenGLFilterYuv();
    ~OpenGLFilterYuv();

    // 需要实现一个onCreate
    void onSurfaceCreate();

    // onChange
    void onSurfaceChange(int width,int height);

    // 开始绘制
    void onSurfaceDraw();

    void onRelease();

    // 设置一个矩阵
    void setMatrix(int width,int height);

    void onDestroyResource();

    // 设置YUV的数据
    void updateYuvData(void *y,void *u,void *v,int width,int height);
};



#endif //COLLECTIONS_OPENGL_FILTER_YUV_H
