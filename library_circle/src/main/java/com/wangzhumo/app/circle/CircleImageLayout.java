package com.wangzhumo.app.circle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-12-17  10:49
 * <p>
 * 圆形的Layout,固定为12份
 * 1.每一份30度
 * 2.4个圆可以定位与约束所有的Item
 * 3.通过角度与半径计算path的4个坐标
 */
public class CircleImageLayout extends ViewGroup {

    /**
     * 可以设置的半径(整个View的最大圆半径,通过整体尺寸计算)
     */
    private int mRadius;

    /**
     * ImageView的外部边缘.
     */
    private float mViewOutRadius;
    private static final float DEFAULT_OUT_RADIUS_RADIO = (float) (3.5 / 5F);

    /**
     * ImageView的内部边缘.
     */
    private float mViewInnerRadius;
    private static final float DEFAULT_INNER_RADIUS_RADIO = 2 / 5F;
    private float mAuxiliaryRadius;


    /**
     * 布局时的开始角度
     */
    private int mDefaultAngle = 15;

    /**
     * Item的图标
     */
    private List<ICircleData> mCircleDataList;

    /**
     * Item的个数
     */
    private final int mDefaultItemCount = 12;

    /**
     * 检测按下到抬起时旋转的角度
     */
    private float mTmpAngle;

    /**
     * 检测按下到抬起时使用的时间
     */
    private long mDownTime;

    /**
     * 判断是否正在自动滚动
     */
    private boolean isFling;

    /**
     * 当每秒移动角度达到该值时，认为是快速移动
     */
    private static final int FLINGABLE_VALUE = 300;

    /**
     * 如果移动角度达到该值，则屏蔽点击
     */
    private static final int NOCLICK_VALUE = 3;

    /**
     * 当每秒移动角度达到该值时，认为是快速移动
     */
    private int mFlingableValue = FLINGABLE_VALUE;

    /**
     * 渐变颜色
     */
    private int[] colors = {Color.parseColor("#80FFFFFF"), Color.parseColor("#4DFFFFFF"), Color.parseColor("#1AFFFFFF")};

    /**
     * 监听
     */
    private OnItemChangeListener mChangeListener;
    private OnImageLoader mLoader;

    private int anglePre = 360 / (mDefaultItemCount);
    private float mItemWidth, mItemHeight;
    private Path clipPath;
    private RectF rectF;
    private Paint mArcPaint;

    public CircleImageLayout(Context context) {
        this(context, null);
        super.setWillNotDraw(false);
    }

