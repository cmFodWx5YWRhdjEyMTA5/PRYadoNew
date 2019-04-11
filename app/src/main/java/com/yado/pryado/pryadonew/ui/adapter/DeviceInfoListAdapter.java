package com.yado.pryado.pryadonew.ui.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yado.pryado.pryadonew.R;
import com.yado.pryado.pryadonew.bean.DeviceInfoListBean;

import java.util.List;

import javax.inject.Inject;

public class DeviceInfoListAdapter extends BaseQuickAdapter<DeviceInfoListBean.RowsBean, BaseViewHolder> {
    @Inject
    public DeviceInfoListAdapter() {
        super(R.layout.device_item, null);
    }

    public DeviceInfoListAdapter(int layoutResId, @Nullable List<DeviceInfoListBean.RowsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DeviceInfoListBean.RowsBean item) {
        helper.setText(R.id.device_name, item.getDeviceName());
    }
}
