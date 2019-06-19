package com.yado.pryado.pryadonew.ui.alert;

import com.yado.pryado.pryadonew.MyApplication;
import com.yado.pryado.pryadonew.base.BaseModel;
import com.yado.pryado.pryadonew.bean.AlertBean;
import com.yado.pryado.pryadonew.bean.HisData;
import com.yado.pryado.pryadonew.bean.RoomListBean;
import com.yado.pryado.pryadonew.bean.Update;
import com.yado.pryado.pryadonew.net.INetListener;
import com.yado.pryado.pryadonew.net.PRApi;
import com.yado.pryado.pryadonew.net.PRRetrofit;
import com.yado.pryado.pryadonew.subscriber.PRSubscriber;
import com.yado.pryado.pryadonew.ui.widgit.EmptyLayout;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AlertModel extends BaseModel implements AlertContract.Model{

    private PRApi prApi;

    /**
     * 注入到Presenter
     */
    @Inject
    public AlertModel(){
        prApi = PRRetrofit.getInstance(MyApplication.getInstance()).getApi();
    }

    /**
     * 获取站室
     */
    @Override
    public void getRoomList(final INetListener<Object, Throwable, Object> listener) {
        prApi
                .getPDRList()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new PRSubscriber<RoomListBean>() {
                    Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                        disposable = d;
                    }

                    @Override
                    public void onNext(RoomListBean roomListBean) {
                        listener.success(roomListBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        listener.failed(e);
                        deleteDisposable(disposable);
//                        clearPool();
                    }

                });
    }

    /**
     * 获取站室历史数据
     * @param AlarmTime  报警时间
     * @param pid
     * @param did
     * @param tagid
     */
    @Override
    public void getPointHisData(String AlarmTime, String pid, String did, String tagid, final INetListener<Object, Throwable, Object> listener) {
        prApi
                .getPointHisData(AlarmTime, pid, did, tagid)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new PRSubscriber<HisData>() {

                    Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                        disposable = d;
                    }

                    @Override
                    public void onNext(HisData hisData) {
                        listener.success(hisData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        listener.failed(e);
//                        clearPool();
                        deleteDisposable(disposable);
                    }
                });
    }

    /**
     * 获取报警列表
     * @param rows  页数
     * @param type  类型
     * @param page
     * @param pid
     */
    @Override
    public void getAlertList(int rows, int type, int page, int pid, EmptyLayout emptyLayout, final INetListener<Object, Throwable, Object> listener) {
        prApi
                .getAlertList(rows, type, page, pid)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new PRSubscriber<AlertBean>(emptyLayout) {

                    Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                        disposable = d;
                    }

                    @Override
                    public void onNext(AlertBean alertBean) {
                        listener.success(alertBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        listener.failed(e);
                        deleteDisposable(disposable);
//                        clearPool();
                    }

                });
    }

    /**
     * 获取报警列表
     * @param rows  数量
     * @param page
     * @param pid
     */
    @Override
    public void getAlertList_1(int rows, int page, String startDate, String endDate, int pid, EmptyLayout emptyLayout, final INetListener<Object, Throwable, Object> listener) {
        prApi
                .getAlertList_1(rows, page, startDate, endDate, pid)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new PRSubscriber<AlertBean>(emptyLayout) {

                    Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                        disposable = d;
                    }

                    @Override
                    public void onNext(AlertBean alertBean) {
                        listener.success(alertBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        listener.failed(e);
//                        clearPool();
                        deleteDisposable(disposable);
                    }

                });
    }
}
