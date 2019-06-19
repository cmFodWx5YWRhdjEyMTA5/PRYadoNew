package com.yado.pryado.pryadonew.ui.temp_monitor;

import com.yado.pryado.pryadonew.MyApplication;
import com.yado.pryado.pryadonew.base.BaseModel;
import com.yado.pryado.pryadonew.bean.AlertBean;
import com.yado.pryado.pryadonew.bean.DeviceDetailBean2;
import com.yado.pryado.pryadonew.bean.DeviceInfoListBean;
import com.yado.pryado.pryadonew.bean.HisData;
import com.yado.pryado.pryadonew.bean.HisData2;
import com.yado.pryado.pryadonew.bean.RoomListBean;
import com.yado.pryado.pryadonew.bean.TypeBean;
import com.yado.pryado.pryadonew.bean.VagueHistoryGraphBean;
import com.yado.pryado.pryadonew.bean.VagueRealTimeBean;
import com.yado.pryado.pryadonew.net.INetListener;
import com.yado.pryado.pryadonew.net.PRApi;
import com.yado.pryado.pryadonew.net.PRRetrofit;
import com.yado.pryado.pryadonew.subscriber.PRSubscriber;
import com.yado.pryado.pryadonew.ui.widgit.EmptyLayout;


import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class TempMonitorModel extends BaseModel implements TempMonitorContract.Model {

    private PRApi prApi;

    /**
     * 注入到Presenter
     */
    @Inject
    public TempMonitorModel() {
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
//                        clearPool();
                        deleteDisposable(disposable);
                    }

                });
    }

    /**
     * 获取报警状态
     */
    @Override
    public void loadVagueAlarm(int pid, String alarmConfirm, String startDate, String endDate, final INetListener<Object, Throwable, Object> listener) {
        prApi
                .loadVagueAlarm(pid, alarmConfirm, startDate, endDate)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new PRSubscriber<AlertBean>() {
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
//                        clearPool();
                        deleteDisposable(disposable);
                    }

                });
    }

    /**
     * 获取站室历史数据
     */
    @Override
    public void loadMonitorHistoryGraph(int pid, int tagId, String startDate, String endDate, final INetListener<Object, Throwable, Object> listener) {
        prApi.loadMonitorHistoryGraph(pid, tagId, startDate, endDate)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new PRSubscriber<List<VagueHistoryGraphBean>>() {
                    Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                        disposable = d;
                    }

                    @Override
                    public void onNext(List<VagueHistoryGraphBean> vagueHistoryGraphBean) {
                        listener.success(vagueHistoryGraphBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
//                        clearPool();
                        deleteDisposable(disposable);
                        listener.failed(e);
                    }

                });
    }

    /**
     * 获取详情
     */
    @Override
    public void getDetail(String did, final INetListener<Object, Throwable, Object> listener) {
        prApi
                .getDetail2(did)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new PRSubscriber<DeviceDetailBean2>() {

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
//                        clearPool();
                        deleteDisposable(disposable);
                        listener.failed(e);
                    }

                });
    }

    /**
     * 获取非介入式测温实时数据
     *
     * @param tagId
     * @param did
     * @param pid
     */
    @Override
    public void loadVagueRealTime(int tagId, int did, int pid, final INetListener<Object, Throwable, Object> listener) {
        prApi
                .loadVagueRealTime(tagId, did, pid)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new PRSubscriber<VagueRealTimeBean>() {
                    Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                        disposable = d;
                    }

                    @Override
                    public void onNext(VagueRealTimeBean vagueRealTimeBean) {
                        listener.success(vagueRealTimeBean);

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
     * 获取非介入式测温历史曲线数据
     *
     * @param tagID
     * @param startDate
     * @param endDate
     */
    @Override
    public void loadVagueHistoryGraph(int tagID, String startDate, String endDate, final INetListener<Object, Throwable, Object> listener) {
        prApi
                .loadVagueHistoryGraph(tagID, startDate, endDate)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new PRSubscriber<List<VagueHistoryGraphBean>>() {
                    Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                        disposable = d;
                    }

                    @Override
                    public void onNext(List<VagueHistoryGraphBean> list) {
                        listener.success(list);
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
     * 获取站室状态
     * @param pid
     * @param did
     * @param type
     * @param listener
     */
    @Override
    public void getStatusData(int pid, int did, int type, final INetListener<Object, Throwable, Object> listener) {
        prApi.getStatusData(pid, did, type)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new PRSubscriber<ResponseBody>() {
                    Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                        disposable = d;
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        listener.success(responseBody);
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
     * 获取站室平面图是否是常规图
     *
     * @param pid
     */
    @Override
    public void getGraphType(int pid, final INetListener<Object, Throwable, Object> listener) {
        prApi.getGraphType(pid)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new PRSubscriber<TypeBean>() {
                    Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                        disposable = d;
                    }

                    @Override
                    public void onNext(TypeBean typeBean) {
                        listener.success(typeBean);
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
     * 获取配电房所有的设备列表
     * @param pid
     * @param pagesize
     * @param pageindex
     */
    @Override
    public void getDeviceInfoList(int pid, int pagesize, int pageindex, final INetListener<Object, Throwable, Object> listener) {
        prApi.getDeviceInfoList(pid, pagesize, pageindex)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new PRSubscriber<DeviceInfoListBean>() {
                    Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                        disposable = d;
                    }

                    @Override
                    public void onNext(DeviceInfoListBean listBean) {
                        listener.success(listBean);
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
