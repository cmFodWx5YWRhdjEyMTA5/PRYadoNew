package com.yado.pryado.pryadonew.base;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.noober.background.BackgroundLibrary;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.yado.pryado.pryadonew.MyApplication;
import com.yado.pryado.pryadonew.R;
import com.yado.pryado.pryadonew.di.component.ActivityComponent;
import com.yado.pryado.pryadonew.di.component.DaggerActivityComponent;
import com.yado.pryado.pryadonew.di.module.ActivityModule;
import com.yado.pryado.pryadonew.subscriber.DefaultDisposablePoolImpl;
import com.yado.pryado.pryadonew.ui.SplashActivity;
import com.yado.pryado.pryadonew.util.FixMemLeak;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Field;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity<T extends BaseContract.BasePresenter>  extends RxAppCompatActivity implements BaseContract.BaseView{

    private Unbinder mBind;
    protected Context mContext;

    @Nullable
    @Inject
    protected T mPresenter;

    protected ActivityComponent mActivityComponent;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        BackgroundLibrary.inject(this);
        //状态栏透明化
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
        if (this instanceof SplashActivity) {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        initActivityComponent();
        if (isNeedInject()) {
            ARouter.getInstance().inject(this);
        }
        setContentView(inflateContentView());

        mContext = this;
        mBind = ButterKnife.bind(this);
        if (isRegisterEventBus()) {
            if (!EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this);
            }
        }
        initInjector();
        attachView();
        MyApplication.getActivityManager().pushActivity(this);
        initData();
    }

    /**
     * 填充布局
     *
     * @return
     */
    public abstract int inflateContentView();

    protected abstract void initData();

    protected abstract boolean isRegisterEventBus();

    protected abstract boolean isNeedInject();

    public Context getContext() {
        return this;
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        if (startActivitySelfCheck(intent)) {
            // 查看源码得知 startActivity 最终也会调用 startActivityForResult
            super.startActivityForResult(intent, requestCode, options);
        }
    }

    private String mActivityJumpTag;
    private long mActivityJumpTime;
    /**
     * 检查当前 Activity 是否重复跳转了，不需要检查则重写此方法并返回 true 即可
     *
     * @param intent          用于跳转的 Intent 对象
     * @return                检查通过返回true, 检查不通过返回false
     */
    protected boolean startActivitySelfCheck(Intent intent) {
        // 默认检查通过
        boolean result = true;
        // 标记对象
        String tag;
        if (intent.getComponent() != null) { // 显式跳转
            tag = intent.getComponent().getClassName();
        }else if (intent.getAction() != null) { // 隐式跳转
            tag = intent.getAction();
        }else {
            return result;
        }

        if (tag.equals(mActivityJumpTag) && mActivityJumpTime >= SystemClock.uptimeMillis() - 500) {
            // 检查不通过
            result = false;
        }

        // 记录启动标记和时间
        mActivityJumpTag = tag;
        mActivityJumpTime = SystemClock.uptimeMillis();
        return result;
    }



    /**
     * 初始化ActivityComponent
     */
    private void initActivityComponent() {
        mActivityComponent = DaggerActivityComponent.builder()
                .applicationComponent(((MyApplication) getApplication()).getApplicationComponent())
                .activityModule(new ActivityModule(this))
                .build();
    }

    protected void initInjector(){}

    /**
     * 贴上view
     */
    private void attachView() {
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
    }

    /**
     * 分离view
     */
    private void detachView() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBind.unbind();
        detachView();
        if (isRegisterEventBus()) {
            if (EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().unregister(this);
            }
        }
        MyApplication.getActivityManager().popActivity((BaseActivity) mContext);
        FixMemLeak.fixLeak(mContext);
        mContext = null;
        mPresenter = null;
        mActivityComponent = null;
    }
}
