package com.wangzhumo.app.module.media.publisher.camera

import android.graphics.SurfaceTexture
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.TextureView
import com.alibaba.android.arouter.facade.annotation.Route
import com.wangzhumo.app.base.IRoute
import com.wangzhumo.app.mdeia.CameraManager
import com.wangzhumo.app.module.media.R
import com.wangzhumo.app.origin.BaseActivity
import kotlinx.android.synthetic.main.activity_glcamera.*


/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2020-01-17  17:29
 *
 * 相机预览
 */
@Route(path = IRoute.MEDIA.CAMERA_SHOW)
class GLCameraActivity : BaseActivity() {

    override fun getLayoutId(): Int = R.layout.activity_glcamera

    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        gl_camera_bt.setOnClickListener {
            gl_surfaceView.switchCamera()
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        gl_surfaceView.release()
    }

}
