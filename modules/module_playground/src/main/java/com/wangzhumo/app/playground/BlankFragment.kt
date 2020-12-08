package com.wangzhumo.app.playground

import android.os.Bundle
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import android.util.Log
import android.view.View
import android.widget.SeekBar
import androidx.viewbinding.ViewBinding
import com.wangzhumo.app.origin.utils.DensityUtils
import kotlinx.android.synthetic.main.fragment_blank.*


/**
 * A simple [Fragment] subclass.
 *
 */
public class BlankFragment<FragmentBlankBinding : ViewBinding> : BaseAnimFragment<FragmentBlankBinding>() {

    var dampingProgress: Float = SpringForce.DAMPING_RATIO_NO_BOUNCY
    var stiffnessProgress: Float = SpringForce.STIFFNESS_VERY_LOW



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startAnim(dampingProgress, stiffnessProgress)
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

        val animation = SpringAnimation(root_layout, SpringAnimation.TRANSLATION_X)
            .setSpring(force)
            .setStartValue(DensityUtils.dp2px(context, 225F).toFloat())
        animation.start()
        //animation.addEndListener { dynamicAnimation, b, v, v1 -> dismiss() }
    }
}
