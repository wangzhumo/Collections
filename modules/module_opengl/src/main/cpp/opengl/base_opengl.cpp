//
// Created by wangzhumo on 2020/8/28.
//

#include "../include/opengl/base_opengl.h"



// 我们可以在构造方法中，创建两个数组
BaseOpenGl::BaseOpenGl() {
    pBaseVertexArr = new float[8];
    pBaseSurfaceArr = new float[8];

    const float VERTEX_ARR[] = {
            1, 1,
            -1, 1,
            1, -1,
            -1, -1
    };

    // 翻转这个图片
    const float TEXTURE_TURN[] = {
            0.0f, 0.0f,//左下角
            1.0f, 0.0f,//右下角
            0.0f, 1.0f,//左上角
            1.0f, 1.0f,//右上角
    };

    // 拷贝数据到顶点数组
    memcpy(pBaseVertexArr, VERTEX_ARR, sizeof(VERTEX_ARR));
    memcpy(pBaseSurfaceArr, TEXTURE_TURN, sizeof(TEXTURE_TURN));
}

BaseOpenGl::~BaseOpenGl() {
    delete [] pBaseVertexArr;
    delete [] pBaseSurfaceArr;

    // 释放之前的数组
    pBaseVertexArr = nullptr;
    pBaseSurfaceArr = nullptr;
}


// --------------- 空实现，子类实现-------------
void BaseOpenGl::onSurfaceCreate() {

}

void BaseOpenGl::onSurfaceChange(int width, int height) {

}

void BaseOpenGl::onSurfaceDraw() {

}

void BaseOpenGl::onRelease() {

}