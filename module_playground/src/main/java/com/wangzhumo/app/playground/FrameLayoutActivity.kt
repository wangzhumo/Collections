package com.wangzhumo.app.playground

import com.alibaba.android.arouter.facade.annotation.Route
import com.squareup.picasso.Picasso
import com.wangzhumo.app.base.IRoute
import com.wangzhumo.app.circle.ImageData
import com.wangzhumo.app.origin.BaseActivity
import kotlinx.android.synthetic.main.activity_fream_layout.*


@Route(path = IRoute.FRAME_ACTIVITY)
class FrameLayoutActivity : BaseActivity() {

    override fun getLayoutId(): Int = R.layout.activity_fream_layout


    override fun onResume() {
        super.onResume()
        semicircle_layout.addOnImageLoader { imageView, iData ->
            Picasso.with(imageView.context).load(iData.imageUrl).into(imageView)
        }

        semicircle_layout.setCircleData(listOf(
            ImageData("https://wangzhumo.com/img/avatar.png"),
            ImageData("https://wangzhumo.com/img/android.png"),
            ImageData("https://wangzhumo.com/img/webrtc.png"),
            ImageData("https://wangzhumo.com/img/media.png"),
            ImageData("https://wangzhumo.com/img/ffmpeg.png")
        ))


    }
}
