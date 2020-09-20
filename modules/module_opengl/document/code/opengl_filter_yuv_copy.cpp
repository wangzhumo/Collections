////
//// Created by wangzhumo on 9/8/20.
////
//
//#include <jni.h>
//#include "../include/filter/opengl_filter_yuv_copy.h"
//
//OpenGLFilterYuv::OpenGLFilterYuv() = default;
//
//OpenGLFilterYuv::~OpenGLFilterYuv() = default;
//
//void OpenGLFilterYuv::onSurfaceCreate() {
//    LOGD("OpenGL OpenGLFilterYuv onSurfaceCreate");
//    // 为base中声明的数据赋值
//    pBaseVertexSource = GLSLConst::VERTEX_MATRIX_SOURCE;
//    LOGD("OpenGL OpenGLFilterYuv onSurfaceCreate OpenGLFilterNormal pBaseVertexSource = %s",
//         pBaseVertexSource);
//    pBaseFragmentSource = GLSLConst::FRAGMENT_YUV_SOURCE;
//    LOGD("OpenGL OpenGLFilterYuv onSurfaceCreate  pBaseFragmentSource = %s", pBaseFragmentSource);
//
//    // 测试opengl初始化 shader
//    baseProgramId = loadShader2Program(pBaseVertexSource, pBaseFragmentSource,
//                                       &vertexShaderId, &fragmentShaderId);
//    LOGD("OpenGLCPP OpenGLFilterYuv onSurfaceCreate loadShader2Program programId = %d vertexShaderId = %d fragmentShaderId = %d",
//         baseProgramId, vertexShaderId, fragmentShaderId);
//
//    // 获取参数
//    vPosition = glGetAttribLocation(baseProgramId, "vPosition");  //顶点的坐标
//    fPosition = glGetAttribLocation(baseProgramId, "fPosition");  //这个纹理的坐标
//    uMatrix = glGetUniformLocation(baseProgramId, "uMatrix");     //矩阵
//    sampler_y = glGetUniformLocation(baseProgramId, "sTexture_y");  //2d纹理
//    sampler_u = glGetUniformLocation(baseProgramId, "sTexture_u");  //2d纹理
//    sampler_v = glGetUniformLocation(baseProgramId, "sTexture_v");  //2d纹理
//    LOGD("OpenGLCPP OpenGLFilterYuv onSurfaceCreate vPosition = %d fPosition = %d sampler_y = %d sampler_u = %d sampler_v = %d uMatrix = %d",vPosition,fPosition,sampler_y,sampler_u,sampler_v,uMatrix);
//
//    // 创建三个texture，并且赋值到 textureIds
//    glGenTextures(3, textureIds);
//
//    // 循环绑定textureId
//    for (unsigned int textureId : textureIds) {
//        // 绑定纹理
//        glBindTexture(GL_TEXTURE_2D, textureId);
//
//        // 设置环绕方式
//        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
//        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
//        // 设置过滤方式
//        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
//        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
//
//        // 设置完毕之后,解除绑定纹理
//        glBindTexture(GL_TEXTURE_2D, 0);
//    }
//}
//
//void OpenGLFilterYuv::onSurfaceChange(int width, int height) {
//    baseSurfaceWidth = width;
//    baseSurfaceHeight = height;
//    // 设置视窗大小
//    glViewport(0, 0, width, height);
//    // 设置矩阵
//    setMatrix(width, height);
//}
//
//void OpenGLFilterYuv::setMatrix(int width, int height) {
//    initMatrix(matrixArr);
//    if (yuvHeight > 0 && yuvWidth > 0){
//        float screenR = 1.0 * width / height;
//        float sourceR = 1.0 * yuvWidth / yuvHeight;
//
//        // 计算他们的比值
//        if (screenR > sourceR) {
//            // 图片的宽度的缩放
//            float r = width / (1.0 * height / yuvHeight * yuvWidth);
//            LOGD("OpenGL OpenGLFilterYuv setMatrix YUV宽度的缩放 r = %f", r);
//            orthoM(-r, 1, r, -1, matrixArr);
//        } else {
//            // 图片的高度缩放
//            float r = height / (1.0 * width / yuvWidth * yuvHeight);
//            LOGD("OpenGL OpenGLFilterYuv setMatrix YUV高度缩放 r = %f", r);
//            orthoM(-1, r, 1, -r, matrixArr);
//        }
//    }
//}
//
//
//void OpenGLFilterYuv::onSurfaceDraw() {
//    glClearColor(0.0f, 0.0f, 1.0f, 0.0f);
//    glClear(GL_COLOR_BUFFER_BIT);
//
//    // 使用程序
//    glUseProgram(baseProgramId);
//
//    // 启用矩阵
//    // count 表示要传递几个矩阵过去.
//    // GL_FALSE 表示不需要交换行 与 列
//    glUniformMatrix4fv(uMatrix, 1, GL_FALSE, matrixArr);
//
//
//
//    // 设置顶点数组可用 - 顶点坐标
//    glEnableVertexAttribArray(vPosition);
//    glVertexAttribPointer(
//            vPosition,
//            2,
//            GL_FLOAT,
//            false,
//            8,
//            pBaseVertexArr
//    );
//
//
//    // 设置数组可用 - 给纹理坐标
//    glEnableVertexAttribArray(fPosition);
//    glVertexAttribPointer(
//            fPosition,
//            2,
//            GL_FLOAT,
//            false,
//            8,
//            pBaseSurfaceArr);
//
//
//    LOGD("OpenGLCPP OpenGLFilterYuv onSurfaceDrawCall yuvHeight = %d, yuvWidth = %d",yuvHeight,yuvWidth);
//    if (yuvHeight > 0 && yuvWidth > 0) {
//        //LOGD("OpenGL OpenGLFilterYuv onSurfaceDrawCall pDataY length = %d",getArrayLen(pDataY));
//        LOGD("OpenGLCPP OpenGLFilterYuv onSurfaceDrawCall Start Y y = %p",pDataY);
//        if (pDataY != nullptr) {
//            // 激活这个samplerId - 0
//            glActiveTexture(GL_TEXTURE0);
//            LOGD("OpenGLCPP OpenGLFilterYuv onSurfaceDrawCall Start Y 1");
//            // 绑定textureID
//            glBindTexture(GL_TEXTURE_2D, textureIds[0]);
//            LOGD("OpenGLCPP OpenGLFilterYuv onSurfaceDrawCall Start Y 2");
//            // 设置纹理数据
//            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, yuvWidth, yuvHeight,
//                         0, GL_RGBA, GL_UNSIGNED_BYTE, pDataY);
//
//            LOGD("OpenGLCPP OpenGLFilterYuv onSurfaceDrawCall Start Y 3");
//            // 把sampler_y 绑定数据到 0 这个纹理上
//            glUniform1i(sampler_y, 0);
//            LOGD("OpenGLCPP OpenGLFilterYuv onSurfaceDrawCall Start Y 4");
//        }
//        LOGD("OpenGLCPP OpenGLFilterYuv onSurfaceDrawCall End Y");
//        if (pDataU != nullptr) {
//            // Y的数据量 * 1/4  =  U数据量 = 宽度 * 高度 * 1/4
//            // 1/2宽度 * 1/2高度 = U数据量
//
//            // 激活这个samplerId - 0
//            glActiveTexture(GL_TEXTURE1);
//            // 绑定textureID
//            glBindTexture(GL_TEXTURE_2D, textureIds[1]);
//            // 设置纹理数据
//            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, yuvWidth / 2, yuvHeight / 2,
//                         0, GL_RGBA, GL_UNSIGNED_BYTE, pDataU);
//            // 把sampler_y 绑定数据到 1 这个纹理上
//            glUniform1i(sampler_u, 1);
//        }
//        LOGD("OpenGLCPP OpenGLFilterYuv onSurfaceDrawCall End U");
//        //LOGD("OpenGL OpenGLFilterYuv onSurfaceDrawCall pDataV length = %d",getArrayLen(pDataV));
//        if (pDataV != nullptr) {
//            // 激活这个samplerId - 0
//            glActiveTexture(GL_TEXTURE2);
//            // 绑定textureID
//            glBindTexture(GL_TEXTURE_2D, textureIds[2]);
//            // 设置纹理数据
//            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, yuvWidth / 2, yuvHeight / 2,
//                         0, GL_RGBA, GL_UNSIGNED_BYTE, pDataV);
//            // 把sampler_y 绑定数据到 2 这个纹理上
//            glUniform1i(sampler_v, 2);
//        }
//        LOGD("OpenGLCPP OpenGLFilterYuv onSurfaceDrawCall End V");
//        // 绘制这个三角形,共有3个点
//        //glDrawArrays(GL_TRIANGLES,0,6);
//        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
//
//        // 解除绑定textureID
//        glBindTexture(GL_TEXTURE_2D, 0);
//        LOGD("OpenGLCPP OpenGLFilterYuv onSurfaceDrawCall End");
//    }
//}
//
//
//
//void OpenGLFilterYuv::updateYuvData(void *y, void *u, void *v, int width, int height) {
//    // 设置数据.
//    if (width > 0 && height > 0) {
//        int dataSize = width * height;
//        // 如果宽度、高度变化需要重新分配内存空间
//        if (yuvWidth != width || yuvHeight != height) {
//            yuvHeight = height;
//            yuvWidth = width;
//            dataSize = yuvHeight * yuvWidth;
//            // 释放之前的
//            if (pDataY != nullptr) {
//                free(pDataY);
//                pDataY = nullptr;
//            }
//            if (pDataU != nullptr) {
//                free(pDataU);
//                pDataU = nullptr;
//            }
//            if (pDataV != nullptr) {
//                free(pDataV);
//                pDataV = nullptr;
//            }
//            // 创建新的数据
//            pDataY = malloc(dataSize);
//            pDataU = malloc(dataSize / 4);
//            pDataV = malloc(dataSize / 4);
//            // 尺寸变化,需要调用一下
//            setMatrix(baseSurfaceWidth,baseSurfaceHeight);
//        }
//
//        // 赋值
//
//        memcpy(pDataY, y, dataSize);
//        memcpy(pDataU, u, dataSize / 4);
//        memcpy(pDataV, v, dataSize / 4);
//        LOGD("OpenGLCPP  OpenGLFilterYuv::updateYuvData  y = %p, u = %p, v = %p , dataSize = %d",pDataY,pDataU,pDataV,dataSize);
//    }
//}
//
//void OpenGLFilterYuv::onRelease() {
//    glDeleteTextures(3, textureIds);
//    BaseOpenGl::onRelease();
//}
//
//void OpenGLFilterYuv::onDestroyResource() {
//    // 删除数据 y,u,v
//    if (pDataY != nullptr) {
//        free(pDataY);
//        pDataY = nullptr;
//    }
//    if (pDataU != nullptr) {
//        free(pDataU);
//        pDataU = nullptr;
//    }
//    if (pDataV != nullptr) {
//        free(pDataV);
//        pDataV = nullptr;
//    }
//    yuvHeight = 0;
//    yuvWidth = 0;
//}
//
//
