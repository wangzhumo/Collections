package com.wangzhumo.app.module.media.opengl.camera

import android.annotation.SuppressLint
import android.graphics.SurfaceTexture
import android.util.DisplayMetrics
import android.util.Log
import android.util.Rational
import android.util.Size
import android.view.TextureView
import androidx.camera.core.CameraX
import androidx.camera.core.Preview
import androidx.camera.core.PreviewConfig
import androidx.lifecycle.LifecycleOwner
import com.wangzhumo.app.module.media.targets.utils.TextureUtils

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-11-21  17:24
 *
 * 需要LifeRecycler
 * 需要View
 */
class CameraOpenHelper constructor(private val lifeOwner: LifecycleOwner) :
    TextureView.SurfaceTextureListener{

    private var mLensFacing = CameraX.LensFacing.BACK
    private var textureEGLHelper: TextureEGLHelper = TextureEGLHelper()
    private var preview : Preview? = null
    private var viewFinder : TextureView? = null



    fun bindCameraUseCases(textureView: TextureView) {
        viewFinder = textureView
        viewFinder?.surfaceTextureListener = this
        // Make sure that there are no other use cases bound to CameraX
        CameraX.unbindAll()
        //sys display info
        val metrics = DisplayMetrics().also { viewFinder?.display?.getRealMetrics(it) }
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
            viewFinder?.display?.rotation?.let { setTargetRotation(it) }
        }.build()

        preview = Preview(viewFinderConfig)
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
        textureEGLHelper.onSurfaceChanged(width, height)
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
        onDestroy()
        return false
    }

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
        //当外部的TextureView可用之后，开启摄像头，打开渲染线程
        val textureId = TextureUtils.loadOESTexture()
        textureEGLHelper.initEGL(viewFinder,textureId)
        //不使用自己的SurfaceView，另外构建一个SurfaceView来接收Camera的预览数据
        preview?.setOnPreviewOutputUpdateListener {
            //此处是相机的绑定.
            textureEGLHelper.loadOESTexture(it.surfaceTexture)
        }
    }

}