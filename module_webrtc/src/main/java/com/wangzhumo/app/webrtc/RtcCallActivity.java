package com.wangzhumo.app.webrtc;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.wangzhumo.app.base.IRoute;
import com.wangzhumo.app.origin.BaseActivity;


/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-04-20  13:15
 *
 * 实现通话的地方
 */
@Route(path = IRoute.WEBRTC_CALL)
public class RtcCallActivity extends BaseActivity {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_rtc_call;
    }


}
