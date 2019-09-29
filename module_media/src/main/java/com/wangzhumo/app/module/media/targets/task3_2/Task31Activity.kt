package com.wangzhumo.app.module.media.targets.task3_2

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.wangzhumo.app.base.IRoute
import com.wangzhumo.app.module.media.R
import com.wangzhumo.app.origin.BaseActivity

@Route(path = IRoute.MEDIA_TASK_3)
class Task31Activity : BaseActivity() {

    override fun getLayoutId(): Int = R.layout.activity_task31

    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
    }

}
