package com.yado.pryado.pryadonew.ui.report;

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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.jmolsmobile.landscapevideocapture.VideoCaptureActivity;
import com.yado.pryado.pryadonew.MyConstants;
import com.yado.pryado.pryadonew.R;
import com.yado.pryado.pryadonew.base.BaseActivity;
import com.yado.pryado.pryadonew.bean.RoomListBean;
import com.yado.pryado.pryadonew.net.OkhttpUtils;
import com.yado.pryado.pryadonew.receiver.DetectInfraredReceiver;
import com.yado.pryado.pryadonew.ui.adapter.PhotoAdapter;
import com.yado.pryado.pryadonew.ui.widgit.MyClearEditText;
import com.yado.pryado.pryadonew.ui.widgit.PlayView;
import com.yado.pryado.pryadonew.util.AddPhotoUtil;
import com.yado.pryado.pryadonew.util.RecorderAndShootUtil;

import org.angmarch.views.NiceSpinner;
import org.greenrobot.eventbus.Subscribe;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;

@Route(path = "/ui/report/ReportActivity")
public class ReportActivity extends BaseActivity<ReportPresent> implements ReportContract.View {

    private static final String TAG = "ReportActivity";
    @BindView(R.id.tv_pre)
    TextView tvPre;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.rooms_spinner)
    NiceSpinner roomsSpinner;
    @BindView(R.id.note)
    MyClearEditText note;
    @BindView(R.id.bug_Location)
    MyClearEditText bugLocation;
    @BindView(R.id.tv_record_play)
    PlayView tvRecordPlay;
    @BindView(R.id.iv_thumbnail)
    ImageView ivThumbnail;
    @BindView(R.id.rv_photos)
    RecyclerView rvPhotos;
    @BindView(R.id.rv_infrared_photos)
    RecyclerView rvInfraredPhotos;
    @BindView(R.id.tv_record_name)
    TextView tvRecordName;
    @BindView(R.id.tv_video_name)
    TextView tvVideoName;
    @BindView(R.id.ib_add_image)
    ImageButton ibAddImage;
    @BindView(R.id.tv_record_add)
    TextView tvRecordAdd;
    @BindView(R.id.tv_noPic)
    TextView tvNoPic;
    @BindView(R.id.linearLayout_image)
    LinearLayout linearLayoutImage;
    @BindView(R.id.tv_noInfrared)
    TextView tvNoInfrared;
    @BindView(R.id.ib_add_infrared)
    ImageButton ibAddInfrared;
    @BindView(R.id.linearLayout_infrared)
    LinearLayout linearLayoutInfrared;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.sv)
    ScrollView sv;

    private WeakReference<String[]> mItems; //站室名称数组
//    private String[] mItems;    //站室名称数组

    private ArrayList<RoomListBean.RowsEntity> rooms;
    private int selectPos;
    private int pid;

    private WeakReference<String> videostatusMessage, path;
    private String videofilename;
