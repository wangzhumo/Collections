package com.wangzhumo.app.base.utils

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

object UIUtils {


    /**
     * 获取String
     * @param res: Int
     * @return String
     */
    @JvmStatic
    fun getString(@StringRes res: Int): String {
        return try {
            AppUtils.getApplication().resources.getString(res)

        } catch (e: Resources.NotFoundException) {
            ""
        }
    }

    /**
     * 读取Raw资源
     *
     * @param resourceId
     * @return String
     *
     */
    @JvmStatic
    fun readRaw(@RawRes resourceId: Int): String {
        val builder = StringBuilder()
        try {
            val inputStream = AppUtils.getApplication().resources.openRawResource(resourceId)
            val streamReader = InputStreamReader(inputStream)

            val bufferedReader = BufferedReader(streamReader)
            var textLine: String = bufferedReader.readLine()
            while (!TextUtils.isEmpty(textLine)) {
                builder.append(textLine)
                builder.append("\n")
                textLine = bufferedReader.readLine()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: Resources.NotFoundException) {
            e.printStackTrace()
        }
        return builder.toString()
    }


}
