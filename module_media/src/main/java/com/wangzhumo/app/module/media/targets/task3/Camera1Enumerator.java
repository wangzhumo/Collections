package com.wangzhumo.app.module.media.targets.task3;

import android.hardware.Camera;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;

import static android.hardware.usb.UsbDevice.getDeviceName;

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-08-23  14:25
 */
public class Camera1Enumerator implements CameraEnumerator {
    @Override
    public String[] getDeviceNames() {
        ArrayList<String> namesList = new ArrayList();

        for(int i = 0; i < Camera.getNumberOfCameras(); ++i) {
            String name = getDeviceName(i);
            if (name != null) {
                namesList.add(name);
                Log.d("Camera1Enumerator", "Index: " + i + ". " + name);
            } else {
                Log.e("Camera1Enumerator", "Index: " + i + ". Failed to query camera name.");
            }
        }

        String[] namesArray = new String[namesList.size()];
        return (String[])namesList.toArray(namesArray);
    }

    @Override
    public boolean isFrontFacing(String deviceName) {
        Camera.CameraInfo info = getCameraInfo(getCameraIndex(deviceName));
        return info != null && info.facing == 1;
    }

    @Override
    public boolean isBackFacing(String deviceName) {
        Camera.CameraInfo info = getCameraInfo(getCameraIndex(deviceName));
        return info != null && info.facing == 0;
    }

    @Nullable
    private static Camera.CameraInfo getCameraInfo(int index) {
        Camera.CameraInfo info = new Camera.CameraInfo();

        try {
            Camera.getCameraInfo(index, info);
            return info;
        } catch (Exception var3) {
            Log.e("Camera1Enumerator", "getCameraInfo failed on index " + index, var3);
            return null;
        }
    }

    static int getCameraIndex(String deviceName) {
        Log.d("Camera1Enumerator", "getCameraIndex: " + deviceName);

        for(int i = 0; i < Camera.getNumberOfCameras(); ++i) {
            if (deviceName.equals(getDeviceName(i))) {
                return i;
            }
        }

        throw new IllegalArgumentException("No such camera: " + deviceName);
    }
}
