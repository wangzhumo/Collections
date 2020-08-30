//
// Created by 王诛魔 on 2020/8/28.
//

#include "../include/opengl/opengl_filter_normal.h"


// 使用父类的构造即可,不需要实现.
OpenGLFilterNormal::OpenGLFilterNormal(){}

OpenGLFilterNormal::~OpenGLFilterNormal(){}



void OpenGLFilterNormal::onSurfaceCreate() {
    // 为base中声明的数据赋值
    pBaseVertexSource = VERTEX_MATRIX_SOURCE;
    LOGD("OpenGLFilterNormal pBaseVertexSource = %s",pBaseVertexSource);
}

void OpenGLFilterNormal::onSurfaceChange(int width, int height) {
}

void OpenGLFilterNormal::onSurfaceDraw() {
}


