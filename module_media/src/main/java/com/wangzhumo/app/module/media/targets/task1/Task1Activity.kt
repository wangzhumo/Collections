package com.wangzhumo.app.module.media.targets.task1

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.alibaba.android.arouter.facade.annotation.Route
import com.wangzhumo.app.base.DensityUtils
import com.wangzhumo.app.base.IRoute
import com.wangzhumo.app.module.media.R
import com.wangzhumo.app.origin.BaseActivity
import kotlinx.android.synthetic.main.activity_task1.*


/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-08-22  19:26
 *
 * Task1:
 *    SurfaceView显示Image
 *
 *    1.初始化SurfaceView
 *    2.当SurfaceView可用 - 调起绘制
 *    3.绘制图片  lockCanvans
 */
@Route(path = IRoute.MEDIA_TASK_1)
class Task1Activity : BaseActivity(), SurfaceHolder.Callback {

    val TAG = "Task1Activity"


    override fun getLayoutId(): Int = R.layout.activity_task1

    private lateinit var mBitmapOrigin: Bitmap
    lateinit var mSurfaceHolder: SurfaceHolder


    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        //load images
        mBitmapOrigin = scaleBitmap(DensityUtils.getScreenWidth(this).toFloat())
        //初始化SurfaceView
        initSurface(surface_view)
    }


    /**
     * 初始化SurfaceView、
     */
    private fun initSurface(surfaceView: SurfaceView) {
        surfaceView.setZOrderOnTop(true)
        //获取Holder
        mSurfaceHolder = surfaceView.holder
        mSurfaceHolder.addCallback(this)
        Log.e(TAG,"drawImage")
    }


    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {}

    override fun surfaceDestroyed(holder: SurfaceHolder?) {}

    override fun surfaceCreated(holder: SurfaceHolder?) {
        //start draw image
        drawImage(holder)
        Log.e(TAG,"surfaceCreated")
    }

    private fun drawImage(holder: SurfaceHolder?) {
        Log.e(TAG,"drawImage")
        val canvas = holder?.lockCanvas()
        canvas?.drawColor(Color.WHITE)
        canvas?.drawBitmap(mBitmapOrigin,0F,0F,null)
        holder?.unlockCanvasAndPost(canvas)
    }


    /**
     * 根据给定的宽和高进行拉伸
     *
     * @param scaleWidth  图的宽
     * @return new Bitmap
     */
    private fun scaleBitmap(scaleWidth: Float): Bitmap {
        //load info
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(resources, R.mipmap.ic_over_watch, options)

        //calculate
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        //ratio
        val ratio = width / height
        val scaleHeight = scaleWidth / ratio
        //获取Bitmap
        if (height > scaleHeight || width > scaleWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2
            //计算inSampleSize直到缩放后的宽高都小于指定的宽高
            while ((halfHeight / inSampleSize) >= scaleHeight && (halfWidth / inSampleSize) >= scaleWidth) {
                inSampleSize *= 2
            }
        }
        options.inSampleSize = inSampleSize
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeResource(resources, R.mipmap.ic_over_watch, options)
    }

}
