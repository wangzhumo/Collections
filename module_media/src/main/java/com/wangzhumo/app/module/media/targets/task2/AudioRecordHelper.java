package com.wangzhumo.app.module.media.targets.task2;


import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-08-22  23:17
 */
public class AudioRecordHelper {

    private static final String TAG = "AudioRecordHelper";

    private AudioRecord mRecorder;
    private int mBufferSizeInBytes = 0; // Bufffer的大小字段
    private int channel = AudioFormat.CHANNEL_IN_STEREO;
    private int sampleRate = 44100;
    private int encodingFormat = AudioFormat.ENCODING_PCM_16BIT;
    private File outputFile;  //输出文件 pcm
    private ExecutorService mExecutor;

    private int mState = AudioState.DISABLE;


    /**
     * 创建Record实例
     */
    private boolean createRecorder() {
        //获取系统提供的bufferSize
        mBufferSizeInBytes = AudioRecord.getMinBufferSize(sampleRate, channel, encodingFormat);
        //创建AudioRecord实例
        mRecorder = new AudioRecord(
                MediaRecorder.AudioSource.MIC,
                sampleRate,
                channel,
                encodingFormat,
                mBufferSizeInBytes
        );

        if (mRecorder.getState() != AudioRecord.STATE_INITIALIZED) {
            mRecorder = null;
            mBufferSizeInBytes = 0;
            mState = AudioState.DISABLE;
            return false;
        }
        mState = AudioState.INIT;
        return true;
    }

    public void setOutPutFile(File outputFile) {
        this.outputFile = outputFile;
    }

    /**
     * 开始录制音频
     */
    public void startRecord() {
        //是否有文件
        if (outputFile == null || !outputFile.exists() || !outputFile.isFile()) {
            throw new IllegalArgumentException("OutputFile Error.");
        }
        if (mState == AudioState.DISABLE || mState == AudioState.RECORDING) {
            throw new IllegalStateException("AudioRecord State Error.");
        }
        Log.d(TAG, "startRecord outputFile = " + outputFile.getAbsolutePath());
        //开始录制
        mRecorder.startRecording();
        mState = AudioState.RECORDING;

        //开启写文件线程.
        if (mExecutor == null) {
            mExecutor = Executors.newCachedThreadPool();
        }
        mExecutor.execute(writePcmToFile);
    }


    /**
     * 停止录制.
     */
    public void stopRecord() {
        Log.d(TAG, "stopRecord");
        if (mState == AudioState.DISABLE || mState == AudioState.INIT) {
            Log.d(TAG, "没有开始录制");
        } else {
            mRecorder.stop();
            mState = AudioState.INIT;
            release();
        }
    }


    /**
     * 释放资源
     */
    public void release() {
        Log.d(TAG, "release record");
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }
        mState = AudioState.DISABLE;
    }

    private Runnable writePcmToFile = new Runnable() {
        @Override
        public void run() {
            OutputStream outputStream = null;
            try {
                outputStream = new BufferedOutputStream(new FileOutputStream(outputFile));
                byte[] audioData = new byte[mBufferSizeInBytes];
                while (mState == AudioState.RECORDING) {
                    int readSize = mAudioRecord.read(audioData, 0, mBufferSizeInBytes);
                    if (readSize > 0) {
                        try {
                            bos.write(audioData, 0, readSize);
                            if (mRecordStreamListener != null) {
                                mRecordStreamListener.onRecording(audioData, 0, readSize);
                            }
                        } catch (IOException e) {
                            Log.e(TAG, "writeAudioDataToFile", e);
                        }
                    } else {
                        Log.w(TAG, "writeAudioDataToFile readSize: " + readSize);
                    }
                }
                bos.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

}
