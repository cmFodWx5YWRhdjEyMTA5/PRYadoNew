package com.yado.pryado.pryadonew.ui.todo;

import com.yado.pryado.pryadonew.MyApplication;
import com.yado.pryado.pryadonew.MyConstants;
import com.yado.pryado.pryadonew.base.BaseModel;
import com.yado.pryado.pryadonew.bean.ListmapBean;
import com.yado.pryado.pryadonew.bean.OrderDetail;
import com.yado.pryado.pryadonew.bean.OrderList;
import com.yado.pryado.pryadonew.bean.SaveOrderResult;
import com.yado.pryado.pryadonew.net.INetListener;
import com.yado.pryado.pryadonew.net.PRApi;
import com.yado.pryado.pryadonew.net.PRRetrofit;
import com.yado.pryado.pryadonew.subscriber.PRSubscriber;
import com.yado.pryado.pryadonew.ui.widgit.EmptyLayout;
import com.yado.pryado.pryadonew.util.SharedPrefUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;

public class TodoModel extends BaseModel implements TodoContract.Model {

    private PRApi prApi;

    /**
     * 注入到Presenter
     */
    @Inject
    public TodoModel() {
        prApi = PRRetrofit.getInstance(MyApplication.getInstance()).getApi();
    }

    /**
     * 获取代办
     */
    @Override
    public void getAwaitOrder(String orderType, EmptyLayout emptyLayout, final INetListener<Object, Throwable, Object> listener) {
        Observable
                .zip(prApi.getOrderList(SharedPrefUtil.getInstance(MyApplication.getInstance()).getT(MyConstants.USERNAME, "unknown"),
                                        MyConstants.ORDER_TYPE_AWAIT, orderType),
                        prApi.getOrderList(SharedPrefUtil.getInstance(MyApplication.getInstance()).getT(MyConstants.USERNAME, "unknown"),
                                        MyConstants.ORDER_TYPE_ACCEPTET, orderType), new BiFunction<OrderList, OrderList, List<ListmapBean>>() {

                            @Override
                            public List<ListmapBean> apply(OrderList orderList, OrderList orderList2) throws Exception {
                                List<ListmapBean> result = new ArrayList<>();
                                if (orderList != null) {
                                    result.addAll(orderList.getListmap());
                                }
                                if (orderList2 != null) {
                                    result.addAll(orderList2.getListmap());
                                }
                                return result;
                            }
                        })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new PRSubscriber<List<ListmapBean>>(emptyLayout) {

                    Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                        disposable = d;
                    }

                    @Override
                    public void onNext(List<ListmapBean> listmapBeans) {
                        listener.success(listmapBeans);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
//                        clearPool();
                        deleteDisposable(disposable);
                    }

                });
    }
}
