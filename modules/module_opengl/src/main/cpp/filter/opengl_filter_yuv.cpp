//
// Created by wangzhumo on 9/8/20.
//

#include "../include/filter/opengl_filter_yuv.h"

OpenGLFilterYuv::OpenGLFilterYuv() = default;

OpenGLFilterYuv::~OpenGLFilterYuv() = default;

void OpenGLFilterYuv::onSurfaceCreate() {
    LOGD("OpenGLFilterYuv onSurfaceCreate");
    // 为base中声明的数据赋值
    pBaseVertexSource = GLSLConst::VERTEX_MATRIX_SOURCE;
    LOGD("OpenGLFilterYuv onSurfaceCreate OpenGLFilterNormal pBaseVertexSource = %s",
         pBaseVertexSource);
    pBaseFragmentSource = GLSLConst::FRAGMENT_YUV_SOURCE;
    LOGD("OpenGLFilterYuv onSurfaceCreate  pBaseFragmentSource = %s", pBaseFragmentSource);

    // 测试opengl初始化 shader
    baseProgramId = loadShader2Program(pBaseVertexSource, pBaseFragmentSource,
                                       &vertexShaderId, &fragmentShaderId);
    LOGD("OpenGLFilterNormalCopy onSurfaceCreate loadShader2Program programId = %d vertexShaderId = %d fragmentShaderId = %d",
         baseProgramId, vertexShaderId, fragmentShaderId);

    // 获取参数
    vPosition = glGetAttribLocation(baseProgramId, "vPosition");  //顶点的坐标
    fPosition = glGetAttribLocation(baseProgramId, "fPosition");  //这个纹理的坐标
    sampler_y = glGetUniformLocation(baseProgramId, "sTextureY");  //2d纹理
    sampler_u = glGetUniformLocation(baseProgramId, "sTextureU");  //2d纹理
    sampler_v = glGetUniformLocation(baseProgramId, "sTextureV");  //2d纹理
    uMatrix = glGetUniformLocation(baseProgramId, "uMatrix");  //矩阵


    // 创建一个texture，并且赋值到 textureId
    //glGenTextures(1, &textureIds);
    // 绑定纹理
    //glBindTexture(GL_TEXTURE_2D, textureId);

    // 设置环绕方式
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
    // 设置过滤方式
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

    // 设置完毕之后,解除绑定纹理
    glBindTexture(GL_TEXTURE_2D, 0);
}

void OpenGLFilterYuv::onSurfaceChange(int width, int height) {

}

void OpenGLFilterYuv::onSurfaceDraw() {

}


void OpenGLFilterYuv::updateYuvData(void *y, void *u, void *v, int width, int height) {

}

void OpenGLFilterYuv::onRelease() {

}

void OpenGLFilterYuv::onDestroyResource() {

}
