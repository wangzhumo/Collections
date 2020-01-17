#version 300 es

//顶点数据，代表了要画的每个顶点,每画一个点就会调用一次，a_Position就代表当前要画的点
layout (location = 0) in vec4 aPosition;

//纹理坐标数据，用于描述要画的纹理顶点
layout (location = 1) in vec4 aTextureCoord;

//纹理坐标变换矩阵
uniform mat4 uTextureMatrix;

//纹理坐标变换后的输出,用于将Vertex Shader中接受到的纹理顶点数据传递到Fragment Shader中
out vec2 vTextureCoord;

void main() {
     gl_Position  = aPosition;          //gl_position
     vTextureCoord = (uTextureMatrix * aTextureCoord).xy;  //对纹理坐标进行变换运算
}