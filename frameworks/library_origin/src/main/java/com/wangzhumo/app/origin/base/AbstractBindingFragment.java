package com.wangzhumo.app.origin.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;

import com.wangzhumo.app.origin.utils.ViewBindingCreator;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 12/8/20  8:31 PM
 */
public abstract class AbstractBindingFragment<VB extends ViewBinding> extends SupportFragment {
    /**
     * 页面布局View
     */
    protected View contentView;

    /**
     * ViewBinding实例
     */
    protected VB vBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (vBinding == null) {
            vBinding = ViewBindingCreator.create(getClass(), inflater, container);
        }
        if (contentView == null){
            contentView = vBinding.getRoot();
        }
        return contentView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        contentView = null;
    }
}
