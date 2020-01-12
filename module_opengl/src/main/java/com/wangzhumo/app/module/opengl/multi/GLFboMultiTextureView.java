package com.wangzhumo.app.module.opengl.multi;

import android.content.Context;
import android.util.AttributeSet;

import com.wangzhumo.app.module.opengl.customgl.CustomGLSurfaceView;
import com.wangzhumo.app.module.opengl.fbo.GLFboImageRenderer;
import com.wangzhumo.app.module.opengl.image.GLImageRenderer;
import com.wangzhumo.app.module.opengl.matrix.GLFboMatrixRenderer;

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2020-01-08  17:55
 */
public class GLFboMultiTextureView extends CustomGLSurfaceView {

    public GLFboMultiTextureView(Context context) {
        this(context,null);
    }

    public GLFboMultiTextureView(Context context, AttributeSet attrs) {
        this(context, attrs , 0);
    }

    public GLFboMultiTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


}
