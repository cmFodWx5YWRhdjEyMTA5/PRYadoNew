package com.yado.pryado.pryadonew.ui.device_detail.danger;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.blankj.utilcode.util.ToastUtils;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.jmolsmobile.landscapevideocapture.VideoCaptureActivity;
import com.yado.pryado.pryadonew.MyConstants;
import com.yado.pryado.pryadonew.R;
import com.yado.pryado.pryadonew.base.BaseActivity;
import com.yado.pryado.pryadonew.base.BaseFragment;
import com.yado.pryado.pryadonew.bean.DeviceDetailBean2;
import com.yado.pryado.pryadonew.bean.DeviceInfo;
import com.yado.pryado.pryadonew.bean.PostBugResult;
import com.yado.pryado.pryadonew.net.OkhttpUtils;
import com.yado.pryado.pryadonew.receiver.DetectInfraredReceiver;
import com.yado.pryado.pryadonew.ui.adapter.PhotoAdapter;
import com.yado.pryado.pryadonew.ui.device_detail.DeviceDetailContract;
import com.yado.pryado.pryadonew.ui.device_detail.DeviceDetailPresent;
import com.yado.pryado.pryadonew.ui.widgit.EmptyLayout;
import com.yado.pryado.pryadonew.ui.widgit.MyClearEditText;
import com.yado.pryado.pryadonew.ui.widgit.PlayView;
import com.yado.pryado.pryadonew.util.AddPhotoUtil;
import com.yado.pryado.pryadonew.util.BitmapUtil;
import com.yado.pryado.pryadonew.util.LocationUtil;
import com.yado.pryado.pryadonew.util.RecorderAndShootUtil;

import org.greenrobot.eventbus.Subscribe;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;


public class DangerFragment extends BaseFragment<DeviceDetailPresent> implements DeviceDetailContract.View {

    @BindView(R.id.tv_PName)
    TextView tvPName;
    @BindView(R.id.tv_device_name)
    TextView tvDeviceName;
    @BindView(R.id.note)
    MyClearEditText note;
    @BindView(R.id.bug_Location)
    MyClearEditText bugLocation;
    @BindView(R.id.latitude)
    TextView latitude;
    @BindView(R.id.longitude)
    TextView longitude;
    @BindView(R.id.risk_State)
    RadioGroup riskState;
    @BindView(R.id.tv_record_name)
    TextView tvRecordName;
    @BindView(R.id.tv_record_play)
    PlayView tvRecordPlay;
    @BindView(R.id.tv_video_name)
    TextView tvVideoName;
    @BindView(R.id.iv_thumbnail)
    ImageView ivThumbnail;
    @BindView(R.id.rv_photos)
    RecyclerView rvPhotos;
    @BindView(R.id.rv_infrared_photos)
    RecyclerView rvInfraredPhotos;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.emptyLayout)
    EmptyLayout emptyLayout;

    private WeakReference<DeviceDetailBean2> mBean;
    //    private DeviceDetailBean2 mBean;
    private int pid;
    private NiftyDialogBuilder dialogBuilder;

    private ArrayList<String> upLoadPhotos = new ArrayList<>();
        private WeakReference<String> videostatusMessage, path;
    private String voice_path = "";
    private String voiceName = "";
    private String videofilename;
//    private String videostatusMessage;
    private ArrayList<String> upLoadInfred = new ArrayList<>();
    private HashMap<String, String> temps = new HashMap<>();
//        private String path;
    private float maxTemp, minTemp;
    //    private WeakReference<DeviceInfo> mDeviceInfo;
    private DeviceInfo mDeviceInfo;

    private LatLng latLng;
    private Thread thread;
    private boolean isRun;
    private String riskStatus;


    @Inject
    PhotoAdapter normalAdapter;

    @Inject
    PhotoAdapter infraredAdapter;

    private LocationUtil locationUtil;

    private View progressView;
    private NumberProgressBar numberProgressBar;
    private TextView tv_msg;
