//
// Created by wangzhumo on 2020/8/25.
//

#ifndef COLLECTIONS_MATRIX_UTILS_H
#define COLLECTIONS_MATRIX_UTILS_H

#include <math.h>

// 单位矩阵初始化
// 单位矩阵不会改变原来顶点坐标的数据。
static void initMatrix(float *matrix){
    // 初始化这个矩阵的数据
    for (int i = 0; i < 16; i++) {
       // 第五个1 其他的0
       if (i % 5 == 0){
           matrix[i] = 1;
       }else{
           matrix[i] = 0;
       }
    }
}

// 搞一个旋转矩阵
// 传入一个角度，生成一个旋转矩阵
static void rotateMatrix(double angle,float *matrix){
    // 先给他reset,由外部确认执行。

    // 因为计算的公式需要的是弧度，我们把这个角度转为弧度
    angle = angle * (M_PI / 180.0f);
    // cos   -sin 0   0
    // sin   cos  0   0
    // 0     0    1   0
    // 0     0    0   1
    // 那么就是说，要计算四个位置的值
    matrix[0] = cos(angle);
    matrix[1] = -sin(angle);
    matrix[4] = sin(angle);
    matrix[5] = cos(angle);
    // 旋转矩阵完成。
}



// 缩放 - 均匀缩放
static void scaleMatrix(double scale, float *matrix){
    // s1  0   0   0
    // 0   s2  0   0
    // 0   0   s3  0
    // 0   0   0   1
    // x , y ,z  z 的方向不需要动，只用修改 s1,s2
    // 那么就是说，要计算两个位置的值
    // matrix[0] = matrix[0] * scale = 1 * scale = scale
    matrix[0] = scale;
    matrix[5] = scale;
}

// 平移
static void translateMatrix(double dx,double dy, float *matrix){
    // s1  0   0   dx
    // 0   s2  0   dy
    // 0   0   s3  0
    // 0   0   0   1
    // x , y ,z  z 的方向不需要动，只用修改 s1,s2
    // 那么就是说，要计算两个位置的值
    // matrix[0] = matrix[0] * scale = 1 * scale = scale
    matrix[3] = dx;
    matrix[7] = dy;
}

#endif //COLLECTIONS_MATRIX_UTILS_H
