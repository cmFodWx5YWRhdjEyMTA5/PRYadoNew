package com.yado.pryado.pryadonew.ui.alert;

import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.yado.pryado.pryadonew.MyApplication;
import com.yado.pryado.pryadonew.R;
import com.yado.pryado.pryadonew.base.BasePresenter;
import com.yado.pryado.pryadonew.bean.AlertBean;
import com.yado.pryado.pryadonew.bean.HisData;
import com.yado.pryado.pryadonew.bean.RoomListBean;
import com.yado.pryado.pryadonew.bean.Update;
import com.yado.pryado.pryadonew.net.INetListener;
import com.yado.pryado.pryadonew.ui.widgit.CustomMarkerView;
import com.yado.pryado.pryadonew.ui.widgit.EmptyLayout;
import com.yado.pryado.pryadonew.util.DateUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by OnexZgj on 2018/9/11:09:37.
 * des:
 */

public class AlertPresent extends BasePresenter<AlertContract.View, AlertModel> implements AlertContract.Presenter {

    /**
     * 注入到Fragment
     */
    @Inject
    public AlertPresent() {
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
                if (roomListBean == null) {
                    return;
                }
                ((AlertActivity)mView).setRooms(roomListBean.getRows());

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
     * @param alarmTime  报警时间
     * @param pid
     * @param did
     * @param tagid
     */
    @Override
    public void getPointHisData(String alarmTime, String pid, String did, String tagid) {
        mModel.getPointHisData(alarmTime, pid, did, tagid, new INetListener<Object, Throwable, Object>() {
            @Override
            public void success(Object o) {
                HisData hisData = (HisData) o;
                ((AlertFragment) mView).setLineDatas(hisData);
            }

            @Override
            public void failed(Throwable throwable) {
                ((AlertFragment) mView).setError();
            }

            @Override
            public void loading(Object o) {

            }
        });
    }

    /**
     * 获取报警列表
     * @param rows  页数
     * @param type  类型
     * @param page
     * @param pid
     */
    @Override
    public void getAlertList(int rows, int type, int page, int pid, EmptyLayout emptyLayout) {
        mModel.getAlertList(rows, type, page, pid, emptyLayout, new INetListener<Object, Throwable, Object>() {
            @Override
            public void success(Object o) {
                AlertBean alertBean = (AlertBean) o;
                ((AlertFragment)mView).setAlertList(alertBean);
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
     * 获取报警列表
     * @param rows  页数
     * @param page
     * @param pid
     */
    @Override
    public void getAlertList_1(int rows, int page, String startDate, String endDate, int pid, EmptyLayout emptyLayout) {
        mModel.getAlertList_1(rows, page, startDate, endDate, pid, emptyLayout, new INetListener<Object, Throwable, Object>() {
            @Override
            public void success(Object o) {
                AlertBean alertBean = (AlertBean) o;
                ((AlertFragment)mView).setAlertList(alertBean);
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
    public void setLineType(LineChart mChart3) {
        mChart3.setDrawGridBackground(false);
        mChart3.setDescription("");
        mChart3.setNoDataText("");
        mChart3.setNoDataTextDescription(MyApplication.getInstance().getString(R.string.no_recent_data));
        mChart3.setTouchEnabled(true);
        mChart3.setDragEnabled(true);
        mChart3.setScaleEnabled(true);
        mChart3.setPinchZoom(true);
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

    /**
     * 获取间隔时间
     */
    public String getInternalDate(int type) {
        String startDate, endDate;
        switch (type) {
            case 0:
                startDate = DateUtils.getStringDateShort() + " 00:00:00";
                endDate = DateUtils.getStringDate();
                break;
            case 1:
                startDate = DateUtils.startWeek(DateUtils.getNow()) + " 00:00:00";
                endDate = DateUtils.getStringDate();
                break;
            case 2:
                endDate = DateUtils.getStringDateMiddle() + ":00:00";
                startDate = DateUtils.timeStamp2Date(Long.parseLong(DateUtils.getSupportBeginDayofMonth(DateUtils.strToDate(endDate))), null);
                break;
            default:
                endDate = DateUtils.getPreOrNextDate(DateUtils.strToDate(DateUtils.getStringDateShort()), true) + " 23:59:59";
                startDate = endDate.split(" ")[0] + " 00:00:00";
                break;
        }
        return startDate + "*" + endDate;

    }

}
