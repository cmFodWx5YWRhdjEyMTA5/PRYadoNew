package com.yado.pryado.pryadonew.ui.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yado.pryado.pryadonew.MyConstants;
import com.yado.pryado.pryadonew.R;
import com.yado.pryado.pryadonew.bean.AlertBean;
import com.yado.pryado.pryadonew.bean.BugList;
import com.yado.pryado.pryadonew.util.ItemAnimHelper;

import java.util.List;
import java.util.Random;

import javax.inject.Inject;

public class DangerAdapter extends BaseQuickAdapter<BugList.ListmapBean, BaseViewHolder> {
    private ItemAnimHelper itemAnimHelper;
    @Inject
    public DangerAdapter() {
        super(R.layout.item_danger, null);
        itemAnimHelper = new ItemAnimHelper();
    }
    public DangerAdapter(int layoutResId, @Nullable List<BugList.ListmapBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BugList.ListmapBean item) {
        itemAnimHelper.showItemAnim(helper.itemView, helper.getPosition() - 1);
        String str;
        switch (item.getBugLevel()) {
            case "紧急":
                str = getContentStr("紧急隐患:" + item.getPName() + "/" + item.getDeviceName() + "/" + item.getBugDesc(),20);
                helper.setTextColor(R.id.tv_dec, Color.RED);
                break;
            case "重大":
                str = getContentStr("重大隐患:" + item.getPName() + "/" + item.getDeviceName() + "/" + item.getBugDesc(),20);
                helper.setTextColor(R.id.tv_dec, Color.YELLOW);
                break;
            default:
                str = getContentStr("重大隐患:" + item.getPName() + "/" + item.getDeviceName() + "/" + item.getBugDesc(),20);
                helper.setTextColor(R.id.tv_dec, Color.BLACK);
                break;
        }

        helper.setText(R.id.tv_dec, str);
        Random random = new Random();
        int i = random.nextInt(MyConstants.array.length);
        ((CardView) helper.getView(R.id.cardView)).setCardBackgroundColor(MyConstants.array[i]);
    }

    private String getContentStr(String s,int length) {
        if (s.length() > length) {
            return s.substring(0,length)+"...";
        } else {
            return s;
        }
    }
}
