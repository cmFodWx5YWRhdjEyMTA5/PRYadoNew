package com.yado.pryado.pryadonew.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.yado.pryado.pryadonew.R;
import com.yado.pryado.pryadonew.base.BaseActivity;
import com.yado.pryado.pryadonew.ui.main.MainActivity;
import com.yado.pryado.pryadonew.util.FixMemLeak;

import java.lang.ref.WeakReference;
import java.util.logging.LoggingMXBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class DialogActivity extends AppCompatActivity{

    private NiftyDialogBuilder niftyDialogBuilder;
    private Unbinder mBind;
    private WeakReference<Context> weakReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //四个标志位顾名思义，分别是锁屏状态下显示，解锁，保持屏幕长亮，打开屏幕。这样当Activity启动的时候，它会解锁并亮屏显示。
        Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED //锁屏状态下显示
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD //解锁
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON //保持屏幕长亮
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON); //打开屏幕
        weakReference = new WeakReference<>((Context) this);
        mBind = ButterKnife.bind(this);
        showDialog();
    }

    private void showDialog() {
        if (niftyDialogBuilder == null) {
            niftyDialogBuilder = NiftyDialogBuilder.getInstance(weakReference.get());
        }
        niftyDialogBuilder
                .withTitle("新任务")                               //标题.withTitle(null)  no title
                .withTitleColor("#FFFFFF")                               //def  标题颜色
                .withDividerColor("#11000000")                           //def
                .withMessage(null)                 //.withMessage(null)  no Msg   内容
                .withMessageColor("#FFCFCFCF")                          //def  | withMessageColor(int resid)   内容颜色
                .withDialogColor("#AF444D50")                            //def  | withDialogColor(int resid)   dialog框颜色
                .withIcon(weakReference.get().getResources().getDrawable(R.drawable.ic_logo2))  //标题栏图标
                .withDuration(700)                                      //def      动画持续时间
                .withEffect(Effectstype.SlideBottom)                                     //def Effectstype.Slidetop  动画模式
                .withButton1Text("取消")                                  //def gone     按钮文字
                .withButton2Text("查看")                              //def gone
                .isCancelableOnTouchOutside(true)                       //def    | isCancelable(true)是否支持点击dialog框外关闭dialog
                .setCustomView(R.layout.new_order_alert, weakReference.get())     //.setCustomView(View or ResId,context)    自定义布局
                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        niftyDialogBuilder.dismiss();

                    }
                })
                .setButton2Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        niftyDialogBuilder.dismiss();
                        ARouter.getInstance()
                        .build("/ui/todo/MyTodoActivity")
                        .withFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        .navigation();
                    }
                })
                .show();    //展示
        niftyDialogBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBind.unbind();
        FixMemLeak.fixLeak(this);
    }
}
