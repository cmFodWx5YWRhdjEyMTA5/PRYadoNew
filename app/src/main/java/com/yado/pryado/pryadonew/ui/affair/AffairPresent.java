package com.yado.pryado.pryadonew.ui.affair;

import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.yado.pryado.pryadonew.MyApplication;
import com.yado.pryado.pryadonew.MyConstants;
import com.yado.pryado.pryadonew.base.BasePresenter;
import com.yado.pryado.pryadonew.greendao.UserEntityDao;
import com.yado.pryado.pryadonew.greendaoEntity.UserEntity;
import com.yado.pryado.pryadonew.net.INetListener;
import com.yado.pryado.pryadonew.util.SharedPrefUtil;

import javax.inject.Inject;

/**
 * Created by OnexZgj on 2018/9/11:09:37.
 * des:
 */

public class AffairPresent extends BasePresenter<AffairContract.View, AffairModel> implements AffairContract.Presenter {

//    @Inject
//    CartoonFragmentModel model;

    /**
     * 注入到Fragment
     */
    @Inject
    public AffairPresent() {
    }

    //获取代办数量
    @Override
    public void getCounts() {
        mModel.getCounts(new INetListener<Object, Throwable, Object>() {
            @Override
            public void success(Object o) {
                mView.setCounts((Integer) o);
            }

            @Override
            public void failed(Throwable throwable) {
            }

            @Override
            public void loading(Object o) {

            }
        });
    }

    //设置代办数量
    @Override
    public void setCounts(int count) {
        mModel.setCounts(count, new INetListener<Object, Throwable, Object>() {
            @Override
            public void success(Object o) {
                if ((Integer)o > 0) {
                    mView.setBadgeView((Integer) o);
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
}
