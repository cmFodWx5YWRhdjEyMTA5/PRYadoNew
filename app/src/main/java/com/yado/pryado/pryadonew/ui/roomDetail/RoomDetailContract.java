package com.yado.pryado.pryadonew.ui.roomDetail;

import com.yado.pryado.pryadonew.base.BaseContract;
import com.yado.pryado.pryadonew.bean.DeviceInfoListBean;
import com.yado.pryado.pryadonew.bean.RoomDetail;
import com.yado.pryado.pryadonew.bean.TypeBean;
import com.yado.pryado.pryadonew.net.INetListener;

/**
 * Created by yuong on 2018/11/15.
 * des:
 */

public interface RoomDetailContract {


    interface View extends BaseContract.BaseView {
        /**
         * 设置站室详情信息
         * @param roomDetail
         */
        void setRoomDetail(RoomDetail roomDetail);

        /**
         * 设置错误信息
         * @param error
         */
        void setError(Throwable error);

        /**
         * 设置最高温度Div
         * @param string
         */
        void setMaxDivResource(String string);

        /**
         * 设置设备列表
         * @param listBean
         */
        void setDeviceInfoListBean(DeviceInfoListBean listBean);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {

        /**
         * 获取站室详情
         */
        void getRoomDetail(int pid, int ver);


        /**
         * 获取最高温的浮窗div
         * @param pid
         */
        void getMaxDiv(int pid);

        /**
         * 获取配电房所有的设备列表
         * @param pid
         * @param pagesize
         * @param pageindex
         */
        void getDeviceInfoList(int pid, int pagesize, int pageindex);

    }

    interface Model extends BaseContract.BaseModel {

        /**
         * 获取站室详情
         */
        void getRoomDetail(int pid, int ver, INetListener<Object, Throwable, Object> listener);

        /**
         * 获取最高温的浮窗div
         * @param pid
         */
        void getMaxDiv(int pid, INetListener<Object, Throwable, Object> listener);

        /**
         * 获取配电房所有的设备列表
         * @param pid
         * @param pagesize
         * @param pageindex
         */
        void getDeviceInfoList(int pid, int pagesize, int pageindex, INetListener<Object, Throwable, Object> listener);
    }


}
