package com.wangzhumo.app.module.opengl.cpp;


import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.wangzhumo.app.base.IRoute;
import com.wangzhumo.app.module.opengl.cpp.opengl.NativeOpenGl;
import com.wangzhumo.app.module.opengl.databinding.ActivityCppYuvBinding;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@Route(path = IRoute.CPPGLES.CPP_YUV)
public class CppYuvActivity extends AppCompatActivity {

    NativeOpenGl nativeOpenGl;
    ActivityCppYuvBinding yuvBinding;

    private boolean isExitPlay = true;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        yuvBinding = ActivityCppYuvBinding.inflate(getLayoutInflater());
        setContentView(yuvBinding.getRoot());
        nativeOpenGl = new NativeOpenGl();
        yuvBinding.surfaceView.setNativeOpenGl(nativeOpenGl);
        yuvBinding.btYuvPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExitPlay) {
                    File voidFile = new File(getExternalFilesDir(""), "out.yuv");
                    if(voidFile.exists()){
                        play(voidFile);
                    }else{
                        Toast.makeText(CppYuvActivity.this, "voidFile empty", Toast.LENGTH_LONG).show();
                    }
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
                        if (ySize > 0 && uSize > 0 && vSize > 0) {
                            nativeOpenGl.updateYuvData(dataY, dataU, dataV, width, height);
                            Thread.sleep(40);
                        }else{
                            isExitPlay = true;
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