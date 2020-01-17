#version 300 es

//顶点位置
in vec4 a_Position;

//纹理位置
in vec2 a_TexCoord;

//纹理位置  与fragment_shader交互
out vec2 v_texPo;

void main() {
    v_texPo = a_TexCoord;
    gl_Position = a_Position;
}

