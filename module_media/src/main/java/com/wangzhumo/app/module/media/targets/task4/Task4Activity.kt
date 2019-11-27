package com.wangzhumo.app.module.media.targets.task4

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.media.MediaExtractor
import android.media.MediaFormat
import android.media.MediaMuxer
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import com.alibaba.android.arouter.facade.annotation.Route
import com.tencent.mars.xlog.Log
import com.wangzhumo.app.base.IRoute
import com.wangzhumo.app.module.media.R
import com.wangzhumo.app.origin.BaseActivity
import kotlinx.android.synthetic.main.activity_task4.*
import sun.jvm.hotspot.utilities.IntArray
import java.io.File
import java.io.FileOutputStream
import java.net.URISyntaxException
import java.nio.ByteBuffer


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
    //准备写入的文件
    val OUTPUT_DIR = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        button_choose.setOnClickListener {
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


    /**
     * @param path 媒体文件地址
     *
     */
    fun extractorMedia(path: String) {
        val extractor = MediaExtractor()
        //设置来源
        extractor.setDataSource(path)
        //视频文件
        val videoStream = File(OUTPUT_DIR, "output_video_mp4.mp4").outputStream()
        //音频文件
        val audioStream = File(OUTPUT_DIR, "output_audio_mp4").outputStream()

        //记录音频/视屏轨道的index
        var videoTrackIndex = -1
        var audioTrackIndex = -1
        //获取媒体文件的track数量
        val numTracks = extractor.trackCount
        for (index in 0 until numTracks) {
            val format = extractor.getTrackFormat(index)
            val mimeType = format.getString(MediaFormat.KEY_MIME)
            Log.d(
                TAG,
                "com.wangzhumo.app.module.media.targets.task4.Task4Activity",
                "extractorMedia",
                61,
                format.toString()
            )
            //标记视频轨道
            if (mimeType.startsWith("video/")) {
                videoTrackIndex = index
            }
            //标记音频轨道
            if (mimeType.startsWith("audio/")) {
                audioTrackIndex = index
            }

            appendLogs("视频分离 - 任务开始")
            extractor.selectTrack(videoTrackIndex)
            readTrackStream(videoStream,extractor)
            videoStream.close()
            appendLogs("视频分离 - 任务结束")

            appendLogs("音频分离 - 任务开始")
            extractor.selectTrack(audioTrackIndex)
            readTrackStream(audioStream,extractor)
            audioStream.close()
            appendLogs("音频分离 - 任务结束")
        }
        appendLogs("任务结束")
        Log.d(TAG,"com.wangzhumo.app.module.media.targets.task4.Task4Activity","extractorMedia",107,"任务结束")
        extractor.release()
    }

    /**
     * 读取数据，写入到文件中
     */
    private fun readTrackStream(fileInput: FileOutputStream, extractor: MediaExtractor) {
        //Buffer
        val byteBuffer = ByteBuffer.allocate(1024 * 500)
        var readCount = 0
        do {
            //读取数据
            readCount = extractor.readSampleData(byteBuffer,0)
            if (readCount < 0){
                break
            }
            //写入文件
            val buffer = ByteArray(readCount)
            byteBuffer.get(buffer)
            fileInput.write(buffer)
            byteBuffer.clear()
            //移动到下一帧
            extractor.advance()
        }while (readCount > 0)
        byteBuffer.clear()
    }


    /**
     * 合并音视频
     */
    fun muxerMediaStream(){
        val mOutputVideoPath = File(OUTPUT_DIR, "output_mp4_file.mp4").absolutePath
        //指定输出目录，指定Format格式
        val mMediaMuxer = MediaMuxer(
            mOutputVideoPath,
            MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4
        )

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
                    extractorMedia(path)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun appendLogs(log: String) {
        Log.d(
            TAG,
            "com.wangzhumo.app.module.media.targets.task4.Task4Activity",
            "appendLogs",
            97,
            "[$lines].$log"
        )
        lines++
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
