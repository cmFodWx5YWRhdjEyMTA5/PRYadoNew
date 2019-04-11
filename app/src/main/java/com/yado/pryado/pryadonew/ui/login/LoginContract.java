package com.yado.pryado.pryadonew.ui.login;

import com.yado.pryado.pryadonew.base.BaseContract;
import com.yado.pryado.pryadonew.net.INetListener;

import java.util.List;

/**
 * Created by yuong on 2018/11/15.
 * des:
 */

public interface LoginContract {


    interface View extends BaseContract.BaseView{
        void  goToMain();

        void showLoadingDialog();

        void hideLoadingDialog();

    }

    interface Presenter extends BaseContract.BasePresenter<View>{

        /**
         *用户登录
         */
        void login(String username, String password);

        /**
         * 取消登录
         */
        void cancelLogin();

    }

    interface Model extends BaseContract.BaseModel {
        /**
         *用户登录
         */
        void login(String username, String password, INetListener<Object, Throwable, Object> listener);

        /**
         * 取消登录
         */
        void cancelLogin();
    }



}
