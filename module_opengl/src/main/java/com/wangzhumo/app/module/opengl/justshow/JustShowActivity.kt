package com.wangzhumo.app.module.opengl.justshow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.wangzhumo.app.base.IRoute
import com.wangzhumo.app.module.opengl.R
import com.wangzhumo.app.origin.BaseActivity

@Route(path = IRoute.OPENGL.JUST_SHOW)
class JustShowActivity : BaseActivity() {


    override fun getLayoutId(): Int = R.layout.activity_just_show

}
