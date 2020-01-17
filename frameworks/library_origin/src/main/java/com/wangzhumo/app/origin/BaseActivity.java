package com.wangzhumo.app.origin;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import com.gyf.immersionbar.ImmersionBar;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import me.yokeyword.fragmentation.SupportActivity;

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019/3/29  2:21 PM
 *
 *  Activity的基类
 *  1.CompositeDisposable
 *  2.加载View/加载Data
 */
public abstract class BaseActivity extends SupportActivity {

    /**
     * 收集Dis
     */
    public CompositeDisposable mDisposable;

    /**
     * Context
     */
    public Context mContext;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getLayoutId() == 0) {
            throw new IllegalArgumentException("layoutResID is empty");
        }
        this.mContext = this;
        setContentView(getLayoutId());
        initStatusBar();
        //此时可以加载View / 设置data
        initViews(savedInstanceState);
        initData(savedInstanceState);
    }


    /**
     * 设置View资源
     *
     * @param savedInstanceState Bundle
     */
    protected void initViews(@Nullable Bundle savedInstanceState) {

    }

    /**
     * 加载数据
     *
     * @param savedInstanceState Bundle
     */
    protected void initData(Bundle savedInstanceState) {

    }

    /**
     * 设置资源ID
     *
     * @return layoutID
     */
    protected abstract @LayoutRes int getLayoutId();

    /**
     * setContentView 之前
     */
    public void initStatusBar() {
        //在BaseActivity里初始化
        ImmersionBar.with(this)
                .transparentStatusBar()
                .statusBarDarkFont(true)   //状态栏字体是深色，不写默认为亮色
                .navigationBarDarkIcon(true) //导航栏图标是深色，不写默认为亮色
                .init();
    }

    /**
     * 加入管理器
     *
     * @param disposable disposable
     */
    public void addDisposable(Disposable disposable) {
        if (mDisposable == null) {
            mDisposable = new CompositeDisposable();
        }
        mDisposable.add(disposable);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //网络资源
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }

}
