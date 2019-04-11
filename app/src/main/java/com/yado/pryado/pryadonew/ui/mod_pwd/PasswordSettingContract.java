package com.yado.pryado.pryadonew.ui.mod_pwd;

import com.yado.pryado.pryadonew.base.BaseContract;
import com.yado.pryado.pryadonew.bean.ChangePwNean;
import com.yado.pryado.pryadonew.net.INetListener;

/**
 * Created by yuong on 2018/11/15.
 * des:
 */

public interface PasswordSettingContract {


    interface View extends BaseContract.BaseView{
       void setChangePwdBean(ChangePwNean changePwdBean);
    }

    interface Presenter extends BaseContract.BasePresenter<View>{

        /**
         * 修改密码
         * @param username  用户名
         * @param oldPassword   旧密码
         * @param newPassword   新密码
         */
      void changePW(String username, String oldPassword,String newPassword);

    }

    interface Model extends BaseContract.BaseModel {
        /**
         * 修改密码
         * @param username  用户名
         * @param oldPassword   旧密码
         * @param newPassword   新密码
         */
        void changePW(String username, String oldPassword,String newPassword, INetListener<Object, Throwable, Object> listener);
    }



}
