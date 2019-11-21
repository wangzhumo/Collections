package com.wangzhumo.app.module.media.opengl.camera

import android.annotation.SuppressLint
import android.graphics.SurfaceTexture
import android.util.DisplayMetrics
import android.util.Log
import android.util.Rational
import android.util.Size
import android.view.TextureView
import android.view.View
import androidx.camera.core.CameraX
import androidx.camera.core.Preview
import androidx.camera.core.PreviewConfig
import androidx.lifecycle.LifecycleOwner

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-11-21  17:24
 *
 * 需要LifeRecycler
 * 需要View
 */
class CameraOpenHelper constructor(private val viewFinder: View,private val lifeOwner: LifecycleOwner) :
    TextureView.SurfaceTextureListener{

    private var mLensFacing = CameraX.LensFacing.BACK

    init {
        //初始化一些东西
        //1.GLThread
        //2. 加载loadOESTexture
        //2. 创建Renderer Thread


    }

    fun bindCameraUseCases() {
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
            setLensFacing(mLensFacing)
            setTargetRotation(viewFinder.display.rotation)
        }.build()

        val useCase = Preview(viewFinderConfig)
        useCase.setOnPreviewOutputUpdateListener {
            //此处是相机的绑定.
        }

        // Apply declared configs to CameraX using the same lifecycle owner
        CameraX.bindToLifecycle(lifeOwner, useCase)
    }


    /**
     * 修改摄像头方向.
     */
    @SuppressLint("RestrictedApi")
    fun switchFacing(lensface: CameraX.LensFacing) {
        if (mLensFacing != lensface) {
            //如果一致，需要修改
            mLensFacing = lensface
            CameraX.getCameraWithLensFacing(lensface)
            bindCameraUseCases()
        }
    }

    /**
     * 销毁资源
     */
    fun onDestroy(){
        //销毁.
        CameraX.unbind()
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {

    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
        return false

    }

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {

    }


}