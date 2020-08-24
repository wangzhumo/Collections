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


#endif //COLLECTIONS_SHADER_GLSL_H
