package com.yado.pryado.pryadonew.net;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public abstract class MyCallBack implements Callback {

    ProgressDialog dialog;
    private Activity activity;
    public MyCallBack(Activity activity, ProgressDialog dialog) {
        this.activity = activity;
        this.dialog = dialog;
    }
    @Override
    public void onFailure(Call call, IOException e) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, "请求网络失败", Toast.LENGTH_SHORT).show();
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
    }
    @Override
    public void onResponse(Call call, Response response) throws IOException {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                Toast.makeText(activity,"请求网络成功",Toast.LENGTH_SHORT).show();
                if (dialog != null&&dialog.isShowing())
                    dialog.dismiss();
            }
        });
    }
}
