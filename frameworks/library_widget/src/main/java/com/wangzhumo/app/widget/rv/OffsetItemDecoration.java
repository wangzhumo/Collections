package com.wangzhumo.app.widget.rv;

import android.content.Context;
import android.graphics.Rect;
import android.util.TypedValue;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2020-01-07  16:18
 */
public class OffsetItemDecoration extends RecyclerView.ItemDecoration{

    private int mOffsetTop, mOffsetRight, mOffsetBottom, mOffsetLeft;
    private Context context;

    public OffsetItemDecoration(Context context, int top, int right, int bot, int left) {
        this.context = context;
        mOffsetTop = dp2px(context, top);
        mOffsetRight = dp2px(context, right);
        mOffsetBottom = dp2px(context, bot);
        mOffsetLeft = dp2px(context, left);
    }

    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.top = mOffsetTop;
        outRect.right = mOffsetRight;
        outRect.bottom = mOffsetBottom;
        outRect.left = mOffsetLeft;
    }

    /**
     * dp转px
     */
    private int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }
}
