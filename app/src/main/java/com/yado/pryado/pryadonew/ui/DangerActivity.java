package com.yado.pryado.pryadonew.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.yado.pryado.pryadonew.MyConstants;
import com.yado.pryado.pryadonew.R;
import com.yado.pryado.pryadonew.base.BaseActivity;
import com.yado.pryado.pryadonew.util.BitmapUtil;

import org.angmarch.views.NiceSpinner;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = "/ui/DangerActivity")
public class DangerActivity extends BaseActivity {

    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.ll_assess)
    LinearLayout llAssess;


    @Override
    public int inflateContentView() {
        return R.layout.activity_danger;
    }

    @Override
    protected void initData() {
        name.setText(getResources().getString(R.string.risk_check));
    }

    @Override
    protected boolean isRegisterEventBus() {
        return false;
    }

    @Override
    protected boolean isNeedInject() {
        return false;
    }

    @OnClick({R.id.tv_shouye, R.id.ll_assess, R.id.ll_report})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_shouye:
                finish();
                break;
            case R.id.ll_assess:
                ARouter.getInstance().build(MyConstants.ASSESS).navigation();
                break;
            case R.id.ll_report:
                ARouter.getInstance().build(MyConstants.REPORT).navigation();
                break;
            default:
                break;
        }
    }


}
