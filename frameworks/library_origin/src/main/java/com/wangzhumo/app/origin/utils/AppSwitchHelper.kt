package com.wangzhumo.app.origin.utils

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.lifecycle.MutableLiveData

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2/4/21  5:20 PM
 */
class AppSwitchHelper private constructor() {
    private object SingletonClass {
        val instance = AppSwitchHelper()
    }

    /**
     * 注册状态监听，仅在Application中使用
     *
     * @param application
     */
    fun register(application: Application) {
        hideObserve.value = false
        activityStartCount = 0
        application.registerActivityLifecycleCallbacks(activityLifecycleCallbacks)
    }

    fun unRegister(application: Application) {
        hideObserve.value = false
        application.unregisterActivityLifecycleCallbacks(activityLifecycleCallbacks)
    }

    private val activityLifecycleCallbacks: Application.ActivityLifecycleCallbacks =
        object : Application.ActivityLifecycleCallbacks {

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
            override fun onActivityStarted(activity: Activity) {
                activityStartCount++
                //数值从0变到1说明是从后台切到前台
                if (activityStartCount == 1) {
                    //从后台切到前台
                    hideObserve.postValue(true)
                }
            }

            override fun onActivityResumed(activity: Activity) {}
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {
                activityStartCount--
                //数值从1到0说明是从前台切到后台
                if (activityStartCount == 0) {
                    //从前台切到后台
                    hideObserve.postValue(false)
                }
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {}
        }

    companion object {

        @JvmStatic
        val holder = SingletonClass.instance

        /**
         * 获取前后台的监听
         *
         * @return MutableLiveData
         */
        @JvmStatic
        val hideObserve = MutableLiveData<Boolean>()

        /**
         * 打开的Activity数量统计
         */
        private var activityStartCount = 0
    }
}