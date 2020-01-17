package com.wangzhumo.app.module.media.targets.task2;


import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
    private File wavFile;  //输出文件 wav
    private ExecutorService mExecutor;
    private onRecordListener listener;

    private int mState = AudioState.DISABLE;


    private AudioRecordHelper() {
        createRecorder();
    }

    private static class Singleton {
        private static AudioRecordHelper INSTANCE = new AudioRecordHelper();
    }


    /**
     * 获取AudioRecordHelper实例
     *
     * @return AudioRecordHelper
     */
    public static AudioRecordHelper getInstance() {
        return Singleton.INSTANCE;
    }


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

        if (mRecorder.getState() == AudioRecord.STATE_UNINITIALIZED) {
            mRecorder = null;
            mBufferSizeInBytes = 0;
            mState = AudioState.DISABLE;
            return false;
        }
        mState = AudioState.INIT;
        return true;
    }


    /**
     * 设置输出文件
     *
     * @param outputFile
     */
    public void setOutPutFile(File outputFile) {
        this.outputFile = outputFile;
    }


    /**
     * 设置录制的监听
     * @param recordListener onRecordListener
     */
    public void setOnRecordListener(onRecordListener recordListener){
        this.listener = recordListener;
    }


    /**
     * 开始录制音频
     */
    public void startRecord() {
        //是否有文件
        if (outputFile == null) {
            throw new IllegalArgumentException("OutputFile Error.");
        }
        if (mState == AudioState.DISABLE || mState == AudioState.RECORDING) {
            throw new IllegalStateException("AudioRecord State Error.");
        }
        Log.d(TAG, "startRecord outputFile = " + outputFile.getAbsolutePath());
        //开始录制
        mRecorder.startRecording();
        mState = AudioState.RECORDING;
        if (listener != null){
            listener.onRecording();
        }
        //开启写文件线程.
        if (mExecutor == null) {
            mExecutor = Executors.newCachedThreadPool();
        }
        mExecutor.execute(writePcmToFile);
    }


    /**
     * PCM转化为Wav文件
     */
    public void covertPcm2Wav() {
        createWavFile();
        makePcm2Wav();
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

    /**
     * 创建一个临时文件
     */
    private void createWavFile() {
        String filePcmFile = outputFile.getAbsolutePath();
        //创建一个临时文件
        String wavFilePath = filePcmFile.replace(IMediaType.PCM, IMediaType.WAV);
        wavFile = new File(wavFilePath);
    }


    /**
     * 将一个pcm文件转化为wav文件
     *
     * @return boolean
     */
    private boolean makePcm2Wav() {
        byte[] buffer;
        if (!outputFile.exists()) {
            return false;
        }
        WavHeader wavHeader = new WavHeader(outputFile.length(), sampleRate, channel, encodingFormat);
        byte[] h = wavHeader.toBytes();
        if (h.length != 44) // WAV标准，头部应该是44字节,如果不是44个字节则不进行转换文件
            return false;
        //合成所有的pcm文件的数据，写到目标文件
        try {
            buffer = new byte[1024 * 4]; // Length of All Files, Total Size
            InputStream inStream;
            OutputStream ouStream;
            ouStream = new BufferedOutputStream(new FileOutputStream(wavFile));
            ouStream.write(h, 0, h.length);
            inStream = new BufferedInputStream(new FileInputStream(outputFile));
            int size = inStream.read(buffer);
            while (size != -1) {
                ouStream.write(buffer);
                size = inStream.read(buffer);
            }
            inStream.close();
            ouStream.close();
            if (listener != null){
                listener.onWavFile(wavFile);
            }
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException ioe) {
            return false;
        }
        return true;
    }

    /**
     * 写PCM数据到文件中
     */
    private Runnable writePcmToFile = new Runnable() {
        @Override
        public void run() {
            OutputStream outputStream = null;
            try {
                outputStream = new BufferedOutputStream(new FileOutputStream(outputFile));
                byte[] audioData = new byte[mBufferSizeInBytes];
                while (mState == AudioState.RECORDING) {
                    int readSize = mRecorder.read(audioData, 0, mBufferSizeInBytes);
                    if (readSize > 0) {
                        outputStream.write(audioData, 0, readSize);
                    } else {
                        Log.e(TAG, "writePcmToFile readSize: " + readSize);
                    }
                }
                if (listener != null){
                    listener.onRecorded(outputFile);
                }
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };


    public interface onRecordListener {
        void onRecording();
        void onRecorded(File pcm);
        void onWavFile(File wav);
    }
}
