package com.yado.pryado.pryadonew.util;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ToastUtils;
import com.jmolsmobile.landscapevideocapture.VideoCaptureActivity;
import com.jmolsmobile.landscapevideocapture.configuration.CaptureConfiguration;
import com.jmolsmobile.landscapevideocapture.configuration.PredefinedCaptureConfigurations;
import com.yado.pryado.pryadonew.R;
import com.yado.pryado.pryadonew.base.BaseActivity;
import com.yado.pryado.pryadonew.service.RecordService;
import com.yado.pryado.pryadonew.ui.widgit.AudioRecoderDialog;
import com.yado.pryado.pryadonew.ui.widgit.PlayView;

import java.lang.ref.WeakReference;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class RecorderAndShootUtil {
    public static String TAG = "RecorderAndShootUtil";
    @SuppressLint("StaticFieldLeak")
    private static RecorderAndShootUtil instance;
    private WeakReference<Context>mContext;
//    private Context mContext;

    //录音相关时间参数
    private long lastTime = 0;
    private long curTime;
    private boolean isRecordOK = false;
    private RecordService mRecordService;
    private boolean isPlaying;
    private String voice_path = "";
    private String voiceName = "";
    private AudioRecoderDialog recoderDialog;

    public static RecorderAndShootUtil getInstance(Context context) {
        if (instance == null) {
            instance = new RecorderAndShootUtil(context);
        }
        return instance;
    }

    private RecorderAndShootUtil(Context context) {
        mContext = new WeakReference<>(context);
        mRecordService = new RecordService(mContext.get());
    }

    public RecordService getmRecordService() {
        return mRecordService;
    }

    public String getVoice_path() {
        return voice_path;
    }

    public void playVideo(String videofilename) {
        if (videofilename == null) {
            ToastUtils.showShort("当前无视频，请录制后播放！");
            return;
        }
        final Intent videoIntent = new Intent(Intent.ACTION_VIEW);
        videoIntent.setDataAndType(Uri.parse(videofilename), "video/*");
        try {
            mContext.get().startActivity(videoIntent);
        } catch (ActivityNotFoundException e) {
            // NOP
        }
    }

    public void gotoCaptureActivity() {
        final CaptureConfiguration config = createCaptureConfiguration();

        String name = new SimpleDateFormat("_yyyyMMdd_kkmmss", Locale.getDefault()).format(new Date(System.currentTimeMillis()));
        final String filename = CameraFileUtil.initPath() + "Video" + name + ".mp4";

        final Intent intent = new Intent(mContext.get(), VideoCaptureActivity.class);
        intent.putExtra(VideoCaptureActivity.EXTRA_CAPTURE_CONFIGURATION, config);
        intent.putExtra(VideoCaptureActivity.EXTRA_OUTPUT_FILENAME, filename);
        ((BaseActivity) mContext.get()).startActivityForResult(intent, 101);
    }

    public void updateStatusAndThumbnail(TextView tvVideoName, ImageView ivThumbnail, String videostatusMessage, String video_path) {
        tvVideoName.setText(getShowName(video_path));
        if (TextUtils.isEmpty(videostatusMessage)) {
            videostatusMessage = mContext.get().getString(R.string.has_no_video);
        }
        ToastUtils.showShort(videostatusMessage);
        final Bitmap thumbnail = getThumbnail(video_path);
        if (thumbnail != null) {
            ivThumbnail.setImageBitmap(thumbnail);
        } else {
            ivThumbnail.setImageResource(R.drawable.ic_video);
        }
    }

    private Bitmap getThumbnail(String video_path) {
        if (video_path == null) {
            return null;
        }
        return ThumbnailUtils.createVideoThumbnail(video_path, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
    }

    private String getShowName(String video_path) {
        String showName = "";
        if (!TextUtils.isEmpty(video_path)) {
            try {
                showName = video_path.substring(video_path.lastIndexOf("/") + 1, video_path.length());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (TextUtils.isEmpty(showName)) {
            showName = mContext.get().getString(R.string.has_no_video);
        }
        return showName;
    }

    private CaptureConfiguration createCaptureConfiguration() {
        final PredefinedCaptureConfigurations.CaptureResolution resolution = PredefinedCaptureConfigurations.CaptureResolution.RES_720P;
        final PredefinedCaptureConfigurations.CaptureQuality quality = PredefinedCaptureConfigurations.CaptureQuality.LOW;
        CaptureConfiguration.Builder builder = new CaptureConfiguration.Builder(resolution, quality);
        builder.maxDuration(10);
        builder.maxFileSize(2);
//        builder.frameRate(20);
        builder.showRecordingTime();
        builder.noCameraToggle();
        return builder.build();
    }

    public void playRecord(String voicePath, final PlayView tvRecordPlay) {
        isPlaying = mRecordService.getPlayerState();
        isPlaying = !isPlaying;
        if ("暂未录音".equals(voicePath)) {
            mRecordService.setRecordFileName(null);
        } else if (voicePath.contains("http")) {
            mRecordService.setRecordFileName(voicePath);
            tvRecordPlay.startPlay();
        } else if("".equals(voicePath)){
            mRecordService.setRecordFileName(null);
        }
        else {
            mRecordService.setRecordFileName(CameraFileUtil.initPath() + voicePath);
            tvRecordPlay.startPlay();
        }
        mRecordService.onPlay(isPlaying);
        mRecordService.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                tvRecordPlay.stopPlay();
            }
        });
    }

    public void recordSound(final TextView view) {
        recoderDialog = new AudioRecoderDialog(mContext.get()) {
            @Override
            protected void startRecord() {
                lastTime = System.currentTimeMillis();
                mRecordService.onRecord(true);
                isRecordOK = false;
            }

            @Override
            protected void cancelRecord() {
                curTime = System.currentTimeMillis();
                ToastUtils.showShort("取消录音！");
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        mRecordService.onRecord(false);
                    }
                }, 500);
                isRecordOK = false;
            }

            @Override
            protected void stopRecord() {
                curTime = System.currentTimeMillis();
                //具体时间值未知
                if (curTime - lastTime >= 1000) {
                    mRecordService.onRecord(false);
                    isRecordOK = true;
                } else {
                    ToastUtils.showShort("说话时间太短！");
                    //释放Record
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            mRecordService.onRecord(false);
                        }
                    }, 500);
                    isRecordOK = false;
                }
                if (isRecordOK) {
                    if (mRecordService.getRecordFileName() != null) {
                        voiceName = mRecordService.getRecordFileName();
                        view.setText(voiceName);
                        voice_path = mRecordService.getFilePath();
                    }
                } else {
                    ToastUtils.showShort("录音保存失败！");
                }
            }
        };
        recoderDialog.setShowAlpha(0.98f);
        mRecordService.setOnAudioStatusUpdateListener(new RecordService.OnAudioStatusUpdateListener() {
            @Override
            public void onUpdate(double db) {
                if (null != recoderDialog) {
                    recoderDialog.setLevel((int) db);
                    recoderDialog.setTime(System.currentTimeMillis() - lastTime);
                }
            }
        });
        recoderDialog.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    public void reset() {
        if (instance != null) {
            instance = null;
            mContext = null;
            mRecordService.stopPlaying();
            mRecordService = null;
        }
    }

}
