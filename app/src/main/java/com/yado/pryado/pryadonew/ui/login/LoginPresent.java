package com.yado.pryado.pryadonew.ui.login;

import android.text.TextUtils;

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

    @Override
    public void login(final String username, final String password) {
        mView.showLoadingDialog();
        mModel.login(username, password, new INetListener<Object, Throwable, Object>() {
            @Override
            public void success(Object o) {
                String msg = (String) o;
                mView.hideLoadingDialog();
                if (!TextUtils.isEmpty(msg) && ! msg.contains("错误")) {
                    SharedPrefUtil.getInstance(MyApplication.getInstance()).saveObject(MyConstants.USERNAME, username);
                    SharedPrefUtil.getInstance(MyApplication.getInstance()).saveObject(MyConstants.PWD, password);
                    SharedPrefUtil.getInstance(MyApplication.getInstance()).saveObject("Sessionid", msg);
                    UserEntityDao userEntityDao = MyApplication.getInstance().getDaoSession().getUserEntityDao();
                    UserEntity user = userEntityDao.queryBuilder().where(UserEntityDao.Properties.Username.eq(username)).unique();
                    if (user != null) {
                        userEntityDao.update(user);
                    } else {
                        userEntityDao.insert(new UserEntity(null, username, password, msg));
                    }
                    mView.goToMain();
                } else {
                    ToastUtils.showShort(msg);
                }
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

    @Override
    public void cancelLogin() {
        mModel.cancelLogin();
    }
}
