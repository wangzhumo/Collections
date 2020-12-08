package com.wangzhumo.app.origin.utils;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewbinding.ViewBinding;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 12/8/20  6:02 PM
 */
public class ViewBindingCreator {


    public static <Binding extends ViewBinding> Binding create(Class<?> bindingClazz, LayoutInflater inflater, ViewGroup root) {
        return create(bindingClazz, inflater, root, false);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public static <Binding extends ViewBinding> Binding create(Class<?> bindingClazz, LayoutInflater inflater, ViewGroup root, boolean attachToRoot) {
        ParameterizedType parameterizedType = (ParameterizedType) bindingClazz.getGenericSuperclass();
        Type[] types = Objects.requireNonNull(parameterizedType).getActualTypeArguments();
        Class<?> clazz = null;
        Binding binding = null;
        for (Type type : types) {
            Class<?> temp = (Class<?>) type;
            if (ViewBinding.class.isAssignableFrom(temp)) {
                clazz = temp;
            }
        }
        if (clazz != null) {
            try {
                Method method = clazz.getMethod("inflate", LayoutInflater.class, ViewGroup.class, boolean.class);
                binding = (Binding) method.invoke(null, inflater, root, attachToRoot);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return Objects.requireNonNull(binding);
    }
}
