package com.wangzhumo.app.module.opengl.justshow

import android.graphics.PixelFormat
import android.opengl.GLES20
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import androidx.viewbinding.ViewBinding
import com.alibaba.android.arouter.facade.annotation.Route
import com.wangzhumo.app.base.IRoute
import com.wangzhumo.app.mdeia.gles.EGLCore
import com.wangzhumo.app.mdeia.gles.WindowSurface
import com.wangzhumo.app.origin.utils.DensityUtils
import com.wangzhumo.app.module.opengl.R
import com.wangzhumo.app.origin.BaseActivity
import kotlinx.android.synthetic.main.activity_just_show.*

@Route(path = IRoute.OPENGL.JUST_SHOW)
class JustShowActivity<ActivityJustShowBinding : ViewBinding> : BaseActivity<ActivityJustShowBinding>() ,SurfaceHolder.Callback {



    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        val holder = surfaceView.holder
        surfaceView.setZOrderOnTop(true)
        holder.setFormat(PixelFormat.TRANSLUCENT)
        holder.addCallback(this)
    }

    override fun surfaceChanged(
        holder: SurfaceHolder?,
        format: Int,
        width: Int,
        height: Int
    ) {

        Log.d(EGLCore.TAG,"surfaceChanged ")

    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        Log.d(EGLCore.TAG,"surfaceDestroyed ")
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        Log.d(EGLCore.TAG,"surfaceCreated ")
        Thread {
            val eglHelper = EGLCore(
                null,
                EGLCore.FLAG_TRY_GLES3
            )
            val windowSurface = WindowSurface(
                eglHelper,
                holder?.surface,
                false
            )
            windowSurface.makeCurrent()
            GLES20.glViewport(0, 0,
                DensityUtils.getScreenWidth(this),
                DensityUtils.getScreenHeight(this))
            while (true){

                GLES20.glClearColor(1F,0F,0F,0.3F)
                GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

                windowSurface.swapBuffers()

                try{
                    Thread.sleep(16)
                }catch (e : InterruptedException){}

            }
        }.start()
    }
}
