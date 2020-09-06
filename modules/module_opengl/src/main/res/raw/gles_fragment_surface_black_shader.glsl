// surface 纹理的渲染 - 片元
precision mediump float;

// uniform 用于在application中向 vertex 以及 framgent中传递数据
uniform sampler2D sTexture;

// 整一个共享数据用的 - 接受vertex中共享出来的数据
// varying 用于在vertex和fragment之间传递数据
varying vec2 ftPosition;

void main() {
    // 取出颜色的数据
    lowp vec4 textureColor = texture2D(sTexture,ftPosition);
    // 计算颜色的值
    float grayColor = textureColor.r * 0.2125 + textureColor.g * 0.7154 + textureColor.b * 0.0721;
    // 设置这个颜色到 gl_FragColor
    gl_FragColor = vec4(grayColor,grayColor,grayColor,textureColor.w);
}
