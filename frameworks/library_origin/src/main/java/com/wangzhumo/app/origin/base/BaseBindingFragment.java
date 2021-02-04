package com.wangzhumo.app.origin.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

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
public abstract class BaseBindingFragment<VB extends ViewBinding> extends AbstractBindingFragment<VB> {


    /**
     * 收集Dis
     */
    protected CompositeDisposable mDisposable;

    protected Context mContext;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        //加载View资源
        initViews(contentView);
        return contentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //可以加载资源
        initData(savedInstanceState);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    /**
     * 设置View资源
     */
    protected void initViews(View rootView) {

    }

    /**
     * 加载数据
     */
    protected void initData(@Nullable Bundle savedInstanceState) {

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
        if (disposable == null){
            return;
        }
        mDisposable.add(disposable);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //网络资源
        if (mDisposable != null) {
            mDisposable.clear();
        }
    }
}
