package com.yado.pryado.pryadonew.ui.device_detail;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.yado.pryado.pryadonew.R;
import com.yado.pryado.pryadonew.base.BasePresenter;
import com.yado.pryado.pryadonew.bean.AlertBean;
import com.yado.pryado.pryadonew.bean.BugList;
import com.yado.pryado.pryadonew.bean.DeviceDetailBean2;
import com.yado.pryado.pryadonew.bean.DeviceInfo;
import com.yado.pryado.pryadonew.bean.HisData;
import com.yado.pryado.pryadonew.bean.HisData2;
import com.yado.pryado.pryadonew.bean.PostBugResult;
import com.yado.pryado.pryadonew.bean.RoomListBean;
import com.yado.pryado.pryadonew.net.INetListener;
import com.yado.pryado.pryadonew.ui.alert.AlertActivity;
import com.yado.pryado.pryadonew.ui.alert.AlertFragment;
import com.yado.pryado.pryadonew.ui.device_detail.account_info.AccountInfoFragment;
import com.yado.pryado.pryadonew.ui.device_detail.danger.DangerFragment;
import com.yado.pryado.pryadonew.ui.device_detail.real_time_data.RealtimeDataFragment;
import com.yado.pryado.pryadonew.ui.widgit.CustomMarkerView;
import com.yado.pryado.pryadonew.ui.widgit.MyProgressDialog;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by OnexZgj on 2018/9/11:09:37.
 * des:
 */

public class DeviceDetailPresent extends BasePresenter<DeviceDetailContract.View, DeviceDetailModel> implements DeviceDetailContract.Presenter {

    /**
     * 注入到Fragment
     */
    @Inject
    public DeviceDetailPresent() {
    }

    @Override
    public void getDetail(String did, MyProgressDialog progressDialog) {
        mModel.getDetail(did,progressDialog, new INetListener<Object, Throwable, Object>() {
            @Override
            public void success(Object o) {
                DeviceDetailBean2 detailBean = (DeviceDetailBean2) o;
                ((DeviceDetailActivity)mView).setDeviceDetail(detailBean);
            }

            @Override
            public void failed(Throwable throwable) {
            }

            @Override
            public void loading(Object o) {

            }
        });
    }

    @Override
    public void getDetail2(String did1, String did2) {
        mModel.getDetail2(did1, did2, new INetListener<Object, Throwable, Object>() {
            @Override
            public void success(Object o) {
                List<DeviceDetailBean2.RealTimeParamsBean> realTimeParamsBeans = (List<DeviceDetailBean2.RealTimeParamsBean>) o;
                ((DeviceDetailActivity2)mView).setDataList(realTimeParamsBeans);
            }

            @Override
            public void failed(Throwable throwable) {

            }

            @Override
            public void loading(Object o) {

            }
        });
    }

    @Override
    public void getBugList(String did) {
        mModel.getBugList(did, new INetListener<Object, Throwable, Object>() {
            @Override
            public void success(Object o) {
                BugList bugList = (BugList) o;
                ((AccountInfoFragment) mView).setBuglist(bugList);
            }

            @Override
            public void failed(Throwable throwable) {

            }

            @Override
            public void loading(Object o) {

            }
        });
    }

    @Override
    public void getPointData(String dateTime, String hour, String pid, String did, String tagid) {
        mModel.getPointData(dateTime, hour, pid, did, tagid, new INetListener<Object, Throwable, Object>() {
            @Override
            public void success(Object o) {
                HisData2 hisData2 = (HisData2) o;
                ((RealtimeDataFragment) mView).setHisData(hisData2);
            }

            @Override
            public void failed(Throwable throwable) {
                ((RealtimeDataFragment) mView).showNoDataView();
            }

            @Override
            public void loading(Object o) {

            }
        });
    }

    @Override
    public void postNewBug(String pID, int did, String bugLocation, String bugDesc) {
        mModel.postNewBug(pID, did, bugLocation, bugDesc, new INetListener<Object, Throwable, Object>() {
            @Override
            public void success(Object o) {
                PostBugResult postBugResult = (PostBugResult) o;
                ((DangerFragment)mView).setPostBugResult(postBugResult);
            }

            @Override
            public void failed(Throwable throwable) {
//                ToastUtils.showShort(throwable.getMessage());
            }

            @Override
            public void loading(Object o) {

            }
        });
    }

