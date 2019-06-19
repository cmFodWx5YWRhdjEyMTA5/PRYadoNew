package com.yado.pryado.pryadonew.ui.main;

import android.os.Handler;

import com.yado.pryado.pryadonew.MyApplication;
import com.yado.pryado.pryadonew.base.BaseModel;
import com.yado.pryado.pryadonew.bean.Update;
import com.yado.pryado.pryadonew.net.EadoUrl;
import com.yado.pryado.pryadonew.net.INetListener;
import com.yado.pryado.pryadonew.net.OkhttpUtils;
import com.yado.pryado.pryadonew.net.PRApi;
import com.yado.pryado.pryadonew.net.PRRetrofit;
import com.yado.pryado.pryadonew.subscriber.PRSubscriber;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainModel extends BaseModel implements MainContract.Model{

    private PRApi prApi;

    /**
     * 注入到Presenter
     */
    @Inject
    public MainModel(){
        prApi = PRRetrofit.getInstance(MyApplication.getInstance()).getApi();
    }

    /**
     * 检查更新
     */
    @Override
    public void CheckUpdate(final int versionCode, final INetListener<Object, Throwable, Object> listener) {
        prApi
                .CheckUpdate()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new PRSubscriber<Update>() {

                    Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                        disposable = d;
                    }

                    @Override
                    public void onNext(Update update) {
                        listener.success(update);
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
     * 下载安装
     */
    @Override
    public void DownLoadApk(String downloadUrl, Handler handler,  INetListener<Object, Throwable, Object> listener) {
        OkhttpUtils.getInstance().downLoad(downloadUrl, EadoUrl.DEFAULT_SAVE_FILE_PATH, handler);
    }
}
