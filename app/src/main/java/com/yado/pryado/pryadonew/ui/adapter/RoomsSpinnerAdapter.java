package com.yado.pryado.pryadonew.ui.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yado.pryado.pryadonew.R;
import com.yado.pryado.pryadonew.bean.RoomListBean;

import java.util.List;

import javax.inject.Inject;

public class RoomsSpinnerAdapter  extends BaseQuickAdapter<RoomListBean.RowsEntity, BaseViewHolder> {
    @Inject
    public RoomsSpinnerAdapter() {
        super(R.layout.device_item, null);
    }

    public RoomsSpinnerAdapter(int layoutResId, @Nullable List<RoomListBean.RowsEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RoomListBean.RowsEntity item) {
        helper.setText(R.id.device_name, item.getName());
    }
}
