package com.wangzhumo.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 3/30/21  7:31 PM
 *
 * 第三个
 */
public class ThirdlyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thirdly);
    }


    private int count;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        count++;

        if (count >= 3){
            try {
                Thread.sleep(5001);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return super.onTouchEvent(event);
    }
}