package com.yado.pryado.pryadonew.subscriber;

import android.app.Dialog;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.yado.pryado.pryadonew.MyApplication;
import com.yado.pryado.pryadonew.MyConstants;
import com.yado.pryado.pryadonew.R;
import com.yado.pryado.pryadonew.exception.ApiExceptionFactory;
import com.yado.pryado.pryadonew.ui.widgit.EmptyLayout;
import com.yado.pryado.pryadonew.ui.widgit.MyProgressDialog;
import com.yado.pryado.pryadonew.util.LoginManager;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class PRSubscriber<T> implements Observer<T> {
    private EmptyLayout emptyLayout;
    private MyProgressDialog dialog;

    protected PRSubscriber() {
        this(null);
    }

    protected PRSubscriber(Object view) {
        if (view instanceof EmptyLayout) {
            this.emptyLayout = (EmptyLayout) view;
        } else if (view instanceof MyProgressDialog) {
            this.dialog = (MyProgressDialog) view;
        }
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T t) {

    }

    @Override
    public void onError(Throwable e) {
        if (emptyLayout != null) {
            emptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
        }
        if (dialog != null) {
            dialog.dismiss();
        }
//        ToastUtils.showShort(ApiExceptionFactory.getApiException(e).getDisplayMessage());
        if (!MyApplication.getInstance().getString(R.string.auth_error).equals(ApiExceptionFactory.getApiException(e).getDisplayMessage())) {
//            ToastUtils.showShort(ApiExceptionFactory.getApiException(e).getDisplayMessage());
        }
        switch (ApiExceptionFactory.getApiException(e).getCode()) {
            case MyConstants.NETWORD_ERROR:
                ToastUtils.showShort(MyApplication.getContext().getString(R.string.server_connect_error));
                break;
            case MyConstants.CONNECT_ERROR:
                ToastUtils.showShort(MyApplication.getInstance().getString(R.string.timeout_error));
            case MyConstants.ERROR404:
//                ToastUtils.showShort(MyApplication.getContext().getString(R.string.error404));
                break;
            default:
                break;
        }
//        if (!MyApplication.getInstance().getString(R.string.server_connect_error).equals(ApiExceptionFactory.getApiException(e).getDisplayMessage())) {
//            ToastUtils.showShort(ApiExceptionFactory.getApiException(e).getDisplayMessage());
//        }
        if (ApiExceptionFactory.getApiException(e).getCode() == MyConstants.AUTH_ERROR) {
            LoginManager loginManager = new LoginManager() {

                @Override
                public void goToMain() {

                }

                @Override
                public void goToLogin() {

                }
            };
            loginManager.tryLogin();
        }
    }

    @Override
    public void onComplete() {

    }
}
