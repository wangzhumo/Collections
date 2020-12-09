package com.wangzhumo.app.webrtc.page;

import android.Manifest;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.wangzhumo.app.webrtc.databinding.ActivityWebRtcBinding;
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
public class WebRtcActivity extends BaseActivity<ActivityWebRtcBinding> implements View.OnClickListener {


    public AppCompatEditText mWebrtcAddress;
    public AppCompatEditText mWebrtcRoomName;
    public Button mButtonJoin;


    @Override
    protected void initViews(@Nullable Bundle savedInstanceState) {
        mWebrtcAddress = vBinding.webrtcAddress;
        mWebrtcRoomName = vBinding.webrtcRoomName;
        mButtonJoin = vBinding.buttonJoin;
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
                        if (aBoolean) {
                            //如果有的话,就开始跳转
                            verifyAndJumpCall();
                        } else {
                            Toast.makeText(mContext, "请授予必须的权限", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        addDisposable(disposable);
    }

    /**
     * 跳转至Call
     */
    private void verifyAndJumpCall() {
        String address = mWebrtcAddress.getText().toString();
        String roomName = mWebrtcRoomName.getText().toString();

        if (TextUtils.isEmpty(address) || TextUtils.isEmpty(roomName)) {
            Toast.makeText(mContext, "请完成信息录入", Toast.LENGTH_SHORT).show();
            return;
        }

        //跳转
        ARouter.getInstance()
                .build(IRoute.WEBRTC_CALL)
                .withString("address", address)
                .withString("roomName", roomName)
                .navigation();

        finish();
    }
}
