package com.yado.pryado.pryadonew.ui.roomDetail;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Matrix;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.blankj.utilcode.util.ToastUtils;
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
import com.yado.pryado.pryadonew.MyConstants;
import com.yado.pryado.pryadonew.R;
import com.yado.pryado.pryadonew.base.BaseActivity;
import com.yado.pryado.pryadonew.base.BasePresenter;
import com.yado.pryado.pryadonew.bean.BugInfo;
import com.yado.pryado.pryadonew.bean.BugList;
import com.yado.pryado.pryadonew.bean.DeviceInfoListBean;
import com.yado.pryado.pryadonew.bean.RoomDetail;
import com.yado.pryado.pryadonew.bean.RoomListBean;
import com.yado.pryado.pryadonew.bean.TypeBean;
import com.yado.pryado.pryadonew.net.EadoUrl;
import com.yado.pryado.pryadonew.net.INetListener;
import com.yado.pryado.pryadonew.ui.riskAssessAndDetail.AssessActivity;
import com.yado.pryado.pryadonew.ui.riskAssessAndDetail.DangerDetailActivity;
import com.yado.pryado.pryadonew.ui.widgit.CustomMarkerView;
import com.yado.pryado.pryadonew.util.SharedPrefUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.ResponseBody;

/**
 * Created by OnexZgj on 2018/9/11:09:37.
 * des:
 */

public class RoomDetailPresent extends BasePresenter<RoomDetailContract.View, RoomDetailModel> implements RoomDetailContract.Presenter {

    /**
     * 注入到Fragment
     */
    @Inject
    public RoomDetailPresent() {
    }

    /**
     * 获取站室详情
     */
    @Override
    public void getRoomDetail(int pid, int ver) {
        mModel.getRoomDetail(pid, ver, new INetListener<Object, Throwable, Object>() {
            @Override
            public void success(Object o) {
                RoomDetail roomDetail = (RoomDetail) o;
                mView.setRoomDetail(roomDetail);
            }

            @Override
            public void failed(Throwable throwable) {
                mView.setError(throwable);
            }

            @Override
            public void loading(Object o) {

            }
        });
    }

