package com.wangzhumo.app.module.media.targets.task1

import android.graphics.*
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.alibaba.android.arouter.facade.annotation.Route
import com.wangzhumo.app.base.utils.DensityUtils
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
 *    3.绘制图片  lockCanvas
 */
@Route(path = IRoute.MEDIA_TASK_1)
class Task1Activity : BaseActivity(), SurfaceHolder.Callback {

    val TAG = "Task1Activity"
    var srcRect = Rect()
    var dstRect = Rect()

    override fun getLayoutId(): Int = R.layout.activity_task1

    private lateinit var mBitmapOrigin: Bitmap
    lateinit var mSurfaceHolder: SurfaceHolder


    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        //load images
        val mOrigin = BitmapFactory.decodeResource(resources, R.mipmap.ic_over_watch)
        mBitmapOrigin = scaleBitmap(mOrigin, DensityUtils.getScreenWidth(this).toFloat())
//        mBitmapOrigin = BitmapFactory.decodeResource(resources, R.mipmap.ic_over_watch)
//        srcRect.set(0, 0, mBitmapOrigin.width, mBitmapOrigin.height)
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
        Log.e(TAG, "drawImage")
    }


    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {}

    override fun surfaceDestroyed(holder: SurfaceHolder?) {}

    override fun surfaceCreated(holder: SurfaceHolder?) {
        //start draw image
        drawImage(holder)
        Log.e(TAG, "surfaceCreated")
    }

    private fun drawImage(holder: SurfaceHolder?) {
        Log.e(TAG, "drawImage")
        val canvas = holder?.lockCanvas()
        canvas?.drawColor(Color.WHITE)
        canvas?.drawBitmap(mBitmapOrigin, srcRect, dstRect, null)
        holder?.unlockCanvasAndPost(canvas)
    }


    /**
     * 根据给定的宽和高进行拉伸
     *
     * @param scaleWidth  图的宽
     * @return new Bitmap
     */
    private fun scaleBitmap(mOrigin: Bitmap, scaleWidth: Float): Bitmap {
        //calculate
        val height = mOrigin.height
        val width = mOrigin.width
        Log.e(TAG, "height = $height   width = $width")
        //ratio
        val ratio = scaleWidth / width
        val scaleHeight = height * ratio
        Log.e(TAG, "ratio = $ratio   scaleWidth = $scaleWidth  scaleHeight = $scaleHeight")
        //获取Bitmap
        val matrix = Matrix()
        matrix.postScale(ratio, ratio)// 使用后乘

        srcRect.set(0, 0, scaleWidth.toInt(), scaleHeight.toInt())
        dstRect.set(0, 200, scaleWidth.toInt(), scaleHeight.toInt() + 200)
        return Bitmap.createBitmap(mOrigin, 0, 0, mOrigin.width, mOrigin.height, matrix, false)
    }
}
