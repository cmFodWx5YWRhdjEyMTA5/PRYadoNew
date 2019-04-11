package com.yado.pryado.pryadonew.ui.widgit;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yado.pryado.pryadonew.MyConstants;
import com.yado.pryado.pryadonew.R;

public class ResultDialog extends DialogFragment implements View.OnClickListener {


    private boolean hasOrder = false;
    private boolean hasRoom = false;
    private String hasDanger;

    public View.OnClickListener getOnClickListenerOrder() {
        return onClickListenerOrder;
    }

    public void setOnClickListenerOrder(View.OnClickListener onClickListenerOrder) {
        this.onClickListenerOrder = onClickListenerOrder;
    }

    public View.OnClickListener getOnClickListenerUp() {
        return onClickListenerUp;
    }

    public void setOnClickListenerUp(View.OnClickListener onClickListenerUp) {
        this.onClickListenerUp = onClickListenerUp;
    }

    public View.OnClickListener getOnClickListenerClose() {
        return OnClickListenerClose;
    }

    public void setOnClickListenerClose(View.OnClickListener OnClickListenerClose) {
        this.OnClickListenerClose = OnClickListenerClose;
    }

    public View.OnClickListener getOnClickListenerGoRoom() {
        return onClickListenerGoRoom;
    }

    public void setOnClickListenerGoRoom(View.OnClickListener onClickListenerGoRoom) {
        this.onClickListenerGoRoom = onClickListenerGoRoom;
    }

    private View.OnClickListener onClickListenerOrder;
    private View.OnClickListener onClickListenerGoRoom;
    private View.OnClickListener onClickListenerUp;
    private View.OnClickListener OnClickListenerClose;

    @NonNull

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        hasOrder = getArguments().getBoolean(MyConstants.HASORDERS, false);
        hasRoom = getArguments().getBoolean(MyConstants.HASROOM, false);
        hasDanger = getArguments().getString(MyConstants.DangerType);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_choice_dialog, null);
        TextView tv_choice_order = (TextView) (view.findViewById(R.id.tv_choice_order));
        TextView tv_go_room = (TextView) (view.findViewById(R.id.tv_go_room));
        TextView tv_choice_up = (TextView) (view.findViewById(R.id.tv_choice_up));
        View divider = view.findViewById(R.id.divide_view);
        View divider2 = view.findViewById(R.id.divide_view2);
        ImageView iv_close = (ImageView) (view.findViewById(R.id.iv_close));
        if (!hasRoom) {
            tv_go_room.setVisibility(View.GONE);
            divider2.setVisibility(View.GONE);
            tv_choice_order.setVisibility(View.GONE);
            divider.setVisibility(View.GONE);
        } else {
            tv_go_room.setVisibility(View.VISIBLE);
            divider2.setVisibility(View.VISIBLE);
        }
        if (!hasOrder) {
            tv_choice_order.setVisibility(View.GONE);
            divider.setVisibility(View.GONE);
        } else {
            tv_choice_order.setVisibility(View.VISIBLE);
            divider.setVisibility(View.VISIBLE);
        }
        iv_close.setOnClickListener(this);
        tv_choice_order.setOnClickListener(this);
        tv_go_room.setOnClickListener(this);
        tv_choice_up.setOnClickListener(this);
        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_choice_order:
                if (onClickListenerOrder != null) {
                    onClickListenerOrder.onClick(v);
                }
                break;
            case R.id.tv_go_room:
                if (onClickListenerGoRoom != null) {
                    onClickListenerGoRoom.onClick(v);
                }
                break;
            case R.id.tv_choice_up:
                if (onClickListenerUp != null) {
                    onClickListenerUp.onClick(v);
                }
                break;
            case R.id.iv_close:
                if (OnClickListenerClose != null) {
                    OnClickListenerClose.onClick(v);
                }
                break;
            default:
                break;
        }

    }


    @Override
    public void onDismiss(final DialogInterface dialog) {
        super.onDismiss(dialog);
        final Activity activity = getActivity();
        if (activity instanceof DialogInterface.OnDismissListener) {
            ((DialogInterface.OnDismissListener) activity).onDismiss(dialog);
        }
    }
}
