package com.wangzhumo.app.module.media;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.wangzhumo.app.base.IRoute;
import com.wangzhumo.app.origin.BaseActivity;

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-04-20  12:33
 *
 * CPP TEST
 */
@Route(path = IRoute.JNI_CPP)
public class CppActivity extends BaseActivity {




    @Override
    protected int getLayoutId() {
        return R.layout.activity_cpp;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        ARouter.getInstance()
                .build(IRoute.MEDIA_TASK_3)
                .navigation();
    }

}
