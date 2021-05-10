package com.wangzhumo.app

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.jakewharton.rxbinding4.view.clicks
import com.tbruyelle.rxpermissions3.RxPermissions
import com.tencent.mars.xlog.Log
import com.wangzhumo.app.base.IRoute
import com.wangzhumo.app.databinding.ActivityMainBinding
import com.wangzhumo.app.origin.base.BaseBindingActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers


@Route(path = IRoute.APP_MAIN)
public class MainActivity : BaseBindingActivity<ActivityMainBinding>() {

    private val TAG = "MainActivity"


    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        vBinding.btAnr.clicks()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                startActivity(Intent(this,SecondActivity::class.java))
            }

        vBinding.btMedia.clicks()
            .subscribeOn(AndroidSchedulers.mainThread())
            .flatMap {
                RxPermissions(this).request(
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO
                )
            }
            .subscribe {
                if (it) {
                    // 如果完成，则点击事件
                    ARouter.getInstance()
                        .build(IRoute.MEDIA_MAIN)
                        .navigation()
                } else {
                    Toast.makeText(this, "请授权", Toast.LENGTH_LONG).show()
                }
            }


        vBinding.btOpengl.clicks()
            .subscribeOn(AndroidSchedulers.mainThread())
            .flatMap {
                RxPermissions(this).request(
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO
                )
            }
            .subscribe {
                if (it) {
                    // 如果完成，则点击事件
                    ARouter.getInstance()
                        .build(IRoute.OPENGL_LIST)
                        .navigation()
                } else {
                    Toast.makeText(this, "请授权", Toast.LENGTH_LONG).show()
                }
            }
        vBinding.btPlayground.setOnClickListener {
            ARouter.getInstance()
                .build(IRoute.FRAME_ACTIVITY)
                .navigation()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG, "onDestroy")
    }

}

