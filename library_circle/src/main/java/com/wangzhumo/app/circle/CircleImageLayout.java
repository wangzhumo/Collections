package com.wangzhumo.app.circle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Shader;
import android.media.Image;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.util.SparseLongArray;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
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
public class CircleImageLayout extends ViewGroup implements View.OnTouchListener {

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
    private int mDefaultAngle = 0;

    /**
     * Item的图标
     */
    private List<ICircleData> mCircleDataList;

    /**
     * Item的个数
     */
    private final int mDefaultItemCount = 12;

    /**
     * 监听
     */
    private OnItemChangeListener mChangeListener;
    private OnImageLoader mLoader;
    private int anglePre = 360 / (mDefaultItemCount);
    private float mItemWidth, mItemHeight;
    private Path clipPath;
    private RectF rectF;
    private SparseArray<PointF> mAngleOutPoint;
    private SparseArray<PointF> mAngleInnerPoint;
    private SparseArray<PointF> mAngleFullPoint;
    private Paint mArcPaint,mShaderPaint;
    private GestureDetector mGestureDetector;

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
        mArcPaint.setColor(Color.BLACK);
        mArcPaint.setStrokeWidth(1);

        mShaderPaint = new Paint();
        mShaderPaint.setAntiAlias(true);
        mShaderPaint.setStyle(Paint.Style.FILL);
        mAngleOutPoint = new SparseArray<>();
        mAngleInnerPoint = new SparseArray<>();
        mAngleFullPoint = new SparseArray<>();
        int tempAngle = mDefaultAngle;
        //初始化
        for (int i = 0; i < mDefaultItemCount; i++) {
            mAngleOutPoint.put(tempAngle, new PointF());
            mAngleInnerPoint.put(tempAngle, new PointF());
            mAngleFullPoint.put(tempAngle, new PointF());
            tempAngle += anglePre;
        }
        rectF = new RectF();
        mCircleDataList = new ArrayList<>();
        mGestureDetector = new GestureDetector(getContext(), new CircleGestureListener());
        setOnTouchListener(this);
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

        float outX, outY, fullX, fullY, innerX, innerY;
        int tempAngle = mDefaultAngle - 15;
        for (int i = 0; i < mDefaultItemCount; i++) {
            innerX = (mRadius >> 1) + (int) Math.round(tempValue1 * Math.cos(Math.toRadians(tempAngle)));
            innerY = (mRadius >> 1) + (int) Math.round(tempValue1 * Math.sin(Math.toRadians(tempAngle)));

            outX = (mRadius >> 1) + (int) Math.round(tempValue2 * Math.cos(Math.toRadians(tempAngle)));
            outY = (mRadius >> 1) + (int) Math.round(tempValue2 * Math.sin(Math.toRadians(tempAngle)));

            fullX = (mRadius >> 1) + (int) Math.round(tempValue3 * Math.cos(Math.toRadians(tempAngle)));
            fullY = (mRadius >> 1) + (int) Math.round(tempValue3 * Math.sin(Math.toRadians(tempAngle)));

            if (mAngleOutPoint.get(tempAngle + 15) == null) {
                mAngleOutPoint.put(tempAngle + 15,new PointF(outX,outY));
            }else {
                mAngleOutPoint.get(tempAngle + 15).set(outX, outY);
            }
            if (mAngleFullPoint.get(tempAngle + 15) == null) {
                mAngleFullPoint.put(tempAngle + 15,new PointF(fullX,fullY));
            }else {
                mAngleFullPoint.get(tempAngle + 15).set(fullX,fullY);
            }
            if (mAngleInnerPoint.get(tempAngle + 15) == null) {
                mAngleInnerPoint.put(tempAngle + 15,new PointF(innerX,innerY));
            }else {
                mAngleInnerPoint.get(tempAngle + 15).set(innerX,innerY);
            }

            tempAngle += anglePre;
        }
        addItems();
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
        int tag = (int) child.getTag();
        PointF innerf = mAngleInnerPoint.get(tag);
        PointF outf = mAngleOutPoint.get(tag);
        PointF innerfnext;
        if (tag + anglePre == 360) {
            innerfnext = mAngleInnerPoint.get(0);
        } else {
            innerfnext = mAngleInnerPoint.get(tag + anglePre);
        }
        canvas.save();
        clipPath.reset();
        clipPath.moveTo(outf.x, outf.y);
        rectF.set((mRadius - mViewOutRadius) / 2, (mRadius - mViewOutRadius) / 2, (mRadius >> 1) + (mViewOutRadius / 2), (mRadius >> 1) + (mViewOutRadius / 2));
        clipPath.addArc(rectF, tag-15, anglePre);
        clipPath.lineTo(innerfnext.x, innerfnext.y);
        int conX = (mRadius >> 1) + (int) Math.round((mViewInnerRadius/2 + inner_out_offset) * Math.cos(Math.toRadians(tag)));
        int conY = (mRadius >> 1) + (int) Math.round((mViewInnerRadius/2 + inner_out_offset) * Math.sin(Math.toRadians(tag)));
        clipPath.quadTo(conX,conY,innerf.x,innerf.y);
        clipPath.lineTo(outf.x, outf.y);
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
            CircleItemView imageView = new CircleItemView(getContext(), (int) inner_out_offset);
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


    class CircleGestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return super.onFling(e1, e2, velocityX, velocityY);
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

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return mGestureDetector.onTouchEvent(motionEvent);
    }

    public interface OnImageLoader {
        void onLoader(ImageView imageView, ICircleData iData);
    }
}
