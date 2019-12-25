package com.wangzhumo.app.module.media.opengl.record

import android.content.Context
import android.util.AttributeSet
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import java.util.jar.Attributes

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-12-25  14:53
 *
 * 为了使用FBO录制,自定的提供EGL环境的View,用于预览相机采集的数据
 */
class FBOSurfaceView @JvmOverloads constructor(ctx: Context,attrs : AttributeSet,defaultAttr: Int) : SurfaceView(ctx,attrs,defaultAttr) , SurfaceHolder.Callback{

    var surface: Surface? = null

    init {
        //添加一个监听.
        holder.addCallback(this)
    }






    override fun surfaceCreated(holder: SurfaceHolder?) {
        //初始化好,surface就可用了
        this.surface  = holder?.surface

    }


    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {

    }


}