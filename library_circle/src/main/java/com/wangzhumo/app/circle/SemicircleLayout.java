package com.wangzhumo.app.circle;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-12-16  19:35
 * <p>
 * 半圆形的菜单
 */
public class SemicircleLayout extends ConstraintLayout implements SemicircleRes, View.OnTouchListener {

    //---------------View的参数-----------------
    private SparseIntArray mAngleOffset;
    private SparseArray<ImageView> mImageArray;
    final int FLING_MIN_DISTANCE = 40;
    final int FLING_MIN_VELOCITY = 100;

    private GestureDetector mGestureDetector;
    private ColorMatrixColorFilter mGrayColorFilter;
    private OnItemChangeListener mListener;
    private int currentIndex = 2;
    //---------------View的参数-----------------


    /**
     * 是否需要改变View的位置
     */
    private boolean isNeedChangeViewPosition = true;


    public SemicircleLayout(Context context) {
        super(context);
        initViews();
    }

    public SemicircleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }


    private void initViews() {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.view_semicircle_layout, this, true);
        ImageView mSemicircleFirst = rootView.findViewById(R.id.semicircle_first);
        ImageView mSemicircleSecond = rootView.findViewById(R.id.semicircle_second);
        ImageView mSemicircleThird = rootView.findViewById(R.id.semicircle_third);
        ImageView mSemicircleFourth = rootView.findViewById(R.id.semicircle_fourth);
        ImageView mSemicircleFive = rootView.findViewById(R.id.semicircle_five);
        mImageArray = new SparseArray<>();
        mImageArray.put(0,mSemicircleFirst);
        mImageArray.put(1,mSemicircleSecond);
        mImageArray.put(2,mSemicircleThird);
        mImageArray.put(3,mSemicircleFourth);
        mImageArray.put(4,mSemicircleFive);
        //设置默认值.
        for (int i = 0; i < mImageArray.size(); i++) {
            mImageArray.get(i).setImageResource(SELECT.RES[i]);
        }

        //设置灰色
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        mGrayColorFilter = new ColorMatrixColorFilter(cm);

        //设置偏移量.
        mAngleOffset = new SparseIntArray();
        mAngleOffset.put(0,-60);
        mAngleOffset.put(1,-30);
        mAngleOffset.put(2,0);
        mAngleOffset.put(3,30);
        mAngleOffset.put(4,60);
        mGestureDetector = new GestureDetector(getContext(), new SemicircleGestureListener());
        setOnTouchListener(this);
        selectIndexSelf(currentIndex);
    }

    /**
     * 修改当前的位置.
     * @param index
     */
    private void selectIndexSelf(int index){
        selectIndex(index);
        if (mListener != null){
            mListener.onItemChange(index,null);
        }
    }



    /**
     * 暴露给外部的切换方法.
     * @param index
     */
    public void selectIndex(int index){
        //这里对每一个角度进行运算.
        int offset = 90 - mAngleOffset.get(index);
        for (int i = 0; i < mAngleOffset.size(); i++) {
            mAngleOffset.put(i, mAngleOffset.get(i) + offset);
            //调用他们自己LayoutParam改变位置.
            if (isNeedChangeViewPosition){
                changeAngle(mImageArray.get(i), mAngleOffset.get(i));
            }
            if (i == index){
                mImageArray.get(i).setColorFilter(null);
            }else{
                mImageArray.get(i).setColorFilter(mGrayColorFilter);
            }
        }
        if (isNeedChangeViewPosition){
            requestLayout();
        }
    }

    /**
     * 修改位置
     * @param view
     * @param angle
     */
    private void changeAngle(View view,int angle) {
        ConstraintLayout.LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        layoutParams.circleAngle = angle;
        view.setLayoutParams(layoutParams);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return true;
    }


    class SemicircleGestureListener extends GestureDetector.SimpleOnGestureListener{

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            //通过它判断向下,还是向上.
            if (e1.getY() - e2.getY() > FLING_MIN_DISTANCE
                    && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
                if (currentIndex + 1 < 5){
                    currentIndex = currentIndex + 1;
                    selectIndexSelf(currentIndex);
                }else{
                    return false;
                }
            } else if (e2.getY() - e1.getY() > FLING_MIN_DISTANCE
                    && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
                if (currentIndex - 1 >= 0){
                    currentIndex = currentIndex - 1;
                    selectIndexSelf(currentIndex);
                }else{
                    return false;
                }
            }else{
                return false;
            }
            //其他的可以也移动,但是没有大于 FLING_MIN_DISTANCE ,则遗弃
            return true;
        }
    }

    /**
     * 设置item变化的监听
     * @param listener
     */
    public void setOnItemChangeListener(OnItemChangeListener listener) {
        this.mListener = listener;
    }

}
