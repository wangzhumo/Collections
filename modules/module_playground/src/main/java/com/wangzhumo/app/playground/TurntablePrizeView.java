package com.wangzhumo.app.playground;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.wangzhumo.app.playground.R;


/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2/26/21  10:17 AM
 * <p>
 * 夺宝的动画View
 */
public class TurntablePrizeView extends View implements ValueAnimator.AnimatorUpdateListener {
    /**
     * 半径大小
     */
    private int radius;
    /**
     * 数量
     */
    private int count;
    /**
     * 时间间隔
     */
    private int duration;

    /**
     * 边距预留的大小
     */
    private int offsetSize = 0;

    /**
     * 6个圆点偏移的位置
     */
    private int offsetPoint = 0;

    /**
     * 图片资源.
     */
    private int res;

    /**
     * 6个点对应的颜色
     */
    private final int[] colors = new int[6];

    /**
     * 6个点
     */
    private final Point[] points = new Point[6];

    /**
     * 是否显示闪动的动画
     */
    private boolean disableSharkFlag = true;

    /**
     * 当前闪动的动画
     */
    private int currentIndex = -1;

    /**
     * View的资源
     */
    private Rect rootRect;
    private Rect imageIn;
    private Rect imageDes;
    private Paint mPaint;
    private Bitmap bitmap;
    private final BlurMaskFilter blurMaskFilter = new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL);

    /**
     * 闪动的动画
     */
    private ValueAnimator animator;

    public TurntablePrizeView(Context context) {
        super(context);
        init();
    }

    public TurntablePrizeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TurntablePrizeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // 加载
        rootRect = new Rect();
        imageIn = new Rect();
        imageDes = new Rect();
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.FILL);
        if (res == 0) {
            res = R.mipmap.widget_bg_turntable_prize;
        }
        // 获取图片
        bitmap = BitmapFactory.decodeResource(getResources(), res);
        // 设置
        imageIn.set(0, 0, bitmap.getWidth(), bitmap.getHeight());
        // 设置颜色
        colors[0] = Color.parseColor("#FFFE00");
        colors[1] = Color.parseColor("#00FFED");
        colors[2] = Color.parseColor("#FFFE00");
        colors[3] = Color.parseColor("#00FFED");
        colors[4] = Color.parseColor("#FFFE00");
        colors[5] = Color.parseColor("#00FFED");
        for (int i = 0; i < points.length; i++) {
            points[i] = new Point();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 测量出图像的大小
        rootRect.set(0, 0, MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(widthMeasureSpec));
        // 计算图片的大小
        imageDes.set(rootRect.left + offsetSize, rootRect.top + offsetSize, rootRect.right - offsetSize, rootRect.bottom - offsetSize);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制背景图
        drawBackground(canvas);
        if (!disableSharkFlag) {
            // 绘制缺少的那个点
            drawPointLight(canvas, currentIndex);
        }
        // 绘制背景上的点
        drawBackgroundPoint(canvas);
    }


    /**
     * 绘制背景,计算点
     *
     * @param canvas 画布
     */
    private void drawBackground(Canvas canvas) {
        canvas.drawBitmap(bitmap, imageIn, imageDes, mPaint);
        int round = 270;
        int currentRadius = imageDes.width() / 2 - radius / 2 - offsetPoint;
        for (int i = 0; i < 6; i++) {
            points[i].x = (int) (imageDes.centerX() + currentRadius * Math.cos(Math.toRadians(round)));
            points[i].y = (int) (imageDes.centerY() + currentRadius * Math.sin(Math.toRadians(round)));
            round += 60;
            round %= 360;
        }
    }

    /**
     * 绘制背景上的点
     *
     * @param canvas 画布
     */
    private void drawBackgroundPoint(Canvas canvas) {
        // 绘制6个点
        for (int i = 0; i < 6; i++) {
            mPaint.setColor(colors[i]);
            canvas.drawCircle((float) points[i].x, (float) points[i].y, radius, mPaint);
        }
        mPaint.setColor(Color.WHITE);
        mPaint.setMaskFilter(blurMaskFilter);
        for (int i = 0; i < 6; i++) {
            canvas.drawCircle((float) points[i].x, (float) points[i].y, radius / 2F, mPaint);
        }
        mPaint.setMaskFilter(null);
    }

    /**
     * 需要绘制的那个点
     *
     * @param canvas 画布
     * @param index  index
     */
    private void drawPointLight(Canvas canvas, int index) {
        if (index == -1 || disableSharkFlag) return;
        Shader mShader = new RadialGradient((float) points[index].x, (float) points[index].y, radius * 3, new int[]{colors[index], Color.TRANSPARENT}, null, Shader.TileMode.CLAMP);
        mPaint.setColor(colors[index]);
        mPaint.setShader(mShader);
        canvas.drawCircle((float) points[index].x, (float) points[index].y, radius * 3, mPaint);
        mPaint.setShader(null);
    }


    /**
     * 开始闪
     */
    public void startSharkAnim() {
        if (!disableSharkFlag) return;
        disableSharkFlag = false;
        if (animator == null) {
            animator = ValueAnimator.ofInt(0, 6);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    animator.removeAllUpdateListeners();
                    currentIndex = -1;
                    disableSharkFlag = true;
                }
            });
        }
        animator.addUpdateListener(this);
        animator.setDuration(duration * 6);
        animator.setRepeatCount(count);
        animator.start();
    }

    /**
     * 背景的圆半径
     *
     * @param radius 背景上的6个圆半径
     */
    public void setRadius(int radius) {
        this.radius = radius;
    }

    /**
     * 设置每一个点的间隔时间
     *
     * @param duration ms
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * 设置背景圆与View的边距
     *
     * @param offsetSize 边距
     */
    public void setOffset(int offsetSize) {
        this.offsetSize = offsetSize;
    }

    /**
     * 设置背景6个圆的偏移
     *
     * @param offsetPoint 偏移
     */
    public void setPointOffset(int offsetPoint) {
        this.offsetPoint = offsetPoint;
    }

    /**
     * 背景圆的图片
     *
     * @param res res
     */
    public void setRes(int res) {
        this.res = res;
    }

    /**
     * 外圈的圆，旋转多少次
     *
     * @param count count
     */
    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        int value = (int) animation.getAnimatedValue();
        value %= 6;
        if (currentIndex != value) {
            currentIndex = value;
            postInvalidate();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        // 删除这个动画
        if (animator != null) {
            animator.removeAllListeners();
            animator.removeUpdateListener(this);
            animator.cancel();
        }
        try {
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
