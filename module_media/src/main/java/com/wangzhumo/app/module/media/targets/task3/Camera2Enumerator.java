package com.wangzhumo.app.module.media.targets.task3;

import android.content.Context;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.support.annotation.Nullable;
import android.util.AndroidException;
import android.util.Log;

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-08-23  14:25
 */
public class Camera2Enumerator implements CameraEnumerator {

    private final Context context;
    @Nullable
    private final CameraManager cameraManager;

    public Camera2Enumerator(Context context) {
        this.context = context;
        this.cameraManager = (CameraManager)context.getSystemService(Context.CAMERA_SERVICE);
    }


    @Override
    public String[] getDeviceNames() {
        try {
            return this.cameraManager.getCameraIdList();
        } catch (AndroidException var2) {
            Log.e("Camera2Enumerator", "Camera access exception: " + var2);
            return new String[0];
        }
    }

    @Override
    public boolean isFrontFacing(String deviceName) {
        CameraCharacteristics characteristics = this.getCameraCharacteristics(deviceName);
        return characteristics != null && (Integer)characteristics.get(CameraCharacteristics.LENS_FACING) == 0;
    }

    @Override
    public boolean isBackFacing(String deviceName) {
        CameraCharacteristics characteristics = this.getCameraCharacteristics(deviceName);
        return characteristics != null && (Integer)characteristics.get(CameraCharacteristics.LENS_FACING) == 1;
    }


    @Nullable
    private CameraCharacteristics getCameraCharacteristics(String deviceName) {
        try {
            return this.cameraManager.getCameraCharacteristics(deviceName);
        } catch (AndroidException var3) {
            Log.e("Camera2Enumerator", "Camera access exception: " + var3);
            return null;
        }
    }

    /**
     * 判断是否支持Camera2
     * @param context ctx
     * @return bool
     */
    public static boolean isSupported(Context context) {
        CameraManager cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        try {
            String[] cameraIds = cameraManager.getCameraIdList();
            String[] var3 = cameraIds;
            int var4 = cameraIds.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                String id = var3[var5];
                CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(id);
                if (characteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL) == 2) {
                    return false;
                }
            }
            return true;
        } catch (AndroidException var8) {
            Log.e("Camera2Enumerator", "Camera access exception: " + var8);
            return false;
        }

    }


}
