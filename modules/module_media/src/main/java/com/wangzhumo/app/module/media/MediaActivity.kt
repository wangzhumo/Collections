package com.wangzhumo.app.module.media

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager

import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.drakeet.multitype.MultiTypeAdapter
import com.wangzhumo.app.base.IRoute
import com.wangzhumo.app.module.media.model.ActivityItem
import com.wangzhumo.app.origin.BaseActivity
import com.wangzhumo.app.widget.rv.OffsetItemDecoration
import com.wangzhumo.app.widget.rv.OnItemActionListener
import kotlinx.android.synthetic.main.activity_media_main.*

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-04-20  12:33
 *
 * CPP TEST
 */
@Route(path = IRoute.MEDIA_MAIN)
class MediaActivity : BaseActivity() , OnItemActionListener {

    override fun getLayoutId(): Int {
        return R.layout.activity_media_main
    }


    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(OffsetItemDecoration(this,5,0,5,0))
        val mAdapter = MultiTypeAdapter()
        val itemData = create()

        mAdapter.register(ActivityItem::class,ActivityListBinder(this))
        recyclerView.adapter = mAdapter
        mAdapter.items = itemData
        mAdapter.notifyDataSetChanged()

    }

    private fun create() : List<ActivityItem>{
        return mutableListOf(
            ActivityItem(
                "Camera GL",
                "使用GL预览Camera",
                IRoute.MEDIA.CAMERA_SHOW
            ),
            ActivityItem(
                "Record Camrea",
                "使用MediaCodec录制视频",
                IRoute.MEDIA.CAMERA_RECORD
            )
        )
    }

    override fun onAction(what: Int, any: Any?) {
        ARouter.getInstance()
            .build((any as ActivityItem).path)
            .navigation()
    }
}
