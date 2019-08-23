package com.wangzhumo.app.origin;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
 *
 * Fragment的基类
 * 1.CompositeDisposable
 * 2.可见状态
 * 3.加载View/加载Data
 */
public abstract class BaseFragment extends SupportFragment {


    /**
     * 收集Dis
     */
    public CompositeDisposable mDisposable;

    /**
     * 根布局
     */
    private View mView;

    /**
     * 是否已经展示过
     */
    private boolean isShowed = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(getLayoutId(), container, false);
        //加载View资源
        initViews();
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //可以加载资源
        initData();
    }

//    @Override
//    public void onSupportVisible() {
//        super.onSupportVisible();
//        if (!isShowed) {
//            //第一次可见
//            onFirstVisible();
//            isShowed = true;
//        } else {
//            //每次可见
//            onFragmentVisible();
//        }
//    }

//    @Override
//    public void onSupportInvisible() {
//        super.onSupportInvisible();
//        onFragmentInvisible();
//    }

//    /**
//     * Fragment不可见
//     */
//    protected void onFragmentInvisible() {
//
//    }

//
//    /**
//     * Fragment可见 (当onFirstVisible()调用时,不会调用)
//     */
//    protected void onFragmentVisible() {
//
//    }
//
//    /**
//     * Fragment不可见
//     */
//    protected void onFirstVisible() {
//
//    }
//

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
     * 设置资源ID
     *
     * @return layoutID
     */
    protected abstract int getLayoutId();


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
