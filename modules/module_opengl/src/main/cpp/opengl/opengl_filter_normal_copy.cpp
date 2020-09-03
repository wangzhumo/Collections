//
// Created by 王诛魔 on 2020/8/28.
//

#include "../include/opengl/opengl_filter_normal_copy.h"


// 使用父类的构造即可,不需要实现.
OpenGLFilterNormalCopy::OpenGLFilterNormalCopy(){}

OpenGLFilterNormalCopy::~OpenGLFilterNormalCopy(){}



void OpenGLFilterNormalCopy::onSurfaceCreate() {
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

    // 创建一个原始的矩阵 - 单位矩阵
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

void OpenGLFilterNormalCopy::onSurfaceChange(int width, int height) {
    glViewport(0, 0, width, height);
    LOGD("OpenGLFilterNormal onSurfaceChange width = %d ,height = %d", width, height);
    setMatrix(width, height);
}

void OpenGLFilterNormalCopy::onSurfaceDraw() {
    LOGD("OpenGLFilterNormal onSurfaceDrawCall");
    glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    glClear(GL_COLOR_BUFFER_BIT);

    // 使用程序
    glUseProgram(baseProgramId);

    // 在onSurfaceCreate中，有可能数据还没有，有可能会遗失数据
    // 所以,我们在这里重新调用一次
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

    // 启用矩阵
    // count 表示要传递几个矩阵过去.
    // GL_FALSE 表示不需要交换行 与 列
    glUniformMatrix4fv(uMatrix,1,GL_FALSE, matrixArr);

    // 激活这个samplerId
    glActiveTexture(GL_TEXTURE5);
    glUniform1i(samplerId, 5);

    // 绑定textureID
    glBindTexture(GL_TEXTURE_2D, textureId);

    // 设置顶点数组可用 - 顶点坐标
    glEnableVertexAttribArray(vPosition);
    // 设置参数
    // vPosition 参数的指向
    // 2         每一个顶点由几个数据组成，这里（1,1）两个float 2
    // GL_FLOAT  参数的数据类型
    // false     是否归一化坐标
    // 8         每一个顶点的长度   float 4 * 2 = 8
    // vertexArr 要赋值的数值数组
    glVertexAttribPointer(
            vPosition,
            2,
            GL_FLOAT,
            false,
            8,
           pBaseVertexArr
    );


    // 设置数组可用 - 给纹理坐标
    glEnableVertexAttribArray(fPosition);
    glVertexAttribPointer(
            fPosition,
            2,
            GL_FLOAT,
            false,
            8,
            pBaseSurfaceArr);


    // 绘制这个三角形,共有3个点
    //glDrawArrays(GL_TRIANGLES,0,6);
    glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);


    // 解除绑定textureID
    glBindTexture(GL_TEXTURE_2D, 0);
}

// 设置投影矩阵数据
void OpenGLFilterNormalCopy::setMatrix(int width, int height) {
    LOGD("OpenGLFilterNormal setMatrix width = %d ,height = %d", width, height);
    initMatrix(matrixArr);

    float screenR = 1.0 * width / height;
    float sourceR = 1.0 * pixWidth / pixHeight;

    // 计算他们的比值
    if (screenR > sourceR) {
        // 图片的宽度的缩放
        float r = width / (1.0 * height / pixHeight * pixWidth);
        LOGD("OpenGLFilterNormal setMatrix 图片的宽度的缩放 r = %f", r);
        orthoM(-r,1,r,-1,matrixArr);
    }else{
        // 图片的高度缩放
        float r = height / (1.0 * width / pixWidth * pixHeight);
        LOGD("OpenGLFilterNormal setMatrix 图片的高度缩放 r = %f", r);
        orthoM(-1,r,1,-r,matrixArr);
    }
}


void OpenGLFilterNormalCopy::setPixelsData(int width, int height,int len, void *pixArr) {
    pixHeight = height;
    pixWidth = width;
    pPixelsArr=pixArr;

    // 设置矩阵数据
    if (baseSurfaceHeight > 0 && baseSurfaceWidth > 0){
        setMatrix(baseSurfaceWidth,baseSurfaceHeight);
    }
}


