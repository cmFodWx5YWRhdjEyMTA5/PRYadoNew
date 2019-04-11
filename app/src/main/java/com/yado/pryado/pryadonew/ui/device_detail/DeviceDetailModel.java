package com.yado.pryado.pryadonew.ui.device_detail;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.yado.pryado.pryadonew.MyApplication;
import com.yado.pryado.pryadonew.MyConstants;
import com.yado.pryado.pryadonew.base.BaseModel;
import com.yado.pryado.pryadonew.bean.AlertBean;
import com.yado.pryado.pryadonew.bean.BugList;
import com.yado.pryado.pryadonew.bean.DeviceDetailBean2;
import com.yado.pryado.pryadonew.bean.DeviceInfo;
import com.yado.pryado.pryadonew.bean.HisData;
import com.yado.pryado.pryadonew.bean.HisData2;
import com.yado.pryado.pryadonew.bean.ListmapBean;
import com.yado.pryado.pryadonew.bean.OrderList;
import com.yado.pryado.pryadonew.bean.PostBugResult;
import com.yado.pryado.pryadonew.bean.RoomListBean;
import com.yado.pryado.pryadonew.net.INetListener;
import com.yado.pryado.pryadonew.net.PRApi;
import com.yado.pryado.pryadonew.net.PRRetrofit;
import com.yado.pryado.pryadonew.subscriber.PRSubscriber;
import com.yado.pryado.pryadonew.ui.widgit.MyProgressDialog;
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

public class DeviceDetailModel extends BaseModel implements DeviceDetailContract.Model {

    private PRApi prApi;

    /**
     * 注入到Presenter
     */
    @Inject
    public DeviceDetailModel() {
        prApi = PRRetrofit.getInstance(MyApplication.getInstance()).getApi();
    }


    @Override
    public void getDetail(String did, MyProgressDialog progressDialog, final INetListener<Object, Throwable, Object> listener) {
        prApi
                .getDetail2(did)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new PRSubscriber<DeviceDetailBean2>(progressDialog) {

                    Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                        disposable = d;
                    }

                    @Override
                    public void onNext(DeviceDetailBean2 deviceDetailBean2) {
                        listener.success(deviceDetailBean2);
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
    public void getDetail2(String did1, String did2, final INetListener<Object, Throwable, Object> listener) {
        Observable
                .zip(prApi.getDetail2(did1),
                        prApi.getDetail2(did2), new BiFunction<DeviceDetailBean2, DeviceDetailBean2, List<DeviceDetailBean2.RealTimeParamsBean>>() {
                            @Override
                            public List<DeviceDetailBean2.RealTimeParamsBean> apply(DeviceDetailBean2 deviceDetailBean, DeviceDetailBean2 deviceDetailBean2) throws Exception {
                                ArrayList<DeviceDetailBean2.RealTimeParamsBean> mRealTimeParamsList = new ArrayList<>();

                                String deviceName = deviceDetailBean.getDeviceName();
                                for (DeviceDetailBean2.RealTimeParamsBean bean : deviceDetailBean.getRealTimeParams()) {
                                    bean.setPName(deviceName + "-" + bean.getPName());
                                    mRealTimeParamsList.add(bean);
                                }
                                String deviceName2 = deviceDetailBean2.getDeviceName();
                                for (DeviceDetailBean2.RealTimeParamsBean bean2 : deviceDetailBean2.getRealTimeParams()) {
                                    bean2.setPName(deviceName2 + "-" + bean2.getPName());
                                    mRealTimeParamsList.add(bean2);
                                }
                                return mRealTimeParamsList;
                            }
                        })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new PRSubscriber<List<DeviceDetailBean2.RealTimeParamsBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(List<DeviceDetailBean2.RealTimeParamsBean> realTimeParamsBeans) {
                        listener.success(realTimeParamsBeans);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        clearPool();
                    }

                });

    }

    @Override
    public void getBugList(String did, final INetListener<Object, Throwable, Object> listener) {
        prApi
                .getBugList(did)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new PRSubscriber<BugList>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(BugList bugList) {
                        listener.success(bugList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        clearPool();
                    }


                });
    }

    @Override
    public void getPointData(String dateTime, String hour, String pid, String did, String tagid, final INetListener<Object, Throwable, Object> listener) {
        prApi
                .getPointData(dateTime, hour, pid, did, tagid)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new PRSubscriber<HisData2>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(HisData2 hisData2) {
                        listener.success(hisData2);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        listener.failed(e);
                        clearPool();
                    }

                });
    }

    @Override
    public void postNewBug(String pID, int did, String bugLocation, String bugDesc, final INetListener<Object, Throwable, Object> listener) {
        prApi
                .postNewBug(pID, did, bugLocation, bugDesc)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new PRSubscriber<PostBugResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(PostBugResult postBugResult) {
                        listener.success(postBugResult);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        clearPool();
                    }


                });
    }

    @Override
    public void getInfoByCode(String code, final INetListener<Object, Throwable, Object> listener) {
        prApi
                .getInfoByCode(code)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new PRSubscriber<DeviceInfo>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(DeviceInfo deviceInfo) {
                        listener.success(deviceInfo);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        clearPool();
                    }


                });
    }
}
