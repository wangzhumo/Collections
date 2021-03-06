package com.wangzhumo.app.module.opengl.image

import com.alibaba.android.arouter.facade.annotation.Route
import com.wangzhumo.app.base.IRoute
import com.wangzhumo.app.module.opengl.databinding.ActivityGlimageBinding
import com.wangzhumo.app.origin.base.BaseBindingActivity


/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2020-01-08  17:53
 *
 * 显示一个图片纹理到OpenGL
 */
@Route(path = IRoute.OPENGL.IMAGE_TEXTURE)
class GLImageActivity : BaseBindingActivity<ActivityGlimageBinding>() {

    override fun onDestroy() {
        super.onDestroy()
        vBinding.imageTextureView.release()
    }
}
