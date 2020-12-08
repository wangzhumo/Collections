package com.wangzhumo.app.playground

import androidx.viewbinding.ViewBinding
import com.alibaba.android.arouter.facade.annotation.Route
import com.wangzhumo.app.base.IRoute
import com.wangzhumo.app.origin.BaseActivity
import kotlinx.android.synthetic.main.activity_fream_layout.*


@Route(path = IRoute.FRAME_ACTIVITY)
public class FrameLayoutActivity<ActivityFreamLayoutBinding : ViewBinding?> : BaseActivity<ActivityFreamLayoutBinding>() {

}
