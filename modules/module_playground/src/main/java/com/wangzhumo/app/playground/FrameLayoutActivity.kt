package com.wangzhumo.app.playground

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.SimpleAdapter
import androidx.databinding.library.baseAdapters.R
import com.alibaba.android.arouter.facade.annotation.Route
import com.drakeet.multitype.MultiTypeAdapter
import com.wangzhumo.app.base.IRoute
import com.wangzhumo.app.origin.base.BaseBindingActivity
import com.wangzhumo.app.origin.utils.DensityUtils
import com.wangzhumo.app.playground.databinding.ActivityFreamLayoutBinding
import com.wangzhumo.app.widget.rv.adapter.SimpleViewBinder


@Route(path = IRoute.FRAME_ACTIVITY)
public class FrameLayoutActivity : BaseBindingActivity<ActivityFreamLayoutBinding>() {

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        vBinding.root.setLeftEdgeSize(DensityUtils.dp2px(this , 200F))

        initRecyclerView()
    }

    private fun initRecyclerView() {
        var listData: MutableList<String> = mutableListOf()

        val adpater = MultiTypeAdapter()
        adpater.register(String::class,SimpleViewBinder())
        for (index in 1..50){
            listData.add(index.toString())
        }
        adpater.items = listData
        vBinding.recycler.adapter = adpater
        adpater.notifyDataSetChanged()
    }
}
