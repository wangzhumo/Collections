#extension GL_OES_EGL_image_external: require

//精度
precision mediump float;
//一般的2d纹理
uniform samplerExternalOES sTexture;
//接收 vertexshader的变量
varying vec2 vTextureCoord;

void main() {
    //内置的变量
    gl_FragColor = texture2D(sTexture,vTextureCoord);
}
