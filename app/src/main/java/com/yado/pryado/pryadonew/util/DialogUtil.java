package com.yado.pryado.pryadonew.util;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.View;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ToastUtils;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.yado.pryado.pryadonew.ActivityManager;
import com.yado.pryado.pryadonew.MyApplication;
import com.yado.pryado.pryadonew.MyConstants;
import com.yado.pryado.pryadonew.R;
import com.yado.pryado.pryadonew.ui.login.LoginActivity;
import com.yado.pryado.pryadonew.ui.main.MainActivity;

import java.lang.ref.WeakReference;

@SuppressLint("StaticFieldLeak")
public class DialogUtil {
    private static DialogUtil instance;
    private NiftyDialogBuilder dialogBuilder;
//    private static Context mContext;
    private WeakReference<Context> mContext;

    private DialogUtil(Context context) {
        mContext = new WeakReference<>(context);
        dialogBuilder = NiftyDialogBuilder.getInstance(mContext.get());
    }

    public static DialogUtil getInstance(Context context) {
        if (instance == null) {
            instance = new DialogUtil(context);
        }
        return instance;
    }

    public void reset(){
        if (instance != null) {
            instance = null;
            dialogBuilder = null;
        }
    }

    public void showDialog(String title, final String msg, final String btn1, final String btn2, Effectstype effect) {
        dialogBuilder
                .withTitle(title)                               //标题.withTitle(null)  no title
                .withTitleColor("#FFFFFF")                               //def  标题颜色
                .withDividerColor("#11000000")                           //def
                .withMessage(msg)                 //.withMessage(null)  no Msg   内容
                .withMessageColor("#FFCFCFCF")                          //def  | withMessageColor(int resid)   内容颜色
                .withDialogColor("#AF444D50")                            //def  | withDialogColor(int resid)   dialog框颜色
                .withIcon(mContext.get().getResources().getDrawable(R.drawable.ic_logo2))  //标题栏图标
                .withDuration(700)                                      //def      动画持续时间
                .withEffect(effect)                                     //def Effectstype.Slidetop  动画模式
                .withButton1Text(btn1)                                  //def gone     按钮文字
                .withButton2Text(btn2)                              //def gone
                .isCancelableOnTouchOutside(true)                       //def    | isCancelable(true)是否支持点击dialog框外关闭dialog
//                .setCustomView(R.layout.custom_view, mContext)     //.setCustomView(View or ResId,context)    自定义布局
                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                        if ("否".equals(btn1)) {
                        }else {
                            excitApp();

                        }
                    }
                })
                .setButton2Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                        if ("是".equals(btn2)) {     //注销登录
                            // 1. 应用内简单的跳转
                            ARouter.getInstance().build(MyConstants.LOGIN).navigation();
//                            mContext.startActivity(new Intent(mContext, LoginActivity.class));
                            ((MainActivity) mContext.get()).finish();

                        }else {   //后台运行
                            BackgroundProcess();
                        }
                    }
                })
                .show();    //展示
    }


    private void excitApp() {
        MyApplication.getActivityManager().popAllActivityExceptOne(mContext.get().getClass());
        System.exit(0);
    }

    private void BackgroundProcess() {
        PackageManager pm = mContext.get().getPackageManager();
        ResolveInfo homeInfo = pm.resolveActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME), 0);
        ActivityInfo ai = homeInfo.activityInfo;
        Intent startIntent = new Intent(Intent.ACTION_MAIN);
        startIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        startIntent.setComponent(new ComponentName(ai.packageName, ai.name));
        startActivitySafely(startIntent);
        dialogBuilder.dismiss();
    }

    private void startActivitySafely(Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            mContext.get().startActivity(intent);
        } catch (ActivityNotFoundException | SecurityException e) {
            ToastUtils.showShort("null");
        }
    }
}
