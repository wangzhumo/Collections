package com.wangzhumo.app.module.media.targets.task4

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.media.MediaExtractor
import android.net.Uri
import android.os.Bundle
import android.widget.MediaController
import android.widget.Toast
import com.alibaba.android.arouter.facade.annotation.Route
import com.tencent.mars.xlog.Log
import com.wangzhumo.app.base.IRoute
import com.wangzhumo.app.module.media.R
import com.wangzhumo.app.origin.BaseActivity
import kotlinx.android.synthetic.main.activity_task32.*
import kotlinx.android.synthetic.main.activity_task4.*
import java.net.URISyntaxException


/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-11-26  19:41
 */
@Route(path = IRoute.MEDIA_TASK_4)
class Task4Activity : BaseActivity() {


    override fun getLayoutId(): Int = R.layout.activity_task4
    val stringBuffer = StringBuffer()
    var lines = 0


    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        button_choose.setOnClickListener {
            //开始选择
            appendLogs("开始选择文件")
            showFileChooser()
        }

    }

    private val FILE_SELECT_CODE = 0
    private fun showFileChooser() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/mp4"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        try {
            startActivityForResult(
                Intent.createChooser(intent, "Select a File to Upload"),
                FILE_SELECT_CODE
            )
        } catch (ex: ActivityNotFoundException) { // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.", Toast.LENGTH_SHORT).show()
        }
    }


    fun playVideo(path :String){
//        videoPlayer.setAspectRatio(path)
//        videoPlayer.setMediaController(AndroidMediaController(this))
//        videoPlayer.seekTo(0)
//        videoPlayer.requestFocus()
//        videoPlayer.start()
    }


    /**
     * @param path 媒体文件地址
     *
     */
    fun extractorMedia(path: String) {
        val extractor = MediaExtractor()
        //设置来源
        extractor.setDataSource(path)
        //获取媒体文件的track数量
        val numTracks = extractor.trackCount
        for (index in 0 until numTracks){
            val format = extractor.getTrackFormat(index)
            Log.d(TAG,"com.wangzhumo.app.module.media.targets.task4.Task4Activity","extractorMedia",61,format.toString())
            //获取到你想要使用的Track
            appendLogs(format.toString())
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            FILE_SELECT_CODE -> if (resultCode == Activity.RESULT_OK) { // Get the Uri of the selected file
                val uri = data?.data
                // Get the path
                if (uri != null) {
                    val path: String = getPath(this, uri)
                    appendLogs(path)
                    //extractorMedia(path)
                    playVideo(path)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun appendLogs(log : String){
        Log.d(TAG,"com.wangzhumo.app.module.media.targets.task4.Task4Activity","appendLogs",97,"[$lines].$log")
        lines ++
        stringBuffer.append("[$lines].").append(log).append("\n")
        textView.text = stringBuffer.toString()
    }


    private val TAG = "ChooseFile"
    @Throws(URISyntaxException::class)
    fun getPath(context: Context, uri: Uri): String {
        if ("content".equals(uri.scheme, ignoreCase = true)) {
            val projection = arrayOf("_data")
            var cursor: Cursor? = null
            try {
                cursor = context.contentResolver.query(uri, projection, null, null, null)
                val column_index: Int = cursor.getColumnIndexOrThrow("_data")
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index)
                }
            } catch (e: Exception) { // Eat it  Or Log it.
            }
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return ""
    }
}
