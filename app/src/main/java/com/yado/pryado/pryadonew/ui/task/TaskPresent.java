package com.yado.pryado.pryadonew.ui.task;

import com.blankj.utilcode.util.ToastUtils;
import com.yado.pryado.pryadonew.R;
import com.yado.pryado.pryadonew.base.BasePresenter;
import com.yado.pryado.pryadonew.bean.BugInfo;
import com.yado.pryado.pryadonew.bean.BugList;
import com.yado.pryado.pryadonew.bean.OrderDetail;
import com.yado.pryado.pryadonew.bean.OrderList;
import com.yado.pryado.pryadonew.bean.RoomListBean;
import com.yado.pryado.pryadonew.bean.SaveOrderResult;
import com.yado.pryado.pryadonew.net.INetListener;
import com.yado.pryado.pryadonew.ui.riskAssessAndDetail.AssessActivity;
import com.yado.pryado.pryadonew.ui.riskAssessAndDetail.DangerDetailActivity;
import com.yado.pryado.pryadonew.ui.widgit.EmptyLayout;

import javax.inject.Inject;

import static io.reactivex.plugins.RxJavaPlugins.onError;

public class TaskPresent extends BasePresenter<TaskContract.View, TaskModel> implements TaskContract.Presenter {

    @Inject
    public TaskPresent() {
    }

    /**
     * 获取工单详细情况
     * @param orderId  工单id
     */
    @Override
    public void getOrderDetail(int orderId, EmptyLayout emptyLayout) {
        mModel.getOrderDetail(orderId, emptyLayout, new INetListener<Object, Throwable, Object>() {
            @Override
            public void success(Object o) {
                OrderDetail orderDetail = (OrderDetail) o;
                mView.setOrderDetail(orderDetail);
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
     * 获取工单列表
     * @param pid  设备id
     * @param orderState 工单状态
     */
    @Override
    public void getOrderList(String pid, int orderState, EmptyLayout emptyLayout) {
        mModel.getOrderList(pid, orderState, emptyLayout, new INetListener<Object, Throwable, Object>() {
            @Override
            public void success(Object o) {
                OrderList orderList = (OrderList) o;
                mView.getDetail(orderList.getListmap().get(0).getOrderID());
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
     * 提交到数据库
     * @param orderID 工单id
     * @param isQualified  是否合格（1是/0否）
     * @param checkInfo 检查情况
     * @param latitude 纬度
     * @param longtitude 经度
     * @param rectification 整改信息
     */
    @Override
    public void saveOrderInfo(String orderID, String isQualified, String checkInfo, float latitude, float longtitude, String rectification) {
        mModel.saveOrderInfo(orderID, isQualified, checkInfo, latitude, longtitude, rectification, new INetListener<Object, Throwable, Object>() {
            @Override
            public void success(Object o) {
                SaveOrderResult saveOrderResult = (SaveOrderResult) o;
                if (saveOrderResult != null && saveOrderResult.getResultCode() == 1) {

                    ((TaskActivity)mView).upFile();
                } else {
                    ToastUtils.showShort(R.string.submit_failure);
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
     * 删除文件
     * @param fileName
     * @param ctype
     */
    @Override
    public void postDelete(final String fileName, String ctype) {
        mModel.postDelete(fileName, ctype, new INetListener<Object, Throwable, Object>() {
            @Override
            public void success(Object o) {
                if ("success".equals( o)) {
                    ((TaskActivity)mView).hideDialog();
                } else {
                    failed(null);
                }
            }

            @Override
            public void failed(Throwable throwable) {
                ToastUtils.showShort("删除失败." + fileName);
            }

            @Override
            public void loading(Object o) {

            }
        });
    }
}
