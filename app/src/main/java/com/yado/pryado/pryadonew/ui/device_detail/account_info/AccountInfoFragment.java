package com.yado.pryado.pryadonew.ui.device_detail.account_info;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yado.pryado.pryadonew.MyConstants;
import com.yado.pryado.pryadonew.R;
import com.yado.pryado.pryadonew.base.BaseFragment;
import com.yado.pryado.pryadonew.bean.BugList;
import com.yado.pryado.pryadonew.bean.DeviceDetailBean2;
import com.yado.pryado.pryadonew.ui.adapter.DangerAdapter;
import com.yado.pryado.pryadonew.ui.device_detail.DeviceDetailContract;
import com.yado.pryado.pryadonew.ui.device_detail.DeviceDetailPresent;
import com.yado.pryado.pryadonew.ui.widgit.EmptyLayout;
import com.yado.pryado.pryadonew.ui.widgit.HeaderScrollHelper;
import com.yado.pryado.pryadonew.ui.widgit.HeaderScrollView;
import com.yado.pryado.pryadonew.util.Util;

import java.lang.ref.WeakReference;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class AccountInfoFragment extends BaseFragment<DeviceDetailPresent> implements DeviceDetailContract.View, HeaderScrollHelper.ScrollableContainer {

    @BindView(R.id.tv_device_code)
    TextView tvDeviceCode;
    @BindView(R.id.tv_device_model)
    TextView tvDeviceModel;
    @BindView(R.id.tv_date_of_production)
    TextView tvDateOfProduction;
    @BindView(R.id.tv_device_name)
    TextView tvDeviceName;
    @BindView(R.id.tv_device_producer)
    TextView tvDeviceProducer;
    @BindView(R.id.tv_date_of_operation)
    TextView tvDateOfOperation;
    @BindView(R.id.tv_installed_place)
    TextView tvInstalledPlace;
    @BindView(R.id.tv_organization)
    TextView tvOrganization;
    @BindView(R.id.tv_last_date_of_maintenance)
    TextView tvLastDateOfMaintenance;
    @BindView(R.id.tv_maintainer)
    TextView tvMaintainer;
    @BindView(R.id.linear_black_information)
    LinearLayout linearBlackInformation;
    @BindView(R.id.rv_dangers)
    RecyclerView rvDangers;
    @BindView(R.id.ll_dangers)
    LinearLayout llDangers;
    @BindView(R.id.nestedScrollView)
    HeaderScrollView scrollView;
    @BindView(R.id.emptyLayout)
    EmptyLayout emptyLayout;
    @BindView(R.id.ll_content)
    LinearLayout ll_content;

    private WeakReference<DeviceDetailBean2> mBean;
//    private DeviceDetailBean2 mBean;
    @Inject
    DangerAdapter adapter;

    @Override
    protected boolean isRegisterEventBus() {
        return false;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_account_information;
    }

    public static AccountInfoFragment newInstance(DeviceDetailBean2 mBean) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("DeviceDetailBean", mBean);
        AccountInfoFragment fragment = new AccountInfoFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    public void initView() {

    }

    @Override
    protected void loadData() {
        scrollView.setCurrentScrollableContainer(this);
        mBean = new WeakReference<>((DeviceDetailBean2)getArguments().getParcelable("DeviceDetailBean"));
        emptyLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
        setView();
        initRecyclerView();
        assert mPresenter != null;
        mPresenter.getBugList(mBean.get().getDID() + "");

    }

    private void setView() {
        if (mBean != null) {
            tvDeviceCode.setText("设备编码  :  " + mBean.get().getDeviceCode());
            tvDeviceModel.setText("设备型号  :  " + mBean.get().getDeviceModel());
            tvDateOfProduction.setText("生产日期  :  " + mPresenter.handle(mBean.get().getBuyTime()));
            tvDeviceName.setText("设备名称  :  " + mBean.get().getDeviceName());
            tvDeviceProducer.setText("设备厂家  :  " + mBean.get().getMFactory());
            tvDateOfOperation.setText("投运日期  :  " + mPresenter.handle(mBean.get().getUseDate()));
            tvInstalledPlace.setText("安装地点:  " + mBean.get().getInstallAddr());
            tvOrganization.setText("所属单位  :  " + mBean.get().getCompany());

            tvLastDateOfMaintenance.setText("最后维护日期  :  " + mPresenter.handle(mBean.get().getLastMtcDate()));
            tvMaintainer.setText("维护人  :  " + mBean.get().getLastMtcPerson());
            emptyLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
            emptyLayout.setVisibility(View.GONE);
            ll_content.setVisibility(View.VISIBLE);
        }
    }

    private void initRecyclerView() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvDangers.setLayoutManager(mLayoutManager);
        rvDangers.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                 BugList.ListmapBean bug = (BugList.ListmapBean) adapter.getData().get(position);
                if (!Util.isDoubleClick()) {
                    ARouter.getInstance().build(MyConstants.DANGER_DETAIL)
                            .withParcelable("bug", bug)
                            .navigation();
                }

            }
        });
    }



    public void setBuglist(BugList bugList) {
        List<BugList.ListmapBean> danger_list = bugList.getListmap();
        if (danger_list != null && danger_list.size() > 0) {
            adapter.setNewData(danger_list);
            llDangers.setVisibility(View.VISIBLE);
            rvDangers.setVisibility(View.VISIBLE);
        }else {
            llDangers.setVisibility(View.GONE);
            rvDangers.setVisibility(View.GONE);
        }
    }

    @Override
    public View getScrollableView() {
        return rvDangers;
    }


}
