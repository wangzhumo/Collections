package com.wangzhumo.app.playground

import android.util.Log
import com.alibaba.android.arouter.facade.annotation.Route
import com.wangzhumo.app.base.IRoute
import com.wangzhumo.app.base.utils.DensityUtils
import com.wangzhumo.app.origin.BaseActivity
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_fream_layout.*
import java.util.concurrent.TimeUnit
import kotlin.random.Random


@Route(path = IRoute.FRAME_ACTIVITY)
class FrameLayoutActivity : BaseActivity() {

    override fun getLayoutId(): Int = R.layout.activity_fream_layout


    override fun onResume() {
        super.onResume()
    }
}
