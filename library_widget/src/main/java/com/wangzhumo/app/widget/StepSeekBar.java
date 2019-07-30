package com.wangzhumo.app.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2016-12-27  16:20
 */
public class StepSeekBar extends View {

    /**
     * 默认的拖动间距
     */
    private static final int DEFAULT_DURATION = 100;
    private int mDuration;


    /**
     * Scrollers 控制seekbar上的游标
     */
    private Scroller mScroller;


    /**
     * 游标的背景
     */
    private Drawable mCursorBG;


    /**
     * Represent states.
     */
    private int[] mPressedEnableState = new int[]{
            android.R.attr.state_pressed, android.R.attr.state_enabled};
    private int[] mUnPresseEanabledState = new int[]{
            -android.R.attr.state_pressed, android.R.attr.state_enabled};


    /**
     * 游标与文字的选中颜色
     */
    private int mTextColorNormal;
    private int mTextColorSelected;
    private int mSeekbarColorNormal;
    private int mSeekbarColorSelected;

    /**
     * Height of seekbar
     */
    private int mSeekbarHeight;

    /**
     * Size of text mark.
     */
    private int mTextSize;

    /**
     * Space between the text and the seekbar
     */
    private int mMarginBetween;

    /**
     * Length of every part. As we divide some parts according to marks.
     */
    private int mPartLength;

    /**
     * Contents of text mark.
     */
    private CharSequence[] mTextArray;

    /**
     * 文字宽度的数组
     */
    private float[] mTextWidthArray;

    /**
     * 游标和边距的矩形
     */
    private Rect mPaddingRect;
    private Rect mCursorRect;

    /**
     * seekbar和数据
     */
    private RectF mSeekbarRect;
    private RectF mSeekbarRectSelected;

    private float mCursorIndex = 0;
    private int mCursorNextIndex = 0;


    /**
     * 用来绘制的画笔
     */
    private Paint mPaint;

    private int mPointerLastX;

    private int mPointerID = -1;

    private boolean mHited;

    private OnCursorChangeListener mListener;


    //点击的集合
    private Rect[] mClickRectArray;
    //当前的点击位置
    private int mClickIndex = -1;
    private int mClickDownLastX = -1;
    private int mClickDownLastY = -1;


    public StepSeekBar(Context context) {
        this(context, null, 0);
    }

    public StepSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StepSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //读取设置的参数
        initConfig(context, attrs);

        //--------------------开始准备各种数据的容器
        if (mPaddingRect == null) {
            mPaddingRect = new Rect();
        }

        mPaddingRect.left = getPaddingLeft();
        mPaddingRect.top = getPaddingTop();
        mPaddingRect.right = getPaddingRight();
        mPaddingRect.bottom = getPaddingBottom();

        mCursorRect = new Rect();
        mSeekbarRect = new RectF();
        mSeekbarRectSelected = new RectF();
        if (mTextArray != null) {
            mTextWidthArray = new float[mTextArray.length];
            mClickRectArray = new Rect[mTextArray.length];
        }
        mScroller = new Scroller(context, new DecelerateInterpolator());
        //---------------------准备完毕,初始化数据

        //初始化画笔
        initPaint();
        //确认文字的宽度
        initTextWidthArray();

