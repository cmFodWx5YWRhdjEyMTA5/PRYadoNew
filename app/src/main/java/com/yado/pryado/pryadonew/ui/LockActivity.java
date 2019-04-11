package com.yado.pryado.pryadonew.ui;

import android.app.KeyguardManager;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.yado.pryado.pryadonew.R;
import com.yado.pryado.pryadonew.base.BaseActivity;
import com.yado.pryado.pryadonew.ui.todo.MyTodoActivity;
import com.yado.pryado.pryadonew.util.FixMemLeak;

import java.lang.ref.WeakReference;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class LockActivity extends BaseActivity {
    private long exitTime = 0;
    private WeakReference<Context> mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = new WeakReference<>((Context) this);
        //四个标志位顾名思义，分别是锁屏状态下显示，解锁，保持屏幕长亮，打开屏幕。这样当Activity启动的时候，它会解锁并亮屏显示。
        Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED //锁屏状态下显示
//                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD //解锁
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON //保持屏幕长亮
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON); //打开屏幕
        //使用手机的背景
        Drawable wallPaper = WallpaperManager.getInstance(mContext.get()).getDrawable();
        win.setBackgroundDrawable(wallPaper);
    }

    @Override
    public int inflateContentView() {
        return R.layout.activity_lock;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected boolean isRegisterEventBus() {
        return false;
    }

    @Override
    protected boolean isNeedInject() {
        return false;
    }


    @OnClick({R.id.iv_close, R.id.message_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                finish();
                break;
            case R.id.message_layout:
                if ((System.currentTimeMillis() - exitTime) > 2000) {
                    exitTime = System.currentTimeMillis();
                } else {
                    //先解锁系统自带锁屏服务，放在锁屏界面里面
                    KeyguardManager keyguardManager = (KeyguardManager) mContext.get().getSystemService(Context.KEYGUARD_SERVICE);
                    keyguardManager.newKeyguardLock("").disableKeyguard(); //解锁
                    //点击进入消息对应的页面
                    ARouter.getInstance()
                            .build("/ui/todo/MyTodoActivity")
                            .withFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                            .navigation();
//                    mContext.startActivity(new Intent(mContext, MyTodoActivity.class));
                    finish();
                }
                break;
            default:
                break;
        }
    }

}
