package com.yado.pryado.pryadonew.ui.task;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.jmolsmobile.landscapevideocapture.VideoCaptureActivity;
import com.yado.pryado.pryadonew.MyApplication;
import com.yado.pryado.pryadonew.MyConstants;
import com.yado.pryado.pryadonew.R;
import com.yado.pryado.pryadonew.base.BaseActivity;
import com.yado.pryado.pryadonew.bean.OrderDetail;
import com.yado.pryado.pryadonew.net.EadoUrl;
import com.yado.pryado.pryadonew.net.OkhttpUtils;
import com.yado.pryado.pryadonew.receiver.DetectInfraredReceiver;
import com.yado.pryado.pryadonew.ui.adapter.PhotoAdapter;
import com.yado.pryado.pryadonew.ui.widgit.EmptyLayout;
import com.yado.pryado.pryadonew.ui.widgit.PlayView;
import com.yado.pryado.pryadonew.util.AddPhotoUtil;
import com.yado.pryado.pryadonew.util.DateUtils;
import com.yado.pryado.pryadonew.util.MapDialogUtil;
import com.yado.pryado.pryadonew.util.RecorderAndShootUtil;
import com.yado.pryado.pryadonew.util.SharedPrefUtil;

import org.greenrobot.eventbus.Subscribe;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;

@Route(path = "/ui/task/TaskActivity")
public class TaskActivity extends BaseActivity<TaskPresent> implements TaskContract.View {
    public static int TYPE_INI = 0;//初始的收到的下发工单
    @BindView(R.id.tv_pre)
    TextView tvPre;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_check_date)
    TextView tvCheckDate;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.tv_tel)
    TextView tvTel;
    @BindView(R.id.tv_plan_date)
    TextView tvPlanDate;
    @BindView(R.id.et_last_date)
    EditText etLastDate;
    @BindView(R.id.et_last_name)
    EditText etLastName;
    @BindView(R.id.tv_last_tel)
    TextView tvLastTel;
    @BindView(R.id.tv_prior)
    TextView tvPrior;
    @BindView(R.id.task_dec)
    TextView taskDec;
    @BindView(R.id.et_instructions)
    EditText etInstructions;
    @BindView(R.id.et_Rectification)
    EditText etRectification;
    @BindView(R.id.cb_qualified)
    CheckBox cbQualified;
    @BindView(R.id.latitude)
    TextView latitude;
    @BindView(R.id.longitude)
    TextView longitude;
    @BindView(R.id.tv_record_add)
    TextView tvRecordAdd;
    @BindView(R.id.tv_record_name)
    TextView tvRecordName;
    @BindView(R.id.tv_record_play)
    PlayView tvRecordPlay;
    @BindView(R.id.tv_video_add)
    TextView tvVideoAdd;
    @BindView(R.id.tv_video_name)
    TextView tvVideoName;
    @BindView(R.id.rl_video)
    RelativeLayout rlVideo;
    @BindView(R.id.iv_thumbnail)
    ImageView ivThumbnail;
    @BindView(R.id.ib_add_image)
    ImageButton ibAddImage;
    @BindView(R.id.rv_photos)
    RecyclerView rvPhotos;
    @BindView(R.id.ib_add_infrared)
    ImageButton ibAddInfrared;
    @BindView(R.id.rv_infrared_photos)
    RecyclerView rvInfraredPhotos;
    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.sv_task_detail)
    ScrollView sv_task_detail;
    @BindView(R.id.tv_noInfrared)
    TextView tv_noInfrared;
    @BindView(R.id.tv_noPic)
    TextView tv_noPic;
    @BindView(R.id.emptyLayout)
    EmptyLayout emptyLayout;

    @Autowired
    public String strType;
    @Autowired
    public int orderId;
    @Autowired
    public int type;
    @Autowired
    public int pid;

    @Inject
    PhotoAdapter normalAdapter;
    @Inject
    PhotoAdapter infraredAdapter;


    private WeakReference<String> voice_net_name,
            video_name, videostatusMessage, path;
    private String video_path;

    //    private String voice_net_name = "";
