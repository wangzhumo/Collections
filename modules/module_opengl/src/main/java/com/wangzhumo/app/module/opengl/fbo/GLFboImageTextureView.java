package com.wangzhumo.app.module.opengl.fbo;

import android.content.Context;
import android.util.AttributeSet;

import com.wangzhumo.app.mdeia.CustomGLSurfaceView;

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2020-01-08  17:55
 */
public class GLFboImageTextureView extends CustomGLSurfaceView {

    public GLFboImageTextureView(Context context) {
        this(context,null);
    }

    public GLFboImageTextureView(Context context, AttributeSet attrs) {
        this(context, attrs , 0);
    }

    public GLFboImageTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setRenderer(new GLFboImageRenderer(getContext()));
    }
}
