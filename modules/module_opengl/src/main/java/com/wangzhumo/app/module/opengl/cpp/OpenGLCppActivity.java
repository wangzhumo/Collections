package com.wangzhumo.app.module.opengl.cpp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.wangzhumo.app.base.IRoute;
import com.wangzhumo.app.module.opengl.R;
import com.wangzhumo.app.module.opengl.cpp.opengl.CppSurfaceView;
import com.wangzhumo.app.module.opengl.cpp.opengl.NativeOpenGl;
import com.wangzhumo.app.origin.BaseActivity;

@Route(path = IRoute.CPPGLES.CPP_GLES)
public class OpenGLCppActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_opengl_cpp;
    }

    @Override
    protected void initViews(@Nullable Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        // find view
        CppSurfaceView surfaceView = findViewById(R.id.surfaceView);

        //native
        NativeOpenGl nativeOpenGl = new NativeOpenGl();
        surfaceView.setNativeOpenGl(nativeOpenGl);
    }
}