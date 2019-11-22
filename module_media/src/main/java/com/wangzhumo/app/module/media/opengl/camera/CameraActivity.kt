package com.wangzhumo.app.module.media.opengl.camera

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.util.Rational
import android.util.Size
import android.view.TextureView
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.camera.core.CameraX
import androidx.camera.core.Preview
import androidx.camera.core.PreviewConfig
import com.alibaba.android.arouter.facade.annotation.Route
import com.wangzhumo.app.base.IRoute
import com.wangzhumo.app.module.media.R
import com.wangzhumo.app.module.media.targets.task3.AutoFitPreviewBuilder
import com.wangzhumo.app.origin.BaseActivity
import kotlinx.android.synthetic.main.activity_camera.*
import kotlinx.android.synthetic.main.activity_camera.switch_camera
import kotlinx.android.synthetic.main.activity_task3.*

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-11-19  17:07
 *
 * 相机预览显示.
 */
@Route(path = IRoute.MEDIA_OPENGL_CAMERA)
class CameraActivity : BaseActivity() {

    private var lensFacing = CameraX.LensFacing.BACK

    override fun getLayoutId(): Int = R.layout.activity_camera


    @SuppressLint("RestrictedApi")
    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        createTextureView()
//        switch_camera.setOnCheckedChangeListener { buttonView, isChecked ->
//            lensFacing = if (CameraX.LensFacing.FRONT == lensFacing) {
//                CameraX.LensFacing.BACK
//            } else {
//                CameraX.LensFacing.FRONT
//            }
//            try {
//                // Only bind use cases if we can query a camera with this orientation
//                CameraX.getCameraWithLensFacing(lensFacing)
//            } catch (exc: Exception) {
//                // Do nothing
//            }
//        }
    }

    private fun createTextureView() {
        val textureView = TextureView(this)
        val layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        container_ff.addView(textureView,layoutParams)

        //创建相机控制器
        val cameraOpenHelper = CameraOpenHelper(this)
        cameraOpenHelper.bindCameraUseCases(textureView)
        //cameraOpenHelper.switchFacing(lensFacing)
    }


}
