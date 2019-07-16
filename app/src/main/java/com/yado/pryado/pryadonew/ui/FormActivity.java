package com.yado.pryado.pryadonew.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.yado.pryado.pryadonew.R;
import com.yado.pryado.pryadonew.base.BaseActivity;

import org.angmarch.views.NiceSpinner;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = "/ui/FormActivity")
public class FormActivity extends BaseActivity {

    @BindView(R.id.name)
    TextView name;

    @Override
    public int inflateContentView() {
        return R.layout.activity_form;
    }

    @Override
    protected void initData() {
        name.setText(R.string.report_monitor);
    }

    @Override
    protected boolean isRegisterEventBus() {
        return false;
    }

    @Override
    protected boolean isNeedInject() {
        return false;
    }

    @OnClick(R.id.tv_shouye)
    public void onViewClicked() {
        finish();
    }

}
