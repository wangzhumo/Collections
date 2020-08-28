package com.wangzhumo.app.module.opengl

import android.app.Application
import android.util.Log
import com.bun.miitmdid.core.MainMdidSdk
import com.bun.miitmdid.core.MdidSdkHelper
import com.bun.miitmdid.interfaces.IIdentifierListener
import com.bun.miitmdid.interfaces.IdSupplier
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
class OpenGLAppDelegate: AppDelegate, IIdentifierListener {

    override fun init(application: Application) {
        Log.d("AppDelegate", "OpenGLAppDelegate = $application")
        MdidSdkHelper.InitSdk(application,true,this)

    }

    override fun OnSupport(p0: Boolean, p1: IdSupplier?) {
        Log.d("AppDelegate", "OnSupport p0 = $p0   p1 = $p1")
    }

}