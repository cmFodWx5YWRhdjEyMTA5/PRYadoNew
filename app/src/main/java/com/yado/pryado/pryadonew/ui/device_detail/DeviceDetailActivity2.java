package com.yado.pryado.pryadonew.ui.device_detail;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.ToastUtils;
import com.yado.pryado.pryadonew.R;
import com.yado.pryado.pryadonew.base.BaseActivity;
import com.yado.pryado.pryadonew.bean.DeviceDetailBean2;
import com.yado.pryado.pryadonew.ui.adapter.RealTimeDataAdapter;
import com.yado.pryado.pryadonew.ui.widgit.EmptyLayout;

import org.angmarch.views.NiceSpinner;

import java.lang.ref.WeakReference;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = "/ui/device_detail/DeviceDetailActivity2")
public class DeviceDetailActivity2 extends BaseActivity<DeviceDetailPresent> implements DeviceDetailContract.View {

    @BindView(R.id.tv_pre)
    TextView tvPre;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.emptyLayout)
    EmptyLayout emptyLayout;
    @BindView(R.id.rc_real_time_param)
    RecyclerView rcRealTimeParam;
    @BindView(R.id.ll_content)
    LinearLayout llContent;

    @Autowired(name = "did")
    public String did;
//    @BindView(R.id.pd_name_spinner)
//    NiceSpinner pdNameSpinner;
    private WeakReference<String> did1, did2;
    //    private String did1, did2;
    private GetRealTask getRealTask;

    @Inject
    RealTimeDataAdapter adapter;

    @Override
    public int inflateContentView() {
        return R.layout.activity_device_detail2;
    }

    @Override
    protected void initData() {
//        pdNameSpinner.setVisibility(View.GONE);
        name.setText("一多展示用柜");
        emptyLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
        if (!TextUtils.isEmpty(did) && did.contains(",")) {
            String[] dids = did.split(",");
            did1 = new WeakReference<>(dids[0]);
            did2 = new WeakReference<>(dids[1]);
//            did2 = dids[1];
        }
        initRecyclerView();
        getRealTask = new GetRealTask();
        getRealTask.start();
    }

    @Override
    protected boolean isRegisterEventBus() {
        return false;
    }

    @Override
    protected void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    protected boolean isNeedInject() {
        return true;
    }

    @OnClick(R.id.tv_shouye)
    public void onViewClicked() {
        finish();
    }

    public void setDataList(List<DeviceDetailBean2.RealTimeParamsBean> realTimeParamsBeans) {
        if (realTimeParamsBeans != null) {
            emptyLayout.setVisibility(View.GONE);
            llContent.setVisibility(View.VISIBLE);
            adapter.setNewData(realTimeParamsBeans);
        } else {
            emptyLayout.setErrorType(EmptyLayout.NODATA);
            ToastUtils.showShort("无数据");
        }
    }

    private void initRecyclerView() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        rcRealTimeParam.setLayoutManager(mLayoutManager);
        rcRealTimeParam.setHasFixedSize(true);
        rcRealTimeParam.setItemAnimator(new DefaultItemAnimator());
        rcRealTimeParam.setAdapter(adapter);
    }

    class GetRealTask extends Thread {
        volatile boolean stop = false;

        @Override
        public void run() {
            while (!stop) {
                getData();
                SystemClock.sleep(10 * 1000);
            }
        }
    }

    private void getData() {
        assert mPresenter != null;
        mPresenter.getDetail2(did1.get(), did2.get());
    }

    @Override
    protected void onDestroy() {
        if (getRealTask != null && getRealTask.isAlive()) {
            getRealTask.stop = true;
            getRealTask.interrupt();
        }
        super.onDestroy();
    }
}
