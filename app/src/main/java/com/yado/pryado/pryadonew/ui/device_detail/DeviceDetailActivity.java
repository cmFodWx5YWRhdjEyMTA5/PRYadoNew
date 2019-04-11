package com.yado.pryado.pryadonew.ui.device_detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.yado.pryado.pryadonew.MyConstants;
import com.yado.pryado.pryadonew.R;
import com.yado.pryado.pryadonew.base.BaseActivity;
import com.yado.pryado.pryadonew.base.BaseFragment;
import com.yado.pryado.pryadonew.bean.DeviceDetailBean2;
import com.yado.pryado.pryadonew.ui.adapter.MyPagerAdapter;
import com.yado.pryado.pryadonew.ui.device_detail.account_info.AccountInfoFragment;
import com.yado.pryado.pryadonew.ui.device_detail.danger.DangerFragment;
import com.yado.pryado.pryadonew.ui.device_detail.real_time_data.RealtimeDataFragment;
import com.yado.pryado.pryadonew.ui.widgit.MyProgressDialog;

import org.angmarch.views.NiceSpinner;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

@Route(path = "/ui/device_detail/DeviceDetailActivity")
public class DeviceDetailActivity extends BaseActivity<DeviceDetailPresent> implements DeviceDetailContract.View {

    private static final String TAG = "DeviceDetailActivity";
    @BindView(R.id.tv_pre)
    TextView tvPre;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
//    @BindView(R.id.pd_name_spinner)
//    NiceSpinner pdNameSpinner;

    private List<BaseFragment> fragments;
    private List<String> titles;
    private MyPagerAdapter mAdapter;
    @Autowired(name = "did")
    public String did;
    @Autowired(name = "Encode")
    public String encode;
    @Autowired(name = "pid")
    public int pid;
    //    private WeakReference<DeviceDetailBean2> detailBean;
    private DeviceDetailBean2 detailBean;

    private MyProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            detailBean = savedInstanceState.getParcelable("detailBean");
            Log.e(TAG, detailBean.getDID() + "");
            Log.e(TAG, "onCreate读取detailBean");
        }
    }

    @Override
    public int inflateContentView() {
        return R.layout.activity_device_detail;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {//存储状态，用于下一次重建activity使用；
        Log.e(TAG, "保存detailBean");
        if (detailBean != null) {
            outState.putParcelable("detailBean", detailBean);
            Log.e(TAG, "保存detailBean");
        }
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {//恢复时调用
        if (savedInstanceState != null) {
            detailBean = savedInstanceState.getParcelable("detailBean");
            Log.e(TAG, "读取detailBean");
        }
        if (detailBean != null) {
            Log.e(TAG, detailBean.getDID() + "");
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    protected void initData() {
//        pdNameSpinner.setVisibility(View.GONE);
        tvPre.setText(R.string.back);
        if (!TextUtils.isEmpty(did) && did.contains(",")) {
            ARouter.getInstance().build(MyConstants.DEVICE_DETAIL2)
                    .withString("did", did)
                    .navigation();
//            Intent in2 = new Intent(mContext, DeviceDetailActivity2.class);
//            in2.putExtra("did", did);
//            startActivity(in2);
            finish();
            return;
        }
        if (detailBean == null) {
            getDataFromServer();
        } else {
            setViewAndData();
        }
    }

    private void setViewAndData() {
        initFragments();
        viewPager.setVisibility(View.VISIBLE);
        hideLoadingDialog();
        if (detailBean.getDeviceName().length() > 15) {
            name.setTextSize(16);
        }
        name.setText(detailBean.getDeviceName());
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(mAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void getDataFromServer() {
        showLoadingDialog();
        viewPager.setVisibility(View.GONE);
        assert mPresenter != null;
        mPresenter.getDetail(did, progressDialog);

    }

    public void showLoadingDialog() {
        if (progressDialog == null) {
            progressDialog = new MyProgressDialog(mContext, R.style.MyProgressDialog);
        }
        progressDialog.setText("加载中...");
        progressDialog.show();
    }

    public void hideLoadingDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public void setDeviceDetail(DeviceDetailBean2 detailBean) {
        this.detailBean =detailBean;
        setViewAndData();
    }

    private void initFragments() {
        fragments = new ArrayList<>();
        fragments.add(RealtimeDataFragment.newInstance(detailBean, pid));
        fragments.add(AccountInfoFragment.newInstance(detailBean));
        fragments.add(DangerFragment.newInstance(encode, detailBean, pid));
        titles = new ArrayList<>();
        titles.add("实时数据");
        titles.add("台账信息");
        titles.add("隐患上报");
        mAdapter = new MyPagerAdapter(getSupportFragmentManager(), fragments, titles);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fragments.get(2).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected boolean isRegisterEventBus() {
        return false;
    }

    @Override
    protected boolean isNeedInject() {
        return true;
    }

    @OnClick(R.id.tv_shouye)
    public void onViewClicked() {
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (fragments != null) {
            fragments.clear();
            fragments = null;
        }
        if (titles != null) {
            titles.clear();
            titles = null;
        }
    }
}
