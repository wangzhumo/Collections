package com.wangzhumo.app.playground

import android.media.MediaPlayer
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.wangzhumo.app.base.IRoute
import com.wangzhumo.app.origin.BaseActivity


@Route(path = IRoute.FRAME_ACTIVITY)
class FrameLayoutActivity : BaseActivity() {
    override fun getLayoutId(): Int = R.layout.activity_fream_layout

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        var mediaPlayer: MediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource("/storage/emulated/0/Android/data/io.liuliu.music/files/audio/e968c8b31b2d51809936e60722892ad9_res/yinyu_audios_part7/warfare_bgm.m4a")
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            mediaPlayer.start()
        }
    }
}
