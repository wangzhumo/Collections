package com.wangzhumo.app.module.opengl.cpp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.wangzhumo.app.base.IRoute;
import com.wangzhumo.app.module.opengl.R;
import com.wangzhumo.app.module.opengl.cpp.opengl.CppSurfaceView;
import com.wangzhumo.app.module.opengl.cpp.opengl.NativeOpenGl;

@Route(path = IRoute.CPPGLES.CPP_GLES)
public class OpenGLCppActivity extends AppCompatActivity {

    private CppSurfaceView surfaceView;
    private NativeOpenGl nativeOpenGl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opengl_cpp);

        // find view
        surfaceView = findViewById(R.id.surfaceView);

        //native
        nativeOpenGl = new NativeOpenGl();
        surfaceView.setNativeOpenGl(nativeOpenGl);
    }


}