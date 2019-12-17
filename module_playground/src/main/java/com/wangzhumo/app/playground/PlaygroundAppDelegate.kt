package com.wangzhumo.app.playground

import android.app.Application
import com.google.auto.service.AutoService
import com.wangzhumo.app.base.delegate.AppDelegate
import com.wangzhumo.app.base.delegate.IApp
import com.wangzhumo.app.base.delegate.IAppConstant


/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-12-17  17:48
 */
@IApp(name = IAppConstant.PLAYGROUND)
@AutoService(AppDelegate::class)
class PlaygroundAppDelegate : AppDelegate{
    override fun init(application: Application?) {

    }

}