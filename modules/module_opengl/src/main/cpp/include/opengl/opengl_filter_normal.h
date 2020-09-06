//
// Created by 王诛魔 on 2020/8/28.
//
#pragma once
#ifndef COLLECTIONS_OPENGL_FILTER_NORMAL_H
#define COLLECTIONS_OPENGL_FILTER_NORMAL_H


#include "base_opengl.h"
#include "../utils/shader_glsl.h"

class OpenGLFilterNormal: public BaseOpenGl{

public:
    GLint vPosition = 0;
    GLint fPosition = 0;
    GLint samplerId = 0;
    GLint uMatrix = 0;
    GLuint textureId = 0;


    int pixWidth = 0;
    int pixHeight = 0;

    void *pPixelsArr = nullptr;

    float matrixArr[16];

public:
    OpenGLFilterNormal();
    ~OpenGLFilterNormal();


    // 需要实现一个onCreate
    void onSurfaceCreate();

    // onChange
    void onSurfaceChange(int width,int height);

    // 开始绘制
    void onSurfaceDraw();

    // 设置一个矩阵
    void setMatrix(int width,int height);

    // 设置图片数据
    void setPixelsData(int width, int height, void *pixArr);

    void onRelease();
};

#endif //COLLECTIONS_OPENGL_FILTER_NORMAL_H
