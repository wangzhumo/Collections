package com.wangzhumo.app.playground

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.wangzhumo.app.base.IRoute
import com.wangzhumo.app.origin.base.BaseBindingActivity
import com.wangzhumo.app.playground.databinding.ActivityFreamLayoutBinding


@Route(path = IRoute.FRAME_ACTIVITY)
public class FrameLayoutActivity : BaseBindingActivity<ActivityFreamLayoutBinding>() {

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        vBinding.animateToStart.setOnClickListener {
            vBinding.turnView.startShark()
        }
    }
}
