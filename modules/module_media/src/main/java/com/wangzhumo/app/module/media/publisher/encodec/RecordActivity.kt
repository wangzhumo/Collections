package com.wangzhumo.app.module.media.publisher.encodec

import android.media.MediaFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.alibaba.android.arouter.facade.annotation.Route
import com.wangzhumo.app.base.IRoute
import com.wangzhumo.app.module.media.R
import com.wangzhumo.app.origin.BaseActivity
import kotlinx.android.synthetic.main.activity_record.*
import java.io.File

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2020/3/20  10:45 AM
 *
 * 录制的 Activity
 */
@Route(path = IRoute.MEDIA.CAMERA_RECORD)
class RecordActivity : BaseActivity() {

    var recordMediaCodec: RecordMediaCodec? = null

    override fun getLayoutId(): Int = R.layout.activity_record


    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        bt_record.setOnClickListener {
            //开始录制
            if (recordMediaCodec == null) {
                //如果是空,那就是录制.
                recordMediaCodec = RecordMediaCodec(gl_camera_record.textureId)
                val file = getExternalFilesDir(null)
                val mediaPath = File((file.absolutePath + File.separator + "record"),"wangzhumo.mp4").absolutePath

                Log.e("Record",mediaPath)
                //初始化
                recordMediaCodec?.initEncoder(gl_camera_record.eglCore,
                    mediaPath,MediaFormat.MIMETYPE_VIDEO_AVC,720,1280)

                //开始录制
                recordMediaCodec?.startRecord()
                bt_record.text = "正在录制"
            }else{
                //应该停止录制
                recordMediaCodec?.stopRecord()
                recordMediaCodec = null
                bt_record.text = "开始录制"
            }
        }
    }
}
