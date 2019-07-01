package com.wangzhumo.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
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
    val mHandler = Handler()

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        val intent: Intent = Intent(Intent.ACTION_VIEW, Uri.parse("alipays://platformapi/startapp?appid=20000118"))
        startActivity(intent)

        mHandler.postDelayed({
            ARouter.getInstance()
                .build(IRoute.FRAME_ACTIVITY)
                .navigation()
        }, 5000)
        Log.e(TAG,IRoute.FRAME_ACTIVITY)
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG,"onDestroy")
    }

}
