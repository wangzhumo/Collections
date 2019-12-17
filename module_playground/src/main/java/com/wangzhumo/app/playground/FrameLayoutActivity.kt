package com.wangzhumo.app.playground

import com.alibaba.android.arouter.facade.annotation.Route
import com.squareup.picasso.Picasso
import com.wangzhumo.app.base.IRoute
import com.wangzhumo.app.origin.BaseActivity
import kotlinx.android.synthetic.main.activity_fream_layout.*


@Route(path = IRoute.FRAME_ACTIVITY)
class FrameLayoutActivity : BaseActivity() {

    override fun getLayoutId(): Int = R.layout.activity_fream_layout


    override fun onResume() {
        super.onResume()
        semicircle_layout.addOnImageLoader { imageView, iData ->

            Picasso.with(this).load("https://wangzhumo.com/img/avatar.png").into(imageView)

        }


    }
}
