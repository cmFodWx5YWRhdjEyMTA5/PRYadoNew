package com.yado.pryado.pryadonew.util;

import android.text.TextUtils;

import com.blankj.utilcode.util.ToastUtils;
import com.yado.pryado.pryadonew.MyApplication;
import com.yado.pryado.pryadonew.MyConstants;
import com.yado.pryado.pryadonew.net.PRRetrofit;
import com.yado.pryado.pryadonew.subscriber.PRSubscriber;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public abstract class LoginManager {

    public void tryLogin() {
        String name = SharedPrefUtil.getInstance(MyApplication.getInstance()).getT(MyConstants.USERNAME, "");
        String pwd = SharedPrefUtil.getInstance(MyApplication.getInstance()).getT(MyConstants.PWD, "");
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(pwd)) {
            login(name, pwd);
        }else {
            goToLogin();
        }
    }

    private void login(final String name, final String pwd) {
        PRRetrofit.getInstance(MyApplication.getInstance()).getApi().userLogin(name, pwd, Util.getIMEI(MyApplication.getInstance().getApplicationContext()))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new PRSubscriber<String>() {

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onNext(String s) {
                        if (!TextUtils.isEmpty(s)) {
                            if(s.contains("-1")) {
                                ToastUtils.showShort("此账号已登录！");
                            } else if (s.contains("请检查用户名密码")) {
                                ToastUtils.showShort(s);
                            } else {
                                goToMain();
                            }
                        } else {
                            ToastUtils.showShort("用户名或密码错误!");
                            goToLogin();
                        }
                    }
                });
    }

    public abstract void goToMain();

    public abstract void goToLogin();
}
