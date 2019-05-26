package com.wangzhumo.app

import android.support.v4.app.FragmentManager
import com.wangzhumo.app.origin.BaseFragment
import com.wangzhumo.app.playground.R

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-05-24  21:23
 */

abstract class BaseAnimFragment : BaseFragment() {


    fun dismiss() {
        try {
            if (fragmentManager != null && !fragmentManager!!.isDestroyed) {
                fragmentManager!!
                    .beginTransaction()
                    .setCustomAnimations(this.enterAnim(), this.exitAnim())
                    .remove(this)
                    .commitAllowingStateLoss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun show(supportFragmentManager: FragmentManager, layoutId: Int) {
        try {
            supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(this.enterAnim(), this.exitAnim())
                .replace(layoutId, this)
                .commitAllowingStateLoss()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    private fun enterAnim(): Int {
        return R.anim.killer_slide_in_from_left
    }

    private fun exitAnim(): Int {
        return R.anim.killer_slide_out_to_right
    }

}