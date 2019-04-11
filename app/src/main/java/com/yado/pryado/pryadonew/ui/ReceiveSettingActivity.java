package com.yado.pryado.pryadonew.ui;

import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ToastUtils;
import com.suke.widget.SwitchButton;
import com.yado.pryado.pryadonew.MyApplication;
import com.yado.pryado.pryadonew.MyConstants;
import com.yado.pryado.pryadonew.R;
import com.yado.pryado.pryadonew.base.BaseActivity;
import com.yado.pryado.pryadonew.bean.VoiceChange;
import com.yado.pryado.pryadonew.util.SharedPrefUtil;
import com.yado.pryado.pryadonew.util.Util;

import org.angmarch.views.NiceSpinner;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = "/ui/ReceiveSettingActivity")
public class ReceiveSettingActivity extends BaseActivity implements SwitchButton.OnCheckedChangeListener {

    @BindView(R.id.tv_shouye)
    LinearLayout tvShouye;
    @BindView(R.id.tb_alert)
    SwitchButton tbAlert;
    @BindView(R.id.tb_alert_detail)
    SwitchButton tbAlertDetail;
    @BindView(R.id.tb_notice)
    SwitchButton tbNotice;
    @BindView(R.id.tb_notice_detail)
    SwitchButton tbNoticeDetail;
    @BindView(R.id.tv_warn)
    TextView tvWarn;
    @BindView(R.id.tv_notice)
    TextView tvNotice;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.tv_pre)
    TextView tvPre;
