package com.wangzhumo.app.playground;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2021/5/10  5:04 下午
 *
 * 约歌成功，显示约歌成功的两个人
 */
public class DateModeSucceedView extends FrameLayout {

    private View roomView;
    private ImageView heartView;

    public DateModeSucceedView(@NonNull @NotNull Context context) {
        this(context,null);
    }

    public DateModeSucceedView(@NonNull @NotNull Context context, @Nullable  AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DateModeSucceedView(@NonNull @NotNull Context context, @Nullable  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        roomView =  LayoutInflater.from(getContext()).inflate(R.layout.speech_view_date_succeed, this, true);
        heartView = roomView.findViewById(R.id.speech_heart);
    }

    public void startMotionAnim() {
        heartView.performClick();
    }
}
