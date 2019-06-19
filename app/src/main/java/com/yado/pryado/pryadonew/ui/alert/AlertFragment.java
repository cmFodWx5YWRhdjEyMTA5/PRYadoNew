package com.yado.pryado.pryadonew.ui.alert;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
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
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.yado.pryado.pryadonew.MyApplication;
import com.yado.pryado.pryadonew.MyConstants;
import com.yado.pryado.pryadonew.R;
import com.yado.pryado.pryadonew.base.BaseFragment;
import com.yado.pryado.pryadonew.bean.AlertBean;
import com.yado.pryado.pryadonew.bean.HisData;
import com.yado.pryado.pryadonew.bean.StatusBean;
import com.yado.pryado.pryadonew.ui.adapter.AlertAdapter;
import com.yado.pryado.pryadonew.ui.widgit.CustomMarkerView;
import com.yado.pryado.pryadonew.ui.widgit.EmptyLayout;
import com.yado.pryado.pryadonew.util.DateUtils;
import com.yado.pryado.pryadonew.util.SharedPrefUtil;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;

public class AlertFragment extends BaseFragment<AlertPresent> implements AlertContract.View {
    @BindView(R.id.rv_alert)
    RecyclerView rvAlert;

    private static final String BUNDLE_Type = "type";
    @BindView(R.id.emptyLayout)
    EmptyLayout emptyLayout;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    private int type;
    private int pageindex = 1;
    private int pid = 0;
    @Inject
    AlertAdapter adapter;

    private LineChart lineChart;
    private View chartView;
    private ProgressBar progressBar;
    private TextView tv_nodata;
    private CustomMarkerView mv;
    private NiftyDialogBuilder dialogBuilder;
    private ArrayList<String> envxVals = new ArrayList<>();
    private ArrayList<Entry> envyVals = new ArrayList<>();

    private String startDate, endDate;

    /**
     * 是否注册 EventBus
     * @return
     */
    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    /**
     * 加载布局
     * @return
     */
    @Override
    public int getLayoutId() {
        return R.layout.fragment_alert;
    }

