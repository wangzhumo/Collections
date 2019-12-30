precision mediump float;
//顶点的坐标
attribute vec4 aPosition;
//一般的2d纹理
uniform sampler2D sTexture;
//接收 vertexshader的变量
varying vec2 vTextureCoord;

void main() {
    //内置的变量
    gl_FragColor = texture2D(sTexture,vTextureCoord);
}
