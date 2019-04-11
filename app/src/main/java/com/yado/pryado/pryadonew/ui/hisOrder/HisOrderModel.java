package com.yado.pryado.pryadonew.ui.hisOrder;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.yado.pryado.pryadonew.MyApplication;
import com.yado.pryado.pryadonew.base.BaseModel;
import com.yado.pryado.pryadonew.bean.AlertBean;
import com.yado.pryado.pryadonew.bean.HisData;
import com.yado.pryado.pryadonew.bean.OrderList;
import com.yado.pryado.pryadonew.bean.RoomListBean;
import com.yado.pryado.pryadonew.net.INetListener;
import com.yado.pryado.pryadonew.net.PRRetrofit;
import com.yado.pryado.pryadonew.subscriber.PRSubscriber;
import com.yado.pryado.pryadonew.ui.widgit.EmptyLayout;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HisOrderModel extends BaseModel implements HisOrderContract.Model {

    /**
     * 注入到Presenter
     */
    @Inject
    public HisOrderModel() {
    }


    @Override
    public void getHisOrderList(String username, int orderState, String orderType, EmptyLayout emptyLayout,  final INetListener<Object, Throwable, Object> listener) {
        PRRetrofit.getInstance(MyApplication.getInstance()).getApi()
                .getOrderList(username, orderState, orderType)
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
                        deleteDisposable(disposable);
//                        clearPool();
                    }
                });
    }
}
