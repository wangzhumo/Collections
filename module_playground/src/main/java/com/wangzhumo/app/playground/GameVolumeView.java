package com.wangzhumo.app.playground;

import android.content.Context;
import android.graphics.*;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-06-28  17:52
 */
public class GameVolumeView extends View {

    private Bitmap mBitmap;
    private Paint mPaint;
    private Rect mRect;

    public GameVolumeView(Context context) {
        this(context,null);
    }

    public GameVolumeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs , 0);
    }

    public GameVolumeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context, attrs);
    }


    /**
     * 加载到Drawable
     * @param context ctx
     * @param attrs attrs
     */
    private void initViews(Context context, AttributeSet attrs) {
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.game_ic_mic_alphe);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mRect = new Rect(0,0,mBitmap.getWidth(),mBitmap.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        PorterDuffXfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
        canvas.drawRect(mRect,mPaint);
        mPaint.setXfermode(xfermode);
        mPaint.setColor(Color.YELLOW);
        canvas.drawBitmap(mBitmap,0,0,mPaint);
    }
}
