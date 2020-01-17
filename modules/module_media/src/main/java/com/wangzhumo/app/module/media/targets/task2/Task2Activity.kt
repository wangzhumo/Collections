package com.wangzhumo.app.module.media.targets.task2

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import com.alibaba.android.arouter.facade.annotation.Route
import com.wangzhumo.app.base.IRoute
import com.wangzhumo.app.module.media.R
import com.wangzhumo.app.origin.BaseActivity
import kotlinx.android.synthetic.main.activity_task2.*
import java.io.File
import java.lang.StringBuilder


/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-08-22  21:36
 *
 * Task2:
 *    AudioRecord & AudioTrack
 *
 */
@Route(path = IRoute.MEDIA_TASK_2)
class Task2Activity : BaseActivity() , AudioRecordHelper.onRecordListener{

    val TAG = "Task2Activity"
    lateinit var file :File
    val sb = StringBuilder()

    override fun getLayoutId(): Int = R.layout.activity_task2

    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        button_record.setOnClickListener {
            if (TextUtils.equals(button_record.text, "Record")) {
                button_record.text = "Stop"
                startRecord()
            } else {
                button_record.text = "Record"
                stopRecord()
            }
        }

        button_play.setOnClickListener {
            if (TextUtils.equals(button_play.text, "Play")) {
                button_play.text = "Stop"
                Log.e(TAG,"StartPlay File = " + file.absoluteFile)
                AudioTrackHelper.getInstance().startPlay(file)
            } else {
                button_play.text = "Play"
                AudioTrackHelper.getInstance().stopPlay()
            }
        }
    }


    /**
     * 停止录制
     */
    private fun stopRecord() {
        AudioRecordHelper.getInstance().stopRecord()
    }

    /**
     * 开始录制
     */
    private fun startRecord() {
        val recordPath = "${getExternalFilesDir(null).absolutePath}/record"
        val recordFile = File(recordPath)
        if (!recordFile.exists()) {
            recordFile.mkdirs()
        }
        file = File(recordFile, "${System.currentTimeMillis()}.pcm")
        AudioRecordHelper.getInstance().setOnRecordListener(this)
        AudioRecordHelper.getInstance().setOutPutFile(file)
        AudioRecordHelper.getInstance().startRecord()
    }


    override fun onRecording() {
        Log.e(TAG, "AudioRecordHelper -----StartRecord-----")
    }

    override fun onRecorded(pcm: File) {
        sb.append(pcm.absolutePath)
        sb.append("\n")
        AudioRecordHelper.getInstance().covertPcm2Wav()
        this.runOnUiThread {
            record_path.text = sb.toString()
        }
        Log.e(TAG, "AudioRecordHelper -----onRecorded----- pcm = " + pcm.absoluteFile)
    }

    override fun onWavFile(wav: File) {
        file = wav
        sb.append(wav.absolutePath)
        sb.append("\n")
        this.runOnUiThread {
            record_path.text = sb.toString()
        }
        Log.e(TAG, "AudioRecordHelper -----onWavFile----- wav = " + wav.absoluteFile)
    }
}
