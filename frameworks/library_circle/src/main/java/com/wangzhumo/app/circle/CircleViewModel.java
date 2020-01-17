package com.wangzhumo.app.circle;

import android.graphics.PointF;

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-12-17  19:52
 */
public class CircleViewModel {

    public int offset;
    public PointF tl;
    public PointF tr;
    public PointF bl;
    public PointF br;

    public CircleViewModel(int offset, PointF tl, PointF tr, PointF bl, PointF br) {
        this.offset = offset;
        this.tl = tl;
        this.tr = tr;
        this.bl = bl;
        this.br = br;
    }
}
