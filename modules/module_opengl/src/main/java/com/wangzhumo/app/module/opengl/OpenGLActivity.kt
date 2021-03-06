package com.wangzhumo.app.module.opengl

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.drakeet.multitype.MultiTypeAdapter
import com.wangzhumo.app.base.IRoute
import com.wangzhumo.app.module.opengl.databinding.ActivityOpenGlBinding
import com.wangzhumo.app.module.opengl.module.ActivityItem
import com.wangzhumo.app.origin.base.BaseBindingActivity
import com.wangzhumo.app.widget.rv.OffsetItemDecoration
import com.wangzhumo.app.widget.rv.OnItemActionListener

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2020-01-02  16:54
 *
 * OpenGL Module的ListActivity
 */
@Route(path = IRoute.OPENGL_LIST)
class OpenGLActivity : BaseBindingActivity<ActivityOpenGlBinding>() ,OnItemActionListener{

    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        vBinding.recyclerView.layoutManager = LinearLayoutManager(this)
        vBinding.recyclerView.addItemDecoration(OffsetItemDecoration(this,5,0,5,0))
        val mAdapter = MultiTypeAdapter()
        val itemData = create()

        mAdapter.register(ActivityItem::class,ActivityListBinder(this))
        vBinding.recyclerView.adapter = mAdapter
        mAdapter.items = itemData
        mAdapter.notifyDataSetChanged()
    }


    private fun create() : List<ActivityItem>{
        return mutableListOf(
            ActivityItem("OpenGL Cpp","Cpp中使用opengl", IRoute.CPPGLES.CPP_GLES),
            ActivityItem("OpenGL YUV","Cpp中使用opengl渲染YUV视频", IRoute.CPPGLES.CPP_YUV)
        )

//        return mutableListOf(
//            ActivityItem("JustShow","简单使用GLSurfaceView显示", IRoute.OPENGL.JUST_SHOW),
//            ActivityItem("Custom GLSurface","使用自己定义的GLSurfaceView显示", IRoute.OPENGL.CUSTOM_GL_SURFACE),
//            ActivityItem("Image GLSurface","使用自己定义的GLSurfaceView显示Image", IRoute.OPENGL.IMAGE_TEXTURE),
//            ActivityItem("Image FBO","使用FBO显示Image", IRoute.OPENGL.IMAGE_TEXTURE_FBO),
//            ActivityItem("Matrix","使用Matrix改变图像", IRoute.OPENGL.IMAGE_TEXTURE_MATRIX),
//            ActivityItem("Multi Surface","显示多个", IRoute.OPENGL.IMAGE_TEXTURE_MULTI)
//        )
    }

    override fun onAction(what: Int, any: Any?) {
        ARouter.getInstance()
            .build((any as ActivityItem).path)
            .navigation()
    }
}
