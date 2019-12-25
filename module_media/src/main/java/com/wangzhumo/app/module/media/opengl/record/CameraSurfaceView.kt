package com.wangzhumo.app.module.media.opengl.record

import android.content.Context
import android.graphics.SurfaceTexture
import android.opengl.GLSurfaceView
import android.util.AttributeSet
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
class CameraSurfaceView @JvmOverloads constructor(
    ctx: Context,
    attrs: AttributeSet?,
    defaultAttr: Int = 0
) : FBOSurfaceView(ctx, attrs, defaultAttr) , OnFBOSurfaceListener{

    private var cameraManager: CameraManager = CameraManager(ctx)
    var fboTextureId = 0


    init {
        renderer = FBORenderer(ctx)
        (renderer as FBORenderer).setFBOListener(this)
        renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
        //previewAngleAdjust(ctx)
    }

    private fun previewAngleAdjust(ctx: Context) {
        var angle = (ctx.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.rotation
        (renderer as FBORenderer).resetMatrix()
        when(angle){
            Surface.ROTATION_0 -> {

            }
        }
    }


    override fun onSurfaceCreate(surfaceTexture: SurfaceTexture?, fboTextureId: Int) {
        cameraManager.startCamera(surfaceTexture)
        this.fboTextureId = fboTextureId
    }

    override fun onFrameAvailable(surfaceTexture: SurfaceTexture?) {

    }
}