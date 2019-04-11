package com.yado.pryado.pryadonew.ui.mod_pwd;

import com.yado.pryado.pryadonew.MyApplication;
import com.yado.pryado.pryadonew.base.BaseModel;
import com.yado.pryado.pryadonew.bean.ChangePwNean;
import com.yado.pryado.pryadonew.net.INetListener;
import com.yado.pryado.pryadonew.net.PRRetrofit;
import com.yado.pryado.pryadonew.subscriber.PRSubscriber;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PasswordSettingModel extends BaseModel implements PasswordSettingContract.Model {

    /**
     * 注入到Presenter
     */
    @Inject
    public PasswordSettingModel() {
    }

    @Override
    public void changePW(String username, String oldPassword, String newPassword, final INetListener<Object, Throwable, Object> listener) {
        PRRetrofit.getInstance(MyApplication.getInstance()).getApi()
                .changePW(username, oldPassword, newPassword)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new PRSubscriber<ChangePwNean>() {

                    Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                        disposable = d;
                    }

                    @Override
                    public void onNext(ChangePwNean changePwNean) {
                        listener.success(changePwNean);
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