//    private String videostatusMessage;

    private List<String> mPicList = new ArrayList<>();
    private List<String> mInfraredList = new ArrayList<>();

    @Inject
    PhotoAdapter normalAdapter;

    @Inject
    PhotoAdapter infraredAdapter;

    /**
     * 调用红外相关
     */
    private HashMap<String, String> temps = new HashMap<>();
    //    private String path;
    private float maxTemp, minTemp;
    private NiftyDialogBuilder dialogBuilder;

    private View progressView;
    private NumberProgressBar numberProgressBar;
    private TextView tv_msg;

    private MyHandler handler = new MyHandler(ReportActivity.this);

    public void setNetError() {
        dialogBuilder.dismiss();
    }

    private static class MyHandler extends Handler {

        WeakReference<ReportActivity> weakReference;

        MyHandler(ReportActivity reportActivity) {
            this.weakReference = new WeakReference<>(reportActivity);

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
    public int inflateContentView() {
        return R.layout.activity_report;
    }

    @Override
    protected void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    protected void initData() {
//        pdNameSpinner.setVisibility(View.GONE);
        initReceiver();
        tvPre.setText("返回");
        boolean fromMain = getIntent().getBooleanExtra("FROM_MAIN", false);
        if (fromMain) {
            name.setText(getString(R.string.catchstr));
        } else {
            name.setText("隐患上报");
        }
        initRecyclerView();
        initLinstener();
        assert mPresenter != null;
        mPresenter.getRoomList();
        initUploadView();
    }

    private void initUploadView() {
        progressView = LayoutInflater.from(mContext).inflate(R.layout.defect_upload_item, null);
        numberProgressBar = progressView.findViewById(R.id.np_progress);
        tv_msg = progressView.findViewById(R.id.tv_msg);
    }

    private void initReceiver() {
        //调用红外app需要
        //注册广播
        reciver = new DetectInfraredReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MyConstants.INFRARED_ACTION);
        registerReceiver(reciver, filter);
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

    }

    private void initLinstener() {
        roomsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                selectPos = position;
                try {
                    pid = rooms.get(selectPos - 1).getPID();
                } catch (Exception e) {
                    e.printStackTrace();
                    pid = -1;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        normalAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PhotoPreview.builder()
                        .setPhotos((ArrayList<String>) mPicList)
                        .setCurrentItem(position)
                        .setFrom(MyConstants.ADD_NORMAL_PHOTO)
                        .start((Activity) mContext);
            }
        });

        infraredAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PhotoPreview.builder()
                        .setPhotos((ArrayList<String>) mInfraredList)
                        .setCurrentItem(position)
                        .setFrom(MyConstants.INFRARED_PHOTO)
                        .start((Activity) mContext);
            }
        });
    }


    public void setRoomlist(RoomListBean roomListBean) {
        rooms = roomListBean.getRows();
        mItems = new WeakReference<>(new String[rooms.size() + 1]);
        mItems.get()[0] = "选择配电房";
        for (int i = 1; i < rooms.size() + 1; i++) {
            mItems.get()[i] = rooms.get(i - 1).getName();
        }
        List<String> dataset = new LinkedList<>(Arrays.asList(mItems.get()));
        roomsSpinner.attachDataSource(dataset);
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected boolean isNeedInject() {
        return false;
    }

    @OnClick({R.id.tv_shouye, R.id.tv_record_add, R.id.tv_record_name, R.id.tv_video_add, R.id.tv_video_name, R.id.iv_thumbnail,
            R.id.ib_add_image, R.id.ib_add_infrared, R.id.btn_submit, R.id.tv_record_play})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_shouye:
                finish();
                break;
            case R.id.tv_record_add:
                RecorderAndShootUtil.getInstance(mContext).recordSound(tvRecordName);
                break;
            case R.id.tv_record_play:
                RecorderAndShootUtil.getInstance(mContext).playRecord(tvRecordName.getText().toString(), tvRecordPlay);
                break;
            case R.id.tv_video_add:
                if (tvRecordPlay.isPlaying()) {
                    tvRecordPlay.stopPlay();
                    RecorderAndShootUtil.getInstance(mContext).getmRecordService().stopPlaying();
                }
                RecorderAndShootUtil.getInstance(mContext).gotoCaptureActivity();
                break;
            case R.id.iv_thumbnail:
                if (tvRecordPlay.isPlaying()) {
                    tvRecordPlay.stopPlay();
                    RecorderAndShootUtil.getInstance(mContext).getmRecordService().stopPlaying();
                }
                RecorderAndShootUtil.getInstance(mContext).playVideo(videofilename);
                break;
            case R.id.ib_add_image:
                AddPhotoUtil.getInstance(mContext).addPic((ArrayList<String>) mPicList);
                break;
            case R.id.ib_add_infrared:
                if (mInfraredList.size() >= 5) {
                    ToastUtils.showShort("您最多能添加5张红外照片！");
                } else {
                    AddPhotoUtil.getInstance(mContext).goToInfredApp();
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
            dialogBuilder = NiftyDialogBuilder.getInstance(mContext);
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
                .withIcon(mContext.getResources().getDrawable(R.drawable.ic_logo2))  //标题栏图标
                .withDuration(700)                                      //def      动画持续时间
                .withEffect(Effectstype.SlideBottom)                                     //def Effectstype.Slidetop  动画模式
                .withButton1Text("取消")                                  //def gone     按钮文字
                .withButton2Text("确定")                              //def gone
                .isCancelableOnTouchOutside(true)                       //def    | isCancelable(true)是否支持点击dialog框外关闭dialog
                .setCustomView(progressView, mContext)     //.setCustomView(View or ResId,context)    自定义布局
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
                        tv_msg.setText(getResources().getString(R.string.upload_file));
                        numberProgressBar.setVisibility(View.VISIBLE);
                    }
                })
                .show();    //展示
        tv_msg.setText("是否上传隐患？");
    }

    private void postNewBug() {
        if (pid <= 0) {
            ToastUtils.showShort("配电房不存在");
            if(dialogBuilder != null && dialogBuilder.isShowing()) {
                dialogBuilder.dismiss();
            }
            return;
        }
        if (TextUtils.isEmpty(bugLocation.getText().toString())) {
            bugLocation.setShakeAnimation();
            ToastUtils.showShort("请输入缺陷部位");
            if(dialogBuilder != null && dialogBuilder.isShowing()) {
                dialogBuilder.dismiss();
            }
            return;
        }
        //IsQualified 是否合格（1是/0否）,CheckInfo 检查情况 ，Latitude 纬度 ，Longtitude 经度
        if (OkhttpUtils.getInstance().compareMax((ArrayList<String>) mPicList, RecorderAndShootUtil.getInstance(mContext).getVoice_path(), (ArrayList<String>) mInfraredList)) {
            return;
        }
        assert mPresenter != null;
        mPresenter.postNewBug(pid, bugLocation.getText().toString(), note.getText().toString());
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (AddPhotoUtil.getInstance(mContext).fromInfred && path != null) {
            AddPhotoUtil.getInstance(mContext).getInfrared(path.get(), temps, minTemp, maxTemp, mInfraredList);
            infraredAdapter.setNewData(mInfraredList);
            path = null;
            minTemp = maxTemp = 0;
        }

    }

    //android7.0以上用getInfrared()获取不到图片，这里使用广播接收并用eventbus发送到此处
    @Subscribe
    public void getInfraredImg(DetectInfraredReceiver.InfraredEvent obj) {
        path = new WeakReference<>(obj.getInfraredPath());
        maxTemp = obj.getMaxTemp();
        minTemp = obj.getMinTemp();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {   //录制视频
            if (resultCode == Activity.RESULT_OK) {
                videofilename = data.getStringExtra(VideoCaptureActivity.EXTRA_OUTPUT_FILENAME);
                videostatusMessage = new WeakReference<>(String.format("录制完成: %s", videofilename));
            } else if (resultCode == Activity.RESULT_CANCELED) {
                videostatusMessage = new WeakReference<>(getString(R.string.status_capturecancelled));
            } else if (resultCode == VideoCaptureActivity.RESULT_ERROR) {
                videofilename = null;
                videostatusMessage = new WeakReference<>(getString(R.string.status_capturefailed));
            }
            RecorderAndShootUtil.getInstance(mContext).updateStatusAndThumbnail(tvVideoName, ivThumbnail, videostatusMessage.get(), videofilename);
        }

        if (data != null && resultCode == RESULT_OK) {
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
                            mPicList.clear();
                        }
                        if (selectPhoto != null && selectPhoto.size() > 0) {
                            mPicList.addAll(selectPhoto);
                        }
                        if (takePhoto != null && takePhoto.size() > 0) {
                            mPicList.addAll(takePhoto);
                        }
                        normalAdapter.setNewData(mPicList);
                    } else if (from != null && from.equals(MyConstants.INFRARED_PHOTO)) {  //添加红外图片
                        ArrayList<String> infraredImg = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                        if (tag == 0) {
                            mInfraredList.clear();
                        }
                        if (infraredImg != null && infraredImg.size() > 0) {
                            mInfraredList.addAll(infraredImg);
                        }
                        infraredAdapter.setNewData(mInfraredList);
                    }
                    break;
                default:
                    break;
            }
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
        OkhttpUtils.getInstance().upLoadFile((ArrayList<String>) mPicList, RecorderAndShootUtil.getInstance(mContext).getVoice_path(), videofilename, (ArrayList<String>) mInfraredList, (Activity) mContext, bugId + "", dialogBuilder, "bug", temps);

    }

    @Override
    protected void onDestroy() {
        OkhttpUtils.getInstance().reset();
        if (reciver != null) {
            unregisterReceiver(reciver);
            reciver = null;
        }
        handler.removeCallbacksAndMessages(null);
        handler = null;
        AddPhotoUtil.getInstance(mContext).reset();
        RecorderAndShootUtil.getInstance(mContext).reset();
        if (rooms != null) {
            rooms.clear();
            rooms = null;
        }
        if (mPicList != null) {
            mPicList.clear();
            mPicList = null;
        }
        if (mInfraredList != null) {
            mInfraredList.clear();
            mInfraredList = null;
        }
        if (temps != null) {
            temps.clear();
            temps = null;
        }
        super.onDestroy();
    }
}
