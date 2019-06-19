package com.yado.pryado.pryadonew.ui.login;

import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.yado.pryado.pryadonew.MyApplication;
import com.yado.pryado.pryadonew.MyConstants;
import com.yado.pryado.pryadonew.base.BasePresenter;
import com.yado.pryado.pryadonew.greendao.UserEntityDao;
import com.yado.pryado.pryadonew.greendaoEntity.UserEntity;
import com.yado.pryado.pryadonew.net.INetListener;
import com.yado.pryado.pryadonew.util.SharedPrefUtil;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by OnexZgj on 2018/9/11:09:37.
 * des:
 */

public class LoginPresent extends BasePresenter<LoginContract.View, LoginModel> implements LoginContract.Presenter {
    /**
     * 注入到Fragment
     */
    @Inject
    public LoginPresent() {
    }

    /**
     * 检查是否登录
     */
    @Override
    public void checkLogin(final String username, final String password) {
        mView.showLoadingDialog();
        mModel.checkLogin(username, password, new INetListener<Object, Throwable, Object>() {
            @Override
            public void success(Object o) {
                String msg = (String) o;
                if (!TextUtils.isEmpty(msg)){
                    if (msg.equals("1")){
                        login(username, password);
                    } else if (msg.equals("2")){
                        mView.showCheckDialog(username, password);
                    } else {
                        ToastUtils.showShort("登录失败！");
                        mView.hideLoadingDialog();
                    }
                } else {
                    ToastUtils.showShort("登录失败！");
                    mView.hideLoadingDialog();
                }

            }

            @Override
            public void failed(Throwable throwable) {
                ToastUtils.showShort("登录失败！");
                mView.hideLoadingDialog();
            }

            @Override
            public void loading(Object o) {

            }
        });
    }

    /**
     *用户登录
     */
    @Override
    public void login(final String username, final String password) {
        mModel.login(username, password, new INetListener<Object, Throwable, Object>() {
            @Override
            public void success(Object o) {
                String msg = (String) o;
                Log.e("login", msg);
                if (!TextUtils.isEmpty(msg)) {
                    if (msg.contains("请检查用户名密码")) {
                        ToastUtils.showShort(msg);
                        mView.hideLoadingDialog();
                    } else {//5rlc5akq3eog0peoiwjrhtd3☆1
                        SharedPrefUtil.getInstance(MyApplication.getInstance()).saveObject("Sessionid", msg);
                        SharedPrefUtil.getInstance(MyApplication.getInstance()).saveObject(MyConstants.USERNAME, username);
                        SharedPrefUtil.getInstance(MyApplication.getInstance()).saveObject(MyConstants.PWD, password);
                        UserEntityDao userEntityDao = MyApplication.getInstance().getDaoSession().getUserEntityDao();
                        UserEntity user = userEntityDao.queryBuilder().where(UserEntityDao.Properties.Username.eq(username)).unique();
                        if (user != null) {
                            userEntityDao.update(user);
                        } else {
                            userEntityDao.insert(new UserEntity(null, username, password, msg));
                        }
                        mView.goToMain();
                    }
                }
                mView.hideLoadingDialog();

            }

            @Override
            public void failed(Throwable throwable) {
                mView.hideLoadingDialog();
            }

            @Override
            public void loading(Object o) {

            }
        });
    }

    /**
     * 取消登录
     */
    @Override
    public void cancelLogin() {
        mModel.cancelLogin();
    }
}
