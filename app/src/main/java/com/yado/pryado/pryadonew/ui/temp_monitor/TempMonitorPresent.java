package com.yado.pryado.pryadonew.ui.temp_monitor;

import android.graphics.Matrix;
import android.text.TextUtils;
import android.view.View;

import com.github.abel533.echarts.Event;
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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.BarLineChartTouchListener;
import com.yado.pryado.pryadonew.MyApplication;
import com.yado.pryado.pryadonew.R;
import com.yado.pryado.pryadonew.base.BasePresenter;
import com.yado.pryado.pryadonew.bean.DeviceDetailBean2;
import com.yado.pryado.pryadonew.bean.DeviceInfoListBean;
import com.yado.pryado.pryadonew.bean.RoomListBean;
import com.yado.pryado.pryadonew.bean.TypeBean;
import com.yado.pryado.pryadonew.bean.VagueHistoryGraphBean;
import com.yado.pryado.pryadonew.bean.VagueRealTimeBean;
import com.yado.pryado.pryadonew.net.INetListener;
import com.yado.pryado.pryadonew.ui.widgit.EmptyLayout;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.ResponseBody;

public class TempMonitorPresent extends BasePresenter<TempMonitorContract.View, TempMonitorModel> implements TempMonitorContract.Presenter {

    /**
     * 注入到Fragment
     */
    @Inject
    public TempMonitorPresent() {
    }

    /**
     * 获取站室
     */
    @Override
    public void getRoomList() {
        mModel.getRoomList(new INetListener<Object, Throwable, Object>() {
            @Override
            public void success(Object o) {
                RoomListBean roomListBean = (RoomListBean) o;
                ((TempMonitorActivity) mView).setRooms(roomListBean.getRows());
            }

            @Override
            public void failed(Throwable throwable) {

            }

            @Override
            public void loading(Object o) {

            }
        });
    }

    /**
     * 获取报警状态
     */
    @Override
    public void loadVagueAlarm(int pid, String alarmConfirm, String startDate, String endDate) {
        mModel.loadVagueAlarm(pid, alarmConfirm, startDate, endDate, new INetListener<Object, Throwable, Object>() {
            @Override
            public void success(Object o) {

            }

            @Override
            public void failed(Throwable throwable) {

            }

            @Override
            public void loading(Object o) {

            }
        });
    }

    /**
     * 获取站室历史数据
     */
    @Override
    public void loadMonitorHistoryGraph(int pid, int tagId, String startDate, String endDate) {
        mModel.loadMonitorHistoryGraph(pid, tagId, startDate, endDate, new INetListener<Object, Throwable, Object>() {
            @Override
            public void success(Object o) {
                List<VagueHistoryGraphBean> vagueHistoryGraphBean = (List<VagueHistoryGraphBean>) o;
                ((TempMonitorAssessFragment) mView).setHisData(vagueHistoryGraphBean);

            }

            @Override
            public void failed(Throwable throwable) {
                ((TempMonitorAssessFragment) mView).setError(2);
            }

            @Override
            public void loading(Object o) {

            }
        });
    }

    /**
     * 获取非介入式测温历史曲线数据
     *
     * @param tagID
     * @param startDate
     * @param endDate
     */
    @Override
    public void loadVagueHistoryGraph(int tagID, String startDate, String endDate) {
        mModel.loadVagueHistoryGraph(tagID, startDate, endDate, new INetListener<Object, Throwable, Object>() {
            @Override
            public void success(Object o) {

                List<VagueHistoryGraphBean> vagueHistoryGraphBean = (List<VagueHistoryGraphBean>) o;
                ((TempMonitorAssessFragment) mView).setVagueHistoryGraphBean(vagueHistoryGraphBean);
            }

            @Override
            public void failed(Throwable throwable) {
                ((TempMonitorAssessFragment) mView).setError(2);
            }

            @Override
            public void loading(Object o) {

            }
        });
    }

    /**
     * 获取详情
     */
    @Override
    public void getDetail(String did) {
        mModel.getDetail(did, new INetListener<Object, Throwable, Object>() {
            @Override
            public void success(Object o) {
                DeviceDetailBean2 detailBean = (DeviceDetailBean2) o;
                ((TempMonitorAssessFragment) mView).setDeviceDetail(detailBean);
            }

            @Override
            public void failed(Throwable throwable) {
                ((TempMonitorAssessFragment) mView).setError(3);
            }

            @Override
            public void loading(Object o) {

            }
        });
    }

    /**
     * 获取非介入式测温实时数据
     *
     * @param tagId
     * @param did
     * @param pid
     */
    @Override
    public void loadVagueRealTime(int tagId, int did, int pid) {
        mModel.loadVagueRealTime(tagId, did, pid, new INetListener<Object, Throwable, Object>() {
            @Override
            public void success(Object o) {
                VagueRealTimeBean vagueRealTimeBean = (VagueRealTimeBean) o;
                ((TempMonitorAssessFragment) mView).setVagueRealTimeBean(vagueRealTimeBean);
            }

            @Override
            public void failed(Throwable throwable) {

            }

            @Override
            public void loading(Object o) {

            }
        });
    }