    @Override
    public void getInfoByCode(String code) {
        mModel.getInfoByCode(code, new INetListener<Object, Throwable, Object>() {
            @Override
            public void success(Object o) {
                DeviceInfo deviceInfo = (DeviceInfo) o;
                ((DangerFragment)mView).setDeviceInfo(deviceInfo);
            }

            @Override
            public void failed(Throwable throwable) {
            }

            @Override
            public void loading(Object o) {

            }
        });
    }

    public String handle(String useDate) {
        String result = "";
        try {
            result = useDate.substring(0, 10);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return result;
    }

    /**
     * 把map类型转化为LineData；
     *
     * @param point
     * @param HisEnvData
     * @return
     */
    public LineData transform_data(String point, LinkedHashMap<String, String> HisEnvData, CustomMarkerView mv, TextView tv_no_data, LineChart lineChart, Context context) {
        ArrayList<String> envxVals = new ArrayList<String>();
        ArrayList<Entry> envyVals = new ArrayList<Entry>();
        if (HisEnvData == null) {
            return null;
        }
        int i = 0;
        for (Map.Entry<String, String> entry : HisEnvData.entrySet()) {
            if (!TextUtils.isEmpty(entry.getValue())) {
                try {
                    envyVals.add(new Entry(Float.valueOf(entry.getValue()), i++));
                    envxVals.add(entry.getKey());
                } catch (Exception e) {

                }
            }
        }
        mv.setXValue(envxVals);
//        LineDataSet set1 = new LineDataSet(envyVals, point);
//        LineDataSet set2 = new LineDataSet(devyVals, "环境温度");
        if (envyVals.size() <= 0) {
            tv_no_data.setVisibility(View.VISIBLE);
            lineChart.setVisibility(View.GONE);
            return null;
        }
        tv_no_data.setVisibility(View.GONE);
        lineChart.setVisibility(View.VISIBLE);
        LineDataSet set1 = new LineDataSet(envyVals, point);

        set1.enableDashedHighlightLine(10f, 5f, 0f);
        set1.setColor(context.getResources().getColor(R.color.xxblue));//折线的颜色
        set1.setLineWidth(1f);
        set1.setCircleSize(1f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setDrawFilled(true);//设置是否填充
        set1.setFillColor(context.getResources().getColor(R.color.xxblue));//设置填充色
        set1.setFillAlpha(65);
        set1.setDrawValues(false);//隐藏点上的数值；
        set1.setDrawCircles(false);//在点上画圆 默认true
        set1.setCircleColor(context.getResources().getColor(R.color.xxblue));
        set1.setDrawCubic(true);
        set1.setCubicIntensity(0.15f);
        set1.setHighlightLineWidth(1f);
        set1.setHighLightColor(context.getResources().getColor(R.color.blue2));
        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set1); // add the datasets
        return new LineData(envxVals, dataSets);
    }


    /**
     * 设置折线图样式
     *
     * @param mChart3
     */
    public void setLineType(LineChart mChart3, Context context) {
        mChart3.setDrawGridBackground(false);
        mChart3.setBackgroundResource(R.drawable.shape_white2);
        mChart3.setDescription("");
        mChart3.setNoDataTextDescription(context.getString(R.string.no_data));
        mChart3.setTouchEnabled(true);
        mChart3.setDragEnabled(true);
        mChart3.setScaleEnabled(true);
        mChart3.setPinchZoom(true);
        mChart3.setNoDataTextDescription(context.getString(R.string.no_recent_data));
        mChart3.setNoDataText("");
        XAxis xAxis3 = mChart3.getXAxis();
        xAxis3.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis3.setAvoidFirstLastClipping(true);

        LimitLine llXAxis = new LimitLine(10f, "Index 10");
        llXAxis.setLineWidth(2f);
        llXAxis.enableDashedLine(10f, 10f, 0f);
        llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        llXAxis.setTextSize(10f);

        YAxis leftAxis3 = mChart3.getAxisLeft();
        leftAxis3.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis3.setAxisMinValue(0f);
        leftAxis3.setStartAtZero(false);
        leftAxis3.enableGridDashedLine(10f, 10f, 0f);
        leftAxis3.setDrawLimitLinesBehindData(true);
        mChart3.getAxisRight().setEnabled(false);
    }
}
