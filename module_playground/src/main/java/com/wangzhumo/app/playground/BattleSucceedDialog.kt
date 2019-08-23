package com.wangzhumo.app

import android.os.Bundle
import androidx.dynamicanimation.animation.FloatValueHolder
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import android.util.Log
import android.view.View
import android.widget.SeekBar
import com.wangzhumo.app.base.DensityUtils
import com.wangzhumo.app.playground.R
import kotlinx.android.synthetic.main.fragment_blank.*
import kotlinx.android.synthetic.main.fragment_blank.bt_start
import kotlinx.android.synthetic.main.fragment_blank.damping_ratio
import kotlinx.android.synthetic.main.fragment_blank.stiffness
import kotlinx.android.synthetic.main.fragment_blank.tv_damping
import kotlinx.android.synthetic.main.fragment_blank.tv_stiffness
import kotlinx.android.synthetic.main.killer_dialog_battle_succeed.*

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-05-24  21:28
 */

class BattleSucceedDialog : BaseAnimFragment(){

    var dampingProgress: Float = SpringForce.DAMPING_RATIO_HIGH_BOUNCY
    var stiffnessProgress: Float = SpringForce.STIFFNESS_MEDIUM


    override fun getLayoutId(): Int = R.layout.killer_dialog_battle_succeed

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bt_start.setOnClickListener {
            startAnim(dampingProgress, stiffnessProgress)
        }

        stiffness.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                stiffnessProgress = (seekBar?.progress)?.toFloat()!!
                Log.e("Progress", "stiffness = $stiffnessProgress")
                tv_stiffness.text = stiffnessProgress.toString()
            }

        })
        damping_ratio.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                dampingProgress = (seekBar?.progress)?.div(10F)!!
                Log.e("Progress", "damping = $dampingProgress")
                tv_damping.text = dampingProgress.toString()
            }

        })
    }


    private fun startAnim(damping: Float, stiffness: Float) {
        val force = SpringForce(0f)
            .setDampingRatio(damping)
            .setStiffness(stiffness)

        val animationRed = SpringAnimation(image_blue,SpringAnimation.TRANSLATION_X)
            .setSpring(force)
            .setStartValue(-DensityUtils.dp2px(context, 225F).toFloat())
        val animationBlue = SpringAnimation(image_red_layout,SpringAnimation.TRANSLATION_X)
            .setSpring(force)
            .setStartValue(DensityUtils.dp2px(context, 225F).toFloat())
        animationRed.start()
        animationBlue.start()
    }
}