package com.wangzhumo.app.playground

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SeekBar
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import com.wangzhumo.app.origin.utils.DensityUtils
import com.wangzhumo.app.playground.databinding.FragmentBlankBinding


/**
 * A simple [Fragment] subclass.
 *
 */
public class BlankFragment : BaseAnimFragment<FragmentBlankBinding>() {

    var dampingProgress: Float = SpringForce.DAMPING_RATIO_NO_BOUNCY
    var stiffnessProgress: Float = SpringForce.STIFFNESS_VERY_LOW


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startAnim(dampingProgress, stiffnessProgress)
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
            .setDampingRatio(damping)
            .setStiffness(stiffness)

        val animation = SpringAnimation(vBinding.rootLayout, SpringAnimation.TRANSLATION_X)
            .setSpring(force)
            .setStartValue(DensityUtils.dp2px(mContext, 225F).toFloat())
        animation.start()
        //animation.addEndListener { dynamicAnimation, b, v, v1 -> dismiss() }
    }
}
