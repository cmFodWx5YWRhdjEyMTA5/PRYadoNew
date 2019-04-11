package com.yado.pryado.pryadonew.ui.riskAssessAndDetail;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yado.pryado.pryadonew.MyApplication;
import com.yado.pryado.pryadonew.MyConstants;
import com.yado.pryado.pryadonew.R;
import com.yado.pryado.pryadonew.base.BaseActivity;
import com.yado.pryado.pryadonew.bean.BugInfo;
import com.yado.pryado.pryadonew.bean.BugList;
import com.yado.pryado.pryadonew.bean.RoomListBean;
import com.yado.pryado.pryadonew.net.EadoUrl;
import com.yado.pryado.pryadonew.service.RecordService;
import com.yado.pryado.pryadonew.ui.adapter.PhotoAdapter;
import com.yado.pryado.pryadonew.ui.widgit.PlayView;
import com.yado.pryado.pryadonew.util.CameraFileUtil;
import com.yado.pryado.pryadonew.util.RecorderAndShootUtil;
import com.yado.pryado.pryadonew.util.SharedPrefUtil;

import org.angmarch.views.NiceSpinner;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPreview;

@Route(path = "/ui/riskAssessAndDetail/DangerDetailActivity")
public class DangerDetailActivity extends BaseActivity<AssessPresent> implements AssessContract.View {

    @BindView(R.id.tv_pre)
    TextView tvPre;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.tv_device_name)
    TextView tvDeviceName;
    @BindView(R.id.tv_device_model)
    TextView tvDeviceModel;
    @BindView(R.id.tv_voltage_level)
    TextView tvVoltageLevel;
    @BindView(R.id.tv_install_location)
    TextView tvInstallLocation;
    @BindView(R.id.tv_report_time)
    TextView tvReportTime;
    @BindView(R.id.tv_report_type)
    TextView tvReportType;
    @BindView(R.id.tv_comfirm_time)
    TextView tvComfirmTime;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.tv_bug_desc)
    TextView tvBugDesc;
    @BindView(R.id.tv_check_time)
    TextView tvCheckTime;
    @BindView(R.id.tv_record_name)
    TextView tvRecordName;
    @BindView(R.id.tv_record_play)
    PlayView tvRecordPlay;
    @BindView(R.id.tv_video_name)
    TextView tvVideoName;
    @BindView(R.id.tv_video_play)
    ImageView tvVideoPlay;
    @BindView(R.id.rv_photos)
    RecyclerView rvPhotos;
    @BindView(R.id.rv_photos_infrared)
    RecyclerView rvPhotosInfrared;
    @BindView(R.id.tv_check_man)
    TextView tvCheckMan;
    @BindView(R.id.tv_Treatment)
    TextView tvTreatment;
    @BindView(R.id.icon_value1)
    ImageView iconValue1;
    @BindView(R.id.icon_value2)
    ImageView iconValue2;
    @BindView(R.id.icon_value3)
    ImageView iconValue3;
    @BindView(R.id.tl_device)
    TableLayout tlDevice;
    @BindView(R.id.tl_defect)
    TableLayout tlDefect;
    @BindView(R.id.tl_inspect)
    TableLayout tlInspect;
    @BindView(R.id.tv_value1)
    TextView tvValue1;
    @BindView(R.id.tv_value2)
    TextView tvValue2;
    @BindView(R.id.tv_value3)
    TextView tvValue3;
//    @BindView(R.id.pd_name_spinner)
//    NiceSpinner pdNameSpinner;
    @BindView(R.id.title_bar)
    RelativeLayout titleBar;
    @BindView(R.id.status_bar)
    View statusBar;

    private String videoUrl;
