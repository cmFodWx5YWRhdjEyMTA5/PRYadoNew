package com.yado.pryado.pryadonew.ui.temp_monitor;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebSettings;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.github.abel533.echarts.Legend;
import com.github.abel533.echarts.Toolbox;
import com.github.abel533.echarts.Tooltip;
import com.github.abel533.echarts.axis.Axis;
import com.github.abel533.echarts.axis.AxisLabel;
import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.Magic;
import com.github.abel533.echarts.code.SeriesType;
import com.github.abel533.echarts.code.Trigger;
import com.github.abel533.echarts.feature.DataView;
import com.github.abel533.echarts.feature.Feature;
import com.github.abel533.echarts.feature.MagicType;
import com.github.abel533.echarts.feature.Restore;
import com.github.abel533.echarts.feature.SaveAsImage;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Line;
import com.github.abel533.echarts.series.Series;
import com.github.abel533.echarts.style.AreaStyle;
import com.github.abel533.echarts.style.TextStyle;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.yado.pryado.pryadonew.R;
import com.yado.pryado.pryadonew.base.BaseFragment;
import com.yado.pryado.pryadonew.bean.DeviceDetailBean2;
import com.yado.pryado.pryadonew.bean.NonIntrusiveEvent;
import com.yado.pryado.pryadonew.bean.TempMonitorBean;
import com.yado.pryado.pryadonew.bean.VagueHistoryGraphBean;
import com.yado.pryado.pryadonew.bean.VagueRealTimeBean;
import com.yado.pryado.pryadonew.ui.adapter.TempMonitorDataAdapter;
import com.yado.pryado.pryadonew.ui.adapter.TempMonitorDataAdapter2;
import com.yado.pryado.pryadonew.ui.widgit.EmptyLayout;
import com.yado.pryado.pryadonew.ui.widgit.TEChartWebView;
import com.yado.pryado.pryadonew.ui.widgit.XYMarkerView;
import com.yado.pryado.pryadonew.util.DateUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class TempMonitorAssessFragment extends BaseFragment<TempMonitorPresent> implements TempMonitorContract.View, RadioGroup.OnCheckedChangeListener, OnChartGestureListener {

    @BindView(R.id.rv_temp_data)
    RecyclerView rvTempData;
    @BindView(R.id.rv_health)
    RecyclerView rvHealth;
    @BindView(R.id.linechart)
    LineChart linechart;
    @BindView(R.id.pb_loading)
    ProgressBar pbLoading;
    @BindView(R.id.rb_day)
    RadioButton rbDay;
    @BindView(R.id.rb_week)
    RadioButton rbWeek;
    @BindView(R.id.rb_month)
    RadioButton rbMonth;
    @BindView(R.id.rg_max)
    RadioGroup rgMax;
    @BindView(R.id.tv_date2)
    TextView tvDate2;
    @BindView(R.id.tv_date1)
    TextView tvDate1;
    @BindView(R.id.linechart2)
    LineChart linechart2;
    @BindView(R.id.pb_loading1)
    ProgressBar pbLoading1;
    @BindView(R.id.rb_day1)
    RadioButton rbDay1;
    @BindView(R.id.rb_week1)
    RadioButton rbWeek1;
    @BindView(R.id.rb_month1)
    RadioButton rbMonth1;
    @BindView(R.id.rg_load)
    RadioGroup rgLoad;
    @BindView(R.id.btn_pre1)
    Button btnPre1;
    @BindView(R.id.btn_next1)
    Button btnNext1;
    @BindView(R.id.btn_pre2)
    Button btnPre2;
    @BindView(R.id.btn_next2)
    Button btnNext2;
    @BindView(R.id.emptyLayout)
    EmptyLayout emptyLayout;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.tv_station)
    TextView tvStation;
    @BindView(R.id.ll_dec)
    LinearLayout llDec;
    @BindView(R.id.ll_dec1)
    LinearLayout llDec1;
    @BindView(R.id.ll_barChartWebView1)
    LinearLayout ll_barChartWebView1;
    private TEChartWebView barChartWebView1;

    @Inject
    TempMonitorDataAdapter adapter;
    @Inject
    TempMonitorDataAdapter2 adapter2;

    private List<TempMonitorBean> monitorBeanList;//供两个 RecyclerView使用
    private List<TempMonitorBean> monitorBeanList2;//供两条曲线使用
    private List<String> tagIds = new ArrayList<>();//上面两个表格的 tagId集合
    private List<String> tagIds2 = new ArrayList<>();//下面两条曲线的 tagId集合
    private String did;
    private int pid;
    private String station;
    private int currentIndex = 0;
    private int currentIndex2 = 0;
    private int vagueIndex = 0;
    private int commenIndex = 0;
    private View footerView;
    private View footerView2;
    private String startDate1, startDate2, endDate1, endDate2;
    private List<LinkedHashMap<String, String>> dataSets1 = new ArrayList<>();//第一条曲线的数据集合
    private List<LinkedHashMap<String, String>> dataSets2 = new ArrayList<>();//第二条曲线的数据集合
    private List<List<String>> xValues1 = new ArrayList<>();//第一条曲线X轴数据集合
    private List<List<String>> xValues2 = new ArrayList<>();//第二条曲线真实时间数据集合

    private int type1, type2;//选择的类型，有日，周，月三种
    private int[] colors;
    private XYMarkerView mv;//第一个提示框
    private XYMarkerView mv1;//第二个提示框

    private List<List<String>> yValues1 = new ArrayList<>();//第一条曲线数据集合， 这个是用来echarts插件用的
    private List<List<String>> xValues11 = new ArrayList<>();//第一条曲线真实时间数据集合
    private List<String> noDataTagIds = new ArrayList<>();//没有数据的设备

    private boolean isFirst;

    /**
     * 是否需要注册 EventBus
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
        return R.layout.fragment_temp_monitor_assess;
    }

    /**
     * 注入View
     */
    @Override
    protected void initInjector() {
        mFragmentComponent.inject(this);
    }

    /**
     * 获取TempMonitorAssessFragment 实例
     * @param pid
     * @return
     */
    public static TempMonitorAssessFragment newInstance(int pid) {
        Bundle bundle = new Bundle();
        bundle.putInt("pid", pid);
        TempMonitorAssessFragment fragment = new TempMonitorAssessFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void initView() {

    }

    /**
     * 加载数据
     */
    @Override
    protected void loadData() {
        assert getArguments() != null;
        pid = getArguments().getInt("pid");
        colors = new int[]{context.getResources().getColor(R.color.brown), context.getResources().getColor(R.color.yiban),
                context.getResources().getColor(R.color.blue2), context.getResources().getColor(R.color.purple),
                context.getResources().getColor(R.color.yanzhong), context.getResources().getColor(R.color.blue_text),
                context.getResources().getColor(R.color.colorAccent), context.getResources().getColor(R.color.laser_color)};
        initFooterView();
        initRecyclerView();

        linechart.getLegend().setEnabled(false);
        linechart2.getLegend().setEnabled(false);

        // set the marker to the chart
        initMarkerView();
        mPresenter.setLineType(linechart, 7);
        mPresenter.setLineType(linechart2, 7);
        initListener();
        emptyLayout.setErrorType(EmptyLayout.NODATA);
        barChartWebView1 = new TEChartWebView(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ll_barChartWebView1.getLayoutParams());
        barChartWebView1.setLayoutParams(lp);
        ll_barChartWebView1.addView(barChartWebView1);

    }

    /**
     * 初始化 MarkerView
     */
    private void initMarkerView() {
        mv = new XYMarkerView(context, R.layout.marker_view);//上面那条
        mv1 = new XYMarkerView(context, R.layout.marker_view);//下面那条
        linechart.setMarkerView(mv);
        linechart2.setMarkerView(mv1);
        mv.setChartView(linechart);
        mv1.setChartView(linechart2);
        mv.setType(1);
        mv1.setType(2);
    }

    /**
     * 初始化时间监听
     */
    private void initListener() {
        linechart.setOnChartGestureListener(this);
        linechart2.setOnChartGestureListener(this);
        rgMax.setOnCheckedChangeListener(this);
        rgLoad.setOnCheckedChangeListener(this);
    }

    /**
     * 初始化RecyclerView的 footerView
     */
    private void initFooterView() {
        footerView = LayoutInflater.from(mFragmentComponent.getActivityContext()).inflate(R.layout.footer, null);
        footerView2 = LayoutInflater.from(mFragmentComponent.getActivityContext()).inflate(R.layout.footer, null);
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getContext());
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(getContext());
        rvTempData.setLayoutManager(mLayoutManager1);
//        rvTempData.addItemDecoration(new DividerItemDecoration(mFragmentComponent.getActivityContext(), DividerItemDecoration.VERTICAL));
        rvTempData.setHasFixedSize(true);
        rvTempData.setAdapter(adapter);
        adapter.addFooterView(footerView);

        rvHealth.setLayoutManager(mLayoutManager2);
//        rvHealth.addItemDecoration(new DividerItemDecoration(mFragmentComponent.getActivityContext(), DividerItemDecoration.VERTICAL));
        rvHealth.setHasFixedSize(true);
        rvHealth.setAdapter(adapter2);
        adapter2.addFooterView(footerView2);
    }

    /**
     * 刷新数据
     * @param did
     * @param station
     */
    @SuppressLint("SetTextI18n")
    public void onRefresh(String did, String station) {
        if (station != null) {
            this.station = station;
            tvStation.setText(station + "柜体诊断信息");
        }
        emptyLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
        llContent.setVisibility(View.GONE);
        this.did = did;
        //清除数据
        if (linechart.getLineData() != null) {
            linechart.clear();
            linechart.notifyDataSetChanged();
        }
        if (linechart2.getLineData() != null) {
            linechart2.clear();
            linechart2.notifyDataSetChanged();
        }

        rgLoad.clearCheck();
        rgMax.clearCheck();
        dataSets1.clear();
        xValues1.clear();
        xValues11.clear();
        noDataTagIds.clear();
        yValues1.clear();
        dataSets2.clear();
        xValues2.clear();
        startDate1 = DateUtils.getStringDateShort() + " 00:00:00";
        endDate1 = DateUtils.getStringDate();
        startDate2 = DateUtils.getStringDateShort() + " 00:00:00";
        endDate2 = DateUtils.getStringDate();
        assert mPresenter != null;
        mPresenter.getDetail(this.did);
    }

    /**
     * 设置设备详情
     * @param detailBean
     */
    public void setDeviceDetail(DeviceDetailBean2 detailBean) {
        if (detailBean.getRealTimeParams() != null && detailBean.getRealTimeParams().size() > 0) {
            if (monitorBeanList == null) {
                monitorBeanList = new ArrayList<>();
                monitorBeanList2 = new ArrayList<>();
            }
            monitorBeanList.clear();
            monitorBeanList2.clear();
            tagIds.clear();
            tagIds2.clear();
            for (int i = 0; i < detailBean.getRealTimeParams().size(); i++) {
                if (!detailBean.getRealTimeParams().get(i).getPName().contains("仪表室") &&    //仪表室没有测温数据，
                        detailBean.getRealTimeParams().get(i).getDataTypeID().equals("120")) {//120表示非介入式温度
                    if (detailBean.getRealTimeParams().get(i).getTagID() == null) {
                        if (detailBean.getRealTimeParams().get(i).getTagIDA() != null) {
                            tagIds2.add(detailBean.getRealTimeParams().get(i).getTagIDA());
                            monitorBeanList2.add(new TempMonitorBean());
                        }
                        if (detailBean.getRealTimeParams().get(i).getTagIDB() != null) {
                            tagIds2.add(detailBean.getRealTimeParams().get(i).getTagIDB());
                            monitorBeanList2.add(new TempMonitorBean());
                        }
                        if (detailBean.getRealTimeParams().get(i).getTagIDC() != null) {
                            tagIds2.add(detailBean.getRealTimeParams().get(i).getTagIDC());
                            monitorBeanList2.add(new TempMonitorBean());
                        }
                    } else {
                        tagIds2.add(detailBean.getRealTimeParams().get(i).getTagID());
                        monitorBeanList2.add(new TempMonitorBean());
                    }

                }
                if (detailBean.getRealTimeParams().get(i).getDataTypeID().equals("120")) {
                    if (detailBean.getRealTimeParams().get(i).getTagID() == null) {
                        if (detailBean.getRealTimeParams().get(i).getTagIDA() != null) {
                            tagIds.add(detailBean.getRealTimeParams().get(i).getTagIDA());
                            monitorBeanList.add(new TempMonitorBean());
                        }
                        if (detailBean.getRealTimeParams().get(i).getTagIDB() != null) {
                            tagIds.add(detailBean.getRealTimeParams().get(i).getTagIDB());
                            monitorBeanList.add(new TempMonitorBean());
                        }
                        if (detailBean.getRealTimeParams().get(i).getTagIDC() != null) {
                            tagIds.add(detailBean.getRealTimeParams().get(i).getTagIDC());
                            monitorBeanList.add(new TempMonitorBean());
                        }
                    } else {
                        tagIds.add(detailBean.getRealTimeParams().get(i).getTagID());
                        monitorBeanList.add(new TempMonitorBean());
                    }
                }
            }
            currentIndex2 = tagIds2.size();
            currentIndex = tagIds.size();
            if (tagIds.size() > 0) {
                assert mPresenter != null;
                mPresenter.loadVagueRealTime(Integer.parseInt(tagIds.get(0)), Integer.parseInt(did), pid);//获取非介入实时数据
            }

            isFirst = true;
            rgLoad.clearCheck();
            rgMax.clearCheck();
            rbDay1.setChecked(true);
            rbDay.setChecked(true);
            rbDay.setTextColor(Color.GREEN);
            rbDay1.setTextColor(Color.GREEN);
            startDate1 = DateUtils.getStringDateShort() + " 00:00:00";//初始化时间
            endDate1 = DateUtils.getStringDate();
            startDate2 = DateUtils.getStringDateShort() + " 00:00:00";
            endDate2 = DateUtils.getStringDate();
            noDataTagIds.clear();
            assert mPresenter != null;
            mPresenter.loadMonitorHistoryGraph(pid, Integer.parseInt(tagIds2.get(0)), startDate1, endDate1);//获取非介入式曲线
            mPresenter.loadVagueHistoryGraph(Integer.parseInt(tagIds2.get(0)), startDate2, endDate2);//获取常规曲线
            isFirst = false;

        } else {
            emptyLayout.setErrorType(EmptyLayout.NODATA);
        }

    }

    /**
     * 设置非介入式实时数据
     * @param vagueRealTimeBean
     */
    public void setVagueRealTimeBean(VagueRealTimeBean vagueRealTimeBean) {
        if (monitorBeanList != null && monitorBeanList.size() > 0 && vagueRealTimeBean.getRows() != null &&
                vagueRealTimeBean.getRows().size() > 0 && currentIndex > 0) {
            monitorBeanList.get(monitorBeanList.size() - currentIndex).setDynamicTemo(vagueRealTimeBean.getRows().get(0).getDynamicTemp());
            monitorBeanList.get(monitorBeanList.size() - currentIndex).setStaticTemp(vagueRealTimeBean.getRows().get(0).getStaticTemp());
            monitorBeanList.get(monitorBeanList.size() - currentIndex).setTempRate(vagueRealTimeBean.getRows().get(0).getTemprise_rate());
            monitorBeanList.get(monitorBeanList.size() - currentIndex).setTempRiseAlarm(vagueRealTimeBean.getRows().get(0).getAlarmState1());
            monitorBeanList.get(monitorBeanList.size() - currentIndex).setPosition(vagueRealTimeBean.getRows().get(0).getTagName());
            monitorBeanList.get(monitorBeanList.size() - currentIndex).setpName(vagueRealTimeBean.getRows().get(0).getPosition());
            monitorBeanList.get(monitorBeanList.size() - currentIndex).setTempValue(vagueRealTimeBean.getRows().get(0).getTemp());
            monitorBeanList.get(monitorBeanList.size() - currentIndex).setRectime(vagueRealTimeBean.getRows().get(0).getRectime());
            monitorBeanList.get(monitorBeanList.size() - currentIndex).setTagid(vagueRealTimeBean.getRows().get(0).getTagid());

            if (currentIndex2 > 0) {
                if (!vagueRealTimeBean.getRows().get(0).getPosition().contains("仪表室")) {//仪表室没有非介入式测温 排除
                    monitorBeanList2.get(monitorBeanList2.size() - currentIndex2).setDynamicTemo(vagueRealTimeBean.getRows().get(0).getDynamicTemp());
                    monitorBeanList2.get(monitorBeanList2.size() - currentIndex2).setStaticTemp(vagueRealTimeBean.getRows().get(0).getStaticTemp());
                    monitorBeanList2.get(monitorBeanList2.size() - currentIndex2).setTempRate(vagueRealTimeBean.getRows().get(0).getTemprise_rate());
                    monitorBeanList2.get(monitorBeanList2.size() - currentIndex2).setTempRiseAlarm(vagueRealTimeBean.getRows().get(0).getAlarmState1());
                    monitorBeanList2.get(monitorBeanList2.size() - currentIndex2).setPosition(vagueRealTimeBean.getRows().get(0).getTagName());
                    monitorBeanList2.get(monitorBeanList2.size() - currentIndex2).setpName(vagueRealTimeBean.getRows().get(0).getPosition());
                    monitorBeanList2.get(monitorBeanList2.size() - currentIndex2).setTempValue(vagueRealTimeBean.getRows().get(0).getTemp());
                    monitorBeanList2.get(monitorBeanList2.size() - currentIndex2).setRectime(vagueRealTimeBean.getRows().get(0).getRectime());
                    monitorBeanList2.get(monitorBeanList2.size() - currentIndex2).setTagid(vagueRealTimeBean.getRows().get(0).getTagid());
                    currentIndex2--;
                }
            }

            currentIndex--;
            if (currentIndex > 0) {
                assert mPresenter != null;
                mPresenter.loadVagueRealTime(Integer.parseInt(tagIds.get(tagIds.size() - currentIndex)), Integer.parseInt(did), pid);
            }
        }
        if (currentIndex == 0) {
            addLegend(monitorBeanList2);//添加图例
            adapter.setNewData(monitorBeanList);//adapter设置数据源
            adapter2.setNewData(monitorBeanList);
            mv.setMonitorBeanList(monitorBeanList2);//为提示框设置数据集合
            mv1.setMonitorBeanList(monitorBeanList2);
            mv.setNoDataTagIds(noDataTagIds);//为提示框设置没有数据的设备集合
            mv1.setNoDataTagIds(noDataTagIds);
            mv.setStation(station);//设置站室
            mv1.setStation(station);
            emptyLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
            llContent.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 添加图例
     * @param monitorBeanList2
     */
    private void addLegend(List<TempMonitorBean> monitorBeanList2) {
        if (monitorBeanList2.size() > 0) {
            llDec.removeAllViews();
            llDec1.removeAllViews();
            for (int i = 0; i < monitorBeanList2.size(); i++) {
                addTextView(monitorBeanList2, i);
            }
        }
    }

    /**
     * 动态添加图例
     * @param monitorBeanList2
     * @param i
     */
    @SuppressLint("SetTextI18n")
    private void addTextView(List<TempMonitorBean> monitorBeanList2, int i) {
        Drawable drawableLeft = null;
        int drawableId;
        drawableId = getResources().getIdentifier("ic_line" + (i + 1), "drawable", context.getPackageName());
        drawableLeft = getResources().getDrawable(drawableId);
        TextView textView = new TextView(mFragmentComponent.getActivityContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        textView.setGravity(Gravity.START);
        textView.setTextSize(10);
        textView.setLayoutParams(lp);
        TextView textView2 = new TextView(mFragmentComponent.getActivityContext());
        if (monitorBeanList2.size() <= 4) {
            textView2.setGravity(Gravity.START | Gravity.CENTER);
        } else {
            textView2.setGravity(Gravity.START);
        }
        textView2.setTextSize(10);
        textView2.setLayoutParams(lp);
        textView.setText(station + monitorBeanList2.get(i).getpName());
        textView2.setText(station + monitorBeanList2.get(i).getPosition());
        textView.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, null, null);
        llDec.addView(textView);
        textView2.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, null, null);
        llDec1.addView(textView2);
    }

    /**
     * 设置PID
     * @param pid
     */
    public void setPid(int pid) {
        this.pid = pid;
        isFirst = true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getIsShow(Integer value) {
        if (value == 0) {
            emptyLayout.setErrorType(EmptyLayout.NODATA);
            llContent.setVisibility(View.GONE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getStation(String station) {
        if (station != null && !station.equals("")) {
            this.station = station;
            mv.setStation(station);
            mv1.setStation(station);
            tvStation.setText(station + "柜体诊断信息");
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        trySetData(checkedId);
    }

    /**
     * RadioGroup 切换监听
     * @param checkedId
     */
    private void trySetData(int checkedId) {
        switch (checkedId) {
            case R.id.rb_day:
                xValues1.clear();
                xValues11.clear();
                noDataTagIds.clear();
                yValues1.clear();
                linechart.setVisibility(View.INVISIBLE);
                pbLoading.setVisibility(View.VISIBLE);
                endDate1 = DateUtils.getStringDate();
                btnPre1.setText("上一天");
                btnNext1.setText("下一天");
                btnNext1.setEnabled(false);
                btnPre1.setEnabled(false);
                for (int i = 0; i < rgMax.getChildCount(); i++) {
                    rgMax.getChildAt(i).setClickable(false);
                }
                type1 = 0;
                dataSets1.clear();
                if (linechart.getLineData() != null) {
                    linechart.clear();
                    linechart.notifyDataSetChanged();
                }
                commenIndex = tagIds2.size();
                startDate1 = DateUtils.getStringDateShort() + " 00:00:00";
                if (!isFirst) {
                    isFirst = false;
                    assert mPresenter != null;
                    mPresenter.loadMonitorHistoryGraph(pid, Integer.parseInt(tagIds2.get(0)), startDate1, endDate1);//获取曲线数据
                }
                rbDay.setTextColor(Color.GREEN);
                rbWeek.setTextColor(Color.BLACK);
                rbMonth.setTextColor(Color.BLACK);
                break;
            case R.id.rb_week:
                xValues1.clear();
                xValues11.clear();
                noDataTagIds.clear();
                yValues1.clear();
                linechart.setVisibility(View.INVISIBLE);
                pbLoading.setVisibility(View.VISIBLE);
                endDate1 = DateUtils.getStringDate();
                startDate1 = DateUtils.startWeek(DateUtils.getNow()) + " 00:00:00";
                btnPre1.setText("上一周");
                btnNext1.setText("下一周");
                btnNext1.setEnabled(false);
                btnPre1.setEnabled(false);
                for (int i = 0; i < rgMax.getChildCount(); i++) {
                    rgMax.getChildAt(i).setClickable(false);
                }
                type1 = 1;
                dataSets1.clear();
                if (linechart.getLineData() != null) {
                    linechart.clear();
                    linechart.notifyDataSetChanged();
                }
                commenIndex = tagIds2.size();
                if (!isFirst) {
                    isFirst = false;
                    assert mPresenter != null;
                    mPresenter.loadMonitorHistoryGraph(pid, Integer.parseInt(tagIds2.get(0)), startDate1, endDate1);
                }

                rbDay.setTextColor(Color.BLACK);
                rbWeek.setTextColor(Color.GREEN);
                rbMonth.setTextColor(Color.BLACK);
                break;
            case R.id.rb_month:
                xValues1.clear();
                xValues11.clear();
                noDataTagIds.clear();
                yValues1.clear();
                linechart.setVisibility(View.INVISIBLE);
                pbLoading.setVisibility(View.VISIBLE);
                endDate1 = DateUtils.getStringDateMiddle() + ":00:00";
                startDate1 = DateUtils.timeStamp2Date(Long.parseLong(DateUtils.getSupportBeginDayofMonth(DateUtils.strToDate(endDate1))), null);
                btnPre1.setText("上一月");
                btnNext1.setText("下一月");
                btnNext1.setEnabled(false);
                btnPre1.setEnabled(false);
                for (int i = 0; i < rgMax.getChildCount(); i++) {
                    rgMax.getChildAt(i).setClickable(false);
                }
                type1 = 2;
                dataSets1.clear();
                if (linechart.getLineData() != null) {
                    linechart.clear();
                    linechart.notifyDataSetChanged();
                }
                commenIndex = tagIds2.size();
                if (!isFirst) {
                    isFirst = false;
                    assert mPresenter != null;
                    mPresenter.loadMonitorHistoryGraph(pid, Integer.parseInt(tagIds2.get(0)), startDate1, endDate1);
                }
                rbDay.setTextColor(Color.BLACK);
                rbWeek.setTextColor(Color.BLACK);
                rbMonth.setTextColor(Color.GREEN);
                break;
            case R.id.rb_day1:
                xValues2.clear();
                endDate2 = DateUtils.getStringDate();
                type2 = 0;
                linechart2.setVisibility(View.INVISIBLE);
                pbLoading1.setVisibility(View.VISIBLE);
                btnPre2.setText("上一天");
                btnNext2.setText("下一天");
                btnNext2.setEnabled(false);
                btnPre2.setEnabled(false);
                for (int i = 0; i < rgLoad.getChildCount(); i++) {
                    rgLoad.getChildAt(i).setClickable(false);
                }
                startDate2 = DateUtils.getStringDateShort() + " 00:00:00";
                vagueIndex = tagIds2.size();
                dataSets2.clear();
                if (linechart2.getLineData() != null) {
                    linechart2.clear();
                    linechart2.notifyDataSetChanged();
                }
                if (!isFirst) {
                    assert mPresenter != null;
                    mPresenter.loadVagueHistoryGraph(Integer.parseInt(tagIds2.get(0)), startDate2, endDate2);
                }

                rbDay1.setTextColor(Color.GREEN);
                rbWeek1.setTextColor(Color.BLACK);
                rbMonth1.setTextColor(Color.BLACK);
                break;
            case R.id.rb_week1:
                xValues2.clear();
                type2 = 1;
                linechart2.setVisibility(View.INVISIBLE);
                pbLoading1.setVisibility(View.VISIBLE);
                btnPre2.setText("上一周");
                btnNext2.setText("下一周");
                btnNext2.setEnabled(false);
                btnPre2.setEnabled(false);
                for (int i = 0; i < rgLoad.getChildCount(); i++) {
                    rgLoad.getChildAt(i).setClickable(false);
                }
                endDate2 = DateUtils.getStringDate();
                startDate2 = DateUtils.startWeek(DateUtils.getNow()) + " 00:00:00";
                vagueIndex = tagIds2.size();
                dataSets2.clear();
                if (linechart2.getLineData() != null) {
                    linechart2.clear();
                    linechart2.notifyDataSetChanged();
                }
                if (!isFirst) {
                    assert mPresenter != null;
                    mPresenter.loadVagueHistoryGraph(Integer.parseInt(tagIds2.get(0)), startDate2, endDate2);
                }
                rbDay1.setTextColor(Color.BLACK);
                rbWeek1.setTextColor(Color.GREEN);
                rbMonth1.setTextColor(Color.BLACK);
                break;
            case R.id.rb_month1:
                xValues2.clear();
                type2 = 2;
                linechart2.setVisibility(View.INVISIBLE);
                pbLoading1.setVisibility(View.VISIBLE);
                btnPre2.setText("上一月");
                btnNext2.setText("下一月");
                btnNext2.setEnabled(false);
                btnPre2.setEnabled(false);
                for (int i = 0; i < rgLoad.getChildCount(); i++) {
                    rgLoad.getChildAt(i).setClickable(false);
                }
                endDate2 = DateUtils.getStringDateMiddle() + ":00:00";
                startDate2 = DateUtils.timeStamp2Date(Long.parseLong(DateUtils.getSupportBeginDayofMonth(DateUtils.strToDate(endDate2))), null);
                vagueIndex = tagIds2.size();
                dataSets2.clear();
                if (linechart2.getLineData() != null) {
                    linechart2.clear();
                    linechart2.notifyDataSetChanged();
                }
                if (!isFirst) {
                    assert mPresenter != null;
                    mPresenter.loadVagueHistoryGraph(Integer.parseInt(tagIds2.get(0)), startDate2, endDate2);
                }
                rbDay1.setTextColor(Color.BLACK);
                rbWeek1.setTextColor(Color.BLACK);
                rbMonth1.setTextColor(Color.GREEN);
                break;
            default:
                break;

        }
    }


    @OnClick({R.id.btn_pre1, R.id.btn_next1, R.id.btn_pre2, R.id.btn_next2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_pre1:
                xValues1.clear();
                xValues11.clear();
                noDataTagIds.clear();
                yValues1.clear();
                linechart.setVisibility(View.INVISIBLE);
                pbLoading.setVisibility(View.VISIBLE);
                for (int i = 0; i < rgMax.getChildCount(); i++) {
                    rgMax.getChildAt(i).setClickable(false);
                }
                switch (type1) {
                    case 0:
                        endDate1 = DateUtils.getPreOrNextDate(DateUtils.strToDate(endDate1), true) + " 23:59:59";
                        startDate1 = endDate1.split(" ")[0] + " 00:00:00";
                        break;
                    case 1:
                        String preWeek = DateUtils.getPreOrNextWeek(endDate1, true);
                        endDate1 = preWeek.split(",")[1];
                        startDate1 = preWeek.split(",")[0];
                        break;
                    case 2:
                        Date month = DateUtils.getPreOrNextMonth(DateUtils.strToDate(endDate1), true);
                        startDate1 = DateUtils.timeStamp2Date(Long.parseLong(DateUtils.getSupportBeginDayofMonth(month)), null);
                        endDate1 = DateUtils.timeStamp2Date(Long.parseLong(DateUtils.getSupportEndDayofMonth(month)), null);
                        break;
                    default:
                        break;
                }
                commenIndex = tagIds2.size();
                dataSets1.clear();
                if (linechart.getLineData() != null) {
                    linechart.clear();
                    linechart.notifyDataSetChanged();
                }
                assert mPresenter != null;
                mPresenter.loadMonitorHistoryGraph(pid, Integer.parseInt(tagIds2.get(0)), startDate1, endDate1);
                tvDate1.setVisibility(View.VISIBLE);
                tvDate1.setText(startDate1.substring(0, 10) + "--" + endDate1.substring(0, 10));
                break;
            case R.id.btn_next1:
                if (endDate1.contains(DateUtils.getStringDateShort())) {
                    ToastUtils.showShort("当前日期是今天，没有下一个数据了！");
                    return;
                }
                xValues1.clear();
                xValues11.clear();
                noDataTagIds.clear();
                yValues1.clear();
                linechart.setVisibility(View.INVISIBLE);
                pbLoading.setVisibility(View.VISIBLE);
                for (int i = 0; i < rgMax.getChildCount(); i++) {
                    rgMax.getChildAt(i).setClickable(false);
                }
                switch (type1) {
                    case 0:
                        endDate1 = DateUtils.getPreOrNextDate(DateUtils.strToDate(endDate1), false);
                        startDate1 = endDate1 + " 00:00:00";
                        if (DateUtils.getStringDateShort().equals(endDate1)) {
                            endDate1 = DateUtils.getStringDate();
                        } else {
                            endDate1 = endDate1 + " 23:59:59";
                        }
                        break;
                    case 1:
                        String preWeek = DateUtils.getPreOrNextWeek(endDate1, false);
                        endDate1 = preWeek.split(",")[1];
                        startDate1 = preWeek.split(",")[0];
                        if (DateUtils.compare2Date(DateUtils.dateToStr(DateUtils.getNow()), endDate1.split(" ")[0]) < 0) {
                            endDate1 = DateUtils.getStringDate();
                        }

                        break;
                    case 2:
                        Date month = DateUtils.getPreOrNextMonth(DateUtils.strToDate(endDate1), false);
                        startDate1 = DateUtils.timeStamp2Date(Long.parseLong(DateUtils.getSupportBeginDayofMonth(month)), null);
                        endDate1 = DateUtils.timeStamp2Date(Long.parseLong(DateUtils.getSupportEndDayofMonth(month)), null);
                        if (DateUtils.compare2Date(DateUtils.dateToStr(DateUtils.getNow()), endDate1.split(" ")[0]) < 0) {
                            endDate1 = DateUtils.getStringDateMiddle() + ":00:00";
                        }
                        break;
                    default:
                        break;
                }
                commenIndex = tagIds2.size();
                dataSets1.clear();
                if (linechart.getLineData() != null) {
                    linechart.clear();
                    linechart.notifyDataSetChanged();
                }
                assert mPresenter != null;
                mPresenter.loadMonitorHistoryGraph(pid, Integer.parseInt(tagIds2.get(0)), startDate1, endDate1);
                tvDate1.setVisibility(View.VISIBLE);
                tvDate1.setText(startDate1.substring(0, 10) + "--" + endDate1.substring(0, 10));
                break;
            case R.id.btn_pre2:
                xValues2.clear();
                linechart2.setVisibility(View.INVISIBLE);
                pbLoading1.setVisibility(View.VISIBLE);
                for (int i = 0; i < rgLoad.getChildCount(); i++) {
                    rgLoad.getChildAt(i).setClickable(false);
                }
                switch (type2) {
                    case 0:
                        endDate2 = DateUtils.getPreOrNextDate(DateUtils.strToDate(endDate2), true) + " 23:59:59";
                        startDate2 = endDate2.split(" ")[0] + " 00:00:00";
                        break;
                    case 1:
                        String preWeek = DateUtils.getPreOrNextWeek(endDate2, true);
                        endDate2 = preWeek.split(",")[1];
                        startDate2 = preWeek.split(",")[0];
                        break;
                    case 2:
                        Date month = DateUtils.getPreOrNextMonth(DateUtils.strToDate(endDate2), true);
                        startDate2 = DateUtils.timeStamp2Date(Long.parseLong(DateUtils.getSupportBeginDayofMonth(month)), null);
                        endDate2 = DateUtils.timeStamp2Date(Long.parseLong(DateUtils.getSupportEndDayofMonth(month)), null);
                        break;
                    default:
                        break;
                }
                vagueIndex = tagIds2.size();
                dataSets2.clear();
                if (linechart2.getLineData() != null) {
                    linechart2.clear();
                    linechart2.notifyDataSetChanged();
                }
                assert mPresenter != null;
                mPresenter.loadVagueHistoryGraph(Integer.parseInt(tagIds2.get(0)), startDate2, endDate2);
                tvDate2.setVisibility(View.VISIBLE);
                tvDate2.setText(startDate2.substring(0, 10) + "--" + endDate2.substring(0, 10));
                break;
            case R.id.btn_next2:
                if (endDate2.contains(DateUtils.getStringDateShort())) {
                    ToastUtils.showShort("当前日期是今天，没有下一个数据了！");
                    return;
                }
                xValues2.clear();
                linechart2.setVisibility(View.INVISIBLE);
                pbLoading1.setVisibility(View.VISIBLE);
                for (int i = 0; i < rgLoad.getChildCount(); i++) {
                    rgLoad.getChildAt(i).setClickable(false);
                }
                switch (type2) {
                    case 0:
                        endDate2 = DateUtils.getPreOrNextDate(DateUtils.strToDate(endDate2), false);
                        startDate2 = endDate2 + " 00:00:00";
                        if (DateUtils.getStringDateShort().equals(endDate2)) {
                            endDate2 = DateUtils.getStringDate();
                        } else {
                            endDate2 = endDate2 + " 23:59:59";
                        }
                        break;
                    case 1:
                        String preWeek = DateUtils.getPreOrNextWeek(endDate2, false);
                        endDate2 = preWeek.split(",")[1];
                        startDate2 = preWeek.split(",")[0];
                        if (DateUtils.compare2Date(DateUtils.dateToStr(DateUtils.getNow()), endDate2.split(" ")[0]) < 0) {
                            endDate2 = DateUtils.getStringDate();
                        }
                        break;
                    case 2:
                        Date month = DateUtils.getPreOrNextMonth(DateUtils.strToDate(endDate2), false);
                        startDate2 = DateUtils.timeStamp2Date(Long.parseLong(DateUtils.getSupportBeginDayofMonth(month)), null);
                        endDate2 = DateUtils.timeStamp2Date(Long.parseLong(DateUtils.getSupportEndDayofMonth(month)), null);
                        if (DateUtils.compare2Date(DateUtils.dateToStr(DateUtils.getNow()), endDate2.split(" ")[0]) < 0) {
                            endDate2 = DateUtils.getStringDateMiddle() + ":00:00";
                        }
                        tvDate2.setVisibility(View.VISIBLE);
                        tvDate2.setText(startDate2.substring(0, 10) + "--" + endDate2.substring(0, 10));
                        break;
                    default:
                        break;
                }
                vagueIndex = tagIds2.size();
                dataSets2.clear();
                if (linechart2.getLineData() != null) {
                    linechart2.clear();
                    linechart2.notifyDataSetChanged();
                }
                assert mPresenter != null;
                mPresenter.loadVagueHistoryGraph(Integer.parseInt(tagIds2.get(0)), startDate2, endDate2);
                break;
            default:
                break;
        }
    }

    /**
     * 设置非介入曲线数据（下面那条）
     * @param vagueHistoryGraphBeans
     */
    public void setVagueHistoryGraphBean(List<VagueHistoryGraphBean> vagueHistoryGraphBeans) {
        if (vagueHistoryGraphBeans != null) {
            LinkedHashMap<String, String> datas = new LinkedHashMap<>();
            if (vagueHistoryGraphBeans.size() > 0 && vagueIndex > 0) {
                String recTime = vagueHistoryGraphBeans.get(0).getRecTime();
                String temp = vagueHistoryGraphBeans.get(0).getTemp();
                String realRecTime = vagueHistoryGraphBeans.get(0).getRealRecTime();
                List<String> keys = Arrays.asList(recTime.split(","));
                List<String> values = Arrays.asList(temp.split(","));
                List<String> xValues = Arrays.asList(realRecTime.split(","));
                xValues2.add(xValues);
                for (int i = 0; i < keys.size(); i++) {
                    if (values.get(i).equals("-")) {
//                        datas.put(keys.get(i), "0");
                        //采用新方式
                        for (int j = i; j < values.size(); j++) {
                            if (!values.get(j).equals("-")){
                                datas.put(keys.get(i), values.get(j));
                                break;
                            }
                        }
                    } else {
                        datas.put(keys.get(i), values.get(i));
                    }
                }
                dataSets2.add(datas);
            }
            vagueIndex--;
            if (vagueIndex > 0) {
                assert mPresenter != null;
                mPresenter.loadVagueHistoryGraph(Integer.parseInt(tagIds2.get(tagIds2.size() - vagueIndex)), startDate2, endDate2);
            } else {
                if (linechart2.getData() != null) {
                    linechart2.clearValues();
                }
                linechart2.setData(mPresenter.transform_data(dataSets2, type2, colors));
                linechart2.notifyDataSetChanged();
                linechart2.setVisibility(View.VISIBLE);
                pbLoading1.setVisibility(View.GONE);
                for (int i = 0; i < rgLoad.getChildCount(); i++) {
                    rgLoad.getChildAt(i).setClickable(true);
                }
                mv1.setXValue(xValues2);
                mv1.setDateType(type2);
            }
        } else {
            pbLoading1.setVisibility(View.GONE);
            for (int i = 0; i < rgLoad.getChildCount(); i++) {
                rgLoad.getChildAt(i).setClickable(true);
            }
            linechart2.setVisibility(View.VISIBLE);
        }
        tvDate2.setVisibility(View.VISIBLE);
        tvDate2.setText(startDate2.substring(0, 10) + "--" + endDate2.substring(0, 10));
        btnNext2.setEnabled(true);
        btnPre2.setEnabled(true);
    }

    /**
     * 设置常规曲线数据（上面那条）
     * @param hisData
     */
    public void setHisData(List<VagueHistoryGraphBean> hisData) {
        if (hisData != null) {
            LinkedHashMap<String, String> datas = new LinkedHashMap<>();
            if (hisData.size() > 0 && commenIndex > 0) {
                String recTime = hisData.get(0).getRecTime();
                String temp = hisData.get(0).getTemp();
                String realRecTime = hisData.get(0).getRealRecTime();
                List<String> keys = Arrays.asList(recTime.split(","));
                List<String> values = Arrays.asList(temp.split(","));
                List<String> xValues = Arrays.asList(realRecTime.split(","));
                xValues1.add(xValues);
                xValues11.add(keys);
                yValues1.add(values);
                for (int i = 0; i < keys.size(); i++) {
                    if (values.get(i).equals("-")) {
//                        datas.put(keys.get(i), "0");
                        //采用新方式
                        for (int j = i; j < values.size(); j++) {
                            if (!values.get(j).equals("-")){
                                datas.put(keys.get(i), values.get(j));
                                break;
                            }
                        }
                    } else {
                        datas.put(keys.get(i), values.get(i));
                    }
                }
                dataSets1.add(datas);

            } else {
                if (commenIndex > 0) {
                    noDataTagIds.add(tagIds2.get(tagIds2.size() - commenIndex));
                }
            }
            commenIndex--;
            if (commenIndex > 0) {
                assert mPresenter != null;
                mPresenter.loadMonitorHistoryGraph(pid, Integer.parseInt(tagIds2.get(tagIds2.size() - commenIndex)), startDate1, endDate1);
            } else {
                //设置数据源
                barChartWebView1.setDataSource(new TEChartWebView.DataSource() {
                    @Override
                    public GsonOption markChartOptions() {
                        return getLineAndBarChartOption(monitorBeanList2, xValues11.get(mPresenter.compareList(xValues11)), yValues1);
                    }
                });
                if (linechart.getData() != null) {
                    linechart.clearValues();
                }
                mv.setXValue(xValues1);
                mv.setDateType(type1);
                linechart.setData(mPresenter.transform_data(dataSets1, type1, colors));
                linechart.notifyDataSetChanged();
                linechart.setVisibility(View.VISIBLE);
                pbLoading.setVisibility(View.GONE);
                for (int i = 0; i < rgMax.getChildCount(); i++) {
                    rgMax.getChildAt(i).setClickable(true);
                }


            }
        } else {
            pbLoading.setVisibility(View.GONE);
            for (int i = 0; i < rgMax.getChildCount(); i++) {
                rgMax.getChildAt(i).setClickable(true);
            }
            linechart.setVisibility(View.VISIBLE);
        }
        tvDate1.setVisibility(View.VISIBLE);
        tvDate1.setText(startDate1.substring(0, 10) + "--" + endDate1.substring(0, 10));
        btnNext1.setEnabled(true);
        btnPre1.setEnabled(true);

    }

    /**
     * echarts控件 此处不用
     * 根据https://mvnrepository.com/artifact/com.github.abel533/ECharts
     * 结合http://echarts.baidu.com/examples.html官方实例
     * 配置json数据
     *
     * @return
     */
    private GsonOption getLineAndBarChartOption(List<TempMonitorBean> monitorBeanList2, List xAxisValues, List<List<String>> yAxisValues) {
        //http://echarts.baidu.com/echarts2/doc/example/mix1.html
        GsonOption option = new GsonOption();
        //title
//        String text = "";
//        String subText = "";
//        option.title(text, subText);
        //tooltip
        Tooltip tooltip = new Tooltip();
        tooltip.trigger(Trigger.axis);
        option.tooltip(tooltip);
        //toolbox
        Toolbox toolbox = new Toolbox();
        toolbox.show(false);
        Map<String, Feature> feature = new HashMap<String, Feature>();
        feature.put("mark", new Feature().show(true));
        feature.put("dataView", new DataView().show(true).readOnly(false));
        feature.put("magicType", new MagicType(Magic.line, Magic.bar).show(true));
        feature.put("restore", new Restore().show(true));
        feature.put("saveAsImage", new SaveAsImage().show(false));
        toolbox.setFeature(feature);
        option.toolbox(toolbox);
        //calculable
        option.setCalculable(true);
        //legend
        List<String> legends = new ArrayList<>();
        for (int i = 0; i < monitorBeanList2.size(); i++) {
            if (noDataTagIds.size() > 0) {
                for (String tagId : noDataTagIds) {
                    if (!monitorBeanList2.get(i).getTagid().equals(tagId)) {
                        legends.add(monitorBeanList2.get(i).getpName());
                    }
                }
            } else {
                legends.add(monitorBeanList2.get(i).getpName());
            }
        }
        String legend1 = "蒸发量";
        String legend2 = "降水量";
        String legend3 = "平均温度";
        Legend legend = new Legend();
//        legend.data(legend1, legend2, legend3);
        TextStyle textStyle = new TextStyle();
        textStyle.setColor("#008ACD");
        textStyle.setFontSize(8);
        legend.setTextStyle(textStyle);
        legend.data(legends);
        option.legend(legend);
        //xAxis
        List<Axis> xAxis = new ArrayList<Axis>();
        CategoryAxis categoryAxis = new CategoryAxis();
        {
            List xAxisValues1 = new ArrayList();
            for (int i = 0; i < xAxisValues.size(); i++) {
                if (type1 == 0) {
                    xAxisValues1.add(((String) xAxisValues.get(i)).substring(((String) xAxisValues.get(i)).lastIndexOf("/") + 3));
                } else {
                    xAxisValues1.add(((String) xAxisValues.get(i)).substring(((String) xAxisValues.get(i)).indexOf("/") + 1));
                }
            }
            categoryAxis.setData(xAxisValues1);
        }
        xAxis.add(categoryAxis);
        option.xAxis(xAxis);
        //yAxis
        List<Axis> yAxis = new ArrayList<Axis>();

        {
            ValueAxis valueAxis = new ValueAxis();
//            valueAxis.name("温度°C");
            valueAxis.axisLabel(new AxisLabel().formatter("{value}"));
            yAxis.add(valueAxis);
        }
        option.yAxis(yAxis);
        //series
        List<Series> series = new ArrayList<Series>();
        for (int i = 0; i < yAxisValues.size(); i++) {
            Line bar = new Line();
            bar.name(legends.get(i)).type(SeriesType.line).yAxisIndex(0).itemStyle().normal().lineStyle().setWidth(1);
            List data = new ArrayList();
            data.addAll(yAxisValues.get(i));
            bar.setData(data);
            series.add(bar);
        }

        option.series(series);
        //
        return option;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        setNull();
    }

    private void setNull() {
        if (monitorBeanList != null && monitorBeanList.size() > 0) {
            monitorBeanList.clear();
            monitorBeanList = null;
        }
        if (monitorBeanList2 != null && monitorBeanList2.size() > 0) {
            monitorBeanList2.clear();
            monitorBeanList2 = null;
        }
        if (tagIds != null && tagIds.size() > 0) {
            tagIds.clear();
            tagIds = null;
        }
        if (tagIds2 != null && tagIds2.size() > 0) {
            tagIds2.clear();
            tagIds2 = null;
        }
        if (dataSets1 != null && dataSets1.size() > 0) {
            dataSets1.clear();
            dataSets1 = null;
        }
        if (dataSets2 != null && dataSets2.size() > 0) {
            dataSets2.clear();
            dataSets2 = null;
        }
        if (xValues1 != null && xValues1.size() > 0) {
            xValues1.clear();
            xValues1 = null;
        }
        if (xValues2 != null && xValues2.size() > 0) {
            xValues2.clear();
            xValues2 = null;
        }
        if (linechart != null && linechart.getLineData() != null) {
            linechart.clear();
            linechart.notifyDataSetChanged();
        }
        if (linechart2 != null && linechart2.getLineData() != null) {
            linechart2.clear();
            linechart2.notifyDataSetChanged();
        }
        if (mv != null) {
            mv.reset();
        }
        if (mv1 != null) {
            mv1.reset();
        }
        if (barChartWebView1 != null) {
            // 如果先调用destroy()方法，则会命中if (isDestroyed()) return;这一行代码，需要先onDetachedFromWindow()，再
            // destory()
            barChartWebView1.clearCache(true);
            barChartWebView1.setWebChromeClient(null);
            barChartWebView1.setWebViewClient(null);
            ViewParent parent = barChartWebView1.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(barChartWebView1);
            }

            barChartWebView1.stopLoading();
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            barChartWebView1.getSettings().setJavaScriptEnabled(false);
            barChartWebView1.clearHistory();
            barChartWebView1.clearView();
            barChartWebView1.removeAllViews();


            try {
                barChartWebView1.destroy();
            } catch (Throwable ex) {

            }
        }
    }


    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        if (lastPerformedGesture != ChartTouchListener.ChartGesture.SINGLE_TAP) {
//            linechart.highlightValues(null);
//            linechart2.highlightValues(null);
        }
    }

    @Override
    public void onChartLongPressed(MotionEvent me) {

    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {

    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {

    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {

    }

    //设置错误信息
    public void setError(int type) {
        if (type == 1) {
            pbLoading.setVisibility(View.GONE);
            for (int i = 0; i < rgMax.getChildCount(); i++) {
                rgMax.getChildAt(i).setClickable(true);
            }
            linechart.setVisibility(View.VISIBLE);
        } else if (type == 2) {
            pbLoading1.setVisibility(View.GONE);
            for (int i = 0; i < rgLoad.getChildCount(); i++) {
                rgLoad.getChildAt(i).setClickable(true);
            }
            linechart2.setVisibility(View.VISIBLE);
        } else if (type == 3) {
            emptyLayout.setErrorType(EmptyLayout.NODATA);
        }

    }

    /**
     * 刷新曲线（echarts 控件使用，此处没有用到）
     */
    public void reFreshLine() {
        ViewParent parent = barChartWebView1.getParent();
        if (parent != null) {
            ((ViewGroup) parent).removeView(barChartWebView1);
        }

        barChartWebView1.stopLoading();
        barChartWebView1 = null;
        barChartWebView1 = new TEChartWebView(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ll_barChartWebView1.getLayoutParams());
        barChartWebView1.setLayoutParams(lp);
        ll_barChartWebView1.addView(barChartWebView1);
        barChartWebView1.setDataSource(new TEChartWebView.DataSource() {
            @Override
            public GsonOption markChartOptions() {
                return getLineAndBarChartOption(monitorBeanList2, xValues11.get(mPresenter.compareList(xValues11)), yValues1);
            }
        });
    }
}
