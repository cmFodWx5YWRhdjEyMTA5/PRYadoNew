package com.yado.pryado.pryadonew.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.yado.pryado.pryadonew.MyApplication;
import com.yado.pryado.pryadonew.MyConstants;
import com.yado.pryado.pryadonew.R;
import com.yado.pryado.pryadonew.base.BaseActivity;
import com.yado.pryado.pryadonew.ui.widgit.Titanic;
import com.yado.pryado.pryadonew.ui.widgit.TitanicTextView;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;
import yanzhikai.textpath.SyncTextPathView;
import yanzhikai.textpath.painter.FireworksPainter;

public class SplashActivity extends BaseActivity {
    @BindView(R.id.iv_welcome)
    ImageView ivWelcome;
    @BindView(R.id.titanic_tv)
    TitanicTextView titanicTv;
    @BindView(R.id.atpv_as)
    SyncTextPathView atpvAs;

    @Override
    public int inflateContentView() {
        return R.layout.activity_flash;
    }

    @Override
    protected void initData() {
//        AlphaAnimation animation = new AlphaAnimation(0.5f, 1.0f);
//        animation.setDuration(3000);
//        ivWelcome.startAnimation(animation);
        atpvAs.setPathPainter(new FireworksPainter());
        atpvAs.startAnimation(0, 1);
        AssetManager mgr = getAssets();
        Typeface tf = Typeface.createFromAsset(mgr, "fonts/FZHaiCKJW.TTF");
        titanicTv.setTypeface(tf);
        final Titanic titanic = new Titanic();
        titanic.start(titanicTv);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                // 1. 应用内简单的跳转
                ARouter.getInstance().build(MyConstants.LOGIN).navigation();
                finish();
                titanic.cancel();
            }
        }, 3000);

    }

    @Override
    protected boolean isRegisterEventBus() {
        return false;
    }

    @Override
    protected boolean isNeedInject() {
        return false;
    }

}
