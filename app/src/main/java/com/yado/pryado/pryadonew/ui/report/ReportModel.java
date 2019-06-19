package com.yado.pryado.pryadonew.ui.report;

import com.yado.pryado.pryadonew.MyApplication;
import com.yado.pryado.pryadonew.base.BaseModel;
import com.yado.pryado.pryadonew.bean.BugInfo;
import com.yado.pryado.pryadonew.bean.BugList;
import com.yado.pryado.pryadonew.bean.PostBugResult;
import com.yado.pryado.pryadonew.bean.RoomListBean;
import com.yado.pryado.pryadonew.net.INetListener;
import com.yado.pryado.pryadonew.net.PRRetrofit;
import com.yado.pryado.pryadonew.subscriber.PRSubscriber;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ReportModel extends BaseModel implements ReportContract.Model {

    /**
     * 注入到Presenter
     */
    @Inject
    public ReportModel() {
    }

    /**
     * 获取站室
     */
    @Override
    public void getRoomList(final INetListener<Object, Throwable, Object> listener) {
        PRRetrofit.getInstance(MyApplication.getInstance()).getApi()
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
//                        clearPool();
                        deleteDisposable(disposable);
                    }

                });
    }

    /**
     * 隐患上报
     */
    @Override
    public void postNewBug(int pid, String bugLocation, String bugDesc, final INetListener<Object, Throwable, Object> listener) {
        PRRetrofit.getInstance(MyApplication.getInstance()).getApi()
                .postNewBug(pid, bugLocation, bugDesc)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new PRSubscriber<PostBugResult>() {
                    Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                        disposable = d;
                    }

                    @Override
                    public void onNext(PostBugResult postBugResult) {
                        listener.success(postBugResult);
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
