package com.wangzhumo.app.module.media.targets.task3

import android.os.Bundle
import com.wangzhumo.app.module.media.R
import com.wangzhumo.app.origin.BaseActivity

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-08-23  12:12
 *
 */
class Task3Activity : BaseActivity() {
    override fun getLayoutId(): Int = R.layout.activity_task3

    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        var cameraEnumerator : CameraEnumerator

        if (Camera2Enumerator.isSupported(this)) {
            cameraEnumerator = Camera2Enumerator(this)
        }else{
            cameraEnumerator = Camera1Enumerator()
        }
    }

}
