//
// Created by wangzhumo on 2020/8/25.
//

#ifndef COLLECTIONS_MATRIX_UTILS_H
#define COLLECTIONS_MATRIX_UTILS_H


// 矩阵初始化
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


#endif //COLLECTIONS_MATRIX_UTILS_H
