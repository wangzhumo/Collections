package com.wangzhumo.app.module.opengl.cpp;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.wangzhumo.app.base.IRoute;
import com.wangzhumo.app.module.opengl.R;
import com.wangzhumo.app.module.opengl.cpp.opengl.CppSurfaceView;
import com.wangzhumo.app.module.opengl.cpp.opengl.NativeOpenGl;
import com.wangzhumo.app.origin.BaseActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@Route(path = IRoute.CPPGLES.CPP_YUV)
public class CppYuvActivity extends BaseActivity {

    private CppSurfaceView surfaceView;
    private Button btStart;
    NativeOpenGl nativeOpenGl;

    private boolean isExitPlay = true;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_cpp_yuv;
    }

    @Override
    protected void initViews(@Nullable Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        surfaceView = findViewById(R.id.surfaceView);
        btStart = findViewById(R.id.bt_yuv_play);

        // 创建 NativeOpenGl
        nativeOpenGl = new NativeOpenGl();
        surfaceView.setNativeOpenGl(nativeOpenGl);

        // 开始播放
        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExitPlay) {
                    File voidFile = new File(getExternalFilesDir(""), "out.yuv");
                    play(voidFile);
                } else {
                    stop();
                }

            }
        });
    }


    int width = 1920;
    int height = 1080;
    int dataSize = 1920 * 1080;
    FileInputStream fis;

    public void play(File file) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    isExitPlay = false;
                    fis = new FileInputStream(file);
                    byte[] dataY = new byte[dataSize];
                    byte[] dataU = new byte[dataSize / 4];
                    byte[] dataV = new byte[dataSize / 4];

                    while (true) {
                        if (isExitPlay) {
                            fis.close();
                            break;
                        }

                        // 开始不停的读取数据。
                        int ySize = fis.read(dataY);
                        int uSize = fis.read(dataU);
                        int vSize = fis.read(dataV);

                        // 设置到jni中
                        if (ySize > 0 || uSize > 0 && vSize > 0) {
                            nativeOpenGl.updateYuvData(dataY, dataU, dataV, width, height);
                            Thread.sleep(40);
                        }
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    public void stop() {
        isExitPlay = true;
    }


}