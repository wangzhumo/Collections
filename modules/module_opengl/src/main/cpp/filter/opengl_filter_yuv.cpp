//
// Created by wangzhumo on 2020/9/20.
//

#include "../include/filter/opengl_filter_yuv.h"

OpenGLFilterYuv::OpenGLFilterYuv() {

}

OpenGLFilterYuv::~OpenGLFilterYuv() {

}

void OpenGLFilterYuv::onSurfaceCreate() {
    pBaseVertexSource = "attribute vec4 v_Position;\n"
                        "attribute vec2 f_Position;\n"
                        "varying vec2 ft_Position;\n"
                        "uniform mat4 u_Matrix;\n"
                        "void main() {\n"
                        "    ft_Position = f_Position;\n"
                        "    gl_Position = v_Position * u_Matrix;\n"
                        "}";

    pBaseFragmentSource = "precision mediump float;\n"
               "varying vec2 ft_Position;\n"
               "uniform sampler2D sampler_y;\n"
               "uniform sampler2D sampler_u;\n"
               "uniform sampler2D sampler_v;\n"
               "void main() {\n"
               "   float y,u,v;\n"
               "   y = texture2D(sampler_y,ft_Position).r;\n"
               "   u = texture2D(sampler_u,ft_Position).r - 0.5;\n"
               "   v = texture2D(sampler_v,ft_Position).r - 0.5;\n"
               "\n"
               "   vec3 rgb;\n"
               "   rgb.r = y + 1.403 * v;\n"
               "   rgb.g = y - 0.344 * u - 0.714 * v;\n"
               "   rgb.b = y + 1.770 * u;\n"
               "\n"
               "   gl_FragColor = vec4(rgb,1);\n"
               "}";


    baseProgramId = loadShader2Program(pBaseVertexSource,pBaseFragmentSource,&vertexShaderId,&fragmentShaderId);
    LOGD("OpenGLCPP  OpenGLFilterYuv  programId = %d, shaderId = &d , fragmentId = &d",baseProgramId,vertexShaderId,fragmentShaderId);

    vPosition = glGetAttribLocation(baseProgramId, "v_Position");//顶点坐标
    fPosition = glGetAttribLocation(baseProgramId, "f_Position");//纹理坐标
    sampler_y = glGetUniformLocation(baseProgramId, "sampler_y");
    sampler_u = glGetUniformLocation(baseProgramId, "sampler_u");
    sampler_v = glGetUniformLocation(baseProgramId, "sampler_v");
    uMatrix = glGetUniformLocation(baseProgramId, "u_Matrix");

    glGenTextures(3, textureIds);

    for(int i = 0; i < 3; i++)
    {
        glBindTexture(GL_TEXTURE_2D, textureIds[i]);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER,GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER,GL_LINEAR);
        glBindTexture(GL_TEXTURE_2D, 0);
    }
}

void OpenGLFilterYuv::onSurfaceChange(int width, int height) {
    baseSurfaceWidth = width;
    baseSurfaceHeight = height;

    glViewport(0,0,baseSurfaceWidth,baseSurfaceHeight);

    setMatrix(baseSurfaceWidth,baseSurfaceHeight);
}

void OpenGLFilterYuv::onSurfaceDraw() {
    glClearColor(0.0,0.0,0.0,1.0);
    glClear(GL_COLOR_BUFFER_BIT);

    // 使用program
    glUseProgram(baseProgramId);

    // 启用matrix
    glUniformMatrix4fv(uMatrix,1,GL_FALSE,matrixArr);

    glEnableVertexAttribArray(vPosition);
    glVertexAttribPointer(vPosition, 2, GL_FLOAT, false, 8, pBaseVertexArr);
    glEnableVertexAttribArray(fPosition);
    glVertexAttribPointer(fPosition, 2, GL_FLOAT, false, 8, pBaseSurfaceArr);

    // 开始渲染纹理.
    if(yuvWidth > 0 && yuvHeight >0)
    {
        if(pDataY != NULL)
        {
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, textureIds[0]);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_LUMINANCE, yuvWidth, yuvHeight, 0, GL_LUMINANCE, GL_UNSIGNED_BYTE, pDataY);
            glUniform1i(sampler_y, 0);
        }
        if(pDataU != NULL)
        {
            glActiveTexture(GL_TEXTURE1);
            glBindTexture(GL_TEXTURE_2D, textureIds[1]);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_LUMINANCE, yuvWidth / 2, yuvHeight / 2, 0, GL_LUMINANCE, GL_UNSIGNED_BYTE, pDataU);
            glUniform1i(sampler_u, 1);
        }

        if(pDataV != NULL)
        {
            glActiveTexture(GL_TEXTURE2);
            glBindTexture(GL_TEXTURE_2D, textureIds[2]);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_LUMINANCE, yuvWidth / 2, yuvHeight / 2, 0, GL_LUMINANCE, GL_UNSIGNED_BYTE, pDataV);
            glUniform1i(sampler_v, 2);
        }
        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
        glBindTexture(GL_TEXTURE_2D, 0);
    }

}

void OpenGLFilterYuv::onRelease() {
    glDeleteTextures(3, textureIds);
    glDetachShader(baseProgramId, vertexShaderId);
    glDetachShader(baseProgramId, fragmentShaderId);
    glDeleteShader(vertexShaderId);
    glDeleteShader(fragmentShaderId);
    glDeleteProgram(baseProgramId);
}


void OpenGLFilterYuv::onDestroyResource() {
    yuvHeight = 0;
    yuvWidth = 0;

    if(pDataY != NULL)
    {
        free(pDataY);
        pDataY = NULL;
    }
    if(pDataU != NULL)
    {
        free(pDataU);
        pDataU = NULL;
    }
    if(pDataV != NULL)
    {
        free(pDataV);
        pDataV = NULL;
    }
}

void OpenGLFilterYuv::setMatrix(int width, int height) {
    initMatrix(matrixArr);

    if(yuvWidth > 0 && yuvHeight > 0)
    {
        float screen_r = 1.0 * width / height;
        float picture_r = 1.0 * yuvWidth / yuvHeight;

        if(screen_r > picture_r) //图片宽度缩放
        {

            float r = width / (1.0 * height / yuvHeight * yuvWidth);
            orthoM(-r, r, -1, 1, matrixArr);

        } else{//图片高度缩放

            float r = height / (1.0 * width / yuvWidth * yuvHeight);
            orthoM(-1, 1, -r, r, matrixArr);
        }
    }

}

void OpenGLFilterYuv::updateYuvData(void *y, void *u, void *v, int width, int height) {

    if(width > 0 && height > 0)
    {
        if(yuvWidth != width || yuvHeight != height)
        {
            yuvWidth = width;
            yuvHeight = height;

            if(pDataY != NULL)
            {
                free(pDataY);
                pDataY = NULL;
            }
            if(pDataU != NULL)
            {
                free(pDataU);
                pDataU = NULL;
            }
            if(pDataV != NULL)
            {
                free(pDataV);
                pDataV = NULL;
            }
            pDataY = malloc(yuvWidth * yuvHeight);
            pDataU = malloc(yuvWidth * yuvHeight / 4);
            pDataV = malloc(yuvWidth * yuvHeight / 4);
            setMatrix(baseSurfaceWidth, baseSurfaceHeight);
        }
        memcpy(pDataY, y, yuvWidth * yuvHeight);
        memcpy(pDataU, u, yuvWidth * yuvHeight / 4);
        memcpy(pDataV, v, yuvWidth * yuvHeight / 4);

    }


}