        setWillNotDraw(false);
        setFocusable(true);
        setClickable(true);
    }


    /**
     * 初始化设置的参数
     *
     * @param context
     * @param attrs
     */
    private void initConfig(Context context, AttributeSet attrs) {
        //如果没有参数,那就over了
        if (attrs == null) {
            return;
        }
        TypedArray typeArr = context.obtainStyledAttributes(attrs, R.styleable.StepSeekbar);
        mDuration = typeArr.getInteger(R.styleable.StepSeekbar_autoMoveDuration, DEFAULT_DURATION);
        mCursorBG = typeArr.getDrawable(R.styleable.StepSeekbar_cursorBackground);
        if (mCursorBG == null) {
            throw new IllegalArgumentException("must set resources for cursor");
        }
        if (mCursorBG.getIntrinsicWidth() == -1) {
            throw new IllegalArgumentException("resources must define width & height");
        }
        mTextColorNormal = typeArr.getColor(R.styleable.StepSeekbar_textColorNormal, Color.BLACK);
        mTextColorSelected = typeArr.getColor(R.styleable.StepSeekbar_textColorSelected, Color.BLUE);
        mSeekbarColorNormal = typeArr.getColor(R.styleable.StepSeekbar_seekbarColorNormal, 0);
        mSeekbarColorSelected = typeArr.getColor(R.styleable.StepSeekbar_seekbarColorSelected, 0);
        mSeekbarHeight = (int) typeArr.getDimension(R.styleable.StepSeekbar_seekbarHeight, 10);
        mTextSize = (int) typeArr.getDimension(R.styleable.StepSeekbar_textSize, 15);
        mMarginBetween = (int) typeArr.getDimension(R.styleable.StepSeekbar_spaceBetween, 20);
        mTextArray = typeArr.getTextArray(R.styleable.StepSeekbar_markTextArray);
        if (mTextArray != null && mTextArray.length > 0) {
            mCursorIndex = 0;
        }
        //收集数据结束,回收对象
        typeArr.recycle();
    }

    /**
     * 画笔的初始化
     */
    private void initPaint() {
        //抗锯齿
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //设置边缘抗锯齿
        mPaint.setAntiAlias(true);
        //实心的画笔
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextSize(mTextSize);
    }

    /**
     * 确认文字的宽度
     */
    private void initTextWidthArray() {
        if (mTextArray != null && mTextArray.length > 0) {
            //开始计算文字
            for (int i = 0; i < mTextArray.length; i++) {
                mTextWidthArray[i] = mPaint.measureText(mTextArray[i].toString());
            }
        }
    }


    /**
     * 将padding值设置给矩形
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom);
        if (mPaddingRect == null) {
            mPaddingRect = new Rect();
        }
        mPaddingRect.left = left;
        mPaddingRect.top = top;
        mPaddingRect.right = right;
        mPaddingRect.bottom = bottom;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //测量模式
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        //高度
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        //该api返回的为  dp 值(点高度)
        int PointerH = mCursorBG.getIntrinsicHeight();
        //如果背景没有高度,则默认为20
        // 获取seekbar的最终高度
        final int maxOfCursorAndSeekbar = Math.max(mSeekbarHeight, PointerH);

        //计算出整个view需要的高度  本省的高度,文字和seekbar距离,文字高度,上下padding
        int heightNeeded = maxOfCursorAndSeekbar + mMarginBetween + mTextSize
                + mPaddingRect.top + mPaddingRect.bottom;

        /**
         *
         * wrap_parent -> MeasureSpec.AT_MOST
         * match_parent -> MeasureSpec.EXACTLY
         * 具体值 -> MeasureSpec.EXACTLY
         *
         */
        if (heightMode == MeasureSpec.EXACTLY) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //就是拿到最少的高度
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(
                    heightSize < heightNeeded ? heightSize : heightNeeded, MeasureSpec.EXACTLY);
        } else {

            heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightNeeded, MeasureSpec.EXACTLY);
        }

        //计算seekbar
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        mSeekbarRect.left = mPaddingRect.left + mCursorBG.getIntrinsicWidth() / 2;
        mSeekbarRect.right = widthSize - mCursorBG.getIntrinsicWidth() / 2;
        mSeekbarRect.top = mPaddingRect.top + mTextSize + mMarginBetween;
        mSeekbarRect.bottom = mSeekbarRect.top + mSeekbarHeight;

        mSeekbarRectSelected.top = mSeekbarRect.top;
        mSeekbarRectSelected.bottom = mSeekbarRect.bottom;

        //点之间的距离
        mPartLength = ((int) (mSeekbarRect.right - mSeekbarRect.left)) / (mTextArray.length - 1);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        /**
         *开始画标记点
         */
        final int length = mTextArray.length;
        mPaint.setTextSize(mTextSize);


        //for循环来画每一个点
        for (int i = 0; i < length; i++) {
            if (i == mCursorIndex || i > mClickIndex) {
                mPaint.setColor(mTextColorSelected);
            } else {
                mPaint.setColor(mTextColorNormal);
            }

            //拿出要绘制的文字
            final String textdraw = mTextArray[i].toString();
            final float textwidth = mTextWidthArray[i];

            float textDraw = 0;
            // The last text mark's draw location should be adjust.
            textDraw = mSeekbarRect.left + i * mPartLength - textwidth / 2;
            if (i == length - 1) {
                textDraw = textDraw - textwidth / 2 + mCursorBG.getIntrinsicWidth() / 2;
            }
            /**
             * mPaddingRect.top + mTextSize  要加上mTextSize的原因在于
             * drawText参数  float y 中的y是指基线的位置,也就是文字偏下位置
             */
            canvas.drawText(textdraw, textDraw, mPaddingRect.top + mTextSize, mPaint);

            //设置点击的范围
            Rect rect = mClickRectArray[i];
            if (rect == null) {
                rect = new Rect();
                rect.top = mPaddingRect.top;
                rect.bottom = rect.top + mTextSize + mMarginBetween + mSeekbarHeight;
                //文字开始的位置
                rect.left = (int) textDraw;
                //文字结束,即开始位置加文字宽度
                rect.right = (int) (rect.left + textwidth);
                mClickRectArray[i] = rect;
            }
        }

        /**
         * 开始画seekbar
         */
        final float radius = (float) mSeekbarHeight / 2;
        mSeekbarRectSelected.left = mSeekbarRect.left + mPartLength * mCursorIndex;
        mSeekbarRectSelected.right = mSeekbarRect.right;

        // If whole of seekbar is selected, just draw seekbar with selected
        if (mCursorIndex == 0) {
            mPaint.setColor(mSeekbarColorNormal);
            canvas.drawRoundRect(mSeekbarRect, radius, radius, mPaint);
        } else {
            mPaint.setColor(mSeekbarColorSelected);
            canvas.drawRoundRect(mSeekbarRect, radius, radius, mPaint);
            //画出选中文字
            mPaint.setColor(mSeekbarColorNormal);
            canvas.drawRect(mSeekbarRectSelected, mPaint);
        }

        /**
         * 开始画游标
         */
        final int width = mCursorBG.getIntrinsicWidth();
        final int height = mCursorBG.getIntrinsicHeight();
        final int left = (int) (mSeekbarRectSelected.left - width / 2);
        final int top = (int) ((mSeekbarRect.top + mSeekbarHeight / 2) - (height / 2));

        mCursorRect.left = left;
        mCursorRect.right = left + width;
        mCursorRect.top = top;
        mCursorRect.bottom = top + height;

        mCursorBG.setBounds(mCursorRect);
        mCursorBG.draw(canvas);
    }

    /**
     * 设置滑动事件
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (getParent() != null) {
            //如果有父控件且不为空,则请求获得触摸事件
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                doTouchDown(event);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                doTouchDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                doTouchMove(event);
                break;
            case MotionEvent.ACTION_POINTER_UP:
                doTouchUp(event);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                doTouchUp(event);
                mClickIndex = -1;
                mClickDownLastX = -1;
                mClickDownLastY = -1;
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 处理落下事件
     *
     * @param event
     */
    private void doTouchDown(MotionEvent event) {
        //event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK
        //通过与运算过滤掉多点触碰
        //The best way to extract the index of the pointer that left the touch sensor.
        //http://stackoverflow.com/questions/14391818/how-do-you-use-motionevent-action-pointer-index-shift
        final int actionIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
        final int downX = (int) event.getX(actionIndex);
        final int downY = (int) event.getY(actionIndex);

        //1.判断是否在游标上
        if (mCursorRect.contains(downX, downY)) {
            if (mHited) {
                return;
            }
            // If hit, change state of drawable, and record id of touch pointer.
            mPointerLastX = downX;
            mCursorBG.setState(mPressedEnableState);
            mPointerID = event.getPointerId(actionIndex);
            mHited = true;

            invalidate();
        } else {
            // If touch x-y not be contained in cursor,
            // then we check if it in click areas
            final int clickBoundaryTop = mClickRectArray[0].top;
            final int clickBoundaryBottom = mClickRectArray[0].bottom;
            mClickDownLastX = downX;
            mClickDownLastY = downY;

            // Step one : if in boundary of total Y.
            if (downY > clickBoundaryBottom || downY < clickBoundaryTop) {
                mClickIndex = -1;
                return;
            }

            // Step two: find nearest mark in x-axis
            final int partIndex = (int) ((downX - mSeekbarRect.left) / mPartLength);
            final int partDelta = (int) ((downX - mSeekbarRect.left) % mPartLength);
            //依据超出的部分 partDelta 来判断应该置为哪一个点
            if (partDelta > mPartLength / 2) {
                mClickIndex = partIndex + 1;
            } else if (partDelta < mPartLength / 2) {
                mClickIndex = partIndex;
            }

            // Step three: check contain
            if (mClickIndex == mCursorIndex) {
                mClickIndex = -1;
                return;
            }

            //如果该点不在点击区域,那就over
            if (!mClickRectArray[mClickIndex].contains(downX, downY)) {
                mClickIndex = -1;
            }
        }
    }

    /**
     * 处理移动事件
     *
     * @param event
     */
    private void doTouchMove(MotionEvent event) {
        //如果是在有效区域移动才开始
        //1.落下在有效区域
        if (mClickIndex != -1) {
            final int actionIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
            final int x = (int) event.getX(actionIndex);
            final int y = (int) event.getY(actionIndex);
            //2.移出有效区域
            if (!mClickRectArray[mClickIndex].contains(x, y)) {
                mClickIndex = -1;
            }
        }
        if (mHited && mPointerID != -1) {
            final int index = event.findPointerIndex(mPointerID);
            final float x = event.getX(index);

            //滑动的位移
            float deltaX = x - mPointerLastX;
            mPointerLastX = (int) x;
            if (deltaX <= 0 && mCursorIndex == 0) {
                return;
            }

            //不明白为毛这么做...
            // Check whether cursor will move out of boundary
            if (mCursorRect.left + deltaX < mPaddingRect.left) {
                mCursorIndex = 0;
                invalidate();
                return;
            }


            // Calculate the movement.
            final float moveX = deltaX / mPartLength;
            mCursorIndex += moveX;

            invalidate();
        }
    }

    /**
     * 处理触摸事件UP
     *
     * @param event
     */
    private void doTouchUp(MotionEvent event) {
        final int actionIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
        final int actionID = event.getPointerId(actionIndex);
        //如果是同一个点才
        if (actionID == mPointerID) {
            if (!mHited) {
                return;
            }

            // If cursor between in tow mark locations, it should be located on
            // the lower or higher one.

            // step 1:Calculate the offset with lower mark.
            final int lower = (int) Math.floor(mCursorIndex);
            final int higher = (int) Math.ceil(mCursorIndex);

            final float offset = mCursorIndex - lower;
            //有位移才判断
            if (offset != 0) {
                // step 2:Decide which mark will go to.
                if (offset < 0.5f) {
                    // If left cursor want to be located on lower mark, go ahead
                    // guys.
                    // Because right cursor will never appear lower than the
                    // left one.
                    mCursorNextIndex = lower;
                } else if (offset > 0.5f) {
                    mCursorNextIndex = higher;
                    // If left cursor want to be located on higher mark,
                    // situation becomes a little complicated.
                    // We should check that whether distance between left and
                    // right cursor is less than 1, and next index of left
                    // cursor is difference with current
                    // of right one.
                    //---------------------
                    //if (Math.abs(mLeftCursorIndex - mRightCursorIndex) <= 1&& mLeftCursorNextIndex == mRightCursorNextIndex) {
                    //    // Left can not go to the higher, just to the lower one.
                    //    mLeftCursorNextIndex = lower;
                    //}
                    //---------------------
                    if (higher >= mTextArray.length) {
                        mCursorNextIndex = mTextArray.length - 1;
                    }
                }
                // step 3: Move to.
                //如果不是在滑动中
                if (!mScroller.computeScrollOffset()) {
                    //calc current location
                    final int fromX = (int) (mCursorIndex * mPartLength);
                    mScroller.startScroll(fromX, 0, mCursorNextIndex * mPartLength - fromX, 0, mDuration);
                    if (mListener != null) {
                        mListener.onCursorChanged(mCursorNextIndex, mTextArray[mCursorNextIndex].toString());
                    }
                }
            }
        }

        // Reset values of parameters
        mPointerLastX = 0;
        mCursorBG.setState(mUnPresseEanabledState);
        mPointerID = -1;
        mHited = false;

        invalidate();
    }


    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            final int deltaX = mScroller.getCurrX();
            mCursorIndex = (float) deltaX / mPartLength;
            invalidate();
        }
    }

    //-------------------------------设置监听事件-------------------

    /**
     * 设置一个游标的监听
     *
     * @param listener
     */
    public void setOnCursorChangeListener(OnCursorChangeListener listener) {
        mListener = listener;
    }


    /**
     * 游标的监听事件
     */
    public interface OnCursorChangeListener {
        void onCursorChanged(int location, String textMark);
    }
}
