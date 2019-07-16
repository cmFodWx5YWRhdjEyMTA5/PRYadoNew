package com.yado.pryado.pryadonew.ui.hisOrder;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yado.pryado.pryadonew.MyApplication;
import com.yado.pryado.pryadonew.MyConstants;
import com.yado.pryado.pryadonew.R;
import com.yado.pryado.pryadonew.base.BaseActivity;
import com.yado.pryado.pryadonew.bean.ListmapBean;
import com.yado.pryado.pryadonew.bean.OrderList;
import com.yado.pryado.pryadonew.ui.adapter.OrderListAdapter;
import com.yado.pryado.pryadonew.ui.widgit.EmptyLayout;
import com.yado.pryado.pryadonew.ui.widgit.SimplePaddingDecoration;
import com.yado.pryado.pryadonew.util.SharedPrefUtil;

import org.angmarch.views.NiceSpinner;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = "/ui/hisOrder/HisOrderActivity")
public class HisOrderActivity extends BaseActivity<HisOrderPresent> implements HisOrderContract.View {

    @BindView(R.id.emptyLayout)
    EmptyLayout emptyLayout;
    @BindView(R.id.rv_his_order)
    RecyclerView rvHisOrder;
    @BindView(R.id.tv_pre)
    TextView tvPre;
    @BindView(R.id.name)
    TextView name;

    @Inject
    OrderListAdapter adapter;//注入adapter

    private WeakReference<String> username;


    /**
     * 加载布局
     * @return
     */
    @Override
    public int inflateContentView() {
        return R.layout.activity_his_order;
    }

    /**
     * 注入 View
     */
    @Override
    protected void initInjector() {
        mActivityComponent.inject(this);
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        adapter.setContext(mContext);
        tvPre.setText(getResources().getString(R.string.back));
        name.setText(getResources().getString(R.string.his_order));
        username = new WeakReference<>(SharedPrefUtil.getInstance(MyApplication.getInstance()).getT(MyConstants.USERNAME, "unknown"));
        initRecyclerView();
        emptyLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
        initListener();
        assert mPresenter != null;
        mPresenter.getHisOrderList(username.get(), MyConstants.ORDER_TYPE_COMPLETE, null, emptyLayout);
    }

    /**
     * 初始化监听事件
     */
    private void initListener() {
        emptyLayout.setOnLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assert mPresenter != null;
                mPresenter.getHisOrderList(username.get(), MyConstants.ORDER_TYPE_COMPLETE, null, emptyLayout);
            }
        });

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                ARouter.getInstance().build(MyConstants.TASK)
                        .withString("strType", mContext.getString(R.string.his_order))
                        .withInt("orderId", ((ListmapBean) (adapter.getData().get(position))).getOrderID())
                        .withInt("type", 1)
                        .navigation();
            }
        });
    }

    public EmptyLayout getEmptyLayout() {
        return emptyLayout;
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        rvHisOrder.setLayoutManager(linearLayoutManager);
        rvHisOrder.addItemDecoration(new SimplePaddingDecoration(mContext));
        rvHisOrder.setAdapter(adapter);
    }

    /**
     * 是否注入EventBus
     * @return
     */
    @Override
    protected boolean isRegisterEventBus() {
        return false;
    }

    /**
     *是否注入 Arouter
     * @return
     */
    @Override
    protected boolean isNeedInject() {
        return false;
    }


    @OnClick(R.id.tv_shouye)
    public void onViewClicked() {
        finish();
    }

    /**
     * 设置工单列表
     * @param orderList
     */
    public void setOrderList(OrderList orderList) {
        if (orderList.getListmap() != null && orderList.getListmap().size() <= 0) {
            emptyLayout.setErrorType(EmptyLayout.NODATA);
        } else {
            rvHisOrder.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);
            adapter.setNewData(orderList.getListmap());
        }
    }

}