    /**
     * 获取站室状态
     * @param pid
     * @param did
     * @param type
     */
    @Override
    public void getStatusData(int pid, int did, int type) {
        mModel.getStatusData(pid, did, type, new INetListener<Object, Throwable, Object>() {
            @Override
            public void success(Object o) {
                ResponseBody responseBody = (ResponseBody) o;
                try {
                    ((TempMonitorPlanFragment) mView).setStatusData(responseBody.string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable throwable) {

            }

            @Override
            public void loading(Object o) {

            }
        });
    }

    /**
     * 获取站室平面图是否是常规图
     *
     * @param pid
     */
    @Override
    public void getGraphType(int pid) {
        mModel.getGraphType(pid, new INetListener<Object, Throwable, Object>() {
            @Override
            public void success(Object o) {
                TypeBean typeBean = (TypeBean) o;
                if (typeBean.getHymannew() != null && typeBean.getHymannew().size() > 0) {
                    ((TempMonitorPlanFragment) mView).setTypeBean(typeBean);
                    ((TempMonitorPlanFragment) mView).setShowOrHide(Integer.parseInt(typeBean.getHymannew().get(0).getTotal2()) > 0);
                }
            }

            @Override
            public void failed(Throwable throwable) {

            }

            @Override
            public void loading(Object o) {

            }
        });
    }

    /**
     * 获取配电房所有的设备列表
     * @param pid
     * @param pagesize
     * @param pageindex
     */
    @Override
    public void getDeviceInfoList(int pid, int pagesize, int pageindex) {
        mModel.getDeviceInfoList(pid, pagesize, pageindex, new INetListener<Object, Throwable, Object>() {
            @Override
            public void success(Object o) {
                DeviceInfoListBean listBean = (DeviceInfoListBean) o;
                ((TempMonitorPlanFragment) mView).setDeviceInfoListBean(listBean);
            }

            @Override
            public void failed(Throwable throwable) {

            }

            @Override
            public void loading(Object o) {

            }
        });
    }


    /**
     * 设置折线图样式
     *
     * @param mChart3
     */
    public void setLineType(LineChart mChart3, float scaleX) {
        mChart3.setDrawGridBackground(false);
//        mChart3.setBackgroundResource(R.drawable.shape_white2);
        mChart3.setDescription("");
        mChart3.setNoDataTextDescription(MyApplication.getInstance().getString(R.string.no_data));
        mChart3.setTouchEnabled(true);
        mChart3.setDragEnabled(true);
        mChart3.setScaleEnabled(true);
        mChart3.setPinchZoom(true);
        mChart3.setScaleMinima(scaleX / 7, 1f);
//        mChart3.zoom(scaleX > 12 ? 3f : 1f,1,0,0);//Zooms in放大Zooms out缩小；
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
        if (scaleX <= 12) {//小于12个点时矩阵重置；7的时候不放大，
            BarLineChartTouchListener listener = mChart3.getBarLineChartTouchListener();
            if (listener != null) {
                Matrix mMatrix = new Matrix();
                mMatrix.setValues(new float[]{1.0f * scaleX / 7, 0.0f, 0.0f, 0.0f, 1.0f, -0.0f, 0.0f, 0.0f, 1.0f});
                listener.setmMatrix(mChart3.getViewPortHandler().refresh(mMatrix, mChart3, true));
            }
        }
    }

    /**
     * 转换曲线数据
     * @param hisData
     * @param type
     * @param colors
     * @return
     */
    public LineData transform_data(List<LinkedHashMap<String, String>> hisData, int type, int[] colors) {

//        ArrayList<String> xVals = new ArrayList<>();
        List<List<String>> xVal = new ArrayList<>();

//        ArrayList<Entry> yVals = new ArrayList<>();//第一条折线；
//        ArrayList<Entry> yVals2 = new ArrayList<>();//第2条折线；
//        ArrayList<Entry> yVals3 = new ArrayList<>();//第3条折线；
//        ArrayList<Entry> yVals4 = new ArrayList<>();//第4条折线；
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        int i, a = 0, s = 0, d = 0;
//        LineDataSet set1 = new LineDataSet(yVals, "");
//        LineDataSet set2 = new LineDataSet(yVals2, "");
//        LineDataSet set3 = new LineDataSet(yVals3, "");
//        LineDataSet set4 = new LineDataSet(yVals4, "");
        for (int j = 0; j < hisData.size(); j++) {
            ArrayList<String> xVals = new ArrayList<>();
            ArrayList<Entry> y = new ArrayList<>();
            LineDataSet set = new LineDataSet(y, "");
            dataSets.add(set);
            i = 0;
            if (hisData.get(j) != null) {
                for (Map.Entry<String, String> entry : hisData.get(j).entrySet()) {
                    if (!TextUtils.isEmpty(entry.getValue())) {
//                        if (!entry.getValue().equals("0")) {
//                            y.add(new Entry(Float.valueOf(entry.getValue()), i++));
//                            xVals.add(getXvalue(entry.getKey(), type));
//                        } else {
//                            i++;
//                            xVals.add(getXvalue(entry.getKey(), type));
//                        }
                        y.add(new Entry(Float.valueOf(entry.getValue()), i++));
                        xVals.add(getXvalue(entry.getKey(), type));
//                        if (j == 0) {
//                            xVals.add(getXvalue(entry.getKey(), type));
//                        }
                    }
                    xVal.add(xVals);
                }
            }
        }
//        for (int j = 0; j < hisData.size(); j++) {
//            switch (j) {
//                case 0:
//                    dataSets.add(set1);
//                    break;
//                case 1:
//                    dataSets.add(set2);
//                    break;
//                case 2:
//                    dataSets.add(set3);
//                    break;
//                case 3:
//                    dataSets.add(set4);
//                    break;
//                default:
//                    break;
//            }
//            if (hisData.get(j) != null) {
//                for (Map.Entry<String, String> entry : hisData.get(j).entrySet()) {
//                    if (!TextUtils.isEmpty(entry.getValue())) {
//                        switch (j) {
//                            case 0:
//                                xVals.add(getXvalue(entry.getKey(), type));
//                                yVals.add(new Entry(Float.valueOf(entry.getValue()), i++));
//                                break;
//                            case 1:
//                                yVals2.add(new Entry(Float.valueOf(entry.getValue()), a++));
//                                break;
//                            case 2:
//                                yVals3.add(new Entry(Float.valueOf(entry.getValue()), s++));
//                                break;
//                            case 3:
//                                yVals4.add(new Entry(Float.valueOf(entry.getValue()), d++));
//                                break;
//                            default:
//                                break;
//                        }
//
//                    }
//                }
//            }
//        }
//        if (type == 1 && xVals.size() > 0) {
//            for (int j = 0; j < xVals.size(); j++) {
//                xVals.set(j, xVals.get(j).substring(0, xVals.get(j).lastIndexOf("/") + 3));
//            }
//        }
        setParm(dataSets, colors);
        if (xVal.size() > 0) {
            List<String> xvals = xVal.get(compareList(xVal));
            return new LineData(xvals, dataSets);
        } else {
            return null;
        }
    }

    /**
     * 获取最大的List
     * @param lists
     * @return
     */
    public int compareList(List<List<String>> lists) {
        int k = 0;
        int maxIndex = lists.get(0).size();//定义最大值为该数组的第一个数
        for (int i = 0; i < lists.size(); i++) {
            if (maxIndex < lists.get(i).size()) {
                maxIndex = lists.get(i).size();
            }
        }
        for (int i = 0; i < lists.size(); i++) {
            if (maxIndex == lists.get(i).size()) {
                k = i;
            }
        }
        return k;
    }

    /**
     * 设置曲线参数
     * @param dataSets
     * @param colors
     */
    private void setParm(ArrayList<ILineDataSet> dataSets, int[] colors) {
        for (int i = 0; i < dataSets.size(); i++) {
            ((LineDataSet) dataSets.get(i)).enableDashedHighlightLine(10f, 5f, 0f);
            ((LineDataSet) dataSets.get(i)).setColor(colors[i]);//折线的颜色
//        set1.setCircleColor(Color.RED);//点的颜色
            ((LineDataSet) dataSets.get(i)).setLineWidth(1f);
            ((LineDataSet) dataSets.get(i)).setCircleSize(1f);
            ((LineDataSet) dataSets.get(i)).setDrawCircleHole(false);
            ((LineDataSet) dataSets.get(i)).setValueTextSize(9f);
            ((LineDataSet) dataSets.get(i)).setDrawFilled(false);//设置是否填充
            ((LineDataSet) dataSets.get(i)).setFillColor(colors[i]);//设置填充色
//            ((LineDataSet) dataSets.get(i)).enableDashedLine(10f, 5f, 0f);//设置虚线；
            ((LineDataSet) dataSets.get(i)).setFillAlpha(65);
            ((LineDataSet) dataSets.get(i)).setDrawValues(false);//隐藏点上的数值；
            ((LineDataSet) dataSets.get(i)).setDrawCircles(false);//在点上画圆 默认true
            ((LineDataSet) dataSets.get(i)).setCircleColor(colors[i]);
            ((LineDataSet) dataSets.get(i)).setDrawCubic(true);
            ((LineDataSet) dataSets.get(i)).setCubicIntensity(0.15f);
            ((LineDataSet) dataSets.get(i)).setHighlightLineWidth(1f);
            ((LineDataSet) dataSets.get(i)).setHighLightColor(MyApplication.getInstance().getResources().getColor(R.color.xxblue));

        }

    }

    /**
     * 获取X轴坐标
     * @param key
     * @param type
     * @return
     */
    private String getXvalue(String key, int type) {
        String result = "";
        switch (type) {
            case 0://day
                result = key.substring(key.lastIndexOf("/") + 3);
                break;
            case 1://week
                result = key.substring(0, key.lastIndexOf("/") + 3);
//                result = key;
                break;
            case 2://month
                result = key.substring(0, key.lastIndexOf(" "));
                break;
            default:
                result = key.substring(0, key.lastIndexOf(" "));
                break;
        }

        return result;
    }

}