//    private String voice_path = "";//当前录音；
    private String voice_net = "";//网络录音；
    //    private String video_path;
//    private String video_name;
//    private String videostatusMessage;
//    private String path;
    private List<OrderDetail.PhotoUrlBean> photos_net_maps;
    private ArrayList<String> mInfraredPathList = new ArrayList<>();
    private ArrayList<String> photos_path = new ArrayList<>();//当前显示的照片集合；
    private ArrayList<String> photos_net = new ArrayList<>();//网络照片集合；
    private static String TAG = "TaskActivity";
    private HashMap<String, String> temps = new HashMap<>();

    private float maxTemp, minTemp;
    private NiftyDialogBuilder niftyDialogBuilder;
    private View progressView;
    private NumberProgressBar numberProgressBar;
    private TextView tv_msg;
    private MyHandler handler = new MyHandler(TaskActivity.this);
    private DetectInfraredReceiver reciver;

    @SuppressLint("HandlerLeak")
    static class MyHandler extends Handler {

        private WeakReference<TaskActivity> weakReference;

        MyHandler(TaskActivity taskActivity) {
            this.weakReference = new WeakReference<>(taskActivity);

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
                weakReference.get().niftyDialogBuilder.dismiss();
            }

        }
    }

    @Override
    public int inflateContentView() {
        return R.layout.activity_task;
    }

    @Override
    protected void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    protected void initData() {
//        pdNameSpinner.setVisibility(View.GONE);
        initReceiver();
        tvPre.setText(getResources().getString(R.string.back));
        name.setText(strType);
        if (getResources().getString(R.string.his_order).equals(strType)) {
            name.setText("工单详情");
            btnSave.setVisibility(View.GONE);
            etLastDate.setEnabled(false);
//            etName.setEnabled(false);
//            etLastName.setEnabled(false);
            etInstructions.setEnabled(false);
            etRectification.setEnabled(false);
            cbQualified.setEnabled(false);
            tvRecordAdd.setVisibility(View.GONE);
            tvVideoAdd.setVisibility(View.GONE);
            ibAddImage.setVisibility(View.GONE);
            ibAddInfrared.setVisibility(View.GONE);
        }
        initRecyclerView();
        initUploadView();
        niftyDialogBuilder = NiftyDialogBuilder.getInstance(mContext);
        sv_task_detail.setVisibility(View.GONE);
        emptyLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
        if (pid > 0) {//来自扫码，获取工单列表
            assert mPresenter != null;
            mPresenter.getOrderList(pid + "", 1, emptyLayout);
        } else {
            getDetail(orderId);
        }

    }

    private void initUploadView() {
        progressView = LayoutInflater.from(mContext).inflate(R.layout.defect_upload_item, null);
        numberProgressBar = progressView.findViewById(R.id.np_progress);
        tv_msg = progressView.findViewById(R.id.tv_msg);
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(mContext);
        linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvPhotos.setLayoutManager(linearLayoutManager1);
        rvPhotos.setAdapter(normalAdapter);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(mContext);
        linearLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvInfraredPhotos.setLayoutManager(linearLayoutManager2);
        rvInfraredPhotos.setAdapter(infraredAdapter);

        normalAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PhotoPreview.builder()
                        .setPhotos(photos_path)
                        .setCurrentItem(position)
                        .setFrom(MyConstants.ADD_NORMAL_PHOTO)
                        .start((Activity) mContext);
            }
        });

        infraredAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PhotoPreview.builder()
                        .setPhotos(mInfraredPathList)
                        .setCurrentItem(position)
                        .setFrom(MyConstants.INFRARED_PHOTO)
                        .start((Activity) mContext);
            }
        });

    }

    @Override
    public void getDetail(int orderId) {
        assert mPresenter != null;
        mPresenter.getOrderDetail(orderId, emptyLayout);
    }

    @Override
    public void setOrderDetail(OrderDetail detail) {
        emptyLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
        sv_task_detail.setVisibility(View.VISIBLE);
        setOrderDetailInfo(detail);
    }

    private void setOrderDetailInfo(OrderDetail detail) {
        tvName.setText(detail.getOrderName());
        tvCheckDate.setText(getDate(detail.getCheckDate()));
        etName.setText(detail.getUserName());
        tvTel.setText(detail.getUserTel());
        etLastDate.setText(detail.getLastDate());
        etLastName.setText(detail.getLastName());
        tvLastTel.setText(detail.getLastTel());
        tvPlanDate.setText(getDate(detail.getPlanDate()));
        tvPrior.setText(getPrior(detail.getPriority()));
        taskDec.setText(detail.getOrderContent());

        cbQualified.setChecked("1".equals(detail.getIsQualified()) ? false : true);//1合格
        longitude.setText(String.format("经度 ：%s", detail.getLongtitude()));
        latitude.setText(String.format("纬度 ：%s", detail.getLatitude()));
        etInstructions.setText(detail.getCheckInfo());
        etRectification.setText(detail.getRectification());
        if (detail.getVoiceUrl() != null && detail.getVoiceUrl().size() > 0) {
            tvRecordName.setText(detail.getVoiceUrl().get(0).getFlieName());
            voice_net_name = new WeakReference<>(detail.getVoiceUrl().get(0).getFlieName());
            voice_net = getRealUrl(detail.getVoiceUrl().get(0).getFliePath());
//            voice_path = new WeakReference<>(getRealUrl(detail.getVoiceUrl().get(0).getFliePath()));
        }
        if (detail.getInfUrl() != null && detail.getInfUrl().size() > 0) {
            video_name = new WeakReference<>(detail.getInfUrl().get(0).getFlieName());
            video_path = getRealUrl(detail.getInfUrl().get(0).getFliePath());
            tvVideoName.setText(video_name.get());
        }
        if (detail.getPhotoUrl() != null) {
            photos_net_maps = detail.getPhotoUrl();
            for (OrderDetail.PhotoUrlBean url_bean : photos_net_maps) {
                if (url_bean.getFlieName().contains("Infrared")) {
                    mInfraredPathList.add(getRealUrl(url_bean.getFliePath()));
                } else {
                    photos_net.add(getRealUrl(url_bean.getFliePath()));
                    photos_path.add(getRealUrl(url_bean.getFliePath()));
                }
            }
        }
        if ("历史工单".equals(strType)) {
            if (photos_path.size() <= 0) {
                tv_noPic.setVisibility(View.VISIBLE);
                rvPhotos.setVisibility(View.GONE);
            } else {
                tv_noPic.setVisibility(View.GONE);
                rvPhotos.setVisibility(View.VISIBLE);
            }
            if (mInfraredPathList.size() <= 0) {
                tv_noInfrared.setVisibility(View.VISIBLE);
                rvInfraredPhotos.setVisibility(View.GONE);
            } else {
                tv_noInfrared.setVisibility(View.GONE);
                rvInfraredPhotos.setVisibility(View.VISIBLE);
            }
        }
        normalAdapter.setNewData(photos_path);
        infraredAdapter.setNewData(mInfraredPathList);
    }

    private String getDate(String planDate) {
        try {
            if (!TextUtils.isEmpty(planDate)) {
                planDate = planDate.substring(0, planDate.indexOf(" "));
                return planDate;
            }
            return "未知";
        } catch (Exception e) {
            e.printStackTrace();
            return "未知";
        }
    }

    private String getPrior(String priority) {
        switch (priority) {
            case "1":
                return getString(R.string.normal);
            case "2":
                return getString(R.string.high);
            case "3":
                return getString(R.string.highest);
            default:
                return getString(R.string.unknow);
        }
    }

    private String getRealUrl(String originUrl) {
        if (!TextUtils.isEmpty(originUrl)) {
            return originUrl.replace("~", SharedPrefUtil.getInstance(MyApplication.getInstance()).getString(MyConstants.BASE_URL, EadoUrl.BASE_URL_WEB));
        }
        return "";
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected boolean isNeedInject() {
        return true;
    }

    void initReceiver() {
        //调用红外app需要
        //注册广播
        reciver = new DetectInfraredReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MyConstants.INFRARED_ACTION);
        registerReceiver(reciver, filter);
    }

    @OnClick({R.id.tv_shouye, R.id.btn_save, R.id.tv_check_date, R.id.tv_record_add, R.id.tv_record_play,
            R.id.tv_video_add, R.id.iv_thumbnail, R.id.ib_add_image, R.id.ib_add_infrared, R.id.location_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_shouye:
                finish();
                break;
            case R.id.btn_save:
                showSubmitDialog();
                break;
            case R.id.tv_check_date:
                //时间选择器
                TimePickerView pvTime = new TimePickerBuilder(mContext, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        if (DateUtils.getNowDateShort(new Date()).getTime() > DateUtils.getNowDateShort(date).getTime()) {
                            ToastUtils.showShort("巡检日期必须比今天大！");
                            return;
                        }
                        tvCheckDate.setText(DateUtils.dateToStrLong(date));
                    }
                }).build();
                pvTime.show();
                break;
            case R.id.tv_record_add:
