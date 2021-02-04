package com.wangzhumo.app.playground

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SeekBar
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import com.jakewharton.rxbinding4.view.clicks
import com.wangzhumo.app.origin.utils.DensityUtils
import com.wangzhumo.app.playground.databinding.KillerDialogBattleSucceedBinding

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-05-24  21:28
 */

class BattleSucceedDialog : BaseAnimFragment<KillerDialogBattleSucceedBinding>() {

    var dampingProgress: Float = SpringForce.DAMPING_RATIO_HIGH_BOUNCY
    var stiffnessProgress: Float = SpringForce.STIFFNESS_MEDIUM


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vBinding.btStart.setOnClickListener {
            startAnim(dampingProgress, stiffnessProgress)
        }
        vBinding.stiffness.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                stiffnessProgress = (seekBar?.progress)?.toFloat()!!
                Log.e("Progress", "stiffness = $stiffnessProgress")
                vBinding.tvStiffness.text = stiffnessProgress.toString()
            }

        })
        vBinding.dampingRatio.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }


            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                dampingProgress = (seekBar?.progress)?.div(10F)!!
                Log.e("Progress", "damping = $dampingProgress")
                vBinding.tvDamping.text = dampingProgress.toString()
            }

        })
    }


    private fun startAnim(damping: Float, stiffness: Float) {
        val force = SpringForce(0f)
            .setDampingRatio(damping)
            .setStiffness(stiffness)
        val animationRed = SpringAnimation(vBinding.imageBlue, SpringAnimation.TRANSLATION_X)
            .setSpring(force)
            .setStartValue(-DensityUtils.dp2px(mContext, 225F).toFloat())
        val animationBlue = SpringAnimation(vBinding.imageRedLayout, SpringAnimation.TRANSLATION_X)
            .setSpring(force)
            .setStartValue(DensityUtils.dp2px(mContext, 225F).toFloat())
        animationRed.start()
        animationBlue.start()
    }
}