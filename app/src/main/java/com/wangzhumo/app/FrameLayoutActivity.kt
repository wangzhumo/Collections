package com.wangzhumo.app

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.wangzhumo.app.base.IRoute
import com.wangzhumo.app.origin.BaseActivity


@Route(path = IRoute.FRAME_ACTIVITY)
class FrameLayoutActivity : BaseActivity() {
    override fun getLayoutId(): Int = R.layout.activity_fream_layout

    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)

        BattleSucceedDialog().show(supportFragmentManager, R.id.frameLayout)
    }
}