//                recordSound();
                RecorderAndShootUtil.getInstance(mContext).recordSound(tvRecordName);
                break;
            case R.id.tv_record_play:
//                playRecord();
                if (type == 1) {
                    RecorderAndShootUtil.getInstance(mContext).playRecord(voice_net, tvRecordPlay);
                } else {
                    RecorderAndShootUtil.getInstance(mContext).playRecord(tvRecordName.getText().toString(), tvRecordPlay);
                }
                break;
            case R.id.tv_video_add:
                if (tvRecordPlay.isPlaying()) {
                    tvRecordPlay.stopPlay();
                    RecorderAndShootUtil.getInstance(mContext).getmRecordService().stopPlaying();
                }
                RecorderAndShootUtil.getInstance(mContext).gotoCaptureActivity();
//                gotoCaptureActivity();
                break;
            case R.id.iv_thumbnail:
//                if (tvRecordPlay.isPlaying()) {
//                    tvRecordPlay.stopPlay();
//                    mRecordService.stopPlaying();
//                }
//                playVideo();
                if (tvRecordPlay.isPlaying()) {
                    tvRecordPlay.stopPlay();
                    RecorderAndShootUtil.getInstance(mContext).getmRecordService().stopPlaying();
                }
                RecorderAndShootUtil.getInstance(mContext).playVideo(video_path);
                break;
            case R.id.ib_add_image:
                AddPhotoUtil.getInstance(mContext).addPic(photos_path);
