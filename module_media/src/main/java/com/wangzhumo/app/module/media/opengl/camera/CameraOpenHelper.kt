package com.wangzhumo.app.module.media.opengl.camera

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.SurfaceTexture
import android.hardware.Camera
import android.view.TextureView
import androidx.camera.core.CameraX
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import com.elvishew.xlog.XLog
import com.wangzhumo.app.module.media.targets.task3_1.CameraV1
import com.wangzhumo.app.module.media.targets.task3_1.ICamera
import com.wangzhumo.app.module.media.targets.utils.TextureUtils

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-11-21  17:24
 *
 * 需要LifeRecycler
 * 需要View
 */
class CameraOpenHelper constructor(private val lifeOwner: FragmentActivity) :
    TextureView.SurfaceTextureListener{

    private var mLensFacing = CameraX.LensFacing.BACK
    private var textureEGLHelper: TextureEGLHelper = TextureEGLHelper()
    private var viewFinder : TextureView? = null

    private var mCamera: ICamera? = null
    private var mCameraId = 0

    fun bindCameraUseCases(textureView: TextureView) {
        XLog.d("bindCameraUseCases  设置 surfaceTextureListener")
        this.viewFinder = textureView
        textureView.surfaceTextureListener = this
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
        XLog.d("onSurfaceTextureSizeChanged")
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
        XLog.d("onSurfaceTextureUpdated")
    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
        onDestroy()
        XLog.d("onSurfaceTextureDestroyed")
        return false
    }

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
        //当外部的TextureView可用之后，开启摄像头，打开渲染线程
        XLog.d("onSurfaceTextureAvailable")
        val textureId = TextureUtils.loadOESTexture()
        XLog.d("onSurfaceTextureAvailable  loadOESTexture = %d",textureId)
        textureEGLHelper.initEGL(viewFinder,textureId)
        XLog.d("onSurfaceTextureAvailable  initEGL")
        val surfaceTexture = textureEGLHelper.loadOESTexture()
        //不使用自己的SurfaceView，另外构建一个SurfaceView来接收Camera的预览数据
        //前置摄像头
        mCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT
        mCamera = CameraV1(lifeOwner)
        mCamera?.apply {
            openCamera(mCameraId)
            setPreviewTexture(surfaceTexture)
            enablePreview(true)
            XLog.d("onSurfaceTextureAvailable  enablePreview  开启摄像头")
        }
    }

}