    /**
     * 获取最高温的浮窗div
     * @param pid
     */
    @Override
    public void getMaxDiv(int pid) {
        mModel.getMaxDiv(pid, new INetListener<Object, Throwable, Object>() {
            @Override
            public void success(Object o) {
                ResponseBody responseBody = (ResponseBody) o;
                try {
                    mView.setMaxDivResource(responseBody.string());
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
                mView.setDeviceInfoListBean(listBean);
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
        mChart3.setNoDataTextDescription("没有数据...");
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
     * 获取屏幕宽度
     * @param context
     * @return
     */
    public int getScreenWidth(Context context) {
        WindowManager manager = ((BaseActivity)context).getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 获取URL返回格式 分非介入式和常规两种
     * @param gragh
     * @param pid
     * @param type
     * @return
     */
    public String getWebUrl(TypeBean gragh, int pid, int type) {
//        return "http://113.106.90.51:8008/Monitor/AppPlanInfo?pid=211";
        String url;
        if (gragh != null && Integer.parseInt(gragh.getHymannew().get(0).getTotal2()) > 0) {    //非介入式测温平面图
            url = SharedPrefUtil.getInstance(MyApplication.getInstance()).getString(MyConstants.BASE_URL, EadoUrl.BASE_URL_WEB) + "/NonIntrusive/PlanInfo/" + pid;
        } else {
            if (SharedPrefUtil.getInstance(MyApplication.getInstance()).getT("Compatibility", false)) {
                if (type == 1) {//平面图;
                    url = SharedPrefUtil.getInstance(MyApplication.getInstance()).getString(MyConstants.BASE_URL, EadoUrl.BASE_URL_WEB) + "/AppPlan/PlanInfo?id=" + pid;
                } else {
                    url = SharedPrefUtil.getInstance(MyApplication.getInstance()).getString(MyConstants.BASE_URL, EadoUrl.BASE_URL_WEB) + "/AppPlan/OneGraph?id=" + pid;  //mod by cww 20180604

                }
            } else {
                if (type == 1) {//平面图;
                    url = SharedPrefUtil.getInstance(MyApplication.getInstance()).getString(MyConstants.BASE_URL, EadoUrl.BASE_URL_WEB) + "/PDRInfo/PlanInfo/" + pid;
                } else {
                    url = SharedPrefUtil.getInstance(MyApplication.getInstance()).getString(MyConstants.BASE_URL, EadoUrl.BASE_URL_WEB) + "/PDRInfo/OneGraph/" + pid;  //mod by cww 20180604
                }
            }

        }
        return url;
    }

    /**
     * 把map类型转化为LineData；
     *
     * @param point
     * @param hisDevData
     * @param devData    环境温度
     * @return
     */
    public LineData transform_data(String point, LinkedHashMap<String, String> hisDevData, LinkedHashMap<String, String> devData, int type, CustomMarkerView markerView) {
        if (hisDevData == null && devData == null) {
            return new LineData();
        }
        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<String> xVals2 = new ArrayList<String>();
        ArrayList<Entry> yVals = new ArrayList<Entry>();//第一条折线；；
        ArrayList<Entry> yVals2 = new ArrayList<Entry>();//第2条折线；；
        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        int i = 0;
        if (hisDevData != null) {
            for (Map.Entry<String, String> entry : hisDevData.entrySet()) {
                if (!TextUtils.isEmpty(entry.getValue())) {
                    xVals.add(getXvalue(entry.getKey(), type));
                    yVals.add(new Entry(Float.valueOf(entry.getValue()), i++));
                }
            }
            LineDataSet set1 = new LineDataSet(yVals, point);
            //        set1.enableDashedLine(10f, 5f, 0f);//设置虚线；
            set1.enableDashedHighlightLine(10f, 5f, 0f);
            set1.setColor(Color.RED);//折线的颜色
            set1.setCircleColor(Color.RED);//点的颜色
            set1.setLineWidth(1f);

            set1.setCircleSize(2f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(9f);
            set1.setFillAlpha(65);
            set1.setFillColor(Color.BLACK);
            if (type == 1) {
                set1.setDrawValues(false);//隐藏点上的数值；
            }

            set1.setDrawCubic(true);
            set1.setCubicIntensity(0.15f);

            dataSets.add(set1);
        }
        markerView.setXValue(xVals);
        if (devData != null) {
            i = 0;
            for (Map.Entry<String, String> entry : devData.entrySet()) {
                if (!TextUtils.isEmpty(entry.getValue())) {
                    xVals2.add(entry.getKey());
                    yVals2.add(new Entry(Float.valueOf(entry.getValue()), i++));
                }
            }
            LineDataSet set2 = new LineDataSet(yVals2, "环境温度");
            set2.enableDashedHighlightLine(10f, 5f, 0f);
            set2.setColor(Color.BLUE);//折线的颜色
            set2.setCircleColor(Color.BLUE);//点的颜色
            set2.setLineWidth(1f);
            set2.setCircleSize(2f);
            set2.setDrawCircleHole(false);
            set2.setValueTextSize(9f);
            set2.setFillAlpha(65);
            set2.setFillColor(Color.BLACK);
//            set2.setDrawValues(false);//隐藏点上的数值；


            set2.setDrawCubic(true);
            set2.setCubicIntensity(0.15f);

            dataSets.add(set2);
        }
        if (xVals.size() > 0 && xVals.size() >= xVals2.size()) {
            return new LineData(xVals, dataSets);
        } else {
            return new LineData(xVals2, dataSets);
        }
    }

    /**
     * 转换数据并添加到LineDataSet中
     * @param point
     * @param hisDevData
     * @return
     */
    public LineData transform_data(String point, LinkedHashMap<String, String> hisDevData) {
        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<Entry> yVals = new ArrayList<Entry>();//第一条折线；；
        if (hisDevData == null) {
            return null;
        }
//        int i = 0;
        for (Map.Entry<String, String> entry : hisDevData.entrySet()) {
            xVals.add(entry.getKey());
        }
        for (int j = 0; j < hisDevData.size(); j++) {
            yVals.add(new Entry((float) (Math.random() * 1000 + 200), j++));
        }
        LineDataSet set1 = new LineDataSet(yVals, "配电房负荷");
//        set1.enableDashedLine(10f, 5f, 0f);//设置虚线；
        set1.enableDashedHighlightLine(10f, 5f, 0f);
        set1.setColor(Color.GREEN);//折线的颜色
        set1.setCircleColor(Color.GREEN);//点的颜色
        set1.setLineWidth(1f);
        set1.setCircleSize(2f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setFillAlpha(65);
        set1.setFillColor(Color.BLACK);
//        set1.setDrawValues(false);//隐藏点上的数值；

        set1.setDrawCubic(true);
        set1.setCubicIntensity(0.15f);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set1);
        return new LineData(xVals, dataSets);
    }

    /**
     * 获取X轴数据
     * @param key
     * @param type
     * @return
     */
    private String getXvalue(String key, int type) {
        String result = "";
        switch (type) {
            case 1://day
                result = key.substring(key.lastIndexOf("/") + 3, key.length());
                break;
            case 2://week
                result = key.substring(0, key.lastIndexOf("/") + 3);
                break;
            case 3://month
                result = key.substring(0, key.lastIndexOf(" "));
                break;
            case 4://all
                result = key.substring(0, key.lastIndexOf(" "));
                break;
            default:
                result = key.substring(0, key.lastIndexOf(" "));
                break;
        }

        return result;
    }


}
