package com.wangzhumo.app.func

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter
import com.wangzhumo.app.BuildConfig

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019/3/29  3:13 PM
 *
 * Application
 */

class APP : Application() {


    override fun onCreate() {
        super.onCreate()
        initARouter()
    }


    /**
     * 加载ARouter
     */
    private fun initARouter() {
        if (BuildConfig.DEBUG) {   // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog()     // 打印日志
            ARouter.openDebug()   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this) // 尽可能早，推荐在Application中初始化
    }
}