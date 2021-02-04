package com.wangzhumo.app.origin.base;

import androidx.lifecycle.ViewModel;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2/4/21  5:06 PM
 */
public class BaseBindingViewModel extends ViewModel {

    protected CompositeDisposable compositeDisposable;


    /**
     * 加入CompositeDisposable管理
     * @param disposable Disposable
     */
    public void addDisposable(Disposable disposable){
        if (compositeDisposable == null){
            compositeDisposable = new CompositeDisposable();
        }
        if (disposable == null){
            return;
        }
        compositeDisposable.add(disposable);
    }


    /**
     * 停止当前的操作
     */
    public void clear(){
        if (compositeDisposable != null){
            compositeDisposable.clear();
        }
    }

    /**
     * 停止当前的操作 - 并且不允许继续进行
     */
    public void dispose(){
        if (compositeDisposable != null){
            compositeDisposable.clear();
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        clear();
    }

}
