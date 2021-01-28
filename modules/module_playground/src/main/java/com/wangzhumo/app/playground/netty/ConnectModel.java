package com.wangzhumo.app.playground.netty;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;

import java.util.List;

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 1/28/21  9:41 PM
 */
public class ConnectModel implements Parcelable{
    /**
     * 当此IP地址Ping不通时的备用IP
     */
    private List<String> mServerUrlList;
    private boolean mIsTryConnect = true;
    private long mRoomId;
    private String mUUID;
    private long mUId;
    //多长时间没收到消息的时候主动检查一次
    private long checkMsgOutTime;
    //主动检查一次没有响应多少秒算是超时
    private long checkMsgOutTimeInterval;
    private long elapsedRealtime;
    private long mServerTime;
    private int mErrorTryMaxCount;

    private final Object syncServerList = new Object();

    private ConnectModel(List<String> serverUrlList, long serverTime) {
        this.mServerUrlList = serverUrlList;
        this.elapsedRealtime = SystemClock.elapsedRealtime();
        this.mServerTime = serverTime;
    }

    protected ConnectModel(Parcel in) {
        mServerUrlList = in.createStringArrayList();
        mIsTryConnect = in.readByte() != 0;
        mRoomId = in.readLong();
        mUUID = in.readString();
        mUId = in.readLong();
        checkMsgOutTime = in.readLong();
        checkMsgOutTimeInterval = in.readLong();
        elapsedRealtime = in.readLong();
        mServerTime = in.readLong();
        mErrorTryMaxCount = in.readInt();
    }

    public static final Parcelable.Creator<ConnectModel> CREATOR = new Parcelable.Creator<ConnectModel>() {
        @Override
        public ConnectModel createFromParcel(Parcel in) {
            return new ConnectModel(in);
        }

        @Override
        public ConnectModel[] newArray(int size) {
            return new ConnectModel[size];
        }
    };

    public Object getSyncServerList() {
        return syncServerList;
    }

    public List<String> getServerUrlList() {
        synchronized (syncServerList) {
            return mServerUrlList;
        }
    }

    public void setServerUrlList(List<String> mServerUrlList) {
        synchronized (syncServerList) {
            this.mServerUrlList = mServerUrlList;
        }
    }

    public long getElapsedRealtime() {
        return elapsedRealtime;
    }

    public void setElapsedRealtime(long elapsedRealtime) {
        this.elapsedRealtime = elapsedRealtime;
    }

    public long getServerTime() {
        return mServerTime;
    }

    public void setServerTime(long mServerTime) {
        this.mServerTime = mServerTime;
    }

    public long getRoomId() {
        return mRoomId;
    }

    public void setRoomId(long mRoomId) {
        this.mRoomId = mRoomId;
    }

    public String getUUID() {
        return mUUID;
    }

    public void setUUID(String mUUID) {
        this.mUUID = mUUID;
    }

    public long getUId() {
        return mUId;
    }

    public void setUId(long mUId) {
        this.mUId = mUId;
    }

    public long getOutTime() {
        return checkMsgOutTime;
    }

    public void setOutTime(long outTime) {
        this.checkMsgOutTime = outTime;
    }

    public long getCheckOutTimeInterval() {
        return checkMsgOutTimeInterval;
    }

    public void setCheckOutTimeInterval(long checkOutTimeInterval) {
        this.checkMsgOutTimeInterval = checkOutTimeInterval;
    }

    public boolean isTryConnect() {
        return mIsTryConnect;
    }

    public void setTryConnect(boolean tryConnect) {
        mIsTryConnect = tryConnect;
    }

    public int getErrorTryMaxCount() {
        return mErrorTryMaxCount;
    }

    public void setErrorTryMaxCount(int errorTryMaxCount) {
        this.mErrorTryMaxCount = errorTryMaxCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConnectModel that = (ConnectModel) o;
        return mIsTryConnect == that.mIsTryConnect &&
                mRoomId == that.mRoomId &&
                mUUID == that.mUUID &&
                mUId == that.mUId &&
                checkMsgOutTime == that.checkMsgOutTime &&
                checkMsgOutTimeInterval == that.checkMsgOutTimeInterval &&
                elapsedRealtime == that.elapsedRealtime &&
                mServerTime == that.mServerTime &&
                equals(mServerUrlList, that.mServerUrlList);
    }

    public static boolean equals(Object a, Object b) {
        return (a == b) || (a != null && a.equals(b));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(mServerUrlList);
        dest.writeByte((byte) (mIsTryConnect ? 1 : 0));
        dest.writeLong(mRoomId);
        dest.writeString(mUUID);
        dest.writeLong(mUId);
        dest.writeLong(checkMsgOutTime);
        dest.writeLong(checkMsgOutTimeInterval);
        dest.writeLong(elapsedRealtime);
        dest.writeLong(mServerTime);
        dest.writeInt(mErrorTryMaxCount);
    }


    public static class Builder {
        private List<String> serverUrlList;
        private boolean isTryConnect = true;
        private long roomId;
        private String uuid;
        private long uId;
        private long checkMsgOutTime = 160 * 1000;//270 检查信息
        private long checkMsgOutTimeInterval = 3 * 1000;
        private long serverTime;
        private int errorTryMaxCount = 10;

        public Builder(List<String> serverUrlList, long serverTime) {
            this.serverUrlList = serverUrlList;
            this.serverTime = serverTime;
        }

        public boolean isTryConnect() {
            return isTryConnect;
        }

        public Builder setTryConnect(boolean tryConnect) {
            isTryConnect = tryConnect;
            return this;
        }

        public long getRoomId() {
            return roomId;
        }

        public Builder setRoomId(long roomId) {
            this.roomId = roomId;
            return this;
        }

        public String getUuid() {
            return uuid;
        }

        public Builder setUuid(String uuid) {
            this.uuid = uuid;
            return this;
        }

        public long getUId() {
            return uId;
        }

        public Builder setUId(long uId) {
            this.uId = uId;
            return this;
        }

        public Builder setOutTime(long outTime) {
            this.checkMsgOutTime = outTime;
            return this;
        }

        public Builder setCheckOutTimeInterval(long checkOutTimeInterval) {
            this.checkMsgOutTimeInterval = checkOutTimeInterval;
            return this;
        }


        public Builder setErrorTryMaxCount(int errorTryMaxCount) {
            this.errorTryMaxCount = errorTryMaxCount;
            return this;
        }

        public ConnectModel build() {
            ConnectModel connectBean = new ConnectModel(serverUrlList, serverTime);
            connectBean.checkMsgOutTimeInterval = checkMsgOutTimeInterval;
            connectBean.mErrorTryMaxCount = errorTryMaxCount;
            connectBean.mIsTryConnect = isTryConnect;
            connectBean.mRoomId = roomId;
            connectBean.mUId = uId;
            connectBean.mUUID = uuid;
            connectBean.checkMsgOutTime = checkMsgOutTime;
            return connectBean;
        }
    }


    @Override
    public String toString() {
        return "ConnectBean{" +
                "mServerUrlList=" + mServerUrlList +
                ", mRoomId=" + mRoomId +
                '}';
    }
}
