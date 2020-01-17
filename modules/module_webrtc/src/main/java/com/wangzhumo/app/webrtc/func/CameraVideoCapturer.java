package com.wangzhumo.app.webrtc.func;

import android.content.Context;
import android.util.Log;
import org.webrtc.*;

import static org.webrtc.VideoFrameDrawer.TAG;

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-04-20  16:22
 *
 * 获取摄像头捕捉
 */
public class CameraVideoCapturer {


    /*
     * Read more about Camera2 here
     * https://developer.android.com/reference/android/hardware/camera2/package-summary.html
     **/
    public static VideoCapturer createCapturer(Context context) {
        if (Camera2Enumerator.isSupported(context)) {
            return createCameraCapturer(new Camera2Enumerator(context.getApplicationContext()));
        } else {
            return createCameraCapturer(new Camera1Enumerator(true));
        }
    }


    private static VideoCapturer createCameraCapturer(CameraEnumerator enumerator) {
        final String[] deviceNames = enumerator.getDeviceNames();

        // First, try to find front facing camera
        Log.d(TAG, "Looking for front facing cameras.");
        for (String deviceName : deviceNames) {
            if (enumerator.isFrontFacing(deviceName)) {
                Logging.d(TAG, "Creating front facing camera capturer.");
                VideoCapturer videoCapturer = enumerator.createCapturer(deviceName, null);
                if (videoCapturer != null) {
                    return videoCapturer;
                }
            }
        }

        // Front facing camera not found, try something else
        Log.d(TAG, "Looking for other cameras.");
        for (String deviceName : deviceNames) {
            if (!enumerator.isFrontFacing(deviceName)) {
                Logging.d(TAG, "Creating other camera capturer.");
                VideoCapturer videoCapturer = enumerator.createCapturer(deviceName, null);
                if (videoCapturer != null) {
                    return videoCapturer;
                }
            }
        }
        return null;
    }
}
