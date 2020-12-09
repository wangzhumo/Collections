package com.wangzhumo.app

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.tencent.mars.xlog.Log
import com.wangzhumo.app.base.IRoute
import com.wangzhumo.app.origin.BaseActivity
import com.wangzhumo.app.databinding.ActivityMainBinding


@Route(path = IRoute.APP_MAIN)
public class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val TAG = "MainActivity"


    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)

        // 获取权限

        vBinding.btMedia.setOnClickListener {
            ARouter.getInstance()
                .build(IRoute.MEDIA_MAIN)
                .navigation()
            finish()
        }

        vBinding.btOpengl.setOnClickListener {
            ARouter.getInstance()
                .build(IRoute.OPENGL_LIST)
                .navigation()
            finish()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG,"onDestroy")
    }

}
