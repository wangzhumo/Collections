//
// Created by 王诛魔 on 2020/8/28.
//

#ifndef COLLECTIONS_OPENGL_FILTER_NORMAL_H
#define COLLECTIONS_OPENGL_FILTER_NORMAL_H


#include "base_opengl.h"

class OpenGLFilterNormal: public BaseOpenGl{

public:
    OpenGLFilterNormal();
    ~OpenGLFilterNormal();


    // 需要实现一个onCreate
    void onSurfaceCreate();

    // onChange
    void onSurfaceChange(int width,int height);

    // 开始绘制
    void onSurfaceDraw();
};

#endif //COLLECTIONS_OPENGL_FILTER_NORMAL_H
