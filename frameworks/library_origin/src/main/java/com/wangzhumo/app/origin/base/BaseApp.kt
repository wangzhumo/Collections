package com.wangzhumo.app.origin.base

import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.multidex.MultiDexApplication
import com.wangzhumo.app.origin.utils.ActivityManagerUtil
import com.wangzhumo.app.origin.utils.AppSwitchHelper
import com.wangzhumo.app.origin.utils.AppUtils

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2/4/21  5:13 PM
 */
open class BaseApp : MultiDexApplication(), ViewModelStoreOwner {


    override fun onCreate() {
        super.onCreate()
        AppUtils.init(this)
        val progress: String = ActivityManagerUtil.initActivityManager(this)
        //是主进程
        if (packageName == progress) run {
            /**
             * 初始化AppSwitch
             */
            AppSwitchHelper.holder.register(this)
        }
    }


    override fun getViewModelStore(): ViewModelStore {
        return mAppViewModelStore
    }


    companion object {
        private val mAppViewModelStore = ViewModelStore()
    }
}