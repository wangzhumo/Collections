package com.wangzhumo.app.origin.utils

import android.app.ActivityManager
import android.content.Context
import android.os.Process

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2/4/21  5:33 PM
 */
object ActivityManagerUtil {
    fun initActivityManager(context: Context): String {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningApps = am.runningAppProcesses ?: return ""
        for (proInfo in runningApps) {
            if (proInfo.pid == Process.myPid()) {
                if (proInfo.processName != null) {
                    return proInfo.processName
                }
            }
        }
        return ""
    }
}