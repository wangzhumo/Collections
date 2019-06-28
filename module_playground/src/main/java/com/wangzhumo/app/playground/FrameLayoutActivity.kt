package com.wangzhumo.app.playground

import com.alibaba.android.arouter.facade.annotation.Route
import com.wangzhumo.app.base.IRoute
import com.wangzhumo.app.origin.BaseActivity


@Route(path = IRoute.FRAME_ACTIVITY)
class FrameLayoutActivity : BaseActivity() {
    override fun getLayoutId(): Int = R.layout.activity_fream_layout
}