//
//    @SuppressLint("HandlerLeak")
//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            boolean isDone = (boolean) msg.obj;
//            int total = msg.arg1;
//            int current = msg.arg2;
//            numberProgressBar.setMax(total);
//            if (!isDone) {
//                numberProgressBar.setProgress(current);
//            } else {
//                dialogBuilder.dismiss();
//            }
//
//        }
//    };

    private MyHandler handler = new MyHandler(DangerFragment.this);

    private static class MyHandler extends Handler {

        WeakReference<DangerFragment> weakReference;

        MyHandler(DangerFragment dangerFragment) {
            this.weakReference = new WeakReference<>(dangerFragment);

        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            boolean isDone = (boolean) msg.obj;
            int total = msg.arg1;
            int current = msg.arg2;
            weakReference.get().numberProgressBar.setMax(total);
            if (!isDone) {
                weakReference.get().numberProgressBar.setProgress(current);
            } else {
                weakReference.get().dialogBuilder.dismiss();
            }

        }
    }

    private DetectInfraredReceiver reciver;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initReceiver();
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.danger_fragment;
    }

    public static DangerFragment newInstance(String encode, DeviceDetailBean2 detailBean, int pid) {
        Bundle bundle = new Bundle();
        bundle.putString(MyConstants.ENCODE, encode);
        bundle.putInt(MyConstants.PID, pid);
        bundle.putParcelable("DeviceDetailBean", detailBean);
        DangerFragment fragment = new DangerFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    public void initView() {

    }

    @Override
    protected void loadData() {
        initRecyclerView();
        initUploadView();
        mBean = new WeakReference<>((DeviceDetailBean2) getArguments().getParcelable("DeviceDetailBean"));
        pid = getArguments().getInt(MyConstants.PID);
        Bundle bundle = getArguments();
        String encode = bundle.getString(MyConstants.ENCODE);
        locationUtil = LocationUtil.getInstance(context);
        locationUtil.initLocation();
        emptyLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
        if (!TextUtils.isEmpty(encode) && encode.length() == 11 && mDeviceInfo == null) {
            assert mPresenter != null;
            mPresenter.getInfoByCode(encode.substring(0, 10));
        } else {
            if (mDeviceInfo == null) {
                mDeviceInfo =new DeviceInfo();
            }
            mDeviceInfo.setPID(pid + "");
            mDeviceInfo.setDID(mBean.get().getDID());
            mDeviceInfo.setPName(mBean.get().getCompany());
            mDeviceInfo.setDeviceName(mBean.get().getDeviceName());
            setViewAndData();
        }
        emptyLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
        scrollView.setVisibility(View.VISIBLE);
        emptyLayout.setVisibility(View.GONE);
        riskState.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton mRadioButton = riskState.findViewById(riskState.getCheckedRadioButtonId());
                riskStatus =mRadioButton.getText().toString();
            }
        });
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(context);
        linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvPhotos.setLayoutManager(linearLayoutManager1);
        rvPhotos.setAdapter(normalAdapter);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(context);
        linearLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvInfraredPhotos.setLayoutManager(linearLayoutManager2);
        rvInfraredPhotos.setAdapter(infraredAdapter);
    }

    private void initUploadView() {
        progressView = LayoutInflater.from(context).inflate(R.layout.defect_upload_item, null);
        numberProgressBar = progressView.findViewById(R.id.np_progress);
        tv_msg = progressView.findViewById(R.id.tv_msg);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState != null) {
            if (voice_path != null) {
                outState.putString("voice_path", voice_path);
            } else {
                outState.putString("voice_path", "");
            }
            if (voiceName != null) {
                outState.putString("voiceName", voiceName);
            } else {
                outState.putString("voiceName", "");
            }
            outState.putStringArrayList("upLoadPhotos", upLoadPhotos);
            outState.putStringArrayList("upLoadInfred", upLoadInfred);
            outState.putParcelable("mDeviceInfo", mDeviceInfo);
        }
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            voice_path = savedInstanceState.getString("voice_path");
            voiceName = savedInstanceState.getString("voiceName");
            upLoadPhotos = savedInstanceState.getStringArrayList("upLoadPhotos");
            upLoadInfred = savedInstanceState.getStringArrayList("upLoadInfred");
            mDeviceInfo =savedInstanceState.getParcelable("mDeviceInfo");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (AddPhotoUtil.getInstance(context).fromInfred && path != null) {
            AddPhotoUtil.getInstance(context).getInfrared(path.get(), temps, minTemp, maxTemp, upLoadInfred);
            infraredAdapter.setNewData(upLoadInfred);
            path = null;
            minTemp = maxTemp = 0;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {   //录制视频
            if (resultCode == Activity.RESULT_OK) {
                videofilename = data.getStringExtra(VideoCaptureActivity.EXTRA_OUTPUT_FILENAME);
                videostatusMessage = new WeakReference<>(String.format("录制完成: %s", videofilename));
            } else if (resultCode == Activity.RESULT_CANCELED) {
                videostatusMessage = new WeakReference<>(getString(R.string.status_capturecancelled));
            } else if (resultCode == VideoCaptureActivity.RESULT_ERROR) {
                videofilename = null;
                videostatusMessage =  new WeakReference<>(getString(R.string.status_capturefailed));
            }
            RecorderAndShootUtil.getInstance(context).updateStatusAndThumbnail(tvVideoName, ivThumbnail, videostatusMessage.get(), videofilename);
        }

        if (data != null && resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                //photoPicker
                case PhotoPicker.REQUEST_CODE://直接选择图库返回
                case PhotoPreview.REQUEST_CODE://预览选择返回
                    int tag = data.getIntExtra("tag", 0);//0:预览/删除返回，2:添加返回
                    String from = data.getStringExtra("from");

                    if (from != null && from.equals(MyConstants.ADD_NORMAL_PHOTO)) {  //添加图片
                        ArrayList<String> selectPhoto = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                        ArrayList<String> takePhoto = data.getStringArrayListExtra(MyConstants.KEY_TAKE_PHOTO);
                        if (tag == 0) {
                            upLoadPhotos.clear();
                        }
                        if (selectPhoto != null && selectPhoto.size() > 0) {
                            upLoadPhotos.addAll(selectPhoto);
                        }
                        if (takePhoto != null && takePhoto.size() > 0) {
                            upLoadPhotos.addAll(takePhoto);
                        }
                        normalAdapter.setNewData(upLoadPhotos);
                    } else if (from != null && from.equals(MyConstants.INFRARED_PHOTO)) {  //添加红外图片
                        ArrayList<String> infraredImg = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                        if (tag == 0) {
                            upLoadInfred.clear();
                        }
                        if (infraredImg != null && infraredImg.size() > 0) {
                            upLoadInfred.addAll(infraredImg);
                        }
                        infraredAdapter.setNewData(upLoadInfred);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    //android7.0以上用getInfrared()获取不到图片，这里使用广播接收并用eventbus发送到此处
    @Subscribe
    public void getInfraredImg(DetectInfraredReceiver.InfraredEvent obj) {
        path =  new WeakReference<>(obj.getInfraredPath());
        maxTemp = obj.getMaxTemp();
        minTemp = obj.getMinTemp();
    }


    private void initReceiver() {
        //调用红外app需要
        //注册广播
        reciver = new DetectInfraredReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MyConstants.INFRARED_ACTION);
        context.registerReceiver(reciver, filter);
    }

    @OnClick({R.id.tv_record_add, R.id.tv_record_play, R.id.tv_video_add, R.id.iv_thumbnail, R.id.ib_add_image, R.id.ib_add_infrared, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_record_add:
                RecorderAndShootUtil.getInstance(context).recordSound(tvRecordName);
                break;
            case R.id.tv_record_play:
                RecorderAndShootUtil.getInstance(context).playRecord(tvRecordName.getText().toString(), tvRecordPlay);
                break;
            case R.id.tv_video_add:
                if (tvRecordPlay.isPlaying()) {
                    tvRecordPlay.stopPlay();
                    RecorderAndShootUtil.getInstance(context).getmRecordService().stopPlaying();
                }
                RecorderAndShootUtil.getInstance(context).gotoCaptureActivity();
                break;
            case R.id.iv_thumbnail:
                if (tvRecordPlay.isPlaying()) {
                    tvRecordPlay.stopPlay();
                    RecorderAndShootUtil.getInstance(context).getmRecordService().stopPlaying();
                }
                RecorderAndShootUtil.getInstance(context).playVideo(videofilename);
                break;
            case R.id.ib_add_image:
                AddPhotoUtil.getInstance(context).addPic(upLoadPhotos);
                break;
            case R.id.ib_add_infrared:
                if (upLoadInfred.size() >= 5) {
                    ToastUtils.showShort("您最多能添加5张红外照片！");
                } else {
                    AddPhotoUtil.getInstance(context).goToInfredApp();
                }
                break;
            case R.id.btn_submit:
                showDialog();
                break;
            default:
                break;
        }
    }

    private void showDialog() {
        if (dialogBuilder == null) {
            dialogBuilder = NiftyDialogBuilder.getInstance(context);
            dialogBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    numberProgressBar.setVisibility(View.GONE);
                }
            });
        }
        dialogBuilder
                .withTitle("隐患上报")                               //标题.withTitle(null)  no title
                .withTitleColor("#FFFFFF")                               //def  标题颜色
                .withDividerColor("#11000000")                           //def
                .withMessage(null)                 //.withMessage(null)  no Msg   内容
                .withMessageColor("#FFCFCFCF")                          //def  | withMessageColor(int resid)   内容颜色
                .withDialogColor("#AF444D50")                            //def  | withDialogColor(int resid)   dialog框颜色
                .withIcon(context.getResources().getDrawable(R.drawable.ic_logo2))  //标题栏图标
                .withDuration(700)                                      //def      动画持续时间
                .withEffect(Effectstype.SlideBottom)                                     //def Effectstype.Slidetop  动画模式
                .withButton1Text("取消")                                  //def gone     按钮文字
                .withButton2Text("确定")                              //def gone
                .isCancelableOnTouchOutside(false)                       //def    | isCancelable(true)是否支持点击dialog框外关闭dialog
                .setCustomView(progressView, context)     //.setCustomView(View or ResId,context)    自定义布局
                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                    }
                })
                .setButton2Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        postNewBug();

                    }
                })
                .show();    //展示
        tv_msg.setText("是否上传隐患？");
    }

    private void postNewBug() {
        if (mDeviceInfo == null) {
            ToastUtils.showShort("没有信息!");
            return;
        }
        if (riskStatus == null || "".equals(riskStatus)) {
            ToastUtils.showShort("请选择隐患状态！");
            return;
        }
        String temp;
        if (voice_path != null) {
            temp = voice_path;
        } else {
            temp = "";
        }
        if (OkhttpUtils.getInstance().compareMax(upLoadPhotos, temp, upLoadInfred)) {
            return;
        }
        tv_msg.setText(getResources().getString(R.string.upload_file));
        numberProgressBar.setVisibility(View.VISIBLE);
        assert mPresenter != null;
        mPresenter.postNewBug(mDeviceInfo.getPID(), mDeviceInfo.getDID(), bugLocation.getText().toString() + " " + latitude.getText().toString() + " " + longitude.getText().toString(), note.getText().toString() + " 隐患状态：" + riskStatus);
    }

    public void setDeviceInfo(DeviceInfo deviceInfo) {
        mDeviceInfo = deviceInfo;
        setViewAndData();
    }

    private void setViewAndData() {
        tvPName.setText("配电房：" + mDeviceInfo.getPName());
        tvDeviceName.setText("设备名称：" + mDeviceInfo.getDeviceName());
        thread = new Thread(new Runnable() {
            @Override
            public void run() {

                while (!isRun) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    latLng = locationUtil.getLatLng();
                    if (context != null) {
                        ((BaseActivity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (latLng != null) {
                                    setText(latLng);
                                }
                            }
                        });
                    }

                    if (latLng != null && latLng.latitude > 0) {
                        isRun = true;
                    }
                }
            }
        });
        thread.start();
    }

    private void setText(LatLng latLng) {
        latitude.setText(String.format("纬度：%.4f", latLng.latitude));
        longitude.setText(String.format("经度：%.4f", latLng.longitude));
    }

    public void setPostBugResult(PostBugResult postBugResult) {
        int resultCode = postBugResult.getResultCode();
        if (resultCode == 1) {
//                                TUtil.t("提交隐患参数成功!bugId=" + postBugResult.getBugid());
            uploadFile(postBugResult.getBugid() + "");
        } else {
            ToastUtils.showShort("提交失败!");
            dialogBuilder.dismiss();
        }
    }

    public void uploadFile(String bugId) {
        //展示
        OkhttpUtils.getInstance().setProgressListener(new OkhttpUtils.ProgressListener() {
            @Override
            public void onProgress(long current, long total, boolean done) {
                Message message = Message.obtain();
                message.obj = done;
                message.arg1 = (int) total;
                message.arg2 = (int) current;
                handler.sendMessage(message);

            }
        });
        if (videofilename != null) {
            OkhttpUtils.getInstance().upLoadFile(upLoadPhotos, RecorderAndShootUtil.getInstance(context).getVoice_path(), videofilename, upLoadInfred, (Activity) context, bugId + "", dialogBuilder, "bug", temps);

        } else {
            OkhttpUtils.getInstance().upLoadFile(upLoadPhotos, RecorderAndShootUtil.getInstance(context).getVoice_path(), "", upLoadInfred, (Activity) context, bugId + "", dialogBuilder, "bug", temps);
        }
//        OkhttpUtils.getInstance().upLoadFile(upLoadPhotos, RecorderAndShootUtil.getInstance(context).getVoice_path(), videofilename.get(), (ArrayList<String>) upLoadInfred, (Activity) context, bugId, dialogBuilder, "bug", temps);
    }

    @Override
    public void onDestroy() {
        AddPhotoUtil.getInstance(context).reset();
        RecorderAndShootUtil.getInstance(context).reset();
        OkhttpUtils.getInstance().reset();
        handler.removeCallbacksAndMessages(null);
        handler = null;
        if (thread != null && thread.isAlive()) {
            thread.interrupt();
            thread = null;
        }
        if (locationUtil != null) {
            locationUtil.reset();
        }
        if (reciver != null) {
            context.unregisterReceiver(reciver);
            reciver = null;
        }
        if (upLoadInfred != null) {
            upLoadInfred.clear();
            upLoadInfred = null;
        }
        if (upLoadPhotos != null) {
            upLoadPhotos.clear();
            upLoadPhotos = null;
        }
        if (temps != null) {
            temps.clear();
            temps = null;
        }
        super.onDestroy();
    }

}
