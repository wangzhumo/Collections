package com.wangzhumo.app

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.tencent.mars.xlog.InyuLog
import com.tencent.mars.xlog.Log
import com.tencent.mars.xlog.Xlog
import com.wangzhumo.app.base.IRoute
import com.wangzhumo.app.origin.BaseActivity
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import java.util.concurrent.TimeUnit


@Route(path = IRoute.APP_MAIN)
class MainActivity : BaseActivity() {

    val TAG = "MainActivity"

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
//        ARouter.getInstance()
//            .build(IRoute.WEBRTC_MAIN)
//            .navigation()


        val disposable = Flowable.intervalRange(0, 100, 0, 100, TimeUnit.MILLISECONDS)
            .subscribe(Consumer {
                Log.d(TAG, "com.wangzhumo.app.MainActivity", "initViews", 33, "日志 - $it")
                InyuLog.e(TAG, "com.wangzhumo.app.MainActivity", "initViews", 33, "日志 - $it")
            })
        addDisposable(disposable)
    }
}
