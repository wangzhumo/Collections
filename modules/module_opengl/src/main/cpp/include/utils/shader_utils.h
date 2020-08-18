//
// Created by wangzhumo on 8/18/20.
//

#ifndef COLLECTIONS_SHADER_UTILS_H
#define COLLECTIONS_SHADER_UTILS_H

#include <GLES2/gl2.h>

// 加载shader的源代码
static int loadShader(int shaderType, const char *source){
    int shaderId = glCreateShader(shaderType);
    glShaderSource(shaderId,1,&source,0);
    glCompileShader(shaderId);
}


#endif //COLLECTIONS_SHADER_UTILS_H
