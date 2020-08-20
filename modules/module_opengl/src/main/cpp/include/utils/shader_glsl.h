//
// Created by wangzhumo on 2020/8/20.
//

#ifndef COLLECTIONS_SHADER_GLSL_H
#define COLLECTIONS_SHADER_GLSL_H

// 简单的三角形绘制
// 顶点着色器
const char *vertexSource = "attribute vec4 vPosition;    \n"
                           "void main(){                 \n"
                           "    gl_Position = vPosition; \n"
                           "}";

// 片着色器
const char *fragmentSource = "precision mediump float;              \n"
                             "void main(){                          \n"
                             "    gl_FragColor = vec4(0f,1f,0f,1f); \n"
                             "}";


// 绘制一个纹理
const char *vertexSurfaceSource = "attribute vec4"

#endif //COLLECTIONS_SHADER_GLSL_H
