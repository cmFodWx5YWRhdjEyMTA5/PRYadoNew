package com.yado.pryado.pryadonew.ui.task;

import com.yado.pryado.pryadonew.base.BaseContract;
import com.yado.pryado.pryadonew.bean.OrderDetail;
import com.yado.pryado.pryadonew.net.INetListener;
import com.yado.pryado.pryadonew.ui.widgit.EmptyLayout;

/**
 * Created by yuong on 2018/11/15.
 * des:
 */

public interface TaskContract {


    interface View extends BaseContract.BaseView {
        void  getDetail(int orderId);

        void setOrderDetail(OrderDetail detail);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {

        /**
         * 获取工单详细情况
         * @param orderId  工单id
         */
        void getOrderDetail(int orderId, EmptyLayout emptyLayout);

        /**
         * 获取工单列表
         * @param pid  设备id
         * @param orderState 工单状态
         */
        void getOrderList(String pid, int orderState, EmptyLayout emptyLayout);

        /**
         * 提交到数据库
         * @param orderID 工单id
         * @param isQualified  是否合格（1是/0否）
         * @param checkInfo 检查情况
         * @param latitude 纬度
         * @param longtitude 经度
         * @param rectification 整改信息
         */
        void saveOrderInfo(String orderID, String isQualified, String checkInfo, float latitude, float longtitude, String rectification);

        void postDelete(String fileName, String ctype);
    }

    interface Model extends BaseContract.BaseModel {

        /**
         * 获取工单详细情况
         * @param orderId  工单id
         */
        void getOrderDetail(int orderId, EmptyLayout emptyLayout, INetListener<Object, Throwable, Object> listener);

        /**
         * 获取工单列表
         * @param pid  设备id
         * @param orderState 工单状态
         */
        void getOrderList(String pid, int orderState, EmptyLayout emptyLayout, INetListener<Object, Throwable, Object> listener);

        /**
         * 提交到数据库
         * @param orderID 工单id
         * @param isQualified  是否合格（1是/0否）
         * @param checkInfo 检查情况
         * @param latitude 纬度
         * @param longtitude 经度
         * @param rectification 整改信息
         */
        void saveOrderInfo(String orderID, String isQualified, String checkInfo, float latitude, float longtitude, String rectification, INetListener<Object, Throwable, Object> listener);

        void postDelete(String fileName, String ctype, INetListener<Object, Throwable, Object> listener);
    }


}
