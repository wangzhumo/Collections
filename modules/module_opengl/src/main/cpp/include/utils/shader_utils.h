//
// Created by wangzhumo on 8/18/20.
//
#pragma once
#ifndef COLLECTIONS_SHADER_UTILS_H
#define COLLECTIONS_SHADER_UTILS_H

#include <GLES2/gl2.h>
#include "../log/android_log_utils.h"



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
static GLuint createProgram(const char *vertexSource, const char *fragmentSource){
    GLuint vertexShader = loadShader(GL_VERTEX_SHADER,vertexSource);
    LOGE("createProgram loadShader vertexShader = %d",vertexShader);
    GLuint fragmentShader = loadShader(GL_FRAGMENT_SHADER,fragmentSource);
    LOGE("createProgram loadShader fragmentShader = %d",fragmentShader);
    // 创建program
    GLuint programId = glCreateProgram();
    if (programId == 0) {
        LOGE("createProgram baseProgramId = %d",programId);
        return false;
    }

    // 加载 vertex 以及 fragment
    glAttachShader(programId,vertexShader);
    glAttachShader(programId,fragmentShader);
    // 链接
    glLinkProgram(programId);
    return programId;
}



#endif //COLLECTIONS_SHADER_UTILS_H
