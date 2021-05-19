package com.wangzhumo.app.playground.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2021/5/12  7:34 下午
 *
 *
 */
public class SimpleAdapter extends ArrayAdapter<String> {


    public SimpleAdapter(@NonNull  Context context, int resource) {
        super(context, resource);
    }

    public SimpleAdapter(@NonNull Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public SimpleAdapter(@NonNull Context context, int resource, @NonNull @NotNull String[] objects) {
        super(context, resource, objects);
    }
}
