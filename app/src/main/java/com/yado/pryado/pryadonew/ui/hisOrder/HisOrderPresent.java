package com.yado.pryado.pryadonew.ui.hisOrder;

import com.blankj.utilcode.util.ToastUtils;
import com.yado.pryado.pryadonew.base.BasePresenter;
import com.yado.pryado.pryadonew.bean.AlertBean;
import com.yado.pryado.pryadonew.bean.HisData;
import com.yado.pryado.pryadonew.bean.OrderList;
import com.yado.pryado.pryadonew.bean.RoomListBean;
import com.yado.pryado.pryadonew.net.INetListener;
import com.yado.pryado.pryadonew.ui.alert.AlertActivity;
import com.yado.pryado.pryadonew.ui.alert.AlertFragment;
import com.yado.pryado.pryadonew.ui.widgit.EmptyLayout;

import javax.inject.Inject;

/**
 * Created by OnexZgj on 2018/9/11:09:37.
 * des:
 */

public class HisOrderPresent extends BasePresenter<HisOrderContract.View, HisOrderModel> implements HisOrderContract.Presenter {

    /**
     * 注入到Fragment
     */
    @Inject
    public HisOrderPresent() {
    }

    /**
     * 获取历史工单
     */
    @Override
    public void getHisOrderList(String username, int orderState, String orderType, EmptyLayout emptyLayout) {
        mModel.getHisOrderList(username, orderState, orderType, emptyLayout,  new INetListener<Object, Throwable, Object>() {
            @Override
            public void success(Object o) {
                OrderList orderList = (OrderList) o;
                ((HisOrderActivity) mView).setOrderList(orderList);

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
