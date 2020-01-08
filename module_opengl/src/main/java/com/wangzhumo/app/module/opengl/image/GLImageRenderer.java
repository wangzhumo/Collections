package com.wangzhumo.app.module.opengl.image;

import android.content.Context;

import com.wangzhumo.app.module.opengl.gles.IGLRenderer;

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2020-01-08  17:57
 */
public class GLImageRenderer implements IGLRenderer {


    private Context mContext;
    private float[] vertexData = {


    };



    public GLImageRenderer(Context context) {
        this.mContext = context;

    }

    @Override
    public void onSurfaceCreate() {

    }

    @Override
    public void onSurfaceChange(int width, int height) {

    }

    @Override
    public void drawFrame() {

    }
}
