package com.yado.pryado.pryadonew.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.yado.pryado.pryadonew.R;
import com.yado.pryado.pryadonew.base.BaseActivity;
import com.yado.pryado.pryadonew.util.BitmapUtil;
import com.yado.pryado.pryadonew.util.Util;

import org.angmarch.views.NiceSpinner;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = "/ui/AboutActivity")
public class AboutActivity extends BaseActivity {

    @BindView(R.id.tv_pre)
    TextView tvPre;
    @BindView(R.id.tv_shouye)
    LinearLayout tvShouye;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.tv_version_name)
    TextView tvVersionName;
    @BindView(R.id.company_url)
    TextView companyUrl;
    @BindView(R.id.iv_logo)
    ImageView ivLogo;

    @Override
    public int inflateContentView() {
        return R.layout.activity_about;
    }

    @Override
    protected void initData() {
//        pdNameSpinner.setVisibility(View.GONE);
        name.setText(getResources().getString(R.string.about));
        tvVersionName.setText(Util.getVersionName(mContext));
    }

    @Override
    protected boolean isRegisterEventBus() {
        return false;
    }

    @Override
    protected boolean isNeedInject() {
        return false;
    }


    @OnClick({R.id.tv_shouye, R.id.company_url})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_shouye:
                finish();
                break;
            case R.id.company_url:
                break;
            default:
                break;
        }
    }

}
