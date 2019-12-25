package com.wangzhumo.app

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.tencent.mars.xlog.Log
import com.wangzhumo.app.base.IRoute
import com.wangzhumo.app.origin.BaseActivity


@Route(path = IRoute.APP_MAIN)
class MainActivity : BaseActivity() {

    private val TAG = "MainActivity"

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        ARouter.getInstance()
            .build(IRoute.MEDIA_OPENGL_RECORD)
            .navigation()
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG,"onDestroy")
    }

}
