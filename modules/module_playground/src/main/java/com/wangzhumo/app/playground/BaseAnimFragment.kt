package com.wangzhumo.app.playground

import androidx.viewbinding.ViewBinding
import com.wangzhumo.app.origin.base.BaseBindingFragment

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-05-24  21:23
 */

abstract class BaseAnimFragment<VB : ViewBinding> : BaseBindingFragment<VB>() {


    fun dismiss() {
        try {
            if (childFragmentManager != null && childFragmentManager.isDestroyed) {
                childFragmentManager
                    .beginTransaction()
                    .setCustomAnimations(this.enterAnim(), this.exitAnim())
                    .remove(this)
                    .commitAllowingStateLoss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun show(supportFragmentManager: androidx.fragment.app.FragmentManager, layoutId: Int) {
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