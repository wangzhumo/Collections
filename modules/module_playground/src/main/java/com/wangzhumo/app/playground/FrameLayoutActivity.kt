package com.wangzhumo.app.playground

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.wangzhumo.app.base.IRoute
import com.wangzhumo.app.origin.base.BaseBindingActivity
import com.wangzhumo.app.origin.utils.DensityUtils
import com.wangzhumo.app.playground.databinding.ActivityFreamLayoutBinding


@Route(path = IRoute.FRAME_ACTIVITY)
public class FrameLayoutActivity : BaseBindingActivity<ActivityFreamLayoutBinding>() {

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        vBinding.turnView.setCount(5)
        vBinding.turnView.setDuration(60)
        vBinding.turnView.setOffset(DensityUtils.dp2px(this,2F))
        vBinding.turnView.setPointOffset(DensityUtils.dp2px(this,1F))
        vBinding.turnView.setRadius(DensityUtils.dp2px(this,2F))
        vBinding.turnView.setRes(R.mipmap.widget_bg_turntable_prize)
        vBinding.animateToStart.setOnClickListener {
            vBinding.turnView.startSharkAnim()
        }
    }
}
