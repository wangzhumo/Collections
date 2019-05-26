package com.wangzhumo.app.playground

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.wangzhumo.app.AllSingFailDialog
import com.wangzhumo.app.base.IRoute
import com.wangzhumo.app.origin.BaseActivity


@Route(path = IRoute.FRAME_ACTIVITY)
class FrameLayoutActivity : BaseActivity() {
    override fun getLayoutId(): Int = R.layout.activity_fream_layout

    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)

        AllSingFailDialog().show(supportFragmentManager, R.id.frameLayout)
    }
}
