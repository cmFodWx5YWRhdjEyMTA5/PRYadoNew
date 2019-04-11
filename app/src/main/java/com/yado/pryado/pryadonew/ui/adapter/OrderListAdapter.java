package com.yado.pryado.pryadonew.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yado.pryado.pryadonew.MyConstants;
import com.yado.pryado.pryadonew.R;
import com.yado.pryado.pryadonew.bean.ListmapBean;
import com.yado.pryado.pryadonew.ui.hisOrder.HisOrderActivity;
import com.yado.pryado.pryadonew.util.ItemAnimHelper;
import com.yado.pryado.pryadonew.util.StringUtils;

import java.util.List;
import java.util.Random;

import javax.inject.Inject;

public class OrderListAdapter extends BaseQuickAdapter<ListmapBean, BaseViewHolder> {

    private ItemAnimHelper itemAnimHelper;
    private Context context;

    @Inject
    public OrderListAdapter() {
        super(R.layout.his_order_item, null);
        itemAnimHelper = new ItemAnimHelper();
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public OrderListAdapter(int layoutResId, @Nullable List<ListmapBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ListmapBean item) {
        Random random = new Random();
        int i = random.nextInt(MyConstants.array.length);
        ((CardView) helper.getView(R.id.cardView)).setCardBackgroundColor(MyConstants.array[i]);
        itemAnimHelper.showItemAnim(helper.itemView, helper.getPosition() - 1);
        int position = helper.getLayoutPosition() + 1;
        helper.setText(R.id.order_name, position + "." + item.getPName() + item.getOrderName());
        if (context instanceof HisOrderActivity) {
            helper.setText(R.id.time, "检查日期:" + StringUtils.friendly_time(item.getCheckDate()));
        } else {
            helper.setText(R.id.time, "检查日期:" + StringUtils.friendly_time(item.getPlanDate()));
        }
//        helper.setText(R.id.time, "检查日期:" + StringUtils.friendly_time(item.getCheckDate()));
    }
}
