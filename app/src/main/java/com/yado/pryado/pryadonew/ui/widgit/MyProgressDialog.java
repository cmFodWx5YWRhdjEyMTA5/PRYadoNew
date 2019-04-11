package com.yado.pryado.pryadonew.ui.widgit;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import com.yado.pryado.pryadonew.R;

public class MyProgressDialog extends Dialog {
    private TextView textView;
    public MyProgressDialog(Context context) {
        super(context);
        init();
    }

    public MyProgressDialog(Context context, int theme) {
        super(context, theme);
        init();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        init();
    }

    private void init() {
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.loading);//loading的xml文件
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);
        textView = findViewById(R.id.tv_load_dialog);
    }

    @Override
    public void show() {//开启
        super.show();
    }

    @Override
    public void dismiss() {//关闭
        super.dismiss();
    }

    public void setText(String text){
        if (textView != null) {
            textView.setText(text);
        }
    }


}
