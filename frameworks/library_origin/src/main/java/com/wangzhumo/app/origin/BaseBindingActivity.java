package com.wangzhumo.app.origin;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;

import com.wangzhumo.app.origin.utils.ViewBindingCreator;

import me.yokeyword.fragmentation.SupportActivity;

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 12/8/20  5:41 PM
 */
abstract class BaseBindingActivity<VB extends ViewBinding> extends SupportActivity {

    protected VB vBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vBinding = ViewBindingCreator.create(vBinding.getClass(), getLayoutInflater(), null);
        setContentView(vBinding.getRoot());
    }
}
