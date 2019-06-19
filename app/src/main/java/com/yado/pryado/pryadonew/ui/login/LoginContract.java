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
        /**
         * 跳转到MainActivity
         */
        void  goToMain();

        /**
         * 显示加载对话框
         */
        void showLoadingDialog();

        /**
         * 隐藏对话框
         */
        void hideLoadingDialog();

        /**
         * 显示确认登录提示框
         * @param username
         * @param password
         */
        void showCheckDialog(String username, String password);
    }

    interface Presenter extends BaseContract.BasePresenter<View>{

        /**
         * 检查是否登录
         */

        void checkLogin(String username, String password);

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
         * 检查是否登录
         */
        void checkLogin(String username, String password, INetListener<Object, Throwable, Object> listener);

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
