package com.wangzhumo.app.playground

import android.os.Bundle
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import android.util.Log
import android.view.View
import android.widget.SeekBar
import com.wangzhumo.app.origin.utils.DensityUtils
import com.wangzhumo.app.playground.databinding.KillerViewAreaSuccessBinding

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-05-26  11:31
 */
class AllSingFailDialog : BaseAnimFragment<KillerViewAreaSuccessBinding>() {

    var dampingProgress: Float = 0.4F
    var stiffnessProgress: Float = 600F


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vBinding.btStart.setOnClickListener {
            startAnim(dampingProgress, stiffnessProgress)
        }


        vBinding.stiffness.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

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
            .setDampingRatio(0.4F)
            .setStiffness(600F)

        val force2 = SpringForce(0f)
            .setDampingRatio(damping)
            .setStiffness(stiffness)

        val animationBg = SpringAnimation(vBinding.killerBg, SpringAnimation.TRANSLATION_X)
            .setSpring(force)
            .setStartValue(DensityUtils.dp2px(context, 225F).toFloat())
        val animationText = SpringAnimation(vBinding.killerText, SpringAnimation.TRANSLATION_X)
            .setSpring(force2)
            .setStartValue(DensityUtils.dp2px(context, 225F).toFloat())
        animationBg.start()
        animationText.start()
    }
}