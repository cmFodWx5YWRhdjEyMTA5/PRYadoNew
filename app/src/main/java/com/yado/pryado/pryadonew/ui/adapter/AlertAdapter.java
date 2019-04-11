package com.yado.pryado.pryadonew.ui.adapter;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;
import com.yado.pryado.pryadonew.MyConstants;
import com.yado.pryado.pryadonew.R;
import com.yado.pryado.pryadonew.bean.AlertBean;
import com.yado.pryado.pryadonew.util.ItemAnimHelper;

import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

public class AlertAdapter extends BaseQuickAdapter<AlertBean.RowsBean, BaseViewHolder> {
    private ItemAnimHelper itemAnimHelper;

    @Inject
    public AlertAdapter() {
        super(R.layout.item_alert, null);
        itemAnimHelper = new ItemAnimHelper();
    }

    public AlertAdapter(int layoutResId, @Nullable List<AlertBean.RowsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AlertBean.RowsBean item) {
        int color = 0;
        if ((item.getAlarmConfirm()).equals("未确认")) {
            switch (item.getALarmType()) {
                case "正常":
                    color = MyConstants.COLORS[0];
                    break;
                case "恢复":
                    color = MyConstants.COLORS[0];
                    break;
                case "一般":
                    color = MyConstants.COLORS[1];
                    break;
                case "关注":
                    color = MyConstants.COLORS[1];
                    break;
                case "严重":
                    color = MyConstants.COLORS[2];
                    break;
                case "预警":
                    color = MyConstants.COLORS[2];
                    break;
                case "危急":
                    color = MyConstants.COLORS[3];
                    break;
                case "报警":
                    color = MyConstants.COLORS[3];
                    break;
                case "未监测":
                    color = MyConstants.COLORS[4];
                    break;
                default:
                    color = MyConstants.COLORS[0];
                    break;
            }
        } else {
            color = MyConstants.COLORS[4];
        }
        ((CardView) helper.getView(R.id.cardView)).setCardBackgroundColor(color);
        itemAnimHelper.showItemAnim(helper.itemView, helper.getPosition() - 1);
        String key2 = "";
        if (item.getAlarmCate().equals("带电状态")) {
            if (item.getAlarmValue().equals("0")) {
                key2 = "不带电";
            } else {
                key2 = "带电";
            }
        } else if (item.getAlarmCate().equals("开关量")) {
            if (item.getAlarmValue().equals("0")) {
                key2 = "断开";
            } else {
                key2 = "闭合";
            }
        } else {
            key2 = item.getAlarmValue();
        }
        highlightKeyword((TextView) helper.getView(R.id.alert_desc), item.getALarmType(), key2 + item.getUnits(), item.getALarmType() + "/" + item.getAlarmCate() + ":" + item.getCompany() + "/" + item.getAlarmArea() + "/" + item.getAlarmAddress() + ":" + key2 + item.getUnits());
//        helper.setText(R.id.alert_desc, item.getALarmType() + "/" + item.getAlarmCate() + ":" + item.getCompany() + "/" + item.getAlarmArea() + "/" + item.getAlarmAddress() + ":" + item.getAlarmValue());
//        helper.setText(R.id.alert_status, getConfirm(item.getAlarmState(), item.getALarmType()));
        helper.setText(R.id.alert_status, item.getAlarmConfirm());
        if (item.getALarmType().equals("恢复")) {
            helper.setText(R.id.alert_time, mContext.getResources().getString(R.string.restore_time) + item.getAlarmDateTime());

        }else {
            helper.setText(R.id.alert_time, mContext.getResources().getString(R.string.alarm_time) + item.getAlarmDateTime());
        }
        if (mContext.getString(R.string.unconfirm).equals(getConfirm(item.getAlarmState(), item.getALarmType())) || mContext.getString(R.string.huifu).equals(getConfirm(item.getAlarmState(), item.getALarmType()))) {
            helper.setGone(R.id.tv_comfirm, false);
        } else {
            helper.setVisible(R.id.tv_comfirm, true);
            helper.setText(R.id.tv_comfirm, "确认时间:" + item.getConfirmDate() + ";  确认人:" + item.getUserName());
        }
        if (helper.getAdapterPosition() == getData().size() - 1) {
            helper.setGone(R.id.view, false);
        } else {
            helper.setVisible(R.id.view, true);
        }
    }

    private String getConfirm(String alarmState, String aLarmType) {
        if (mContext.getString(R.string.huifu).equals(aLarmType)) {
            return mContext.getString(R.string.huifu);
        }
        switch (alarmState) {
            case "0":
                return mContext.getString(R.string.confirm);
            default:
                return mContext.getString(R.string.unconfirm);
        }
    }

    /**
     * 高亮某个关键字，如果有多个则全部高亮
     */
    private void highlightKeyword(TextView textView, String key1, String key2,  String str) {

        SpannableString sp = new SpannableString(str);

        Pattern p1 = Pattern.compile(key1);
        Matcher m1 = p1.matcher(str);
        StyleSpan span = new StyleSpan(Typeface.BOLD);
        sp.setSpan(span, 0, key1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        while (m1.find()) {	//通过正则查找，逐个高亮
            int start = m1.start();
            int end = m1.end();
            sp.setSpan(new ForegroundColorSpan(Color.parseColor("#ffffff")), start ,end,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        Pattern p2 = Pattern.compile(key2);
        Matcher m2 = p2.matcher(str);
        StyleSpan span2 = new StyleSpan(Typeface.BOLD);
        sp.setSpan(span2, 0, key1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        while (m2.find()) {	//通过正则查找，逐个高亮
            int start = m2.start();
            int end = m2.end();
            sp.setSpan(new ForegroundColorSpan(Color.parseColor("#ffffff")), start ,end,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        textView.setText(sp);
    }



    private int getColor(String alarmState, String aLarmType) {
        if (mContext.getString(R.string.huifu).equals(aLarmType)) {
            return mContext.getResources().getColor(R.color.blue_title);
        }
        switch (alarmState) {
            case "0":
                return mContext.getResources().getColor(R.color.blue_title);
            case "1":
                return mContext.getResources().getColor(R.color.yiban);
            case "2":
                return mContext.getResources().getColor(R.color.yanzhong);
            default:
                return mContext.getResources().getColor(R.color.weiji);
        }

    }
}
