package com.wangzhumo.app.func

import android.app.Application
import android.text.TextUtils
import com.alibaba.android.arouter.launcher.ARouter
import com.wangzhumo.app.BuildConfig
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.tencent.bugly.crashreport.CrashReport
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import com.tencent.bugly.crashreport.CrashReport.UserStrategy


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
        initLogger()
        initBugly()
    }


    private fun initLogger() {
        val formatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(true)  // (Optional) Whether to show thread info or not. Default true
            .methodCount(2)         // (Optional) How many method line to show. Default 2
            .methodOffset(7)        // (Optional) Hides internal method calls up to offset. Default 5
            .build()
        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))
    }


    /**
     * 加载ARouter
     */
    private fun initARouter() {
        // 这两行必须写在init之前，否则这些配置在init过程中将无效
        if (BuildConfig.DEBUG) {
            // 打印日志
            ARouter.openLog()
            // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
            ARouter.openDebug()
        }
        // 尽可能早，推荐在Application中初始化
        ARouter.init(this)
    }


    /**
     * 加载Bugly
     */
    private fun initBugly() {
        // 获取当前包名
        val packageName = applicationContext.packageName
        // 获取当前进程名
        val processName = getProcessName(android.os.Process.myPid())
        // 设置是否为上报进程
        val strategy = UserStrategy(applicationContext)
        strategy.isUploadProcess = processName == null || processName == packageName
        // 初始化Bugly
        CrashReport.initCrashReport(applicationContext, "962f4d00a2", BuildConfig.DEBUG,strategy)
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private fun getProcessName(pid: Int): String? {
        var reader: BufferedReader? = null
        try {
            reader = BufferedReader(FileReader("/proc/$pid/cmdline"))
            var processName = reader!!.readLine()
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim({ it <= ' ' })
            }
            return processName
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        } finally {
            try {
                if (reader != null) {
                    reader!!.close()
                }
            } catch (exception: IOException) {
                exception.printStackTrace()
            }

        }
        return null
    }
}