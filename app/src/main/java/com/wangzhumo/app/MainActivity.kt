package com.wangzhumo.app

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.tencent.mars.xlog.Log
import com.wangzhumo.app.base.IRoute
import com.wangzhumo.app.origin.base.BaseBindingActivity
import com.wangzhumo.app.databinding.ActivityMainBinding
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.android.FlutterActivityLaunchConfigs


@Route(path = IRoute.APP_MAIN)
public class MainActivity : BaseBindingActivity<ActivityMainBinding>() {

    private val TAG = "MainActivity"


    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
//        startActivity(
//            FlutterActivity
//                .withCachedEngine("wangzhumo_engine")
//                .backgroundMode(FlutterActivityLaunchConfigs.BackgroundMode.transparent)
//                .build(this)
//        )
        ARouter.getInstance().build(IRoute.SOCKET_ACTIVITY).navigation()
        vBinding.root.postDelayed({ this@MainActivity.finish() }, 500)
//        vBinding.btMedia.clicks()
//            .subscribeOn(AndroidSchedulers.mainThread())
//            .flatMap {
//                RxPermissions(this).request(
//                    Manifest.permission.CAMERA,
//                    Manifest.permission.RECORD_AUDIO
//                )
//            }
//            .subscribe {
//                if (it) {
//                    // 如果完成，则点击事件
//                    ARouter.getInstance()
//                        .build(IRoute.MEDIA_MAIN)
//                        .navigation()
//                } else {
//                    Toast.makeText(this, "请授权", Toast.LENGTH_LONG).show()
//                }
//            }
//
//
//        vBinding.btOpengl.clicks()
//            .subscribeOn(AndroidSchedulers.mainThread())
//            .flatMap {
//                RxPermissions(this).request(
//                    Manifest.permission.CAMERA,
//                    Manifest.permission.RECORD_AUDIO
//                )
//            }
//            .subscribe {
//                if (it) {
//                    // 如果完成，则点击事件
//                    ARouter.getInstance()
//                        .build(IRoute.OPENGL_LIST)
//                        .navigation()
//                } else {
//                    Toast.makeText(this, "请授权", Toast.LENGTH_LONG).show()
//                }
//            }

    }


    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG, "onDestroy")
    }

}

