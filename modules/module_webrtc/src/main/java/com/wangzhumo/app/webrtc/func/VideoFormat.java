package com.wangzhumo.app.webrtc.func;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-04-20  15:43
 *
 * 视屏参数
 */
public class VideoFormat implements Parcelable {


    private int width;
    private int height;
    private int fps;

    public VideoFormat(int width, int height, int fps) {
        this.width = width;
        this.height = height;
        this.fps = fps;
    }

    protected VideoFormat(Parcel in) {
        width = in.readInt();
        height = in.readInt();
        fps = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(width);
        dest.writeInt(height);
        dest.writeInt(fps);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<VideoFormat> CREATOR = new Creator<VideoFormat>() {
        @Override
        public VideoFormat createFromParcel(Parcel in) {
            return new VideoFormat(in);
        }

        @Override
        public VideoFormat[] newArray(int size) {
            return new VideoFormat[size];
        }
    };

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getFps() {
        return fps;
    }
}
