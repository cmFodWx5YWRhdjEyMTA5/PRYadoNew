package com.yado.pryado.pryadonew.ui.todo;

import com.blankj.utilcode.util.ToastUtils;
import com.yado.pryado.pryadonew.base.BasePresenter;
import com.yado.pryado.pryadonew.bean.ListmapBean;
import com.yado.pryado.pryadonew.bean.OrderDetail;
import com.yado.pryado.pryadonew.bean.OrderList;
import com.yado.pryado.pryadonew.net.INetListener;
import com.yado.pryado.pryadonew.ui.task.TaskActivity;
import com.yado.pryado.pryadonew.ui.widgit.EmptyLayout;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by OnexZgj on 2018/9/11:09:37.
 * des:
 */

public class TodoPresent extends BasePresenter<TodoContract.View, TodoModel> implements TodoContract.Presenter {

    @Inject
    public TodoPresent() {
    }

    @Override
    public void getAwaitOrder(String orderType, EmptyLayout emptyLayout) {
        mModel.getAwaitOrder(orderType, emptyLayout, new INetListener<Object, Throwable, Object>() {
            @Override
            public void success(Object o) {
                mView.setOrderList((List<ListmapBean>) o);
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
