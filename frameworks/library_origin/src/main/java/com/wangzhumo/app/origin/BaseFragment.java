package com.wangzhumo.app.origin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019/3/29  2:41 PM
 * <p>
 * Fragment的基类
 * 1.CompositeDisposable
 * 2.可见状态
 * 3.加载View/加载Data
 */
public abstract class BaseFragment<VB extends ViewBinding> extends BaseBindingFragment<VB> {


    /**
     * 收集Dis
     */
    public CompositeDisposable mDisposable;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        //加载View资源
        initViews();
        return contentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //可以加载资源
        initData();
    }


    /**
     * 设置View资源
     */
    protected void initViews() {

    }

    /**
     * 加载数据
     */
    protected void initData() {

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
    public void onDestroyView() {
        super.onDestroyView();
        //网络资源
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }
}
