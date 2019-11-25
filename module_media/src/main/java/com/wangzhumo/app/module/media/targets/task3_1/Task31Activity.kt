package com.wangzhumo.app.module.media.targets.task3_1

import android.os.Bundle
import android.view.TextureView
import com.alibaba.android.arouter.facade.annotation.Route
import com.tencent.mars.xlog.Log
import com.wangzhumo.app.base.IRoute
import com.wangzhumo.app.module.media.R
import com.wangzhumo.app.origin.BaseActivity
import kotlinx.android.synthetic.main.activity_task31.*


/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-09-29  14:50
 */
@Route(path = IRoute.MEDIA_TASK_3_1)
class Task31Activity : BaseActivity() {

    lateinit var mTextureView: TextureView

    lateinit var mCameraPick: CameraV1Pick

    override fun getLayoutId(): Int = R.layout.activity_task31

    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        mTextureView = viewFinder
        Log.d(TAG,"com.wangzhumo.app.module.media.targets.task3_1.Task31Activity","initViews",29,"创建帮助类  CameraV1Pick()")
        mCameraPick = CameraV1Pick()
        Log.d(TAG,"com.wangzhumo.app.module.media.targets.task3_1.Task31Activity","initViews",32,"绑定TextureView bindTextureView")
        mCameraPick.bindTextureView(mTextureView)
    }


    override fun onDestroy() {
        super.onDestroy()
        mCameraPick.onDestroy()
    }

    companion object{
        const val TAG = "Task31Activity"
    }
}
