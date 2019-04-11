package com.yado.pryado.pryadonew.ui.report;

import com.yado.pryado.pryadonew.base.BaseContract;
import com.yado.pryado.pryadonew.net.INetListener;

/**
 * Created by yuong on 2018/11/15.
 * des:
 */

public interface ReportContract {


    interface View extends BaseContract.BaseView {

    }

    interface Presenter extends BaseContract.BasePresenter<View> {

        /**
         * 获取站室
         */
        void getRoomList();

        /**
         * 隐患上报
         */
        void postNewBug(int pid, String bugLocation, String bugDesc);



    }

    interface Model extends BaseContract.BaseModel {

        /**
         * 获取站室
         */
        void getRoomList(INetListener<Object, Throwable, Object> listener);

        /**
         * 隐患上报
         */
        void postNewBug(int pid, String bugLocation, String bugDesc, INetListener<Object, Throwable, Object> listener);


    }


}
