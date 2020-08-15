package com.wangzhumo.app.module.opengl.cpp.opengl;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.tencent.mars.xlog.CLog;


/**
 * SurfaceView自定义，主要调用native的几个方法
 */
public class CppSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = "CppSurfaceView";

    private NativeOpenGl nativeOpenGl;
    private SurfaceHolder mHolder;

    public CppSurfaceView(Context context) {
        this(context,null);
    }

    public CppSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CppSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (mHolder == null){
            Log.e(TAG,"surfaceCreated mHolder");
            mHolder = holder;
        }
        if (nativeOpenGl != null){
            Log.e(TAG,"surfaceCreated surfaceCreate");
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
        Log.e(TAG,"setNativeOpenGl");
        this.nativeOpenGl = nativeOpenGl;
    }
}
