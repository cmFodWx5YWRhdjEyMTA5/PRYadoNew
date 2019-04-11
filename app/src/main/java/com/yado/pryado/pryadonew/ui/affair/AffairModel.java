package com.yado.pryado.pryadonew.ui.affair;

import android.view.View;

import com.yado.pryado.pryadonew.MyApplication;
import com.yado.pryado.pryadonew.MyConstants;
import com.yado.pryado.pryadonew.base.BaseModel;
import com.yado.pryado.pryadonew.bean.BugList;
import com.yado.pryado.pryadonew.bean.ListmapBean;
import com.yado.pryado.pryadonew.bean.OrderList;
import com.yado.pryado.pryadonew.net.INetListener;
import com.yado.pryado.pryadonew.net.PRApi;
import com.yado.pryado.pryadonew.net.PRRetrofit;
import com.yado.pryado.pryadonew.subscriber.DefaultDisposablePoolImpl;
import com.yado.pryado.pryadonew.subscriber.PRSubscriber;
import com.yado.pryado.pryadonew.util.SharedPrefUtil;

import org.reactivestreams.Subscriber;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;

public class AffairModel extends BaseModel implements AffairContract.Model{

    private PRApi prApi;

    /**
     * 注入到Presenter
     */
    @Inject
    public AffairModel(){
        prApi = PRRetrofit.getInstance(MyApplication.getInstance()).getApi();
    }

    @Override
    public void getCounts(final INetListener<Object, Throwable, Object> listener) {
        Observable
                .zip(prApi.getOrderList(SharedPrefUtil.getInstance(MyApplication.getInstance()).getT(MyConstants.USERNAME, "unknown"),
                                        MyConstants.ORDER_TYPE_AWAIT, ""),
                        prApi.getOrderList(SharedPrefUtil.getInstance(MyApplication.getInstance()).getT(MyConstants.USERNAME, "unknown"),
                                        MyConstants.ORDER_TYPE_ACCEPTET, ""), new BiFunction<OrderList, OrderList, List<ListmapBean>>() {

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
                .subscribe(new PRSubscriber<List<ListmapBean>>() {

                    Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                        disposable = d;
                    }

                    @Override
                    public void onNext(List<ListmapBean> listmapBeans) {
                        int count = listmapBeans.size();
                        SharedPrefUtil.getInstance(MyApplication.getInstance()).saveObject(MyConstants.counts_upcoming, count);
                        listener.success(count);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        deleteDisposable(disposable);
//                        clearPool();
                    }

                });
    }

    @Override
    public void setCounts(int count, final INetListener<Object, Throwable, Object> listener) {
        Observable.just(count)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new PRSubscriber<Integer>() {

                    Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                        disposable = d;
                    }

                    @Override
                    public void onNext(Integer integer) {
                        listener.success(integer);

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