//    @BindView(R.id.pd_name_spinner)
//    NiceSpinner pdNameSpinner;

    private ArrayList<String> ringList = new ArrayList<>();
    private RingtoneManager rm;
    private Cursor cursor;

    @Override
    public int inflateContentView() {
        return R.layout.activity_receive_setting;
    }

    @Override
    protected void initData() {
//        pdNameSpinner.setVisibility(View.GONE);
        name.setText(getResources().getString(R.string.receive_setting));
        tbAlert.setChecked(SharedPrefUtil.getInstance(MyApplication.getInstance()).getT(MyConstants.TB_ALERT, true));
        tbAlertDetail.setChecked(SharedPrefUtil.getInstance(MyApplication.getInstance()).getT(MyConstants.ALERT_DETAIL, true));
        tbNotice.setChecked(SharedPrefUtil.getInstance(MyApplication.getInstance()).getT(MyConstants.TB_NOTICE, true));
        tbNoticeDetail.setChecked(SharedPrefUtil.getInstance(MyApplication.getInstance()).getT(MyConstants.TB_NOTICE_DETAIL, true));
        getRing();
        tvWarn.setText(ringList.get(SharedPrefUtil.getInstance(MyApplication.getInstance()).getInt(MyConstants.RING, 0)));
        tvNotice.setText(ringList.get(SharedPrefUtil.getInstance(MyApplication.getInstance()).getInt(MyConstants.RING2, 0)));
        tbAlert.setOnCheckedChangeListener(this);
        tbAlertDetail.setOnCheckedChangeListener(this);
        tbNotice.setOnCheckedChangeListener(this);
        tbNoticeDetail.setOnCheckedChangeListener(this);
    }

    /**
     * 获取铃声列表
     *
     * @return
     */
    public ArrayList<String> getRing() {
        /* 添加“跟随系统”选项 */
        ringList.add(getResources().getString(R.string.follow_system));
        /* 获取RingtoneManager */
        rm = new RingtoneManager(this);
        /* 指定获取类型为短信铃声 */
        rm.setType(RingtoneManager.TYPE_NOTIFICATION);
        /* 创建游标 */
        cursor = rm.getCursor();
        /* 游标移动到第一位，如果有下一项，则添加到ringlist中 */
        if (cursor.moveToFirst()) {
            do { // 游标获取RingtoneManager的列inde x
                ringList.add(cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX));
            } while (cursor.moveToNext());
        }
        return ringList;
    }

    @OnClick({R.id.tv_shouye, R.id.layout_warn, R.id.layout_notice, R.id.ll_reset})
    public void onClick(View view) {
        if (!Util.isDoubleClick()) {
            switch (view.getId()) {

                case R.id.tv_shouye:
                    finish();
                    break;
                case R.id.layout_warn:
//                Intent intent = new Intent(this, ChoiceVoiceActivity.class);
//                intent.putExtra("title", "报警提示音");
//                startActivity(intent);

                    // 2. 跳转并携带参数
                    ARouter.getInstance().build(MyConstants.CHOICE_VOICE)
                            .withString("title", getResources().getString(R.string.voice_notice))
                            .navigation();
                    break;
                case R.id.layout_notice:
//                Intent intent2 = new Intent(this, ChoiceVoiceActivity.class);
//                intent2.putExtra("title", "新消息提示音");
//                startActivity(intent2);
                    // 2. 跳转并携带参数
                    ARouter.getInstance().build(MyConstants.CHOICE_VOICE)
                            .withString("title", getResources().getString(R.string.new_notice))
                            .navigation();
                    break;

                case R.id.ll_reset:
                    ToastUtils.showShort(R.string.restoring_setting);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            SharedPrefUtil.getInstance(MyApplication.getInstance()).saveObject(MyConstants.TB_ALERT, true);
                            tbAlert.setChecked(true);
                            SharedPrefUtil.getInstance(MyApplication.getInstance()).saveObject(MyConstants.ALERT_DETAIL, true);
                            tbAlertDetail.setChecked(true);
                            SharedPrefUtil.getInstance(MyApplication.getInstance()).saveObject(MyConstants.TB_NOTICE, true);
                            tbNotice.setChecked(true);
                            SharedPrefUtil.getInstance(MyApplication.getInstance()).saveObject(MyConstants.TB_NOTICE_DETAIL, true);
                            tbNoticeDetail.setChecked(true);

                            SharedPrefUtil.getInstance(MyApplication.getInstance()).saveObject(MyConstants.RING, 0);
                            SharedPrefUtil.getInstance(MyApplication.getInstance()).saveObject(MyConstants.RING2, 0);
                            tvWarn.setText(ringList.get(0));
                            tvNotice.setText(ringList.get(0));
                            ToastUtils.showShort(getResources().getString(R.string.restored));
                        }
                    }, 2000);

                    break;
                default:
                    break;
            }
        }

    }

    @Override
    public void onCheckedChanged(SwitchButton view, boolean isChecked) {
        switch (view.getId()) {
            case R.id.tb_alert:
                SharedPrefUtil.getInstance(MyApplication.getInstance()).saveObject(MyConstants.TB_ALERT, tbAlert.isChecked());
                break;
            case R.id.tb_alert_detail:
                SharedPrefUtil.getInstance(MyApplication.getInstance()).saveObject(MyConstants.ALERT_DETAIL, tbAlertDetail.isChecked());
                break;
            case R.id.tb_notice:
                SharedPrefUtil.getInstance(MyApplication.getInstance()).saveObject(MyConstants.TB_NOTICE, tbNotice.isChecked());
                break;
            case R.id.tb_notice_detail:
                SharedPrefUtil.getInstance(MyApplication.getInstance()).saveObject(MyConstants.TB_NOTICE_DETAIL, tbNoticeDetail.isChecked());
                break;
            default:
                break;
        }
    }

    @Subscribe
    public void onMessageEvent(VoiceChange change) {
        if (change.getVoiceType().equals(MyConstants.RING)) {
            tvWarn.setText(ringList.get(change.getNamepos()));
        } else {
            tvNotice.setText(ringList.get(change.getNamepos()));
        }
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected boolean isNeedInject() {
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ringList != null && ringList.size() > 0) {
            ringList.clear();
            ringList = null;
        }
        if (cursor != null) {
            cursor.close();
            cursor = null;
        }
    }


}
