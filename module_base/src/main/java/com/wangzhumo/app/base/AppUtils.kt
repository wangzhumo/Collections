package com.wangzhumo.app.base

import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.text.TextUtils
import androidx.annotation.RawRes
import androidx.annotation.StringRes
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-09-28  18:51
 */

object AppUtils {

    lateinit var mApp: Application

    /**
     * 初始化这个工具类
     */
    fun init(app: Application) {
        mApp = app
    }

    /**
     * 获取applicationContext
     * @return applicationContext
     */
    fun getContext(): Context {
        return mApp.applicationContext
    }

    /**
     * 获取Application
     * @return APP.kt
     */
    fun getApplication(): Application {
        return mApp
    }
}
