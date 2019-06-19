package com.yado.pryado.pryadonew.ui.riskAssessAndDetail;

import com.yado.pryado.pryadonew.MyApplication;
import com.yado.pryado.pryadonew.base.BaseModel;
import com.yado.pryado.pryadonew.bean.BugInfo;
import com.yado.pryado.pryadonew.bean.BugList;
import com.yado.pryado.pryadonew.bean.RoomListBean;
import com.yado.pryado.pryadonew.net.INetListener;
import com.yado.pryado.pryadonew.net.PRApi;
import com.yado.pryado.pryadonew.net.PRRetrofit;
import com.yado.pryado.pryadonew.subscriber.PRSubscriber;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AssessModel extends BaseModel implements AssessContract.Model {

    private PRApi prApi;

    /**
     * 注入到Presenter
     */
    @Inject
    public AssessModel() {
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
                        deleteDisposable(disposable);
//                        clearPool();
                    }
                });
    }

    /**
     * 获取隐患
     */
    @Override
    public void getBugList(int pid, final INetListener<Object, Throwable, Object> listener) {
        prApi
                .getBugList(pid)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new PRSubscriber<BugList>() {
                    Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                        disposable = d;
                    }

                    @Override
                    public void onNext(BugList bugList) {
                        listener.success(bugList);
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
     * 获取隐患详情
     */
    @Override
    public void getBugInfo(int bugId, final INetListener<Object, Throwable, Object> listener) {
        prApi
                .getBugInfo(bugId)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new PRSubscriber<BugInfo>() {
                    Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                        disposable = d;
                    }

                    @Override
                    public void onNext(BugInfo bugInfo) {
                        listener.success(bugInfo);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        deleteDisposable(disposable);
                    }

                });
    }
}
