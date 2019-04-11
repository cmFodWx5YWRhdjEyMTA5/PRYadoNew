package com.yado.pryado.pryadonew.ui.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yado.pryado.pryadonew.R;
import com.yado.pryado.pryadonew.bean.TempMonitorBean;

import java.util.List;

import javax.inject.Inject;

public class TempMonitorDataAdapter2 extends BaseQuickAdapter<TempMonitorBean, BaseViewHolder> {


    @Inject
    public TempMonitorDataAdapter2() {
        super(R.layout.item_temp_monitor2, null);
    }

    public TempMonitorDataAdapter2(int layoutResId, @Nullable List<TempMonitorBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TempMonitorBean item) {
        helper.setText(R.id.tv_point_temp, item.getTempValue());

        if (item.getTempRiseAlarm() != null) {
            setImage(item.getTempRiseAlarm(), (ImageView) helper.getView(R.id.iv_state));
        }
        if (item.getpName() != null && item.getpName().contains("柜内环境")) {
            helper.setText(R.id.tv_point_name, "环境温度(℃)");
            helper.setGone(R.id.tv_electric_current, false);
            helper.setGone(R.id.tv_health, false);
            helper.setGone(R.id.ll_state, false);
        } else {
            helper.setText(R.id.tv_point_name, item.getPosition());
            helper.setGone(R.id.tv_electric_current, true);
            helper.setGone(R.id.tv_health, true);
            helper.setGone(R.id.ll_state, true);

        }

    }

    private void setImage(String status, ImageView imageView) {
        int resId = 0;
        switch (status) {
            case "0":
                resId = R.drawable.ic_circle_green;
                break;
            case "1":
                resId = R.drawable.ic_circle_yellow;
                break;
            case "2":
                resId = R.drawable.ic_circle_oringe;
                break;
            case "3":
            case "-1":
                resId = R.drawable.ic_circle_red;
                break;
            default:
                break;
        }
        imageView.setImageResource(resId);

    }

}
