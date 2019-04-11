package com.yado.pryado.pryadonew.ui.hisOrder;

import com.yado.pryado.pryadonew.base.BaseContract;
import com.yado.pryado.pryadonew.net.INetListener;
import com.yado.pryado.pryadonew.ui.widgit.EmptyLayout;

/**
 * Created by yuong on 2018/11/15.
 * des:
 */

public interface HisOrderContract {


    interface View extends BaseContract.BaseView {

    }

    interface Presenter extends BaseContract.BasePresenter<View> {

        /**
         * 获取历史工单
         */
        void getHisOrderList(String username, int orderState, String orderType, EmptyLayout emptyLayout);


    }

    interface Model extends BaseContract.BaseModel {
        /**
         * 获取历史工单
         */
        void getHisOrderList(String username, int orderState, String orderType, EmptyLayout emptyLayout, INetListener<Object, Throwable, Object> listener);

    }


}
