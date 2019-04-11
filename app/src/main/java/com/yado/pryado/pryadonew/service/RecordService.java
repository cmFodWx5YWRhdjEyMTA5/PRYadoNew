package com.yado.pryado.pryadonew.service;


import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.czt.mp3recorder.MP3Recorder;
import com.yado.pryado.pryadonew.util.CameraFileUtil;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class RecordService {

    private Vibrator vibrator;//设备震动
    private static final String TAG = "RecordService";
    private Context mContext;
    //    private MediaRecorder mMediaRecorder = null;
    private MediaPlayer mMediaPlayer = null;
    private String mFileName = null;
    private final Handler mHandler = new Handler();
    private long[] pattern = {100, 200};
    private MP3Recorder mRecorder;
    private OnAudioStatusUpdateListener audioStatusUpdateListener;

    public RecordService(Context context) {
        mContext = context;
        vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);

    }

    public void setOnAudioStatusUpdateListener(OnAudioStatusUpdateListener audioStatusUpdateListener) {
        this.audioStatusUpdateListener = audioStatusUpdateListener;
    }

    public interface OnAudioStatusUpdateListener {
        void onUpdate(double db);
    }

    public String getFilePath() {
        return mFileName;
    }

    public String getRecordFileName() {

        if (mFileName == null) {
            return null;
        }

        File file = new File(mFileName);

        return file.getName();
    }

    public void setRecordFileName(String fileName) {
        mFileName = fileName;
        Log.e(TAG, "setRecordFileName: " + mFileName);
    }

    public void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void startRecording() {
        vibrator.vibrate(pattern, -1);
        //kk为24小时制，hh为12小时制（上下午）
        String name = new SimpleDateFormat("_yyyyMMdd_kkmmss", Locale.getDefault()).format(new Date(System.currentTimeMillis()));
        mFileName = CameraFileUtil.initPath() + "Record" + name + ".mp3";
        mRecorder = new MP3Recorder(new File(CameraFileUtil.initPath(), "Record" + name + ".mp3"));
        try {
            mRecorder.start();
            updateMicStatus();
        } catch (Exception e) {
            Log.e(TAG, "MP3Recorder-- failed");
        }
    }

    private int BASE = 1;
    private int SPACE = 100;// 间隔取样时间

    private void updateMicStatus() {
        if (mRecorder != null) {
            double ratio = (double) mRecorder.getVolume() / BASE;
//            double db = 0;// 分贝
//            if (ratio > 1) {
//                db = 20 * Math.log10(ratio);
//                if (null != audioStatusUpdateListener) {
//                    audioStatusUpdateListener.onUpdate(db);
//                }
//            }
            if (null != audioStatusUpdateListener) {
                audioStatusUpdateListener.onUpdate(ratio);
            }
            mHandler.postDelayed(mUpdateMicStatusTimer, SPACE);
        }
    }

    private Runnable mUpdateMicStatusTimer = new Runnable() {
        @Override
        public void run() {
            updateMicStatus();
        }
    };

    private void stopRecording() {
        try {
            if (mRecorder != null) {
                mRecorder.stop();
                mRecorder = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        if (TextUtils.isEmpty(mFileName)) {
            ToastUtils.showShort( "当前无录音，请录音后播放！");
        } else {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.reset();
            try {
                mMediaPlayer.setDataSource(mFileName);
                if (mFileName.contains("http")) {
                    mMediaPlayer.prepareAsync();
                    mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mMediaPlayer.start();
                        }
                    });
                    mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            if (onCompletionListener != null) {
                                onCompletionListener.onCompletion(mp);
                            }
                        }
                    });
                } else {
                    mMediaPlayer.prepare();
                    mMediaPlayer.start();
                    mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            if (onCompletionListener != null) {
                                onCompletionListener.onCompletion(mp);
                            }
                        }
                    });
                }
            } catch (IOException e) {
                ToastUtils.showShort("播放失败!");
                Log.e(TAG, "MediaPlayer.prepare() failed" + e.getMessage());
            }
        }
    }

    MediaPlayer.OnCompletionListener onCompletionListener;

    public void setOnCompletionListener(MediaPlayer.OnCompletionListener onCompletionListener) {
        this.onCompletionListener = onCompletionListener;
    }

    public void stopPlaying() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public boolean getPlayerState() {
        return mMediaPlayer != null && mMediaPlayer.isPlaying();
    }
}
