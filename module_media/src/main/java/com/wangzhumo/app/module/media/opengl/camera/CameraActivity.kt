package com.wangzhumo.app.module.media.opengl.camera

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.TextureView
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.camera.core.CameraX
import com.alibaba.android.arouter.facade.annotation.Route
import com.tencent.mars.xlog.Log
import com.wangzhumo.app.base.IRoute
import com.wangzhumo.app.module.media.R
import com.wangzhumo.app.origin.BaseActivity
import kotlinx.android.synthetic.main.activity_camera.*

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
//        val textureView = TextureView(this)
//        val layoutParams = FrameLayout.LayoutParams(
//            ViewGroup.LayoutParams.MATCH_PARENT,
//            ViewGroup.LayoutParams.MATCH_PARENT
//        )
//        container_ff.addView(viewFinder,layoutParams)

        //创建相机控制器
        Log.d(TAG,"com.wangzhumo.app.module.media.opengl.camera.CameraActivity","createTextureView",69,"创建帮助类 CameraOpenHelper()")
        val cameraOpenHelper = CameraOpenHelper(this)
        Log.d(TAG,"com.wangzhumo.app.module.media.opengl.camera.CameraActivity","createTextureView",71,"绑定TextureView  bindCameraUseCases（textureView）")
        cameraOpenHelper.bindCameraUseCases(viewFinder)
        //cameraOpenHelper.switchFacing(lensFacing)
    }


    companion object{
        const val TAG = "CameraActivity"
    }
}

