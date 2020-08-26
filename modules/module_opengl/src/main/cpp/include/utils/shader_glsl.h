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
// 顶点
// raw/gles_vertex_surface_shader.glsl
const char *vertexSurfaceSource = "attribute vec4 vPosition;\n"
                                  "attribute vec2 fPosition;\n"
                                  "varying vec2 ftPosition;\n"
                                  "void main() {\n"
                                  "    ftPosition = fPosition;\n"
                                  "    gl_Position = vPosition;\n"
                                  "}";


// 片元
// raw/gles_fragment_surface_shader.glsl
const char *fragmentSurfaceSource = "precision mediump float;\n"
                                    "uniform sampler2D sTexture;\n"
                                    "varying vec2 ftPosition;\n"
                                    "void main() {\n"
                                    "    gl_FragColor = texture2D(sTexture,ftPosition);\n"
                                    "}";


// 绘制一个纹理 - 加入矩阵 - 顶点
// raw/gles_vertex_shader_matrix_texture.glsl
const char *vertexMatrix = "attribute vec4 vPosition;\n"
                           "attribute vec2 fPosition;\n"
                           "varying vec2 ftPosition;\n"
                           "uniform mat4 uMatrix;\n"
                           "void main() {\n"
                               "ftPosition = fPosition;\n"
                               "gl_Position = vPosition * uMatrix;\n"
                           "}";



const float VERTEX_ARR[] = {
        1, 1,
        -1, 1,
        1, -1,
        -1, -1
};

const float TEXTURE[] = {
        0.0f, 1.0f,//左上角
        1.0f, 1.0f,//右上角
        0.0f, 0.0f,//左下角
        1.0f, 0.0f,//右下角
};

// 因为没有翻转的原因，这里是图片的上面半部分
const float TEXTURE_HALF[] = {
        0.0f, 0.5f,//左上角
        1.0f, 0.5f,//右上角
        0.0f, 0.0f,//左下角
        1.0f, 0.0f,//右下角
};

// 翻转这个图片
const float TEXTURE_TURN[] = {
        0.0f, 0.0f,//左下角
        1.0f, 0.0f,//右下角
        0.0f, 1.0f,//左上角
        1.0f, 1.0f,//右上角
};

// 翻转这个图片，而且取下面部分的图
const float TEXTURE_TURN_HALF [] = {
        0.0f, 0.5f,//左下角
        1.0f, 0.5f,//右下角
        0.0f, 1.0f,//左上角
        1.0f, 1.0f,//右上角
};

#endif //COLLECTIONS_SHADER_GLSL_H
