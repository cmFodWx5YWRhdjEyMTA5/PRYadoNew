package com.yado.pryado.pryadonew.ui.monitor;

import com.yado.pryado.pryadonew.base.BaseContract;
import com.yado.pryado.pryadonew.bean.RoomListBean;
import com.yado.pryado.pryadonew.net.INetListener;
import com.yado.pryado.pryadonew.ui.widgit.EmptyLayout;

import java.util.List;

/**
 * Created by yuong on 2018/11/15.
 * des:
 */

public interface MonitorContract {


    interface View extends BaseContract.BaseView {

    }

    interface Presenter extends BaseContract.BasePresenter<View> {

        /**
         * 获取站室
         */
        void getRoomList();


        /**
         * 获取站室平面图是否是常规图
         *
         * @param pid
         */
        void getGraphType(int pid, RoomListBean.RowsEntity room, List<RoomListBean.RowsEntity> rooms, int position);

    }

    interface Model extends BaseContract.BaseModel {
        /**
         * 获取站室
         */
        void getRoomList(INetListener<Object, Throwable, Object> listener);

        /**
         * 获取站室平面图是否是常规图
         *
         * @param pid
         */
        void getGraphType(int pid, INetListener<Object, Throwable, Object> listener);


    }


}
