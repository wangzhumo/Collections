#version 300 es

//使用OES纹理扩展
#extension GL_OES_EGL_image_external_essl3 : require

//精度 为float
precision mediump float;

//纹理位置  接收于vertex_shader
in vec2 v_texPo;

uniform sampler2D  sTexture;

uniform int vChangeType;

uniform vec3 vChangeColor;

//最终告诉OpenGL要画的顶点颜色
out vec4 vFragColor;

void main() {
    vec4 nColor=texture(sTexture, v_texPo);
    if (vChangeType==1){
        //黑白滤镜
        float changeColor=nColor.r*vChangeColor.r+nColor.g*vChangeColor.g+nColor.b*vChangeColor.b;
        vFragColor=vec4(changeColor, changeColor, changeColor, nColor.a);
    } else {
        vFragColor=nColor;
    }
}