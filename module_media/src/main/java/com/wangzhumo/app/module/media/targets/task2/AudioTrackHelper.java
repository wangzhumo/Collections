package com.wangzhumo.app.module.media.targets.task2;

import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-08-23  00:38
 * <p>
 * 使用AudioTrack播放PCM文件
 */
public class AudioTrackHelper {

    private static final String TAG = "AudioTrackHelper";

    private AudioTrack mAudioTrack;
    private int mBufferSizeInBytes = 0; // Bufffer的大小字段
    private int channel = AudioFormat.CHANNEL_IN_STEREO;
    private int sampleRate = 44100;
    private int encodingFormat = AudioFormat.ENCODING_PCM_16BIT;
    private ExecutorService mExecutor;

    private File inputFile;

    private int mState;  //当前的状态

    private AudioTrackHelper() {
        createAudioTrack();
    }

    private static class Singleton {
        private static AudioTrackHelper INSTANCE = new AudioTrackHelper();
    }


    /**
     * 获取AudioTrackHelper实例
     *
     * @return AudioTrackHelper
     */
    public static AudioTrackHelper getInstance() {
        return AudioTrackHelper.Singleton.INSTANCE;
    }


    /**
     * 创建Record实例
     */
    private boolean createAudioTrack() {
        //获取最小缓冲区 (每一秒的buffer大小)
        mBufferSizeInBytes = AudioTrack.getMinBufferSize(sampleRate, channel, encodingFormat);
        //创建AudioTrack实例
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)   //是指声音的用处.
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();

        AudioFormat format = new AudioFormat.Builder()
                .setSampleRate(sampleRate)
                .setEncoding(encodingFormat)
                .setChannelMask(channel)
                .build();

        //AudioManager.AUDIO_SESSION_ID_GENERATE  创建时不知道SESSION_ID,传入此值
        mAudioTrack = new AudioTrack(
                audioAttributes,
                format,
                mBufferSizeInBytes,
                AudioTrack.MODE_STREAM,    //表示输入源的类型.
                AudioManager.AUDIO_SESSION_ID_GENERATE);

        if (mAudioTrack.getState() == AudioTrack.STATE_UNINITIALIZED) {
            mAudioTrack = null;
            mBufferSizeInBytes = 0;
            mState = AudioState.DISABLE;
            return false;
        }
        mState = AudioState.INIT;
        return true;
    }


    /**
     * 设置输入文件
     *
     * @param inputFile
     */
    public void setInputFile(File inputFile) {
        this.inputFile = inputFile;
    }


    public void startPlay() {
        if (inputFile == null) {
            throw new IllegalArgumentException("InputFile Error.");
        }
        startPlay(inputFile);
    }

    /**
     * 开始录制音频
     */
    public void startPlay(File file) {
        //是否有文件
        if (file == null) {
            throw new IllegalArgumentException("OutputFile Error.");
        }
        if (mState == AudioState.DISABLE || mState == AudioState.PLAYING) {
            throw new IllegalStateException("AudioTrack State Error.");
        }
        Log.d(TAG, "startPlay inputFile = " + file.getAbsolutePath());
        //开始播放
        mAudioTrack.play();
        mState = AudioState.PLAYING;

        //开启读取文件线程.
        if (mExecutor == null) {
            mExecutor = Executors.newCachedThreadPool();
        }
        mExecutor.execute(readPcmFile);
    }


    /**
     * 停止播放
     */
    public void stopPlay() {
        Log.d(TAG, "stopPlay");
        if (mState == AudioState.DISABLE || mState == AudioState.INIT) {
            Log.d(TAG, "没有开始播放");
        } else {
            mAudioTrack.stop();
            mState = AudioState.INIT;
            release();
        }
    }


    /**
     * 释放资源
     */
    public void release() {
        Log.d(TAG, "release record");
        if (mAudioTrack != null) {
            mAudioTrack.release();
            mAudioTrack = null;
        }
        mState = AudioState.DISABLE;
    }


    /**
     * 写PCM数据到文件中
     */
    private Runnable readPcmFile = new Runnable() {
        @Override
        public void run() {
            FileInputStream inputStream = null;
            try {
                inputStream = new FileInputStream(inputFile);
                byte[] audioData = new byte[mBufferSizeInBytes];
                while (mState == AudioState.PLAYING && inputStream.available() > 0) {
                    //读取文件
                    int readSize = inputStream.read(audioData);
                    if (readSize == AudioTrack.ERROR_INVALID_OPERATION || readSize == AudioTrack.ERROR_BAD_VALUE) {
                        continue;
                    }
                    if (readSize > 0) {
                        //把读取到的数据放到AudioTrack中
                        mAudioTrack.write(audioData, 0, readSize);
                    }
                }
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
}
