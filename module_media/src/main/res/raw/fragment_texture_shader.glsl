#version 300 es
#extension GL_OES_EGL_image_external_essl3 : require
//使用OES纹理扩展

precision mediump float;  //精度声明
uniform samplerExternalOES uTextureSampler;  //OES纹理，接收相机的纹理作为输入
in vec2 vTextureCoord;  //顶点着色器输入经图元装配和栅格化后的纹理坐标序列
out vec4 vFragColor;

void main()
{
  vFragColor = texture(uTextureSampler, vTextureCoord);
}
