// surface 纹理的渲染 - 片元
precision mediump float;

// uniform 用于在application中向 vertex 以及 framgent中传递数据
uniform sampler2D sTexture;

// 整一个共享数据用的 - 接受vertex中共享出来的数据
// varying 用于在vertex和fragment之间传递数据
varying vec2 ftPosition;

void main() {
    // texture2D 在 sTexture 中取出对应 ftPosition 的像素信息给 gl_FragColor
    gl_FragColor = texture2D(sTexture,ftPosition);
}
