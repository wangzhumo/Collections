#version 300 es

layout (location = 0) in vec4 aPosition;       //NDC坐标点 ？？？
layout (location = 1) in vec4 aTextureCoord;   //纹理坐标点

uniform mat4 uTextureMatrix;  //纹理坐标变换矩阵
out vec2 vTextureCoord;       //纹理坐标变换后的输出

void main() {
     gl_Position  = aPosition;          //gl_position
     vTextureCoord = (uTextureMatrix * aTextureCoord).xy;  //对纹理坐标进行变换运算
}