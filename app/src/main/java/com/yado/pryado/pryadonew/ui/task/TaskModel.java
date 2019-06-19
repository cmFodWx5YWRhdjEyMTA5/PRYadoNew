package com.yado.pryado.pryadonew.ui.task;

import com.yado.pryado.pryadonew.MyApplication;
import com.yado.pryado.pryadonew.MyConstants;
import com.yado.pryado.pryadonew.base.BaseModel;
import com.yado.pryado.pryadonew.bean.BugInfo;
import com.yado.pryado.pryadonew.bean.BugList;
import com.yado.pryado.pryadonew.bean.OrderDetail;
import com.yado.pryado.pryadonew.bean.OrderList;
import com.yado.pryado.pryadonew.bean.RoomListBean;
import com.yado.pryado.pryadonew.bean.SaveOrderResult;
import com.yado.pryado.pryadonew.net.INetListener;
import com.yado.pryado.pryadonew.net.PRApi;
import com.yado.pryado.pryadonew.net.PRRetrofit;
import com.yado.pryado.pryadonew.subscriber.PRSubscriber;
import com.yado.pryado.pryadonew.ui.widgit.EmptyLayout;

import org.reactivestreams.Subscriber;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TaskModel extends BaseModel implements TaskContract.Model {

    private PRApi prApi;

    /**
     * 注入到Presenter
     */
    @Inject
    public TaskModel() {
        prApi = PRRetrofit.getInstance(MyApplication.getInstance()).getApi();
    }

    /**
     * 获取工单详细情况
     * @param orderId  工单id
     */
    @Override
    public void getOrderDetail(int orderId, EmptyLayout emptyLayout, final INetListener<Object, Throwable, Object> listener) {
        prApi
                .getOrderDetail(orderId)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new PRSubscriber<OrderDetail>(emptyLayout) {
                    Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                        disposable = d;
                    }

                    @Override
                    public void onNext(OrderDetail orderDetail) {
                        listener.success(orderDetail);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
//                        clearPool();
                        deleteDisposable(disposable);
                    }


                });
    }

    /**
     * 获取工单列表
     * @param pid  设备id
     * @param orderState 工单状态
     */
    @Override
    public void getOrderList(String pid, int orderState, EmptyLayout emptyLayout, final INetListener<Object, Throwable, Object> listener) {
        prApi
                .getOrderList(pid, orderState)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new PRSubscriber<OrderList>(emptyLayout) {
                    Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                        disposable = d;
                    }

                    @Override
                    public void onNext(OrderList orderList) {
                        listener.success(orderList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
//                        clearPool();
                        deleteDisposable(disposable);
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
    public void saveOrderInfo(String orderID, String isQualified, String checkInfo, float latitude, float longtitude, String rectification, final INetListener<Object, Throwable, Object> listener) {
        prApi
                .saveOrderInfo(orderID, isQualified, checkInfo, latitude, longtitude, rectification)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new PRSubscriber<SaveOrderResult>() {

                    Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                        disposable = d;
                    }

                    @Override
                    public void onNext(SaveOrderResult saveOrderResult) {
                        listener.success(saveOrderResult);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
//                        clearPool();
                        deleteDisposable(disposable);
                    }


                });
    }

    /**
     * 删除文件
     * @param fileName
     * @param ctype
     * @param listener
     */
    @Override
    public void postDelete(String fileName, String ctype, final INetListener<Object, Throwable, Object> listener) {
        prApi
                .deleteFile(fileName, MyConstants.ORDER, ctype).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new PRSubscriber<String>() {
                    Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                        disposable = d;
                    }

                    @Override
                    public void onNext(String s) {
                        listener.success(s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.failed(e);
//                        clearPool();
                        deleteDisposable(disposable);
                    }
                });
    }
}
