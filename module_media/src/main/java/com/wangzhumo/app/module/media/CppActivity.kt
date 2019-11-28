package com.wangzhumo.app.module.media

import android.os.Bundle

import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.wangzhumo.app.base.IRoute
import com.wangzhumo.app.origin.BaseActivity
import kotlinx.android.synthetic.main.activity_cpp.*

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-04-20  12:33
 *
 * CPP TEST
 */
@Route(path = IRoute.JNI_CPP)
class CppActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_cpp
    }


    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        button_task1.setOnClickListener {
            ARouter.getInstance()
                .build(IRoute.MEDIA_TASK_1)
                .navigation()
            finish()
        }

        button_task2.setOnClickListener {
            ARouter.getInstance()
                .build(IRoute.MEDIA_TASK_2)
                .navigation()
            finish()
        }

        button_task3.setOnClickListener {
            ARouter.getInstance()
                .build(IRoute.MEDIA_OPENGL_CAMERA)
                .navigation()
            finish()
        }

        button_task4.setOnClickListener {
            ARouter.getInstance()
                .build(IRoute.MEDIA_TASK_4)
                .navigation()
            finish()
        }

        button_task5.setOnClickListener {
            ARouter.getInstance()
                .build(IRoute.MEDIA_OPENGL_1)
                .navigation()
            finish()
        }

    }
}
