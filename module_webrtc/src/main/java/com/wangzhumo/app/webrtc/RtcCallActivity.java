package com.wangzhumo.app.webrtc;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.wangzhumo.app.base.IRoute;
import com.wangzhumo.app.origin.BaseActivity;
import com.wangzhumo.app.webrtc.signal.Signaling;


/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-04-20  13:15
 * <p>
 * 实现通话的地方
 */
@Route(path = IRoute.WEBRTC_CALL)
public class RtcCallActivity extends BaseActivity implements View.OnClickListener {


    public Button mButtonJoin;
    public Button mButtonLeave;


    @Autowired(name = "address")
    public String roomAddress;
    @Autowired(name = "roomName")
    public String roomName;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_rtc_call;
    }

    @Override
    protected void initViews(@Nullable Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        ARouter.getInstance().inject(RtcCallActivity.this);
        mButtonJoin = findViewById(R.id.button_join);
        mButtonJoin.setOnClickListener(this);
        mButtonLeave = findViewById(R.id.button_leave);
        mButtonLeave.setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.button_join) {
            Signaling.getInstance().joinRoom(roomAddress, roomName);
            Toast.makeText(mContext, "Join Room", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.button_leave) {
            Signaling.getInstance().leaveRoom();
            Toast.makeText(mContext, "Leave Room", Toast.LENGTH_SHORT).show();
        }
    }
}
