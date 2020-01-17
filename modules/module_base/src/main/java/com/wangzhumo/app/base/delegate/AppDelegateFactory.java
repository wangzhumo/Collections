package com.wangzhumo.app.base.delegate;

import android.app.Application;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-09-28  18:12
 */
public class AppDelegateFactory {

    private List<AppDelegate> mDelegateList = new ArrayList<>();
    private HashMap<AppDelegate, Integer> mDelegateOrder = new HashMap<>();
    private HashMap<String, AppDelegate> mDelegateName = new HashMap<>();


    private AppDelegateFactory() {
    }

    private static class Holder {
        static AppDelegateFactory Instance = new AppDelegateFactory();
    }

    /**
     * 获取AppDelegateFactory实例
     *
     * @return AppDelegateFactory
     */
    public static AppDelegateFactory getInstance() {
        return Holder.Instance;
    }


    /**
     * 获取指定的AppDelegate
     *
     * @param name IAppConstant
     * @return AppDelegate
     */
    public AppDelegate getDelegateByName(@IAppConstant String name) {
        if (mDelegateName != null && mDelegateName.size() > 0) {
            return mDelegateName.get(name);
        }
        return null;
    }

    /**
     * 开始加载AppDelegate
     */
    public void startLoadAppDelegate(Application application) {
        //加载Fragment 类型的ServiceLoader
        ServiceLoader<AppDelegate> iterator = ServiceLoader.load(AppDelegate.class);
        for (Iterator<AppDelegate> iterator1 = iterator.iterator(); iterator1.hasNext(); ) {
            final AppDelegate appDelegate = iterator1.next();

            //获取ITabPage 注解的类
            IApp property = appDelegate.getClass().getAnnotation(IApp.class);
            if (property == null) {
                continue;
            }
            mDelegateOrder.put(appDelegate, property.priority());
            mDelegateName.put(property.name(), appDelegate);
            mDelegateList.add(appDelegate);
            Collections.sort(mDelegateList, (o1, o2) -> {
                int order1 = mDelegateOrder.get(o1);
                int order2 = mDelegateOrder.get(o2);
                return order1 - order2;
            });
        }
        //完成之后,就是一个有序的列表了
        init(application);
    }

    /**
     * 开始加载资源
     *
     * @param application Application
     */
    private void init(Application application) {
        if (mDelegateList != null && mDelegateList.size() > 0) {
            for (AppDelegate appDelegate : mDelegateList) {
                appDelegate.init(application);
            }
        }
    }
}
