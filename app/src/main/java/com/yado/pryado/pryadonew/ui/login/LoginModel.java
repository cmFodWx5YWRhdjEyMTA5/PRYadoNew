package com.yado.pryado.pryadonew.ui.login;

import android.util.Log;

import com.google.gson.Gson;
import com.yado.pryado.pryadonew.MyApplication;
import com.yado.pryado.pryadonew.base.BaseModel;
import com.yado.pryado.pryadonew.net.INetListener;
import com.yado.pryado.pryadonew.net.PRRetrofit;
import com.yado.pryado.pryadonew.subscriber.DefaultDisposablePoolImpl;
import com.yado.pryado.pryadonew.subscriber.PRSubscriber;

import java.io.IOException;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class LoginModel extends BaseModel implements LoginContract.Model{

    /**
     * 注入到Presenter
     */
    @Inject
    public LoginModel(){}

    @Override
    public void login(String username, String password, final INetListener<Object, Throwable, Object> listener) {
        PRRetrofit.getInstance(MyApplication.getInstance()).getApi()
                .userLogin(username, password)
                .subscribeOn(Schedulers.io())
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
                        super.onError(e);
                        listener.failed(e);
//                        clearPool();
                        deleteDisposable(disposable);
                    }


                });
    }

    @Override
    public void cancelLogin() {
        clearPool();
    }
}
