package com.yado.pryado.pryadonew.ui.affair;

import com.yado.pryado.pryadonew.base.BaseContract;
import com.yado.pryado.pryadonew.net.INetListener;

/**
 * Created by yuong on 2018/11/15.
 * des:
 */

public interface AffairContract {


    interface View extends BaseContract.BaseView {
        //获取代办数量
        int getCounts();

        //设置代办数量
        void setCounts(int count);

        //设置角标
        void setBadgeView(int count);

    }

    interface Presenter extends BaseContract.BasePresenter<View> {
        //获取代办数量
        void getCounts();

        //设置代办数量
        void setCounts(int count);


    }

    interface Model extends BaseContract.BaseModel {
        //获取代办数量
        void getCounts(INetListener<Object, Throwable, Object> listener);

        //设置代办数量
        void setCounts(int count, INetListener<Object, Throwable, Object> listener);
    }


}
