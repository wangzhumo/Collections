//
// Created by 王诛魔 on 2020/8/28.
//

#include "../include/opengl/opengl_filter_normal.h"


// 使用父类的构造即可,不需要实现.
OpenGLFilterNormal::OpenGLFilterNormal(){}

OpenGLFilterNormal::~OpenGLFilterNormal(){}



void OpenGLFilterNormal::onSurfaceCreate() {
    LOGD("OpenGLFilterNormal onSurfaceCreate");
    // 为base中声明的数据赋值
    pBaseVertexSource = GLSLConst::VERTEX_MATRIX_SOURCE;
    LOGD("OpenGLFilterNormal onSurfaceCreate OpenGLFilterNormal pBaseVertexSource = %s",pBaseVertexSource);
    pBaseFragmentSource = GLSLConst::FRAGMENT_SURFACE_SOURCE;
    LOGD("OpenGLFilterNormal onSurfaceCreate  pBaseFragmentSource = %s",pBaseFragmentSource);

    // 测试opengl初始化 shader
    baseProgramId = createProgram(pBaseVertexSource, pBaseFragmentSource);
    LOGD("OpenGLFilterNormal onSurfaceCreate createProgram programId = %d", baseProgramId);

    // 获取参数
    vPosition = glGetAttribLocation(baseProgramId, "vPosition");  //顶点的坐标
    fPosition = glGetAttribLocation(baseProgramId, "fPosition");  //这个纹理的坐标
    samplerId = glGetUniformLocation(baseProgramId, "sTexture");  //2d纹理
    uMatrix = glGetUniformLocation(baseProgramId,"uMatrix");  //矩阵

    // 创建一个原始的矩阵
    initMatrix(matrixArr);
    // 给他旋转一些角度
    //rotateMatrix(90,matrixArr);
    // 缩放
    //scaleMatrix(1.5,matrixArr);
    // 平移
    //translateMatrix(0.5,0,matrixArr);
    // 修改投影矩阵
    //orthoM(-1,1,1,-1,matrixArr);

    // 创建一个texture，并且赋值到 textureId
    glGenTextures(1, &textureId);
    // 绑定纹理
    glBindTexture(GL_TEXTURE_2D, textureId);

    // 设置环绕方式
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
    // 设置过滤方式
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

    //设置图片，加载资源 - 纹理资源
    if (pPixelsArr != nullptr) {
        // GLenum target,      目标
        // GLint level,        层级?
        // GLint internalformat,  格式
        // GLsizei width, GLsizei height,   宽,高
        // GLint border,
        // GLenum format,
        // GLenum type,
        // const void *pixels
        glTexImage2D(GL_TEXTURE_2D,
                     0,
                     GL_RGBA,
                     pixWidth,
                     pixHeight,
                     0,
                     GL_RGBA,
                     GL_UNSIGNED_BYTE,
                     pPixelsArr);
        LOGD("OpenGLFilterNormal onSurfaceCreate glTexImage2D pixelsArr");
    }

    // 设置完毕之后,解除绑定纹理
    glBindTexture(GL_TEXTURE_2D, 0);
}

void OpenGLFilterNormal::onSurfaceChange(int width, int height) {
}

void OpenGLFilterNormal::onSurfaceDraw() {
}


