package com.yado.pryado.pryadonew.ui.mod_pwd;

import android.text.TextUtils;

import com.blankj.utilcode.util.ToastUtils;
import com.yado.pryado.pryadonew.MyApplication;
import com.yado.pryado.pryadonew.MyConstants;
import com.yado.pryado.pryadonew.base.BasePresenter;
import com.yado.pryado.pryadonew.bean.ChangePwNean;
import com.yado.pryado.pryadonew.greendao.UserEntityDao;
import com.yado.pryado.pryadonew.greendaoEntity.UserEntity;
import com.yado.pryado.pryadonew.net.INetListener;
import com.yado.pryado.pryadonew.util.SharedPrefUtil;

import javax.inject.Inject;

/**
 * Created by OnexZgj on 2018/9/11:09:37.
 * des:
 */

public class PasswordSettingPresent extends BasePresenter<PasswordSettingContract.View, PasswordSettingModel> implements PasswordSettingContract.Presenter {

//    @Inject
//    CartoonFragmentModel model;

    /**
     * 注入到Fragment
     */
    @Inject
    public PasswordSettingPresent() {
    }

    @Override
    public void changePW(String username, String oldPassword, String newPassword) {
        mModel.changePW(username, oldPassword, newPassword, new INetListener<Object, Throwable, Object>() {
            @Override
            public void success(Object o) {
                mView.setChangePwdBean((ChangePwNean) o);
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
