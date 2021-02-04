package com.wangzhumo.app.module.media.publisher.camera

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.wangzhumo.app.base.IRoute
import com.wangzhumo.app.module.media.databinding.ActivityGlcameraBinding
import com.wangzhumo.app.origin.base.BaseBindingActivity


/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2020-01-17  17:29
 *
 * 相机预览
 */
@Route(path = IRoute.MEDIA.CAMERA_SHOW)
class GLCameraActivity : BaseBindingActivity<ActivityGlcameraBinding>() {


    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        vBinding.glCameraBt.setOnClickListener {
            vBinding.glSurfaceView.switchCamera()
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        vBinding.glSurfaceView.release()
    }

}
