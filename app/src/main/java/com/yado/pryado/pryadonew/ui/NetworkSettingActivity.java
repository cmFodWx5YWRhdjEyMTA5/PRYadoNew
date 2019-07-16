package com.yado.pryado.pryadonew.ui;

import android.os.Bundle;
import android.support.v7.widget.AppCompatRadioButton;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.yado.pryado.pryadonew.MyApplication;
import com.yado.pryado.pryadonew.MyConstants;
import com.yado.pryado.pryadonew.R;
import com.yado.pryado.pryadonew.base.BaseActivity;
import com.yado.pryado.pryadonew.util.SharedPrefUtil;

import org.angmarch.views.NiceSpinner;

import butterknife.BindView;
import butterknife.ButterKnife;

@Route(path = "/ui/NetworkSettingActivity")
public class NetworkSettingActivity extends BaseActivity {

    @BindView(R.id.tv_pre)
    TextView tvPre;
    @BindView(R.id.tv_shouye)
    LinearLayout tvShouye;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.radio_button_mobile_network)
    AppCompatRadioButton radioButtonMobileNetwork;
    @BindView(R.id.radio_button_wifi)
    AppCompatRadioButton radioButtonWifi;
    @BindView(R.id.radio_button_all_network)
    AppCompatRadioButton radioButtonAllNetwork;
    @BindView(R.id.radio_group_internet)
    RadioGroup radioGroupInternet;
    @BindView(R.id.btn_determine)
    Button btnDetermine;

    private int type;

    @Override
    public int inflateContentView() {
        return R.layout.activity_network_setting;
    }

    @Override
    protected void initData() {
        name.setText(getResources().getString(R.string.net_setting));
        SharedPrefUtil sharedPrefUtil = SharedPrefUtil.getInstance(MyApplication.getInstance());

        int checkID = sharedPrefUtil.getInt(MyConstants.NETWORK_SETTING, MyConstants.TYPE_ALL_NETWORK);
        radioGroupInternet.clearCheck();
        if (checkID == MyConstants.TYPE_MOBILE) {
            radioGroupInternet.check(R.id.radio_button_mobile_network);
        } else if (checkID == MyConstants.TYPE_WIFI) {
            radioGroupInternet.check(R.id.radio_button_wifi);
        } else {
            radioGroupInternet.check(R.id.radio_button_all_network);
        }

        initEvent();
    }

    private void initEvent() {

        tvShouye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnDetermine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //做一些保存check的操作，存到SharedPreference中
                SharedPrefUtil sharedPrefUtil = SharedPrefUtil.getInstance(MyApplication.getInstance());
                sharedPrefUtil.saveObject(MyConstants.NETWORK_SETTING, type);

                finish();
            }
        });

        radioGroupInternet.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_button_mobile_network:
                        type = MyConstants.TYPE_MOBILE;
                        break;

                    case R.id.radio_button_wifi:
                        type = MyConstants.TYPE_WIFI;
                        break;
                    case R.id.radio_button_all_network:
                        type = MyConstants.TYPE_ALL_NETWORK;
                        break;
                    default:
                        break;
                }

            }
        });

    }


    @Override
    protected boolean isRegisterEventBus() {
        return false;
    }

    @Override
    protected boolean isNeedInject() {
        return false;
    }

}
