package com.wangzhumo.app.circle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-12-17  19:35
 */
public class CircleItemView extends ImageView {

    private Path mPath;
    private int mOffset;
    private float inner,out;
    private PointF tl;
    private PointF tr;
    private PointF bl;
    private PointF br;
    private RectF rectF;

    public CircleItemView(Context context, int params) {
        super(context);
        this.mOffset = params;
        this.mPath = new Path();
        this.rectF = new RectF();
    }


    public void setClipParams(PointF tl,PointF tr,PointF bl,PointF br){
        this.tl = tl;
        this.tr = tr;
        this.bl = bl;
        this.br = br;
    }

    public void setRadiusParams(float inner,float out){
        this.inner = inner;
        this.out = out;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
