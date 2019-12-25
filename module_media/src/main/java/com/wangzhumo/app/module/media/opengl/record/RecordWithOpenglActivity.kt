package com.wangzhumo.app.module.media.opengl.record

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.wangzhumo.app.base.IRoute
import com.wangzhumo.app.module.media.R
import com.wangzhumo.app.origin.BaseActivity

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-12-25  11:46
 *
 * 使用fbo录制视屏
 */
@Route(path = IRoute.MEDIA_OPENGL_RECORD)
class RecordWithOpenglActivity : BaseActivity() {
    override fun getLayoutId(): Int = R.layout.activity_record_with_opengl

    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)

    }

}
