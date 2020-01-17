#version 300 es
//使用OES纹理扩展
#extension GL_OES_EGL_image_external_essl3 : require

//精度
precision mediump float;

//加载流数据(摄像头数据)
uniform samplerExternalOES  sTexture;

//纹理的位置,接收于vertex_shader
in vec2 v_texPo;

//最终告诉OpenGL要画的顶点颜色
out vec4 vFragColor;

void main() {
    vFragColor = texture(sTexture, v_texPo);
}
