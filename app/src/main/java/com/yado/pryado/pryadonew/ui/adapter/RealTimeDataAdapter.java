package com.yado.pryado.pryadonew.ui.adapter;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yado.pryado.pryadonew.MyApplication;
import com.yado.pryado.pryadonew.MyConstants;
import com.yado.pryado.pryadonew.R;
import com.yado.pryado.pryadonew.bean.DeviceDetailBean2;

import java.lang.reflect.Field;
import java.util.List;

import javax.inject.Inject;

public class RealTimeDataAdapter extends BaseQuickAdapter<DeviceDetailBean2.RealTimeParamsBean, BaseViewHolder> {

    @Inject
    public RealTimeDataAdapter() {
        super(R.layout.item_real_time, null);
    }

    public RealTimeDataAdapter(int layoutResId, @Nullable List<DeviceDetailBean2.RealTimeParamsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DeviceDetailBean2.RealTimeParamsBean item) {
        if (item.getPName().contains("()")) {
            helper.setText(R.id.test_name, item.getPName().substring(0, item.getPName().length() - 2));
        } else {
            helper.setText(R.id.test_name, item.getPName());
            helper.addOnClickListener(R.id.test_all);
            helper.addOnClickListener(R.id.test_a);
            helper.addOnClickListener(R.id.test_b);
            helper.addOnClickListener(R.id.test_c);
        }
        boolean isContaintDataTypeID = false;
        try {
            isContaintDataTypeID = checkObjFieldIsNull(item);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        if (isContaintDataTypeID) { //不包含
            if (item.getValue() == null) {
                helper.setGone(R.id.test_all, false);
                helper.setGone(R.id.test_a, true);
                helper.setGone(R.id.test_b, true);
                helper.setGone(R.id.test_c, true);
                helper.setText(R.id.test_a, item.getValueA());
                helper.setText(R.id.test_b, item.getValueB());
                helper.setText(R.id.test_c, item.getValueC());

                handllll(item.getStatusA(), (TextView) helper.getView(R.id.test_a), "");
                handllll(item.getStatusB(), (TextView) helper.getView(R.id.test_b), "");
                handllll(item.getStatusC(), (TextView) helper.getView(R.id.test_c), "");
            } else {
                helper.setGone(R.id.test_all, true);
                helper.setGone(R.id.test_a, false);
                helper.setGone(R.id.test_b, false);
                helper.setGone(R.id.test_c, false);
                helper.setText(R.id.test_all, item.getValue());
                handllll(item.getValue(), (TextView) helper.getView(R.id.test_all), "");
            }
        }else {
            if (item.getDataTypeID() != null && item.getDataTypeID().equals("99") || item.getDataTypeID().equals("23")) {
                if (item.getValue() == null) {
                    helper.setGone(R.id.test_all, false);
                    helper.setGone(R.id.test_a, true);
                    helper.setGone(R.id.test_b, true);
                    helper.setGone(R.id.test_c, true);
                    helper.setText(R.id.test_a, item.getValueA());
                    helper.setText(R.id.test_b, item.getValueB());
                    helper.setText(R.id.test_c, item.getValueC());
//                    if (item.getValueA().equals("1")) {
//                        helper.setText(R.id.test_a, "带电");
//
//                    } else {
//                        helper.setText(R.id.test_a, "不带电");
//                    }
//                    if (item.getValueB().equals("1")) {
//                        helper.setText(R.id.test_b, "带电");
//                    } else {
//                        helper.setText(R.id.test_b, "不带电");
//                    }
//                    if (item.getValueC().equals("1")) {
//                        helper.setText(R.id.test_c, "带电");
//                    } else {
//                        helper.setText(R.id.test_c, "不带电");
//                    }
                    handllll(item.getStatusA(), (TextView) helper.getView(R.id.test_a), item.getDataTypeID());
                    handllll(item.getStatusB(), (TextView) helper.getView(R.id.test_b), item.getDataTypeID());
                    handllll(item.getStatusC(), (TextView) helper.getView(R.id.test_c), item.getDataTypeID());
                } else {
                    helper.setGone(R.id.test_all, true);
                    helper.setGone(R.id.test_a, false);
                    helper.setGone(R.id.test_b, false);
                    helper.setGone(R.id.test_c, false);
                    helper.setText(R.id.test_all, item.getValue());
//                    if (item.getValue().equals("1")) {
//                        helper.setText(R.id.test_all, "带电");
//
//                    } else {
//                        helper.setText(R.id.test_all, "不带电");
//                    }
                }
                handllll(item.getValue(), (TextView) helper.getView(R.id.test_all), item.getDataTypeID());

            } else {
                if (item.getValue() == null) {
                    helper.setGone(R.id.test_all, false);
                    helper.setGone(R.id.test_a, true);
                    helper.setGone(R.id.test_b, true);
                    helper.setGone(R.id.test_c, true);
                    helper.setText(R.id.test_a, item.getValueA());
                    helper.setText(R.id.test_b, item.getValueB());
                    helper.setText(R.id.test_c, item.getValueC());

                    handllll(item.getStatusA(), (TextView) helper.getView(R.id.test_a), item.getDataTypeID());
                    handllll(item.getStatusB(), (TextView) helper.getView(R.id.test_b), item.getDataTypeID());
                    handllll(item.getStatusC(), (TextView) helper.getView(R.id.test_c), item.getDataTypeID());
                } else {
                    helper.setGone(R.id.test_all, true);
                    helper.setGone(R.id.test_a, false);
                    helper.setGone(R.id.test_b, false);
                    helper.setGone(R.id.test_c, false);
                    helper.setText(R.id.test_all, item.getValue());
                    handllll(item.getValue(), (TextView) helper.getView(R.id.test_all), item.getDataTypeID());
                }
            }
        }

//        if (item.getPName().contains("温度")) {
//            if (item.getValue() == null) {
//                helper.setGone(R.id.test_all, false);
//                helper.setGone(R.id.test_a, true);
//                helper.setGone(R.id.test_b, true);
//                helper.setGone(R.id.test_c, true);
//                helper.setText(R.id.test_a, item.getValueA());
//                helper.setText(R.id.test_b, item.getValueB());
//                helper.setText(R.id.test_c, item.getValueC());
//
//                handllll(item.getStatusA(), (TextView) helper.getView(R.id.test_a));
//                handllll(item.getStatusB(), (TextView) helper.getView(R.id.test_b));
//                handllll(item.getStatusC(), (TextView) helper.getView(R.id.test_c));
//            } else {
//                helper.setGone(R.id.test_all, true);
//                helper.setGone(R.id.test_a, false);
//                helper.setGone(R.id.test_b, false);
//                helper.setGone(R.id.test_c, false);
//                helper.setText(R.id.test_all, item.getValue());
//                handllll(item.getValue(), (TextView) helper.getView(R.id.test_all));
//            }
//        } else if (item.getDataTypeID() != null && item.getDataTypeID().equals("99") || item.getDataTypeID().equals("23")) {
//            if (item.getValue() == null) {
//                helper.setGone(R.id.test_all, false);
//                helper.setGone(R.id.test_a, true);
//                helper.setGone(R.id.test_b, true);
//                helper.setGone(R.id.test_c, true);
//                if (item.getValueA().equals("1")) {
//                    helper.setText(R.id.test_a, "带电");
//
//                } else {
//                    helper.setText(R.id.test_a, "不带电");
//                }
//                if (item.getValueB().equals("1")) {
//                    helper.setText(R.id.test_b, "带电");
//                } else {
//                    helper.setText(R.id.test_b, "不带电");
//                }
//                if (item.getValueC().equals("1")) {
//                    helper.setText(R.id.test_c, "带电");
//                } else {
//                    helper.setText(R.id.test_c, "不带电");
//                }
//            }else {
//                helper.setGone(R.id.test_all, true);
//                helper.setGone(R.id.test_a, false);
//                helper.setGone(R.id.test_b, false);
//                helper.setGone(R.id.test_c, false);
//                helper.setText(R.id.test_all, item.getValue());
//                if (item.getValue().equals("1")) {
//                    helper.setText(R.id.test_all, "带电");
//
//                } else {
//                    helper.setText(R.id.test_all, "不带电");
//                }
//            }
//        } else if (!item.getPName().contains("()")){
//            if (item.getValue() == null) {
//                helper.setGone(R.id.test_all, false);
//                helper.setGone(R.id.test_a, true);
//                helper.setGone(R.id.test_b, true);
//                helper.setGone(R.id.test_c, true);
//
//                helper.setText(R.id.test_a, item.getValueA());
//                helper.setText(R.id.test_b, item.getValueB());
//                helper.setText(R.id.test_c, item.getValueC());
//
//                handllll(item.getStatusA(), (TextView) helper.getView(R.id.test_a));
//                handllll(item.getStatusB(), (TextView) helper.getView(R.id.test_b));
//                handllll(item.getStatusC(), (TextView) helper.getView(R.id.test_c));
//            } else {
//                helper.setGone(R.id.test_all, true);
//                helper.setGone(R.id.test_a, false);
//                helper.setGone(R.id.test_b, false);
//                helper.setGone(R.id.test_c, false);
//                helper.setText(R.id.test_all, item.getValue());
//                handllll(item.getValue(), (TextView) helper.getView(R.id.test_all));
//            }
//        }

    }

    private void handllll(String status, TextView textView, String str) {
        if (textView == null || TextUtils.isEmpty(status)) {
            return;
        }
        int reId = 0;
        if (!(str != null && str.equals("99") || str.equals("23"))) {
            switch (status) {
                case MyConstants.INNER_WJ:
                    reId = R.drawable.inner_wj;
                    break;
                case MyConstants.INNER_YZ:
                    reId = R.drawable.inner_yz;
                    break;
                case MyConstants.INNER_YB:
                    reId = R.drawable.inner_yb;
                    break;
                case MyConstants.INNER_ZC:
                    reId = R.drawable.inner_zc;
                    break;

                case MyConstants.OUT_WJ:
                    reId = R.drawable.out_wj;
                    break;
                case MyConstants.OUT_YZ:
                    reId = R.drawable.out_yz;
                    break;
                case MyConstants.OUT_YB:
                    reId = R.drawable.out_yb;
                    break;
                case MyConstants.OUT_YQR:
                    reId = R.drawable.out_yqr;
                    break;
                case MyConstants.OUT_ZC:
                    reId = R.drawable.out_zc;
                    break;

                case MyConstants.DANGER_WJ:
                    reId = R.drawable.danger_wj;
                    break;
                case MyConstants.DANGER_YQR:
                    reId = R.drawable.danger_yqr;
                    break;
                case MyConstants.DANGER_ZC:
                    reId = R.drawable.danger_zc;
                    break;

                case MyConstants.YANZHONG_WJ:
                    reId = R.drawable.yanzhong_wj;
                    break;
                case MyConstants.YANZHONG_YZ:
                    reId = R.drawable.yanzhong_yz;
                    break;
                case MyConstants.YANZHONG_YQR:
                    reId = R.drawable.yanzhong_yqr;
                    break;
                case MyConstants.YANZHONG_ZC:
                    reId = R.drawable.yanzhong_zc;
                    break;

                case MyConstants.YIBAN_WJ:
                    reId = R.drawable.yiban_wj;
                    break;
                case MyConstants.YIBAN_YZ:
                    reId = R.drawable.yiban_yz;
                    break;
                case MyConstants.YIBAN_YB:
                    reId = R.drawable.yiban_yb;
                    break;
                case MyConstants.YIBAN_YQR:
                    reId = R.drawable.yiban_yqr;
                    break;
                case MyConstants.YIBAN_ZC:
                    reId = R.drawable.yiban_zc;
                    break;

                case MyConstants.normal_wj:
                    reId = R.drawable.normal_wj;
                    break;
                case MyConstants.normal_yz:
                    reId = R.drawable.normal_yz;
                    break;
                case MyConstants.normal_yb:
                    reId = R.drawable.normal_yb;
                    break;
                case MyConstants.normal_yqr:
                    reId = R.drawable.normal_yqr;
                    break;
                default://normal_zc
                    reId = R.drawable.normal_zc;
                    break;
            }
        }
        if (reId != 0) {
            Drawable nav_up = MyApplication.getInstance().getResources().getDrawable(reId);
            nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
            textView.setCompoundDrawables(null, null, nav_up, null);
        } else {
            textView.setCompoundDrawables(null, null, null, null);

        }

    }

    private String handler(String valueA) {
        if ("0".equals(valueA)) {
            return "- -";
        } else {
            return valueA;
        }
    }

    private boolean checkObjFieldIsNull(Object obj) throws IllegalAccessException, NoSuchFieldException {

        boolean flag = false;
        Field f = obj.getClass().getDeclaredField("DataTypeID");
        f.setAccessible(true);
        if (f.get(obj) == null) {
            flag = true;
            return flag;
        }

        return flag;
    }

}
