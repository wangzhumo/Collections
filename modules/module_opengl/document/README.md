- call to OpenGL ES API with no current context
```c++

extern "C"
JNIEXPORT void JNICALL
Java_com_wangzhumo_app_module_opengl_cpp_opengl_NativeOpenGl_surfaceCreate(
        JNIEnv *env, jobject thiz, jobject surface) {
    ...

    // 测试opengl初始化 shader
    int baseProgramId = loadShader2Program(vertexSource,fragmentSource);
    LOGD("loadShader2Program baseProgramId = %d",baseProgramId);
}

```
在create中去加载,此时的EGL环境还没有建立,加载失败.

```c++

void onSurfaceCreateCall(void *) {
    LOGD("onSurfaceCreateCall");

    // 测试opengl初始化 shader
    int baseProgramId = loadShader2Program(vertexSource,fragmentSource);
    LOGD("loadShader2Program baseProgramId = %d",baseProgramId);
}

```


- 使用STRIP的方式绘制三角形

```glsl

float vertexArrStrip[] = {
        -1, 1,
        1, -1,
        -1, -1,
        1, 1,
};


// 此时的三角形是有去缺口的, 1,-1   -1,-1 中间的两个点是公用边
// 这样不能组成一个四边形,而会有缺口


float vertexArrStrip[] = {
        -1, -1,
        1, -1,
        -1, 1,
        1, 1,
};
//这样，公用的边就是  -1,1   1,-1 完美


```

- 使用矩阵时不能显示纹理图片

```c++


// 创建一个原始的矩阵
initMatrix(matrixArr);
// 给他旋转一些角度
// rotateMatrix(-90,matrixArr);


// 搞一个旋转矩阵
// 传入一个角度，生成一个旋转矩阵
static void rotateMatrix(double angle,float *matrix){
    // 先给他reset
    //initMatrix(matrix);
    // 因为计算的公式需要的是弧度，我们把这个角度转为弧度
    angle = angle * (M_PI / 180.0f);
    // cos   -sin 0   0
    // sin   cos  0   0
    // 0     0    1   0
    // 0     0    0   1
    // 那么就是说，要计算四个位置的值
    matrix[0] = cos(angle);
    matrix[1] = -sin(angle);
    matrix[5] = sin(angle);
    matrix[6] = cos(angle);
    // 旋转矩阵完成。
}

```

其中注释掉rotateMatrix之后就ok，可以正常显示了。
原因是`matrix[5]`以及`matrix[6]`赋值错误了，应该是 4,5 才对


- 矩阵作用于顶点坐标，因此它准守的是的 `顶点坐标系`


- 页面黑,只显示出刷新屏幕的颜色
  1. 可能是顶点shader 以及 片元shader出问题
  2. 可能是传入的height , width 有问题
  3. 没有释放之前的program ，textureId 等等


https://learnopengl-cn.github.io/01%20Getting%20started/07%20Transformations/#_10


https://learnopengl-cn.github.io/01%20Getting%20started/07%20Transformations/#_10

https://github.com/DomyZhang/OpenGLES_FilterDemo


- 尝试解决问题 - 准备重写一次

- Filter重新写，BUG仍然在，下一步准备重写一下Controller

- 这些天加班，没法搞了