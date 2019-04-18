package com.wangzhumo.app.utils

import android.content.Context
import android.media.AudioManager
import android.media.SoundPool
import com.wangzhumo.app.R


/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019/4/16  2:40 PM
 */

class SoundPoolUtils private constructor() {


    private var mSoundPool : SoundPool = SoundPool(1, AudioManager.STREAM_MUSIC, 0)


    fun play(context: Context){
        mSoundPool.load(context, R.raw.game_all_give_up_challege_sound, 1)
        mSoundPool.setOnLoadCompleteListener { soundPool, sampleId, status ->
            mSoundPool.play(sampleId,1F,1F,0,-1,1F)
        }
    }


    companion object {

        @JvmStatic
        private var INSTANCE : SoundPoolUtils? = null
        get() {
            if (field == null){
                field = SoundPoolUtils()
            }
            return field
        }

        @Synchronized
        fun get(): SoundPoolUtils{
            return INSTANCE!!
        }
    }

}