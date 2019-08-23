package com.wangzhumo.app.module.media.targets.task3;

import java.util.List;

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-08-23  14:20
 */
public interface CameraEnumerator {

    String[] getDeviceNames();

    boolean isFrontFacing(String var1);

    boolean isBackFacing(String var1);

}
