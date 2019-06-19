package com.yado.pryado.pryadonew.ui.todo;

import com.yado.pryado.pryadonew.base.BaseContract;
import com.yado.pryado.pryadonew.bean.ListmapBean;
import com.yado.pryado.pryadonew.bean.OrderDetail;
import com.yado.pryado.pryadonew.net.INetListener;
import com.yado.pryado.pryadonew.ui.widgit.EmptyLayout;

import java.util.List;

/**
 * Created by yuong on 2018/11/15.
 * des:
 */

public interface TodoContract {


    interface View extends BaseContract.BaseView {
        //设置工单列表
        void setOrderList(List<ListmapBean> orderList);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {

        /**
         * 获取代办
         */
        void getAwaitOrder(String orderType, EmptyLayout emptyLayout);

    }

    interface Model extends BaseContract.BaseModel {
        /**
         * 获取代办
         */
        void getAwaitOrder(String orderType, EmptyLayout emptyLayout, INetListener<Object, Throwable, Object> listener);

    }


}