    /**
     * 注入View
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
        assert getArguments() != null;
        type = getArguments().getInt(BUNDLE_Type);
        assert mPresenter != null;
        String date = mPresenter.getInternalDate(type);
        startDate = date.split("\\*")[0];
        endDate = date.split("\\*")[1];
        initRecyclerView();
        initDialogView();
        emptyLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
        assert mPresenter != null;
//        mPresenter.getAlertList(20, type, pageindex, pid, emptyLayout);
        mPresenter.getAlertList_1(20, pageindex, startDate, endDate, pid, emptyLayout);
        smartRefreshLayout.setEnableRefresh(false);
        initListener();
    }

    /**
     * 初始化 RecyclerView
     */
    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rvAlert.setLayoutManager(linearLayoutManager);
//        rvAlert.addItemDecoration(new SimplePaddingDecoration(context));
        rvAlert.setAdapter(adapter);
    }

    /**
     * 初始化监听事件
     */
    private void initListener() {
        //下拉加载更多
        smartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                pageindex++;
                assert mPresenter != null;
//                mPresenter.getAlertList(20, type, pageindex, pid, emptyLayout);
                mPresenter.getAlertList_1(20, pageindex, startDate, endDate, pid, emptyLayout);
            }
        });
        emptyLayout.setOnLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRefresh(pid);
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                AlertBean.RowsBean rowsBean = (AlertBean.RowsBean) adapter.getData().get(position);
                if ((rowsBean.getAlarmCate().equals("带电状态") || rowsBean.getAlarmCate().equals("开关量"))) {//这两种状态没有曲线
                    return;
                }
                envxVals.clear();
                envyVals.clear();
                progressBar.setVisibility(View.VISIBLE);
                tv_nodata.setVisibility(View.GONE);
                lineChart.setVisibility(View.GONE);
                assert mPresenter != null;
                mPresenter.getPointHisData(rowsBean.getAlarmDateTime(), rowsBean.getPID(), rowsBean.getDID(), rowsBean.getTagID());
                showDialog(rowsBean);
            }
        });
    }

    /**
     * 显示Dialog
     * @param rowsBean
     */
    private void showDialog(AlertBean.RowsBean rowsBean) {
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
                    .isCancelableOnTouchOutside(true)                       //def    | isCancelable(true)是否支持点击dialog框外关闭dialog
                    .setCustomView(chartView, context);    //.setCustomView(View or ResId,context)    自定义布局

            dialogBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    lineChart.clear();
                }
            });
        }

        dialogBuilder
                .withMessage(rowsBean.getAlarmCate() + ":" + rowsBean.getCompany() + "-" + rowsBean.getAlarmArea() + "-" + rowsBean.getAlarmAddress())                 //.withMessage(null)  no Msg   内容
                .withButton1Text("返回")                                  //def gone     按钮文字
                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                    }
                }).show();    //展示

    }

    /**
     * 设置曲线数据
     * @param hisData
     */
    public void setLineDatas(HisData hisData) {
        if (hisData != null && hisData.getHisDevData().size() > 0) {
            assert mPresenter != null;
            mPresenter.setLineType(lineChart);
            //设置数据
            lineChart.setData(transformData(hisData.getHisDevData()));
            if (lineChart.getLineData() != null) {
                lineChart.getAxisLeft().setAxisMaxValue(lineChart.getYMax() + 3);
            }
            lineChart.notifyDataSetChanged();

        } else {
            lineChart.setVisibility(View.GONE);
            tv_nodata.setVisibility(View.VISIBLE);
        }
        progressBar.setVisibility(View.GONE);
    }

    /**
     * 初始化Dialog View
     */
    private void initDialogView() {
        chartView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_his, null);
        lineChart = chartView.findViewById(R.id.lineChart);
        progressBar = chartView.findViewById(R.id.progressBar);
        tv_nodata = chartView.findViewById(R.id.tv_no_data);
        mv = new CustomMarkerView(context, R.layout.marker_view);
        lineChart.setMarkerView(mv);
        mv.setChartView(lineChart);
    }

    /**
     * 把map类型转化为LineData；
     *
     * @param HisDevData
     * @return
     */
    private LineData transformData(LinkedHashMap<String, String> HisDevData) {

        if (HisDevData == null) {
            lineChart.setNoDataTextDescription(getActivity().getString(R.string.no_recent_data));
            return null;
        }
        try {
            int i = 0;
            for (Map.Entry<String, String> entry : HisDevData.entrySet()) {
                if (!TextUtils.isEmpty(entry.getValue())) {
                    envxVals.add(entry.getKey());
                    envyVals.add(new Entry(Float.valueOf(entry.getValue()), i++));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        mv.setXValue(envxVals);
        if (envyVals.size() <= 0) {
            tv_nodata.setVisibility(View.VISIBLE);
            lineChart.setVisibility(View.GONE);
            return null;
        }
        tv_nodata.setVisibility(View.GONE);
        lineChart.setVisibility(View.VISIBLE);
        LineDataSet set1 = new LineDataSet(envyVals, "历史曲线");
        set1.enableDashedHighlightLine(10f, 5f, 0f);
        set1.setColor(mFragmentComponent.getActivity().getResources().getColor(R.color.xxblue));//折线的颜色
        set1.setLineWidth(1f);
        set1.setCircleSize(1f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setDrawFilled(true);//设置是否填充
        set1.setFillColor(mFragmentComponent.getActivity().getResources().getColor(R.color.xxblue));//设置填充色
        set1.setFillAlpha(65);
        set1.setDrawValues(false);//隐藏点上的数值；
        set1.setDrawCircles(false);//在点上画圆 默认true
        set1.setCircleColor(mFragmentComponent.getActivity().getResources().getColor(R.color.xxblue));
        set1.setDrawCubic(true);
        set1.setCubicIntensity(0.15f);
        set1.setHighlightLineWidth(1f);
        set1.setHighLightColor(mFragmentComponent.getActivity().getResources().getColor(R.color.blue2));
        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set1); // add the datasets

        return new LineData(envxVals, dataSets);
    }

    /**
     * 获取一个 AlertFragment 实例
     * @param type
     * @return
     */
    public static AlertFragment newInstance(int type) {
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_Type, type);
        AlertFragment fragment = new AlertFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Subscribe
    public void onMessageEvent(StatusBean change) {
        if (getString(R.string.has_new_alert).equals(change.getStatus())) {
            onRefresh(this.pid);
        }
    }

    /**
     * 重新联网请求
     * @param pid
     */
    public void onRefresh(int pid) {
        pageindex = 1;
        this.pid = pid;
        adapter.getData().clear();
        adapter.notifyDataSetChanged();
        emptyLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
        emptyLayout.setVisibility(View.VISIBLE);
        smartRefreshLayout.setVisibility(View.GONE);
        assert mPresenter != null;
//        mPresenter.getAlertList(20, type, pageindex, this.pid, emptyLayout);
        mPresenter.getAlertList_1(20, pageindex, startDate, endDate, pid, emptyLayout);
    }

    /**
     * 设置报警列表
     * @param alertBean
     */
    public void setAlertList(AlertBean alertBean) {
        int notice = 0;
        smartRefreshLayout.finishLoadmore();
        adapter.addData(alertBean.getRows());
        if (adapter.getData().size() <= 0) {
            SharedPrefUtil.getInstance(MyApplication.getInstance()).saveObject(MyConstants.alert_num, 0);
            emptyLayout.setVisibility(View.VISIBLE);
            emptyLayout.setErrorType(EmptyLayout.NODATA);
            smartRefreshLayout.setVisibility(View.GONE);
        } else {
            List<AlertBean.RowsBean> data = adapter.getData();
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).getAlarmConfirm().equals("未确认") && !data.get(i).getALarmType().equals("恢复")) {
                    notice++;
                }
            }
            SharedPrefUtil.getInstance(MyApplication.getInstance()).saveObject(MyConstants.alert_num, notice);
            emptyLayout.setVisibility(View.GONE);
            smartRefreshLayout.setVisibility(View.VISIBLE);
        }
        if (alertBean.getRows().size() <= 0) {
            smartRefreshLayout.finishLoadmoreWithNoMoreData();
            smartRefreshLayout.resetNoMoreData();
        }
    }

    /**
     * 设置开始时间
     * @param startDate
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate + " 00:00:00";
    }

    /**
     * 设置结束时间并加载数据
     * @param endDate
     */
    public void setEndDate(String endDate) {
        emptyLayout.setVisibility(View.VISIBLE);
        emptyLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
        smartRefreshLayout.setVisibility(View.GONE);
        adapter.getData().clear();
        adapter.notifyDataSetChanged();
        if (DateUtils.getStringDateShort().equals(endDate)) {
            this.endDate = DateUtils.getStringDate();
        } else {
            this.endDate = endDate + " 23:59:59";
        }
        pageindex = 1;
        assert mPresenter != null;
        mPresenter.getAlertList_1(20, pageindex, startDate, this.endDate, pid, emptyLayout);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (envxVals.size() > 0) {
            envxVals.clear();
            envxVals = null;
        }
        if (envyVals.size() > 0) {
            envyVals.clear();
            envyVals = null;
        }
    }

    public void setError() {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
            tv_nodata.setVisibility(View.VISIBLE);
        }
    }
}
