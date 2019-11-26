package com.wangzhumo.app.module.media.opengl.camera

import android.annotation.SuppressLint
import android.graphics.SurfaceTexture
import android.view.TextureView
import androidx.camera.core.CameraX
import androidx.fragment.app.FragmentActivity
import com.tencent.mars.xlog.Log
import com.wangzhumo.app.module.media.targets.utils.TextureUtils

//import com.wangzhumo.app.module.media.targets.task3_1.TextureEGLHelper

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
    private var textureEGLHelper: TextureEGLHelper? = null
    private var viewFinder : TextureView? = null

    private var mCameraId = 0

    fun bindCameraUseCases(textureView: TextureView) {
        this.viewFinder = textureView
        Log.d(TAG,"com.wangzhumo.app.module.media.opengl.camera.CameraOpenHelper","bindCameraUseCases",39,"创建EGLHelper TextureEGLHelper（）")
        this.textureEGLHelper = TextureEGLHelper()
        Log.d(TAG,"com.wangzhumo.app.module.media.opengl.camera.CameraOpenHelper","bindCameraUseCases",36,"添加 surfaceTextureListener")
        textureView.surfaceTextureListener = this
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {
        textureEGLHelper?.onSurfaceChanged(width, height)
        Log.d(TAG,"com.wangzhumo.app.module.media.opengl.camera.CameraOpenHelper","onSurfaceTextureSizeChanged",68,"onSurfaceTextureSizeChanged")
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {}

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
        onDestroy()
        Log.d(TAG,"com.wangzhumo.app.module.media.opengl.camera.CameraOpenHelper","onSurfaceTextureDestroyed",77,"onSurfaceTextureDestroyed")
        return false
    }

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
        //当外部的TextureView可用之后，开启摄像头，打开渲染线程
        val textureId = TextureUtils.loadOESTexture()
        Log.d(TAG,"com.wangzhumo.app.module.media.opengl.camera.CameraOpenHelper","onSurfaceTextureAvailable",85,"onSurfaceTextureAvailable  loadOESTexture = %d",textureId)
        textureEGLHelper?.initEGL(viewFinder,textureId)
        //通过传递的textureId,构建一个SurfaceTexture，用于相机的预览
        val surfaceTexture = textureEGLHelper?.loadOESTexture()
        //不使用自己的SurfaceView，另外构建一个SurfaceView来接收Camera的预览数据
        //前置摄像头
//        mCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT
//        mCamera = CameraV1(lifeOwner)
//        mCamera?.apply {
//            openCamera(mCameraId)
//            setPreviewTexture(surfaceTexture)
//            enablePreview(true)
//            Log.d(TAG,"com.wangzhumo.app.module.media.opengl.camera.CameraOpenHelper","onSurfaceTextureAvailable",95,"onSurfaceTextureAvailable  enablePreview  开启摄像头")
//        }
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

    companion object{
        const val TAG = "CameraOpenHelper"
    }
}