package com.wangzhumo.app.origin.utils;

import android.app.Activity;

import java.util.Stack;

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2/4/21  4:59 PM
 *
 * Activity的管理栈
 */
public class AppManager {

    private static final Stack<Activity> activityStack = new Stack<>();

    private static class SingletonHolder {
        private static final AppManager INSTANCE = new AppManager();
    }

    /**
     * 单一实例
     */
    public static AppManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (!activityStack.contains(activity)) {
            activityStack.add(activity);
        }
    }

    /**
     * 移除指定的Activity，需要自己finish
     */
    public void removeActivity(Activity activity) {
        if (activity != null) {
            if (!checkNull()) {
                activityStack.remove(activity);
            }
        }
    }


    /**
     * 获取栈顶Activity（堆栈中最后一个压入的）
     */
    public Activity getTopActivity() {
        if (checkNull() || activityStack.empty()) {
            return null;
        }
        return activityStack.lastElement();
    }

    /**
     * 结束栈顶Activity（堆栈中最后一个压入的）
     */
    public void finishTopActivity() {
        if (checkNull()) {
            return;
        }
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 退出应用程序
     */
    public void appExit() {
        try {
            finishAllActivity();
            System.exit(0);
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 直接暴力退出
     */
    public void killApp() {
        try {
            System.exit(0);
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            if (!checkNull()) {
                activityStack.remove(activity);
            }
            if (!activity.isFinishing() && !activity.isDestroyed()) {
                activity.finish();
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        if (checkNull()) {
            return;
        }
        for (Activity next : activityStack) {
            next.finish();
        }
        activityStack.clear();
    }

    /**
     * 得到指定类名的Activity
     */
    public Activity getActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                return activity;
            }
        }
        return null;
    }


    private boolean checkNull() {
        return activityStack == null;
    }
}
