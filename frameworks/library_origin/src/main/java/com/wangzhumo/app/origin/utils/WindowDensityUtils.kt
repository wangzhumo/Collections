package com.wangzhumo.app.origin.utils

import android.app.Application
import android.content.ComponentCallbacks
import android.content.Context
import android.content.res.Configuration

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2/4/21  5:10 PM
 *
 * 头条的适配方案
 */
object WindowDensityUtils {
    private var mSize = 360f
    private var mDensity = 0f
    private var mScaledDensity = 0f
    @JvmStatic
    fun setCustomDensity(activity: Context, application: Application) {
        val appDisplayMetrics = application.resources.displayMetrics
        if (mDensity == 0f) {
            mDensity = appDisplayMetrics.density
            mScaledDensity = appDisplayMetrics.scaledDensity
            //注册下 onConfigurationChanged 监听,觖决再返回应用，字体并没有变化
            application.registerComponentCallbacks(object : ComponentCallbacks {
                override fun onConfigurationChanged(newConfig: Configuration) {
                    if (newConfig != null && newConfig.fontScale > 0) {
                        mScaledDensity = application.resources.displayMetrics.scaledDensity
                    }
                }

                override fun onLowMemory() {}
            })
        }
        val targetDensity = appDisplayMetrics.widthPixels / mSize
        val targetScaledDensity = targetDensity * (mScaledDensity / mDensity)
        val targetDensityDpi = (160 * targetDensity).toInt()
        //更改application
        appDisplayMetrics.density = targetDensity
        appDisplayMetrics.scaledDensity = targetScaledDensity
        appDisplayMetrics.densityDpi = targetDensityDpi
        //更改activity
        val activityMetrics = activity.resources.displayMetrics
        activityMetrics.density = targetDensity
        activityMetrics.scaledDensity = targetScaledDensity
        activityMetrics.densityDpi = targetDensityDpi
    }

    fun setSize(size: Float) {
        mSize = mDensity
    }
}