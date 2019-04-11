package com.yado.pryado.pryadonew.ui.main;

import android.os.Handler;

import com.yado.pryado.pryadonew.MyApplication;
import com.yado.pryado.pryadonew.base.BasePresenter;
import com.yado.pryado.pryadonew.bean.Update;
import com.yado.pryado.pryadonew.net.INetListener;
import com.yado.pryado.pryadonew.util.SharedPrefUtil;

import javax.inject.Inject;

/**
 * Created by OnexZgj on 2018/9/11:09:37.
 * des:
 */

public class MainPresent extends BasePresenter<MainContract.View, MainModel> implements MainContract.Presenter {

//    @Inject
//    CartoonFragmentModel model;

    /**
     * 注入到Fragment
     */
    @Inject
    public MainPresent() {
    }


    @Override
    public void CheckUpdate(final int versionCode) {
        mModel.CheckUpdate(versionCode, new INetListener<Object, Throwable, Object>() {
            @Override
            public void success(Object o) {
                final Update update = (Update) o;
                if (update.getVersionCode() > versionCode) {
                    if (!SharedPrefUtil.getInstance(MyApplication.getInstance()).getT("isPopUp", false)) {
                        mView.showDownloadDialog(update);
                    }
                }
            }

            @Override
            public void failed(Throwable throwable) {
            }

            @Override
            public void loading(Object o) {

            }
        });
    }

    @Override
    public void DownLoadApk(String downloadUrl, Handler handler) {
        mModel.DownLoadApk(downloadUrl, handler, new INetListener<Object, Throwable, Object>() {
            @Override
            public void success(Object o) {

            }

            @Override
            public void failed(Throwable throwable) {

            }

            @Override
            public void loading(Object o) {

            }
        });
    }
}
