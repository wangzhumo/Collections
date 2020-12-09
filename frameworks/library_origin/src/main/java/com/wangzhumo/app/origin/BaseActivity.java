package com.wangzhumo.app.origin;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;

import com.gyf.immersionbar.ImmersionBar;
import com.jakewharton.rxbinding4.view.RxView;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019/3/29  2:21 PM
 * <p>
 * Activity的基类
 * 1.CompositeDisposable
 * 2.加载ViewBinding
 * 3.加载Data
 * 4.生命周期
 */
public abstract class BaseActivity<VB extends ViewBinding> extends BaseBindingActivity<VB> {

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
        this.mContext = this;
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
     * 设置资源ID
     *
     * @return layoutID
     */
    public  @LayoutRes int getLayoutId(){
        return 0;
    }


    /**
     * 加载数据
     *
     * @param savedInstanceState Bundle
     */
    protected void initData(Bundle savedInstanceState) {

    }


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
