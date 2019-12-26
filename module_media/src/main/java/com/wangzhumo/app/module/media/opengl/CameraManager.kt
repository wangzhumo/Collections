package com.wangzhumo.app.module.media.opengl

import android.content.Context
import android.graphics.SurfaceTexture
import android.hardware.Camera
import android.util.Log
import com.wangzhumo.app.base.utils.DensityUtils

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-12-25  11:50
 *
 * 相机的管理类
 */
class CameraManager(ctx :Context) {

    var camera : Camera? = null
    var surfaceTexture : SurfaceTexture? = null

    //全局变量
    var width = 0
    var height = 0

    var cameraId = Camera.CameraInfo.CAMERA_FACING_BACK

    init {
        width = DensityUtils.getScreenWidth(ctx)
        height = DensityUtils.getScreenHeight(ctx)
        Log.d(TAG,"CameraManager 初始化")
    }


    /**
     * 开始预览
     * @param surfaceTexture
     */
    fun startCamera(surfaceTexture: SurfaceTexture?) {
        Log.d(TAG,"CameraManager startCamera 打开摄像头")
        this.surfaceTexture = surfaceTexture
        startCamera(cameraId)
    }


    /**
     * 停止预览
     */
    fun stopCamera() {
        Log.d(TAG,"CameraManager stopCamera 关闭摄像头")
        if (camera != null) {
            camera!!.stopPreview()
            camera!!.release()
            camera = null
        }
        camera?.let {
            it.stopPreview()
            it.release()
        }
        camera = null
    }


    /**
     * 切换摄像头
     */
    fun switchCamera(){
        val cameraNumber = Camera.getNumberOfCameras()
        var cameraInfo = Camera.CameraInfo()
        for (index in 0 until cameraNumber){
            Camera.getCameraInfo(index, cameraInfo)
            if (cameraId == Camera.CameraInfo.CAMERA_FACING_BACK){
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT){
                    stopCamera()
                    startCamera(index)
                    cameraId = index
                    break
                }
            }else{
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK){
                    stopCamera()
                    startCamera(index)
                    cameraId = index
                    break
                }
            }
        }
    }

    /**
     *
     * 打开摄像头.
     * @param cameraId
     */
    private fun startCamera(cameraId: Int) {
        //打开摄像头
        camera = Camera.open(cameraId)
        camera?.setPreviewTexture(surfaceTexture)

        //设置相机的参数
        camera?.parameters?.apply {
            val cameraSize = getCameraSize(supportedPreviewSizes,width,height,0.1)
            if(cameraSize != null){
                setPreviewSize(cameraSize.width, cameraSize.height)
                width = cameraSize.width
                height = cameraSize.height
                setPictureSize(cameraSize.width,cameraSize.height)
            }
        }
        camera?.startPreview()
        Log.d(TAG, "CameraManager startCamera camera = $camera  surfaceTexture = $surfaceTexture")
        Log.d(TAG,"CameraManager startCamera 摄像头开始预览")
    }

    /**
     * 获取相机硬件实际支持的预览大小、图像大小
     *
     * @param cameraSize List<Camera.Size>
     * @param width      屏幕宽
     * @param height     屏幕高
     * @param diff       加权重比
     * @return 实际支持的预览大小、图像大小
     */
    private fun getCameraSize(cameraSize: List<Camera.Size>?,
        width: Int, height: Int, diff: Double): Camera.Size? {
        var width = width
        var height = height
        var diff = diff
        if (width < height) {
            val temp = height
            height = width
            width = temp
        }
        val ratio = width.toDouble() / height
        if (cameraSize == null || cameraSize.isEmpty()) {
            Log.d(TAG, "getCameraSize: 获取相机预览数据失败")
            return null
        }
        var outputSize: Camera.Size? = null
        for (currentSize in cameraSize) {
            val currentRatio =
                currentSize.width.toDouble() / currentSize.height
            val currentDiff = Math.abs(currentRatio - ratio)
            if (currentDiff > diff) {
                continue
            }
            if (outputSize == null) {
                outputSize = currentSize
            } else {
                if (outputSize.width * outputSize.height < currentSize.width * currentSize.height) {
                    outputSize = currentSize
                }
            }
            diff = currentDiff
        }
        if (outputSize == null) {
            diff += 0.1
            outputSize = if (diff > 1.0f) {
                cameraSize[0]
            } else {
                getCameraSize(cameraSize, width, height, diff)
            }
        }
        return outputSize
    }

    /**
     * 返回当前缩放值
     *
     * @param scaleRatio 缩放系数， 0 表示不缩放，1 表示放大，2 表示缩小
     * @return int
     */
    fun getZoomValue(scaleRatio: Int): Int {
        val parameters = camera!!.parameters
        if (!parameters.isZoomSupported) {
            return -1
        }
        if (scaleRatio == 0) {
            return -1
        }
        val defaultZoom = parameters.zoom
        val zoomValue: Int
        if (scaleRatio == 1) {
            zoomValue = defaultZoom + 1
            if (zoomValue < parameters.maxZoom) {
                parameters.zoom = zoomValue
                camera!!.parameters = parameters
            }
        } else {
            zoomValue = defaultZoom - 1
            if (zoomValue >= 0) {
                parameters.zoom = zoomValue
                camera!!.parameters = parameters
            }
        }
        return zoomValue
    }

    /**
     * 设置缩放值
     *
     * @param zoomValue ：SeekBar拖动的值
     */
    fun setZoomValue(zoomValue: Int) {
        var zoomValue = zoomValue
        val parameters = camera!!.parameters
        if (!parameters.isZoomSupported) {
            return
        }
        Log.d(TAG, "setZoomValue: $zoomValue")
        Log.d(TAG, "setZoomValue: Camera最大缩放值" + getMaxZoomValue())
        if (zoomValue > getMaxZoomValue()) {
            zoomValue = getMaxZoomValue()
        }
        parameters.zoom = zoomValue
        camera!!.parameters = parameters
    }

    /**
     * 返回默认最大缩放值
     *
     * @return int
     */
    fun getMaxZoomValue(): Int {
        val parameters = camera!!.parameters
        return if (!parameters.isZoomSupported) {
            -1
        } else parameters.maxZoom
    }

    companion object{
       const val TAG = "OpenGL Record"
   }
}