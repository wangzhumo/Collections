package com.wangzhumo.app.module.opengl.cpp.opengl;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CppSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private NativeOpenGl nativeOpenGl;

    public CppSurfaceView(Context context) {
        super(context);
    }

    public CppSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CppSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (nativeOpenGl != null){
            nativeOpenGl.surfaceCreate(holder.getSurface());
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    public void setNativeOpenGl(NativeOpenGl nativeOpenGl) {
        this.nativeOpenGl = nativeOpenGl;
    }
}
