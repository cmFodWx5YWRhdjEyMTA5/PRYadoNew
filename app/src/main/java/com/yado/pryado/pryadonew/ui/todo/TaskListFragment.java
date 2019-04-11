package com.yado.pryado.pryadonew.ui.todo;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yado.pryado.pryadonew.MyApplication;
import com.yado.pryado.pryadonew.MyConstants;
import com.yado.pryado.pryadonew.R;
import com.yado.pryado.pryadonew.base.BaseFragment;
import com.yado.pryado.pryadonew.bean.ListmapBean;
import com.yado.pryado.pryadonew.bean.StatusBean;
import com.yado.pryado.pryadonew.ui.adapter.OrderListAdapter;
import com.yado.pryado.pryadonew.ui.task.TaskActivity;
import com.yado.pryado.pryadonew.ui.widgit.EmptyLayout;
import com.yado.pryado.pryadonew.ui.widgit.SimplePaddingDecoration;
import com.yado.pryado.pryadonew.util.Util;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.leolin.shortcutbadger.ShortcutBadger;

public class TaskListFragment extends BaseFragment<TodoPresent> implements TodoContract.View {
    private static final String TASK_TYPE = "type";
    @BindView(R.id.rv_order_list)
    RecyclerView rvOrderList;
    @BindView(R.id.error_layout)
    EmptyLayout errorLayout;
    private String typeStr;

    @Inject
    OrderListAdapter adapter;

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Subscribe
    public void onMessageEvent(StatusBean change) {
        if (getString(R.string.submit_success).equals(change.getStatus())) {
            refresh();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_tasklist;
    }

    public static TaskListFragment newInstance(String type) {
        Bundle bundle = new Bundle();
        bundle.putString(TASK_TYPE, type);
        TaskListFragment fragment = new TaskListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    public void initView() {

    }

    @Override
    protected void loadData() {
        adapter.setContext(context);
        assert getArguments() != null;
        typeStr = getArguments().getString(TASK_TYPE);
        initRecyclerView();
        initListener();
        refresh();
        errorLayout.setOnLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh();
            }
        });
    }

    private void refresh() {
        errorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
        assert mPresenter != null;
        mPresenter.getAwaitOrder(typeStr, errorLayout);
    }

    private void initListener() {
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (!Util.isDoubleClick()){
                    ARouter.getInstance().build(MyConstants.TASK)
                            .withString("strType", typeStr)
                            .withInt("orderId", ((ListmapBean) (adapter.getData().get(position))).getOrderID())
                            .withInt("type", TaskActivity.TYPE_INI)
                            .navigation();
                }

            }
        });

        errorLayout.setOnLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh();
            }
        });
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rvOrderList.setLayoutManager(linearLayoutManager);
        rvOrderList.addItemDecoration(new SimplePaddingDecoration(context));
        rvOrderList.setAdapter(adapter);
    }

    @Override
    public void setOrderList(List<ListmapBean> orderList) {
        if (orderList.size() > 0) {
            errorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
            rvOrderList.setVisibility(View.VISIBLE);
            ShortcutBadger.applyCount(MyApplication.getInstance(), orderList.size());
        } else {
            errorLayout.setVisibility(View.VISIBLE);
            rvOrderList.setVisibility(View.GONE);
            errorLayout.setErrorType(EmptyLayout.NODATA);
        }
        adapter.setNewData(orderList);
    }

}
