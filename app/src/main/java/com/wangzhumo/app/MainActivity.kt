package com.wangzhumo.app

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.jakewharton.rxbinding4.view.clicks
import com.tbruyelle.rxpermissions3.Permission
import com.tbruyelle.rxpermissions3.RxPermissions
import com.tencent.mars.xlog.Log
import com.wangzhumo.app.base.IRoute
import com.wangzhumo.app.origin.BaseActivity
import com.wangzhumo.app.databinding.ActivityMainBinding
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.android.FlutterActivityLaunchConfigs
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.functions.Predicate
import io.reactivex.rxjava3.schedulers.Schedulers


@Route(path = IRoute.APP_MAIN)
public class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val TAG = "MainActivity"


    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        startActivity(
            FlutterActivity
                .withCachedEngine("wangzhumo_engine")
                .backgroundMode(FlutterActivityLaunchConfigs.BackgroundMode.transparent)
                .build(this)
        )
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

