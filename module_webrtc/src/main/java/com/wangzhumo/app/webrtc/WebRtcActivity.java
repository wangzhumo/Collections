package com.wangzhumo.app.webrtc;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.wangzhumo.app.base.IRoute;
import com.wangzhumo.app.origin.BaseActivity;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;



/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-04-20  12:32
 * <p>
 * 入口
 */
@Route(path = IRoute.WEBRTC_MAIN)
public class WebRtcActivity extends BaseActivity implements View.OnClickListener {


    public AppCompatEditText mWebrtcAddress;
    public AppCompatEditText mWebrtcRoomName;
    public Button mButtonJoin;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_web_rtc;
    }


    @Override
    protected void initViews(@Nullable Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        mWebrtcAddress = findViewById(R.id.webrtc_address);
        mWebrtcRoomName = findViewById(R.id.webrtc_room_name);
        mButtonJoin = findViewById(R.id.button_join);

        mButtonJoin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        checkCameraAndRecord();
    }


    private void checkCameraAndRecord() {
        Disposable disposable = new RxPermissions(WebRtcActivity.this)
                .request(Manifest.permission.INTERNET,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.CAMERA)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean){
                            //如果有的话,就开始跳转

                        }else{
                            Toast.makeText(mContext, "请授予必须的权限", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        addDisposable(disposable);
    }
}
