#version 120
attribute vec4 vPosition;
attribute vec2 fPosition;

varying vec2 ftPosition;

uniform mat4 uMatrix;

void main() {
    ft_Position = ftPosition;
    gl_Position = vPosition * uMatrix;
}
