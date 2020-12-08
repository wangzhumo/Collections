package com.wangzhumo.app.playground

import android.os.Bundle
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import android.util.Log
import android.view.View
import android.widget.SeekBar
import androidx.viewbinding.ViewBinding
import com.wangzhumo.app.origin.utils.DensityUtils
import kotlinx.android.synthetic.main.killer_view_area_success.*

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-05-26  11:31
 */
class AllSingFailDialog<KillerViewAreaSuccessBinding : ViewBinding> : BaseAnimFragment<KillerViewAreaSuccessBinding>(){

    var dampingProgress: Float = 0.4F
    var stiffnessProgress: Float = 600F



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
            .setDampingRatio(0.4F)
            .setStiffness(600F)

        val force2 = SpringForce(0f)
            .setDampingRatio(damping)
            .setStiffness(stiffness)

        val animationBg = SpringAnimation(killer_bg, SpringAnimation.TRANSLATION_X)
            .setSpring(force)
            .setStartValue(DensityUtils.dp2px(context, 225F).toFloat())
        val animationText= SpringAnimation(killer_text, SpringAnimation.TRANSLATION_X)
            .setSpring(force2)
            .setStartValue(DensityUtils.dp2px(context, 225F).toFloat())
        animationBg.start()
        animationText.start()
    }
}