//    private String videoUrl;
    private List<BugInfo.PhotoUrlBean> photos_net_maps;
    private ArrayList<String> photos_infrared_net = new ArrayList<>();
    private ArrayList<String> photos_net = new ArrayList<>();

    private int open1, open2, open3;

    @Inject
    PhotoAdapter photoAdapter;
    @Inject
    PhotoAdapter infredAdapter;

    @Autowired(name = "bug")
    public BugList.ListmapBean bug;
    private String voice_net;


    @Override
    public int inflateContentView() {
        return R.layout.activity_danger_detail;
    }

    @Override
    protected void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    protected void initData() {
        titleBar.setBackgroundColor(getResources().getColor(R.color.transparent));
        statusBar.setBackgroundColor(getResources().getColor(R.color.transparent));
//        pdNameSpinner.setVisibility(View.GONE);
        name.setText("隐患详情");
        tvPre.setText("返回");
        initRecyclerView();
//        final BugList.ListmapBean bug = getIntent().getParcelableExtra("bug");
        assert mPresenter != null;
        mPresenter.getBugInfo(bug.getBugID());
//        mRecordService = new RecordService(mContext);
//
//        mRecordService.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//                tvRecordPlay.stopPlay();
//            }
//        });
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(mContext);
        linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvPhotos.setLayoutManager(linearLayoutManager1);
        rvPhotos.setAdapter(photoAdapter);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(mContext);
        linearLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvPhotosInfrared.setLayoutManager(linearLayoutManager2);
        rvPhotosInfrared.setAdapter(infredAdapter);

        photoAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PhotoPreview.builder()
                        .setPhotos(photos_net)
                        .setCurrentItem(position)
                        .setFrom(MyConstants.ADD_NORMAL_PHOTO)
                        .start((Activity) mContext);
            }
        });

        infredAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PhotoPreview.builder()
                        .setPhotos(photos_infrared_net)
                        .setCurrentItem(position)
                        .setFrom(MyConstants.INFRARED_PHOTO)
                        .start((Activity) mContext);
            }
        });
    }

    public void setBugInfo(BugInfo bugInfo) {
        tvVoltageLevel.setText(bugInfo.getVoltageLevel());
        tvDeviceModel.setText(bugInfo.getDeviceModel());
        tvInstallLocation.setText(bugInfo.getInstallAddr());
        tvDeviceName.setText(bugInfo.getDeviceName());
        tvLocation.setText(bugInfo.getBugLocation());
        tvBugDesc.setText(bugInfo.getBugDesc());
        tvReportType.setText(bugInfo.getReportWay());
        tvReportTime.setText(bugInfo.getReportDate());
        tvCheckMan.setText(bugInfo.getUserName());
        tvTreatment.setText(bugInfo.getHandeSituation());
        tvCheckTime.setText(bugInfo.getCheckDate());
        tvComfirmTime.setText(bugInfo.getHandleDate());
        if (bugInfo.getVoiceUrl() != null && bugInfo.getVoiceUrl().size() > 0) {
            tvRecordPlay.setVisibility(View.VISIBLE);
            tvRecordName.setText(bugInfo.getVoiceUrl().get(0).getFlieName());
            voice_net = getRealUrl(bugInfo.getVoiceUrl().get(0).getFliePath());

        }
        if (bugInfo.getInfUrl() != null && bugInfo.getInfUrl().size() > 0) {
            tvVideoPlay.setVisibility(View.VISIBLE);
            tvVideoName.setText(bugInfo.getInfUrl().get(0).getFlieName());
            videoUrl = getRealUrl(bugInfo.getInfUrl().get(0).getFliePath());
        }
//
        if (bugInfo.getPhotoUrl() != null) {
            photos_net_maps = bugInfo.getPhotoUrl();
            for (BugInfo.PhotoUrlBean url_bean : photos_net_maps) {
                if (url_bean.getFlieName().contains("Infrared")) {
                    photos_infrared_net.add(getRealUrl(url_bean.getFliePath()));
                } else {
                    photos_net.add(getRealUrl(url_bean.getFliePath()));
                }
            }
        }
        photoAdapter.setNewData(photos_net);
        infredAdapter.setNewData(photos_infrared_net);
//        setPhotoAdapter();
    }


    private String getRealUrl(String originUrl) {
        if (!TextUtils.isEmpty(originUrl)) {
            return originUrl.replace("~", SharedPrefUtil.getInstance(MyApplication.getInstance()).getString(MyConstants.BASE_URL, EadoUrl.BASE_URL_WEB));
        }
        return "";
    }


    @Override
    protected boolean isRegisterEventBus() {
        return false;
    }

    @Override
    protected boolean isNeedInject() {
        return true;
    }

    @Override
    protected void onDestroy() {
        RecorderAndShootUtil.getInstance(mContext).reset();
        if (photos_net_maps != null) {
            photos_net_maps.clear();
            photos_net_maps = null;
        }
        if (photos_infrared_net != null) {
            photos_infrared_net.clear();
            photos_infrared_net = null;
        }
        if (photos_net != null) {
            photos_net.clear();
            photos_net = null;
        }
        super.onDestroy();
    }

    @OnClick({R.id.tv_shouye, R.id.btn_back, R.id.ll_open_or_close_device, R.id.ll_open_or_close_defect,
            R.id.ll_open_or_close_inspect, R.id.tv_video_play, R.id.tv_record_play})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_shouye:
            case R.id.btn_back:
                finish();
                break;
            case R.id.ll_open_or_close_device:
                if (open1 % 2 == 0) {
                    iconValue1.setImageResource(R.drawable.ic_expand);
                    tlDevice.setVisibility(View.GONE);
                    tvValue1.setText("展开");
                } else {
                    iconValue1.setImageResource(R.drawable.ic_collapse);
                    tlDevice.setVisibility(View.VISIBLE);
                    tvValue1.setText("收起");
                }
                open1++;
                break;
            case R.id.ll_open_or_close_defect:
                if (open2 % 2 == 0) {
                    iconValue2.setImageResource(R.drawable.ic_expand);
                    tlDefect.setVisibility(View.GONE);
                    tvValue2.setText("展开");

                } else {
                    iconValue2.setImageResource(R.drawable.ic_collapse);
                    tlDefect.setVisibility(View.VISIBLE);
                    tvValue2.setText("收起");
                }
                open2++;
                break;
            case R.id.ll_open_or_close_inspect:
                if (open3 % 2 == 0) {
                    iconValue3.setImageResource(R.drawable.ic_expand);
                    tlInspect.setVisibility(View.GONE);
                    tvValue3.setText("展开");
                } else {
                    iconValue3.setImageResource(R.drawable.ic_collapse);
                    tlInspect.setVisibility(View.VISIBLE);
                    tvValue3.setText("收起");
                }
                open3++;
                break;
            case R.id.tv_video_play:
                if (tvRecordPlay.isPlaying()) {
                    tvRecordPlay.stopPlay();
                    RecorderAndShootUtil.getInstance(mContext).getmRecordService().stopPlaying();
                }
                RecorderAndShootUtil.getInstance(mContext).playVideo(videoUrl);
//                playVideo();
                break;
            case R.id.tv_record_play:
                RecorderAndShootUtil.getInstance(mContext).playRecord(voice_net, tvRecordPlay);
                break;
            default:
                break;
        }
    }



}
