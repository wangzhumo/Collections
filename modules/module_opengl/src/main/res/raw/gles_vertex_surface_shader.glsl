#version 120
// surface 纹理的渲染

attribute vec4 vPosition;
attribute vec2 fPosition;

// 整一个共享数据用的
varying vec2 ftPosition;

void main() {
    // 把fPosition 赋值给 ftPosition
    ftPosition = fPosition;
    gl_Position = vPosition;
}
