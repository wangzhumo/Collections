package com.wangzhumo.app

import android.os.Bundle
import com.wangzhumo.app.origin.BaseActivity
import com.wangzhumo.app.utils.SoundPoolUtils

class MainActivity : BaseActivity() {


    override fun getLayoutId(): Int = R.layout.activity_main


    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        SoundPoolUtils.get().play(this)
    }



}
