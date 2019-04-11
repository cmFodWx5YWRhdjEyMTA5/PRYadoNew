package com.yado.pryado.pryadonew.ui.main;

import android.os.Handler;

import com.yado.pryado.pryadonew.base.BaseContract;
import com.yado.pryado.pryadonew.bean.Update;
import com.yado.pryado.pryadonew.net.INetListener;

/**
 * Created by yuong on 2018/11/15.
 * des:
 */

public interface MainContract {


    interface View extends BaseContract.BaseView {
        void showDownloadDialog(Update update);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {

        /**
         * 检查更新
         */
        void CheckUpdate(int versionCode);

        /**
         * 下载安装
         */
        void DownLoadApk(String downloadUrl, Handler handler);


    }

    interface Model extends BaseContract.BaseModel {
        /**
         * 检查更新
         */
        void CheckUpdate(int versionCode, INetListener<Object, Throwable, Object> listener);

        /**
         * 下载安装
         */
        void DownLoadApk(String downloadUrl, Handler handler, INetListener<Object, Throwable, Object> listener);
    }


}
