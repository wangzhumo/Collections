package com.wangzhumo.app.origin.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;

import com.wangzhumo.app.origin.utils.ViewBindingCreator;
import com.wangzhumo.app.origin.utils.WindowDensityUtils;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import me.yokeyword.fragmentation.SupportActivity;

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 12/8/20  5:41 PM
 */
public abstract class AbstractBindingActivity<VB extends ViewBinding> extends SupportActivity {


    /**
     * 布局View
     */
    protected VB vBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isUseCustomDensity()) {
            WindowDensityUtils.setCustomDensity(this, this.getApplication());
        }
        vBinding = ViewBindingCreator.create(getClass(), getLayoutInflater(), null);
        setContentView(vBinding.getRoot());
    }


    /**
     * setContentView 之前设置状态栏
     */
    protected void initStatusBar() {

    }

    /**
     * 是否使用头条适配方案
     *
     * @return bool
     */
    protected boolean isUseCustomDensity() {
        return true;
    }

}
