package com.yado.pryado.pryadonew.ui.temp_monitor;

import com.yado.pryado.pryadonew.base.BaseContract;
import com.yado.pryado.pryadonew.net.INetListener;
import com.yado.pryado.pryadonew.ui.widgit.EmptyLayout;

/**
 * Created by yuong on 2018/11/15.
 * des:
 */

public interface TempMonitorContract {


    interface View extends BaseContract.BaseView {

    }

    interface Presenter extends BaseContract.BasePresenter<View> {

        /**
         * 获取站室
         */
        void getRoomList();

        /**
         * 获取报警状态
         */
        void loadVagueAlarm(int pid, String alarmConfirm, String startDate, String endDate);

        /**
         * 获取站室历史数据
         */
        void loadMonitorHistoryGraph(int pid, int tagId, String startDate, String endDate);

        /**
         * 获取非介入式测温历史曲线数据
         *
         * @param tagID
         * @param startDate
         * @param endDate
         */
        void loadVagueHistoryGraph(int tagID, String startDate, String endDate);

        /**
         * 获取详情
         */
        void getDetail(String did);


        /**
         * 获取非介入式测温实时数据
         *
         * @param tagId
         * @param did
         * @param pid
         */
        void loadVagueRealTime(int tagId, int did, int pid);

        /**
         * 获取站室状态
         * @param pid
         * @param did
         * @param type
         */
        void getStatusData(int pid, int did, int type);

        /**
         * 获取站室平面图是否是常规图
         *
         * @param pid
         */
        void getGraphType(int pid);


    }

    interface Model extends BaseContract.BaseModel {

        /**
         * 获取站室
         */
        void getRoomList(INetListener<Object, Throwable, Object> listener);

        /**
         * 获取报警状态
         */
        void loadVagueAlarm(int pid, String alarmConfirm, String startDate, String endDate, INetListener<Object, Throwable, Object> listener);

        /**
         * 获取站室历史数据
         */
        void loadMonitorHistoryGraph(int pid, int tagId, String startDate, String endDate, INetListener<Object, Throwable, Object> listener);

        /**
         * 获取详情
         */
        void getDetail(String did, INetListener<Object, Throwable, Object> listener);

        /**
         * 获取非介入式测温实时数据
         *
         * @param tagId
         * @param did
         * @param pid
         */
        void loadVagueRealTime(int tagId, int did, int pid, INetListener<Object, Throwable, Object> listener);

        /**
         * 获取非介入式测温历史曲线数据
         *
         * @param tagID
         * @param startDate
         * @param endDate
         */
        void loadVagueHistoryGraph(int tagID, String startDate, String endDate, INetListener<Object, Throwable, Object> listener);

        /**
         * 获取站室状态
         * @param pid
         * @param did
         * @param type
         * @param listener
         */
        void getStatusData(int pid, int did, int type, INetListener<Object, Throwable, Object> listener);

        /**
         * 获取站室平面图是否是常规图
         *
         * @param pid
         */
        void getGraphType(int pid, INetListener<Object, Throwable, Object> listener);

    }


}