//                addPic();
                break;
            case R.id.ib_add_infrared:
                if (mInfraredPathList.size() >= 5) {
                    ToastUtils.showShort("您最多能添加5张红外照片！");
                } else {
                    AddPhotoUtil.getInstance(mContext).goToInfredApp();
                }
                break;
            case R.id.location_layout:
                MapDialogUtil.getInstance(mContext).setMapClickListener(new MapDialogUtil.OnMapClickListener() {
                    @SuppressLint("DefaultLocale")
                    @Override
                    public void onMapClick(double mLatitude, double longtitude) {
                        latitude.setText(String.format("纬度 ：%.4f", mLatitude));
                        longitude.setText(String.format("经度 ：%.4f", longtitude));
                    }
                });
                MapDialogUtil.getInstance(mContext).showMapDialog();
                break;
            default:
                break;
        }
    }

    private void showSubmitDialog() {
        tv_msg.setText("是否提交？");
        niftyDialogBuilder
                .withTitle("任务提交")                               //标题.withTitle(null)  no title
                .withTitleColor("#FFFFFF")                               //def  标题颜色
                .withDividerColor("#11000000")                           //def
                .withMessage(null)                 //.withMessage(null)  no Msg   内容
                .withMessageColor("#FFCFCFCF")                          //def  | withMessageColor(int resid)   内容颜色
                .withDialogColor("#AF444D50")                            //def  | withDialogColor(int resid)   dialog框颜色
                .withIcon(mContext.getResources().getDrawable(R.drawable.ic_logo2))  //标题栏图标
                .withDuration(700)                                      //def      动画持续时间
                .withEffect(Effectstype.SlideBottom)                                     //def Effectstype.Slidetop  动画模式
                .withButton1Text("否")                                  //def gone     按钮文字
                .withButton2Text("是")                              //def gone
                .isCancelableOnTouchOutside(true)                       //def    | isCancelable(true)是否支持点击dialog框外关闭dialog
                .setCustomView(progressView, mContext)     //.setCustomView(View or ResId,context)    自定义布局
                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        niftyDialogBuilder.dismiss();

                    }
                })
                .setButton2Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        submit();
                    }
                })
                .show();    //展示
    }

    private void submit() {//提交参数；
        //IsQualified 是否合格（1是/0否）,CheckInfo 检查情况 ，Latitude 纬度 ，Longtitude 经度
        if (OkhttpUtils.getInstance().compareMax(photos_path, RecorderAndShootUtil.getInstance(mContext).getVoice_path(), mInfraredPathList)) {
            return;
        }
        tv_msg.setText("正在提交...");
        numberProgressBar.setVisibility(View.VISIBLE);
        mPresenter.saveOrderInfo(orderId + "", (cbQualified.isChecked() ? 0 : 1) + "", etInstructions.getText().toString(),
                getLtdOrLtd(latitude.getText().toString()), getLtdOrLtd(longitude.getText().toString()), etRectification.getText().toString());

    }

    public void hideDialog() {
        if (niftyDialogBuilder != null) {
            niftyDialogBuilder.dismiss();
        }
    }

    private float getLtdOrLtd(String aFloat) {
        float result = 0;
        try {
            result = Float.valueOf(aFloat.substring(4, aFloat.length()));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (AddPhotoUtil.getInstance(mContext).fromInfred && path != null) {
            AddPhotoUtil.getInstance(mContext).getInfrared(path.get(), temps, minTemp, maxTemp, mInfraredPathList);
            infraredAdapter.setNewData(mInfraredPathList);
            path = null;
            minTemp = maxTemp = 0;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {   //录制视频
            if (resultCode == Activity.RESULT_OK) {
                video_path = data.getStringExtra(VideoCaptureActivity.EXTRA_OUTPUT_FILENAME);
                videostatusMessage = new WeakReference<>(String.format("录制完成: %s", video_path));
                rlVideo.setVisibility(View.VISIBLE);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                videostatusMessage = new WeakReference<>(getString(R.string.status_capturecancelled));
            } else if (resultCode == VideoCaptureActivity.RESULT_ERROR) {
                video_path = null;
                videostatusMessage = new WeakReference<>(getString(R.string.status_capturefailed));
            }
            RecorderAndShootUtil.getInstance(mContext).updateStatusAndThumbnail(tvVideoName, ivThumbnail, videostatusMessage.get(), video_path);
        }

        if (data != null && resultCode == RESULT_OK && (!getResources().getString(R.string.his_order).equals(strType))) {
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
                            photos_path.clear();
                        }
                        if (selectPhoto != null && selectPhoto.size() > 0) {
                            photos_path.addAll(selectPhoto);
                        }
                        if (takePhoto != null && takePhoto.size() > 0) {
                            photos_path.addAll(takePhoto);
                        }
                        normalAdapter.setNewData(photos_path);
                    } else if (from != null && from.equals(MyConstants.INFRARED_PHOTO)) {  //添加红外图片
                        ArrayList<String> infraredImg = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                        if (tag == 0) {
                            mInfraredPathList.clear();
                        }
                        if (infraredImg != null && infraredImg.size() > 0) {
                            mInfraredPathList.addAll(infraredImg);
                        }
                        infraredAdapter.setNewData(mInfraredPathList);
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
        path = new WeakReference<>(obj.getInfraredPath());
        maxTemp = obj.getMaxTemp();
        minTemp = obj.getMinTemp();
    }

    /**
     * 上传附件；
     * files 文件路径,
     * Modules 所属模块（order/bug）,
     * fk_id 所属模块对象的编号,
     * FSource 来源（app/web），
     * FileType 附件类型（image/voice/infrared）,
     * Remark 备注,
     * CommitUser 上传用户
     */

    public void upFile() {
        if (voice_net != null) {
            if (TextUtils.isEmpty(voice_net)) {
                Log.e(TAG, "无网络声音，直接上传");
            } else if (!voice_net.equals(RecorderAndShootUtil.getInstance(mContext).getVoice_path())) {//
                Log.e(TAG, "有网络声音，切当前不为,先删除后传");
                deleteNetFile(voice_net_name.get(), "voice");
            }
        }

        deleteNetFiles(getDeletePhotos(photos_path, photos_net), "image");//删除网络照片
        deleteNetFiles(getDeletePhotos(photos_path, photos_net), "image");//删除红外照片
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
        OkhttpUtils.getInstance().upLoadFile(photos_path, RecorderAndShootUtil.getInstance(mContext).getVoice_path(), video_path, mInfraredPathList, (Activity) mContext, orderId + "", niftyDialogBuilder, "order", temps);

//        upLoadFile(getUpLoadPhotos(photos_path, photos_net), voice_path, getUpLoadPhotos(photos_infrared_path, photos_infrared_net));
    }

    /**
     * 请求接口删除服务器文件
     *
     * @param fileNames
     */
    private void deleteNetFiles(ArrayList<String> fileNames, String ctype) {
        if (fileNames == null || fileNames.size() <= 0) {
            return;
        }
        for (String fileName : fileNames) {
            try {
                deleteNetFile(fileName, ctype);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // TODO: 2017/2/8 删除的为filename不为http://113.106.90.51:66/UploadFiles/order/636221561385499703.jpg...magazine-unlock-04-2.3.508-bigpicture_04_30.jpg
    private void deleteNetFile(String filePath, String ctype) {
        if (TextUtils.isEmpty(filePath)) {
            return;
        }
        for (OrderDetail.PhotoUrlBean urlBean : photos_net_maps) {
            if (filePath.contains(urlBean.getFliePath().replace("~", ""))) {
                postDelete(urlBean.getFlieName(), ctype);
            }
        }
    }

    private void postDelete(String fileName, String ctype) {
        assert mPresenter != null;
        mPresenter.postDelete(fileName, ctype);
    }

    /**
     * @param photos_path 当前显示的照片集合
     * @param photos_net  网络的照片集合
     * @return 返回删除的照片路径；
     */
    private ArrayList<String> getDeletePhotos(ArrayList<String> photos_path, ArrayList<String> photos_net) {
        Collection deleteFiles = new ArrayList<String>(photos_net);
        Collection notexists = new ArrayList<String>(photos_net);
        deleteFiles.removeAll(photos_path);
        notexists.removeAll(deleteFiles);
        ArrayList list = (ArrayList) deleteFiles;
        return list;
    }

    @Override
    protected void onDestroy() {
        AddPhotoUtil.getInstance(mContext).reset();
        RecorderAndShootUtil.getInstance(mContext).reset();
        MapDialogUtil.getInstance(mContext).setMapClickListener(null);
        MapDialogUtil.getInstance(mContext).reset();
        OkhttpUtils.getInstance().reset();
        if (photos_net_maps != null) {
            photos_net_maps.clear();
            photos_net_maps = null;
        }
        if (mInfraredPathList != null) {
            mInfraredPathList.clear();
            mInfraredPathList = null;
        }
        if (photos_path != null) {
            photos_path.clear();
            photos_path = null;
        }
        if (photos_net != null) {
            photos_net.clear();
            photos_net = null;
        }
        if (temps != null) {
            temps.clear();
            temps = null;
        }
        handler.removeCallbacksAndMessages(null);
        handler = null;
        if (reciver != null) {
            unregisterReceiver(reciver);
            reciver = null;
        }
        super.onDestroy();
    }
}
