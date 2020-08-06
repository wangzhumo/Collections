package com.wangzhumo.app.module.opengl.cpp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.wangzhumo.app.base.IRoute;
import com.wangzhumo.app.module.opengl.R;

@Route(path = IRoute.CPPGLES.CPP_GLES)
public class OpenGLCppActivity extends AppCompatActivity {

    static {
        System.loadLibrary("opengl-cpp");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opengl_cpp);
        printAndroidLog("Hello World.");
    }


    public native int printAndroidLog(String message);


}