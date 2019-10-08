package com.wangzhumo.app.module.media.targets.task3_2

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.util.Rational
import android.util.Size
import android.view.TextureView
import androidx.camera.core.CameraX
import androidx.camera.core.PreviewConfig
import com.alibaba.android.arouter.facade.annotation.Route
import com.wangzhumo.app.base.IRoute
import com.wangzhumo.app.module.media.R
import com.wangzhumo.app.module.media.targets.task3.AutoFitPreviewBuilder
import com.wangzhumo.app.origin.BaseActivity
import kotlinx.android.synthetic.main.activity_task32.*

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-10-08  16:06
 */
@Route(path = IRoute.MEDIA_TASK_3_2)
class Task32Activity :  BaseActivity() {

    private lateinit var viewFinder : TextureView
    private var lensFacing = CameraX.LensFacing.BACK

    override fun getLayoutId(): Int = R.layout.activity_task32

    @SuppressLint("RestrictedApi")
    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        viewFinder = texture_view
        switch_camera.setOnCheckedChangeListener { _, _ ->
            lensFacing = if (CameraX.LensFacing.FRONT == lensFacing) {
                CameraX.LensFacing.BACK
            } else {
                CameraX.LensFacing.FRONT
            }
            try {
                // Only bind use cases if we can query a camera with this orientation
                CameraX.getCameraWithLensFacing(lensFacing)
                bindCameraUseCases()
            } catch (exc: Exception) {
                // Do nothing
            }
        }
    }


    private fun bindCameraUseCases() {
        // Make sure that there are no other use cases bound to CameraX
        CameraX.unbindAll()
        //sys display info
        val metrics = DisplayMetrics().also { viewFinder.display.getRealMetrics(it) }
        val screenSize = Size(metrics.widthPixels, metrics.heightPixels)
        val screenAspectRatio = Rational(metrics.widthPixels, metrics.heightPixels)

        Log.d(javaClass.simpleName, "Metrics: ${metrics.widthPixels} x ${metrics.heightPixels}")

        // Set up the view finder use case to display camera preview
        val viewFinderConfig = PreviewConfig.Builder().apply {
            // We provide an aspect ratio in case the exact resolution is not available
            setTargetAspectRatio(screenAspectRatio)
            // We request a specific resolution matching screen size
            setTargetResolution(screenSize)
            setLensFacing(lensFacing)
            setTargetRotation(viewFinder.display.rotation)
        }.build()


        // Use the auto-fit preview builder to automatically handle size and orientation changes
        val preview = AutoFitPreviewBuilder2.build(viewFinderConfig, viewFinder)

        // Apply declared configs to CameraX using the same lifecycle owner
        CameraX.bindToLifecycle(this, preview)
    }
}
