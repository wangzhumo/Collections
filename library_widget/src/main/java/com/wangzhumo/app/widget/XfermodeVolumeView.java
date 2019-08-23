package com.wangzhumo.app.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-07-31  20:23
 */
public class XfermodeVolumeView extends View {

    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint mPaint;
    private PorterDuffXfermode xfermode;
    private BitmapFactory.Options mIconOptions;

    /**
     * 颜色
     */
    private int mColor = 0xFF45C01A;
    /**
     * 图标
     */
    private Bitmap mIconBitmap;
    /**
     * 限制绘制icon的范围
     */
    private Rect mIconRect;
    /**
     * 进度
     */
    private float mProgress;

    /**
     * MAX
     */
    private int MAX_VALUE;

    public XfermodeVolumeView(Context context) {
        super(context);
        initViews(context, null);
    }

    public XfermodeVolumeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews(context, attrs);
    }

    public XfermodeVolumeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context, attrs);
    }


    /**
     * 初始化.
     *
     * @param context ctx
     * @param attrs   attrs
     */
    private void initViews(Context context, AttributeSet attrs) {
        mIconBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_volume);
        mIconOptions = new BitmapFactory.Options();
        mIconOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.mipmap.icon_volume, mIconOptions);
        mIconRect = new Rect();
        mBitmap = Bitmap.createBitmap(mIconOptions.outWidth, mIconOptions.outHeight,
                Bitmap.Config.ARGB_8888);
        MAX_VALUE = mIconOptions.outHeight;
        mCanvas = new Canvas(mBitmap);
        mPaint = new Paint();
        xfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
        mPaint.setColor(mColor);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setAlpha(255);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        if (mode == MeasureSpec.EXACTLY) {
            //精确模式/最大
            if (getMeasuredWidth() > mIconOptions.outWidth && mIconBitmap == null) {
                mIconOptions.inJustDecodeBounds = false;
                mIconOptions.inSampleSize = calculateInSampleSize(mIconOptions, width, height);
                mIconBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_volume, mIconOptions);
            }
        } else if (mode == MeasureSpec.AT_MOST && mIconBitmap == null) {
            if (width < mIconOptions.outWidth) {
                mIconOptions.inJustDecodeBounds = false;
                mIconOptions.inSampleSize = calculateInSampleSize(mIconOptions, width, height);
                mIconBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_volume, mIconOptions);
            }
        }
//        int widthMeasure = MeasureSpec.makeMeasureSpec(mIconBitmap.getWidth(), MeasureSpec.EXACTLY);
//        int heightMeasure = MeasureSpec.makeMeasureSpec(mIconBitmap.getHeight(), MeasureSpec.EXACTLY);
//        //依据图片修改大小尺寸.
//        mIconRect.top = 0;
//        mIconRect.left = 0;
//        mIconRect.right = mIconBitmap.getWidth();
//        mIconRect.bottom = mIconBitmap.getHeight();
//        MAX_VALUE = mIconBitmap.getHeight();


        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setupTargetBitmap(mProgress);
        canvas.drawBitmap(mBitmap, 0, 0, null);
    }


    /**
     * 绘制图片.
     *
     * @param progress 透明度
     */
    private void setupTargetBitmap(float progress) {
        Log.e("Volume", "progress = " + progress);
        mPaint.setXfermode(null);
        mIconRect.top = (int) (MAX_VALUE * (1F - progress) + 0.5F);
        mCanvas.drawRect(mIconRect, mPaint);
        mPaint.setXfermode(xfermode);
        mIconRect.top = 0;
        mCanvas.drawBitmap(mIconBitmap, null, mIconRect, mPaint);
    }

    @Override
    public void onDrawForeground(Canvas canvas) {
        super.onDrawForeground(canvas);
        mIconRect.top = 0;
        canvas.drawBitmap(mIconBitmap, null, mIconRect, null);
    }

    /**
     * 显示音波 volume.
     */
    public void setVolume(int volume) {
        if (volume > 150) {
            mProgress = 1.0F;
        } else {
            mProgress = volume / 150F;
        }
        invalidate();
    }


    private int calculateInSampleSize(BitmapFactory.Options options,
                                      int requestWidth, int requestHeight) {
        // 原始图片尺寸
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > requestWidth || width > requestHeight) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) requestHeight);
            final int widthRatio = Math.round((float) width / (float) requestWidth);
            // 选择宽和高比率中最小的作为inSampleSize的值，这样可以保证最终生成图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }
}
