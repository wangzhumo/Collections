precision mediump float;
varying vec2 ftPosition;

// YUV data
uniform lowp sampler2D sTextureY;
uniform lowp sampler2D sTextureU;
uniform lowp sampler2D sTextureV;


vec4 YuvToRgb(vec2 uv) {
    float y, u, v, r, g, b;
    y = texture2D(sTextureY, uv).r;
    u = texture2D(sTextureU, uv).r;
    v = texture2D(sTextureV, uv).r;
    u = u - 0.5;
    v = v - 0.5;
    r = y + 1.403 * v;
    g = y - 0.344 * u - 0.714 * v;
    b = y + 1.770 * u;
    return vec4(r, g, b, 1.0);
}

void main(){
   //原始采样像素的 RGBA 值
   vec4 textureColor = YuvToRgb(ftPosition);
   gl_FragColor = textureColor;
}