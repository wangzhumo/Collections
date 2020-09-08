//
// Created by wangzhumo on 8/18/20.
//
#pragma once
#ifndef COLLECTIONS_SHADER_UTILS_H
#define COLLECTIONS_SHADER_UTILS_H

#include <GLES2/gl2.h>
#include "android_log_utils.h"


static void checkGlError(const char* operation){

    for (GLint error = glGetError(); error; error = glGetError()){
        LOGE("after %s() glError (0x%x)", operation, error);
    }
}


// 加载shader的源代码
static GLuint loadShader(int shaderType, const char *source){
    GLuint shaderId = glCreateShader(shaderType);
    if (shaderId == 0) {
        LOGE("loadShader shaderId = %d",shaderId);
        return 0;
    }
    glShaderSource(shaderId,1,&source,0);
    glCompileShader(shaderId);
    return shaderId;
}


// 创建一个program
// 这里的指针，为了让外部自己管理这个program的生命周期
static GLuint loadShader2Program(const char *vertexSource, const char *fragmentSource, GLuint *vertexShader, GLuint *fragmentShader){
    GLuint vertex_Shader = loadShader(GL_VERTEX_SHADER,vertexSource);
    LOGD("loadShader2Program loadShader vertexShader = %d",vertex_Shader);
    GLuint fragment_Shader = loadShader(GL_FRAGMENT_SHADER,fragmentSource);
    LOGD("loadShader2Program loadShader fragmentShader = %d",fragment_Shader);
    // 创建program
    GLuint program_Id = glCreateProgram();
    LOGE("loadShader2Program baseProgramId = %d",program_Id);
    if (program_Id == 0) {
        LOGE("loadShader2Program baseProgramId = %d",program_Id);
        return false;
    }

    // 加载 vertex 以及 fragment
    glAttachShader(program_Id,vertex_Shader);
    glAttachShader(program_Id,fragment_Shader);
    // 链接
    glLinkProgram(program_Id);

    vertexShader = &vertex_Shader;
    fragmentShader = &fragment_Shader;
    return program_Id;
}



#endif //COLLECTIONS_SHADER_UTILS_H
