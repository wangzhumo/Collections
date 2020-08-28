package com.wangzhumo.app.module.opengl

import android.app.Application
import android.util.Log
import com.google.auto.service.AutoService
import com.wangzhumo.app.base.delegate.AppDelegate
import com.wangzhumo.app.base.delegate.IApp
import com.wangzhumo.app.base.delegate.IAppConstant

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-09-28  17:48
 */
@IApp(name = IAppConstant.OPENGL)
@AutoService(AppDelegate::class)
class OpenGLAppDelegate: AppDelegate {

    override fun init(application: Application) {
        Log.d("AppDelegate", "OpenGLAppDelegate = $application")

    }
}