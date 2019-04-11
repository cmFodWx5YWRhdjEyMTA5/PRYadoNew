package com.yado.pryado.pryadonew.ui.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yado.pryado.pryadonew.R;
import com.yado.pryado.pryadonew.bean.RoomListBean;
import com.yado.pryado.pryadonew.util.ItemAnimHelper;

import java.util.List;

import javax.inject.Inject;

public class RoomsAdapter extends BaseQuickAdapter<RoomListBean.RowsEntity, BaseViewHolder> {
    @Inject
    public RoomsAdapter() {
        super(R.layout.room_item, null);
    }

    public RoomsAdapter(int layoutResId, @Nullable List<RoomListBean.RowsEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RoomListBean.RowsEntity item) {
        helper.setText(R.id.room_name, item.getName());
        String status = item.getStatus();
        int textColor;
        if ("0".equals(status)) {
            status = "正常";
            textColor = Color.GREEN;
//            helper.setText(R.id.status, "正常");
//            helper.setTextColor(R.id.status, Color.GREEN);
        } else if ("1".equals(status)) {
            status = "关注";
            textColor = Color.parseColor("#FFFF00");
//            helper.setText(R.id.status, "关注");
//            helper.setTextColor(R.id.status, Color.parseColor("#FFFF00"));
        } else if ("2".equals(status)) {
            status = "预警";
            textColor = Color.parseColor("#FFA500");
//            helper.setText(R.id.status, "预警");
//            helper.setTextColor(R.id.status, Color.parseColor("#FFA500"));
        } else {
            status = "报警";
            textColor = Color.RED;
//            helper.setText(R.id.status, "报警");
//            helper.setTextColor(R.id.status, Color.RED);
        }
        helper.setText(R.id.status, status);
        helper.setTextColor(R.id.status, textColor);
    }
}
