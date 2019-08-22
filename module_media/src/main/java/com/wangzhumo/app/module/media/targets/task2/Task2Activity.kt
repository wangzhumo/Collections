package com.wangzhumo.app.module.media.targets.task2

import android.os.Bundle
import android.text.TextUtils
import com.wangzhumo.app.module.media.R
import com.wangzhumo.app.origin.BaseActivity
import kotlinx.android.synthetic.main.activity_task2.*
import java.io.File


/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-08-22  21:36
 *
 * Task2:
 *    AudioRecord & AudioTrack
 *
 */
class Task2Activity : BaseActivity() {
    override fun getLayoutId(): Int = R.layout.activity_task2

    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        button_record.setOnClickListener {
            if (TextUtils.equals(button_record.text,"Record")){
                button_record.text = "Stop"
                startRecord()
            }else{
                button_record.text = "Record"
                stopRecord()
            }
        }
    }


    /**
     * 停止录制
     */
    private fun stopRecord() {
        val recordPath = "$filesDir/record"
        val recordFile = File(recordPath)
        if (!recordFile.exists()){
            recordFile.mkdirs()
        }
        var tempFile = File(recordFile,"${System.currentTimeMillis()}.pcm")

    }

    /**
     * 开始录制
     */
    private fun startRecord() {
        //
    }
}
