#version 300 es

in vec4 a_Position;  //顶点位置

in vec2 a_TexCoord;  //纹理位置

out vec2 v_texPo;  //纹理位置  与fragment_shader交互

uniform mat4 u_Matrix;  //矩阵变换

void main() {
    v_texPo = a_TexCoord;
    gl_Position = a_Position * u_Matrix;
}
