package com.wangzhumo.app.module.opengl.image;

import android.content.Context;
import android.util.AttributeSet;

import com.wangzhumo.app.module.opengl.customgl.CustomGLSurfaceView;

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2020-01-08  17:55
 */
public class GLImageTextureView extends CustomGLSurfaceView {

    public GLImageTextureView(Context context) {
        this(context,null);
    }

    public GLImageTextureView(Context context, AttributeSet attrs) {
        this(context, attrs , 0);
    }

    public GLImageTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setRenderer(new GLImageRenderer(getContext()));
    }



}
