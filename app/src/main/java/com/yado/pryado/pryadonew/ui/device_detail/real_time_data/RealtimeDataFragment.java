package com.yado.pryado.pryadonew.ui.device_detail.real_time_data;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.yado.pryado.pryadonew.MyConstants;
import com.yado.pryado.pryadonew.R;
import com.yado.pryado.pryadonew.base.BaseFragment;
import com.yado.pryado.pryadonew.bean.DeviceDetailBean2;
import com.yado.pryado.pryadonew.bean.HisData2;
import com.yado.pryado.pryadonew.ui.adapter.RealTimeDataAdapter;
import com.yado.pryado.pryadonew.ui.device_detail.DeviceDetailContract;
import com.yado.pryado.pryadonew.ui.device_detail.DeviceDetailPresent;
import com.yado.pryado.pryadonew.ui.widgit.CustomMarkerView;
import com.yado.pryado.pryadonew.ui.widgit.EmptyLayout;
import com.yado.pryado.pryadonew.util.DateUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RealtimeDataFragment extends BaseFragment<DeviceDetailPresent> implements DeviceDetailContract.View {

    @BindView(R.id.rc_real_time_data)
    RecyclerView rcRealTimeData;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.emptyLayout)
    EmptyLayout emptyLayout;
//    private WeakReference<DeviceDetailBean2> mBean;
    private DeviceDetailBean2 mBean;
    private List<DeviceDetailBean2.RealTimeParamsBean> mRealTimeParamsList = new ArrayList<>();
    private LineChart lineChart;
    private int pid;

    @Inject
    RealTimeDataAdapter adapter;
    private View chartView;
    private TextView tv_no_data;
    private ProgressBar progressBar;
    private WeakReference<DeviceDetailBean2.RealTimeParamsBean> realTimeParamsBean;
    private NiftyDialogBuilder dialogBuilder;
    private CustomMarkerView mv;
    //    private DeviceDetailBean2.RealTimeParamsBean realTimeParamsBean;

    /**
     * 是否注册 EventBus
     * @return
     */
    @Override
    protected boolean isRegisterEventBus() {
        return false;
    }

    /**
     * 获取 RealtimeDataFragment 实例
     * @param mBean
     * @param pid
     * @return
     */
    public static RealtimeDataFragment newInstance(DeviceDetailBean2 mBean, int pid) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("DeviceDetailBean", mBean);
        bundle.putInt(MyConstants.PID, pid);
        RealtimeDataFragment fragment = new RealtimeDataFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * 加载布局
     * @return
     */
    @Override
    public int getLayoutId() {
        return R.layout.fragmnet_realtime_data;
    }

    /**
     * 注入 View
     */
    @Override
    protected void initInjector() {
        mFragmentComponent.inject(this);
    }

    //初始化View
    @Override
    public void initView() {

    }

    /**
     * 加载数据
     */
    @Override
    protected void loadData() {
        mBean =  getArguments().getParcelable("DeviceDetailBean");
        pid = getArguments().getInt(MyConstants.PID);
        initRecyclerView();
        initDialogView();
        emptyLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
        if (mBean != null) {
            emptyLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
            scrollView.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);
            mRealTimeParamsList = mBean.getRealTimeParams();
            adapter.setNewData(mRealTimeParamsList);
        } else {
            emptyLayout.setErrorType(EmptyLayout.NODATA);
        }
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rcRealTimeData.setLayoutManager(mLayoutManager);
        rcRealTimeData.setHasFixedSize(true);
        rcRealTimeData.setItemAnimator(new DefaultItemAnimator());
        rcRealTimeData.setAdapter(adapter);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                realTimeParamsBean = new WeakReference<>((DeviceDetailBean2.RealTimeParamsBean) adapter.getData().get(position));
                int did = mBean.getDID();
                String tagId;
                switch (view.getId()) {
                    case R.id.test_all:
                        tagId = realTimeParamsBean.get().getTagID();
                        break;
                    case R.id.test_a:
                        tagId = realTimeParamsBean.get().getTagIDA();
                        break;
                    case R.id.test_b:
                        tagId = realTimeParamsBean.get().getTagIDB();
                        break;
                    case R.id.test_c:
                        tagId = realTimeParamsBean.get().getTagIDC();
                        break;
                    default:
                        tagId = realTimeParamsBean.get().getTagIDA();
                        break;
                }
                if (!realTimeParamsBean.get().getPName().contains("()")){
                    showDialog(realTimeParamsBean.get());
                    progressBar.setVisibility(View.VISIBLE);
                    tv_no_data.setVisibility(View.GONE);
                    lineChart.setVisibility(View.GONE);
                    assert mPresenter != null;
                    mPresenter.getPointData(DateUtils.getStringToday(), "12", pid + "", did + "", tagId);
                }

            }
        });
    }

    //设置折线样式
    public void setLineDatas(String pName, HisData2 hisData) {

        mPresenter.setLineType(lineChart, mFragmentComponent.getActivityContext());
        //设置数据
        lineChart.setData(mPresenter.transform_data(pName, hisData.getHisDevData(),mv, tv_no_data, lineChart, mFragmentComponent.getActivityContext()));
        if (lineChart.getLineData() != null){
            YAxis axisLeft = lineChart.getAxisLeft();
            axisLeft.setAxisMaxValue(lineChart.getYMax() + 3);
        }

        lineChart.notifyDataSetChanged();

    }

    /**
     * 设置曲线数据
     * @param hisData2
     */
    public void setHisData(HisData2 hisData2) {
        setLineDatas(realTimeParamsBean.get().getPName(), hisData2);
        progressBar.setVisibility(View.GONE);
    }

    /**
     * 显示Dialog
     * @param realTimeParamsBean
     */
    private void showDialog(DeviceDetailBean2.RealTimeParamsBean realTimeParamsBean) {
        if (dialogBuilder == null) {
            dialogBuilder = NiftyDialogBuilder.getInstance(context);
            dialogBuilder
                    .withTitle("历史数据")                               //标题.withTitle(null)  no title
                    .withTitleColor("#FFFFFF")                               //def  标题颜色
                    .withDividerColor("#11000000")                           //def
                    .withMessageColor("#FFCFCFCF")                          //def  | withMessageColor(int resid)   内容颜色
                    .withDialogColor("#AF444D50")                            //def  | withDialogColor(int resid)   dialog框颜色
                    .withIcon(context.getResources().getDrawable(R.drawable.ic_logo2))  //标题栏图标
                    .withDuration(700)                                      //def      动画持续时间
                    .withEffect(Effectstype.SlideBottom)                                     //def Effectstype.Slidetop  动画模式
                    .withButton1Text("返回")                                  //def gone     按钮文字
                    .isCancelableOnTouchOutside(true)                       //def    | isCancelable(true)是否支持点击dialog框外关闭dialog
                    .setCustomView(chartView, context);     //.setCustomView(View or ResId,context)    自定义布局
            dialogBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    if (lineChart != null && lineChart.getLineData() != null) {
                        lineChart.clear();
                        lineChart.notifyDataSetChanged();
                    }
                }
            });
        }

        dialogBuilder
                .withMessage(mBean.getCompany() + "-" + mBean.getDeviceName() + "-" + realTimeParamsBean.getPName())                 //.withMessage(null)  no Msg   内容
                .withButton1Text("返回")                                  //def gone     按钮文字
                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                    }
                }).show();    //展示
    }

    /**
     * 初始化Dialog View
     */
    private void initDialogView() {
        chartView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_his, null);
        lineChart = chartView.findViewById(R.id.lineChart);
        tv_no_data = chartView.findViewById(R.id.tv_no_data);
        progressBar = chartView.findViewById(R.id.progressBar);
        mv = new CustomMarkerView(context, R.layout.marker_view);
        lineChart.setMarkerView(mv);
        mv.setChartView(lineChart);
    }

    public void showNoDataView() {
        tv_no_data.setVisibility(View.VISIBLE);
        lineChart.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        if (mRealTimeParamsList != null) {
            mRealTimeParamsList.clear();
            mRealTimeParamsList = null;
        }
        super.onDestroy();
    }
}