    public CircleImageLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        super.setWillNotDraw(false);
    }

    public CircleImageLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        super.setWillNotDraw(false);
        initViews();
    }


    /**
     * 初始化一些东西.
     */
    private void initViews() {
        clipPath = new Path();
        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true);
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setStrokeWidth(10);
        mArcPaint.setStrokeCap(Paint.Cap.ROUND);

        rectF = new RectF();
        mCircleDataList = new ArrayList<>();
        setFocusableInTouchMode(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int newHeight = Math.min(width, height);
        setMeasuredDimension(newHeight, newHeight);

        //计算半径
        mRadius = Math.max(getMeasuredHeight(), getMeasuredWidth());
        mViewOutRadius = mRadius * DEFAULT_OUT_RADIUS_RADIO;
        mViewInnerRadius = mRadius * DEFAULT_INNER_RADIUS_RADIO;
        mAuxiliaryRadius = mViewInnerRadius + (mViewOutRadius - mViewInnerRadius) / 2;


        float tempValue1 = mViewInnerRadius / 2F;
        float tempValue2 = mViewOutRadius / 2F;
        float tempValue3 = mRadius / 2F;

        addItems();
        Log.e("Circle", "onMeasure");
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //最重要的方法,布局已经加入进来的所有Item.
        //item的 坐标位置.
        float itemX, itemY;
        for (int index = 0; index < mDefaultItemCount; index++) {
            View itemView = getChildAt(index);
            mDefaultAngle %= 360;
            //计算中心点到圆心的距离
            float tempValue = mAuxiliaryRadius / 2F;

            //计算中心点的坐标
            itemX = (mRadius >> 1) + (int) Math.round(tempValue * Math.cos(Math.toRadians(mDefaultAngle)));
            itemY = (mRadius >> 1) + (int) Math.round(tempValue * Math.sin(Math.toRadians(mDefaultAngle)));

            itemView.layout(Float.valueOf(itemX - mItemWidth / 2F).intValue(),
                    Float.valueOf(itemY - mItemHeight / 2).intValue(),
                    Float.valueOf(itemX + mItemWidth / 2).intValue(),
                    Float.valueOf(itemY + mItemHeight / 2).intValue());
            itemView.setRotation((float) (mDefaultAngle));
            itemView.setTag(mDefaultAngle);
            mDefaultAngle += anglePre;
        }
    }


    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        int tag = (int) child.getTag() - 15;
        float tempValue1 = mViewInnerRadius / 2F;
        float tempValue2 = mViewOutRadius / 2F;
        float tempValue3 = mRadius / 2F;
        float outX, outY, fullX, fullY, innerX, innerY, innerNextX, innerNextY;
        innerX = (mRadius >> 1) + (int) Math.round(tempValue1 * Math.cos(Math.toRadians(tag)));
        innerY = (mRadius >> 1) + (int) Math.round(tempValue1 * Math.sin(Math.toRadians(tag)));

        innerNextX = (mRadius >> 1) + (int) Math.round(tempValue1 * Math.cos(Math.toRadians(tag + anglePre)));
        innerNextY = (mRadius >> 1) + (int) Math.round(tempValue1 * Math.sin(Math.toRadians(tag + anglePre)));

        outX = (mRadius >> 1) + (int) Math.round(tempValue2 * Math.cos(Math.toRadians(tag)));
        outY = (mRadius >> 1) + (int) Math.round(tempValue2 * Math.sin(Math.toRadians(tag)));

        fullX = (mRadius >> 1) + (int) Math.round(tempValue3 * Math.cos(Math.toRadians(tag)));
        fullY = (mRadius >> 1) + (int) Math.round(tempValue3 * Math.sin(Math.toRadians(tag)));


        LinearGradient shader = new LinearGradient(outX, outY, fullX, fullY, colors, null, Shader.TileMode.CLAMP);
        mArcPaint.setShader(shader);
        canvas.drawLine(outX, outY, fullX, fullY, mArcPaint);
        canvas.save();
        clipPath.reset();
        clipPath.moveTo(outX, outY);
        rectF.set((mRadius - mViewOutRadius) / 2, (mRadius - mViewOutRadius) / 2, (mRadius >> 1) + (mViewOutRadius / 2), (mRadius >> 1) + (mViewOutRadius / 2));
        clipPath.addArc(rectF, tag, anglePre);
        clipPath.lineTo(innerNextX, innerNextY);
        int conX = (mRadius >> 1) + (int) Math.round((mViewInnerRadius / 2 + inner_out_offset) * Math.cos(Math.toRadians(tag + (anglePre >> 1))));
        int conY = (mRadius >> 1) + (int) Math.round((mViewInnerRadius / 2 + inner_out_offset) * Math.sin(Math.toRadians(tag + (anglePre >> 1))));
        clipPath.quadTo(conX, conY, innerX, innerY);
        clipPath.lineTo(outX, outY);
        canvas.clipPath(clipPath);
        super.drawChild(canvas, child, drawingTime);
        canvas.restore();
        //再绘制一条线
        return true;
    }


    private float inner_out_offset;

    private void addItems() {
        removeAllViews();
        inner_out_offset = (float) ((mViewInnerRadius / 2F) - Math.ceil((mViewInnerRadius / 2F) * Math.cos(Math.toRadians(anglePre / 2F))));
        mItemWidth = (mViewOutRadius - mViewInnerRadius) / 2F + inner_out_offset + 10;
        mItemHeight = (float) Math.ceil((mViewOutRadius / 2F) * Math.sin(Math.toRadians(anglePre / 2F)) * 2) + 10;
        for (int index = 0; index < mDefaultItemCount; index++) {
            CircleItemView imageView = new CircleItemView(getContext());
            if (mLoader != null) {
                // TODO: 2019-12-17  mCircleDataList.get(index)
                mLoader.onLoader(imageView, null);
            }
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            //计算宽度,高度
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                    Float.valueOf(mItemWidth).intValue(), Float.valueOf(mItemHeight).intValue());
            addView(imageView, index, params);
        }
        //添加View完毕,可以开始布局
    }

    /**
     * 根据当前位置计算象限
     *
     * @param x
     * @param y
     * @return
     */
    private int getQuadrant(float x, float y) {
        int tmpX = (int) (x - mRadius / 2);
        int tmpY = (int) (y - mRadius / 2);
        if (tmpX >= 0) {
            return tmpY >= 0 ? 4 : 1;
        } else {
            return tmpY >= 0 ? 3 : 2;
        }
    }


    /**
     * 根据触摸的位置，计算角度
     *
     * @param xTouch
     * @param yTouch
     * @return
     */
    private float getAngle(float xTouch, float yTouch) {
        double x = xTouch - (mRadius / 2d);
        double y = yTouch - (mRadius / 2d);
        return (float) (Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        Log.e("Circle", "dispatchTouchEvent  x = " + x + "  y = " + y);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = x;
                mLastY = y;
                mDownTime = System.currentTimeMillis();
                mTmpAngle = 0;

                // 如果当前已经在快速滚动
                if (isFling) {
                    // 移除快速滚动的回调
                    removeCallbacks(mFlingRunnable);
                    isFling = false;
                    return false;
                }
                Log.e("Circle", "dispatchTouchEvent ACTION_DOWN isFling = " + isFling);
                return true;
            case MotionEvent.ACTION_MOVE:
                float start = getAngle(mLastX, mLastY);
                float end = getAngle(x, y);
                // 如果是一、四象限，则直接end-start，角度值都是正值
                if (getQuadrant(x, y) == 1 || getQuadrant(x, y) == 4) {
                    mDefaultAngle += end - start;
                    mTmpAngle += end - start;
                } else {
                    mDefaultAngle += start - end;
                    mTmpAngle += start - end;
                }
                // 重新布局
                requestLayout();
                Log.e("Circle", "dispatchTouchEvent ACTION_MOVE");
                mLastX = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_UP:
                // 计算，每秒移动的角度
                float anglePerSecond = mTmpAngle * 1000 / (System.currentTimeMillis() - mDownTime);
                Log.e("Circle", "ACTION_UP mDefaultAngle = " + mDefaultAngle);
                // 如果达到该值认为是快速移动
                if (Math.abs(anglePerSecond) > mFlingableValue && !isFling) {
                    // post一个任务，去自动滚动
                    post(mFlingRunnable = new AutoFlingRunnable(anglePerSecond));
                    return true;
                }else {
                    //否则就要自己把他移动到指定的位置

                }

                // 如果当前旋转角度超过NOCLICK_VALUE屏蔽点击
                if (Math.abs(mTmpAngle) > NOCLICK_VALUE) {
                    return true;
                }
                Log.e("Circle", "dispatchTouchEvent ACTION_UP");
                break;
        }
        return super.dispatchTouchEvent(event);
    }


    /**
     * 记录上一次的x，y坐标
     */
    private float mLastX;
    private float mLastY;

    /**
     * 自动滚动的Runnable
     */
    private AutoFlingRunnable mFlingRunnable;


    /**
     * 滚动的任务
     * {link https://sourcegraph.com/github.com/hongyangAndroid/Android-CircleMenu/-/blob/library_zhy_CircleMenu/src/com/zhy/view/CircleMenuLayout.java#L82}
     */
    private class AutoFlingRunnable implements Runnable {

        private float angelPerSecond;

        public AutoFlingRunnable(float velocity) {
            this.angelPerSecond = velocity;
        }

        public void run() {
            // 如果小于20,则停止
            if ((int) Math.abs(angelPerSecond) < 20) {
                Log.e("Circle", "AutoFlingRunnable mDefaultAngle = " + mDefaultAngle);
                //0,90,180
                isFling = false;
                return;
            }
            isFling = true;
            // 不断改变mStartAngle，让其滚动，/30为了避免滚动太快
            mDefaultAngle += (angelPerSecond / 30);
            // 逐渐减小这个值
            angelPerSecond /= 1.1F;
            postDelayed(this, 30);
            // 重新布局
            requestLayout();
        }
    }

    /**
     * 设置Item的资源
     *
     * @param list list.
     */
    public void setCircleData(List list) {
        if (list == null || list.isEmpty()) return;
        //设置给
        mCircleDataList.clear();
        mCircleDataList = list;

        //开始渲染到Item.
        requestLayout();
    }

    /**
     * 设置Item改变的回调.
     *
     * @param listener listener
     */
    public void setOnItemChangeListener(OnItemChangeListener listener) {
        this.mChangeListener = listener;
    }

    /**
     * 设置一个Loader
     *
     * @param loader loader
     */
    public void addOnImageLoader(OnImageLoader loader) {
        this.mLoader = loader;
    }

    public interface OnImageLoader {
        void onLoader(ImageView imageView, ICircleData iData);
    }
}
