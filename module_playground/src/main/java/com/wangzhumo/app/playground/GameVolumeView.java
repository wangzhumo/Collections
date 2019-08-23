package com.wangzhumo.app.playground;

import android.content.Context;
import android.graphics.*;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import android.util.AttributeSet;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-06-28  17:52
 */
public class GameVolumeView extends AppCompatImageView {

    private Bitmap mBitmap;
    private Paint mPaint;
    private Rect mDestRect;
    private float mProgress;
    private int mHeight;

    private Disposable timeDisposable;

    public GameVolumeView(Context context) {
        this(context, null);
    }

    public GameVolumeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GameVolumeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context, attrs);
    }


    /**
     * 加载到Drawable
     *
     * @param context ctx
     * @param attrs   attrs
     */
    private void initViews(Context context, AttributeSet attrs) {
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.game_ic_mic_yellow);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mHeight = mBitmap.getHeight();
        mDestRect = new Rect(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.clipRect(mDestRect);
        canvas.drawBitmap(mBitmap, 0, 0, mPaint);
        canvas.restore();
    }


    private long lastInvalidate;

    /**
     * 设置音波
     *
     * @param volume volume
     */
    public void setVolume(int volume) {
        //决定是否开始更新
        if (lastInvalidate + 20 > System.currentTimeMillis()) {
            return;
        }
        lastInvalidate = System.currentTimeMillis();
        if (volume > 150) {
            mProgress = 0F;
        } else {
            mProgress = 1F - (volume / 150F);
        }
        mDestRect.top = (int) (mHeight * mProgress);
        invalidate();
    }

    /**
     * 开始一些模拟的动画
     */
    public void startFakeAnim() {
        if (timeDisposable != null && !timeDisposable.isDisposed()) {
            timeDisposable.dispose();
            timeDisposable = null;
        }
        Random random = new Random();
        timeDisposable = Observable.interval(10, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        setVolume(random.nextInt(150) + 1);
                    }
                });

    }

    /**
     * 停止动画
     */
    public void release() {
        if (timeDisposable != null && !timeDisposable.isDisposed()) {
            timeDisposable.dispose();
            timeDisposable = null;
        }
    }

}
