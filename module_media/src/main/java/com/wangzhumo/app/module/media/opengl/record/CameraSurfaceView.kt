package com.wangzhumo.app.module.media.opengl.record

import android.content.Context
import android.graphics.SurfaceTexture
import android.hardware.Camera
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.util.Log
import android.view.Surface
import android.view.WindowManager
import com.wangzhumo.app.module.media.opengl.CameraManager
import com.wangzhumo.app.module.media.opengl.OnFBOSurfaceListener

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-12-25  20:32
 *
 * 预览
 */
class CameraSurfaceView : FBOSurfaceView, OnFBOSurfaceListener {

    constructor(ctx: Context) : this(ctx, null)
    constructor(ctx: Context, attrs: AttributeSet?) : this(ctx, attrs, 0)
    constructor(ctx: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(ctx, attrs, defStyleAttr) {
        render = FBORenderer(ctx)
        render.setFBOListener(this)
        cameraManager = CameraManager(ctx)
        renderer = render
        renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
        previewAngleAdjust(ctx)
        Log.d(TAG, "CameraSurfaceView  初始化")
    }


    private var render: FBORenderer
    private var cameraId = Camera.CameraInfo.CAMERA_FACING_BACK
    private var cameraManager: CameraManager
    private var fboTextureId = 0


    private fun previewAngleAdjust(ctx: Context) {
        val angle =
            (ctx.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.rotation
        render.resetMatrix()
        when (angle) {
            Surface.ROTATION_0 -> {
                if (cameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    render.setAngle(90F, 0F, 0F, 1F)
                    render.setAngle(180F, 1F, 0F, 0F)
                } else {
                    render.setAngle(90f, 0f, 0f, 1f)
                }
            }
            Surface.ROTATION_90 -> {
                if (cameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    render.setAngle(180F, 0F, 0F, 1F)
                    render.setAngle(180F, 0F, 1F, 0F)
                } else {
                    render.setAngle(90f, 0f, 0f, 1f)
                }
            }
            Surface.ROTATION_180 -> {
                if (cameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    render.setAngle(90f, 0.0f, 0f, 1f)
                    render.setAngle(180f, 0.0f, 1f, 0f)
                } else {
                    render.setAngle((-90).toFloat(), 0f, 0f, 1f)
                }
            }
            Surface.ROTATION_270 -> {
                if (cameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    render.setAngle(180f, 0.0f, 1f, 0f)
                } else {
                    render.setAngle(0f, 0f, 0f, 1f)
                }
            }
        }
    }


    override fun onSurfaceCreate(surfaceTexture: SurfaceTexture?, fboTextureId: Int) {
        Log.d(TAG, "CameraSurfaceView  onSurfaceCreate 开始预览")
        cameraManager.startCamera(surfaceTexture)
        this.fboTextureId = fboTextureId
    }

    override fun onFrameAvailable(surfaceTexture: SurfaceTexture?) {

    }


    companion object {
        const val TAG = "OpenGL Record"
    }
}