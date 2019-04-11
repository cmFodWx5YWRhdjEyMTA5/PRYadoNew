package com.yado.pryado.pryadonew.ui.alert;

import com.yado.pryado.pryadonew.base.BaseContract;
import com.yado.pryado.pryadonew.bean.AlertBean;
import com.yado.pryado.pryadonew.bean.RoomListBean;
import com.yado.pryado.pryadonew.net.INetListener;
import com.yado.pryado.pryadonew.ui.widgit.EmptyLayout;

import java.util.List;

import retrofit2.http.Query;

/**
 * Created by yuong on 2018/11/15.
 * des:
 */

public interface AlertContract {


    interface View extends BaseContract.BaseView {

    }

    interface Presenter extends BaseContract.BasePresenter<View> {

        /**
         * 获取站室
         */
        void getRoomList();

        /**
         * 获取站室历史数据
         * @param AlarmTime  报警时间
         * @param pid
         * @param did
         * @param tagid
         */
        void getPointHisData(String AlarmTime, String pid, String did, String tagid);

        /**
         * 获取报警列表
         * @param rows  页数
         * @param type  类型
         * @param page
         * @param pid
         */
        void getAlertList(int rows, int type, int page, int pid, EmptyLayout emptyLayout);

        /**
         * 获取报警列表
         * @param rows  页数
         * @param page
         * @param pid
         */
        void getAlertList_1(int rows, int page, String startDate, String endDate, int pid, EmptyLayout emptyLayout);


    }

    interface Model extends BaseContract.BaseModel {
        /**
         * 获取站室
         */
        void getRoomList(INetListener<Object, Throwable, Object> listener);

        /**
         * 获取站室历史数据
         * @param AlarmTime  报警时间
         * @param pid
         * @param did
         * @param tagid
         */
        void getPointHisData(String AlarmTime, String pid, String did, String tagid, INetListener<Object, Throwable, Object> listener);

        /**
         * 获取报警列表
         * @param rows  页数
         * @param type  类型
         * @param page
         * @param pid
         */
        void getAlertList(int rows, int type, int page, int pid, EmptyLayout emptyLayout,  INetListener<Object, Throwable, Object> listener);

        /**
         * 获取报警列表
         * @param rows  数量
         * @param page
         * @param pid
         */
        void getAlertList_1(int rows, int page, String startDate, String endDate, int pid, EmptyLayout emptyLayout, INetListener<Object, Throwable, Object> listener);

    }


}
