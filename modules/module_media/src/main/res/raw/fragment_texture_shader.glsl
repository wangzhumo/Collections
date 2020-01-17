#version 300 es

//使用OES纹理扩展
#extension GL_OES_EGL_image_external_essl3 : require

//精度声明
precision mediump float;

//OES纹理，接收相机的纹理作为输入
uniform samplerExternalOES uTextureSampler;

//顶点着色器输入经图元装配和栅格化后的纹理坐标序列,Vertex Shader中传递过来的 aTextureCoord
in vec2 vTextureCoord;

//最终告诉OpenGL要画的顶点颜色
out vec4 vFragColor;

void main()
{
  vFragColor = texture(uTextureSampler, vTextureCoord);
}
