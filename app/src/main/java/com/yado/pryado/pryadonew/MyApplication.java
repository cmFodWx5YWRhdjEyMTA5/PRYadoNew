package com.yado.pryado.pryadonew;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;

import com.alibaba.android.arouter.launcher.ARouter;
import com.baidu.mapapi.SDKInitializer;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.tinker.entry.ApplicationLike;
import com.tinkerpatch.sdk.TinkerPatch;
import com.tinkerpatch.sdk.loader.TinkerPatchApplicationLike;
import com.yado.pryado.pryadonew.di.component.ApplicationComponent;
import com.yado.pryado.pryadonew.di.component.DaggerApplicationComponent;
import com.yado.pryado.pryadonew.di.module.ApplicationModule;
import com.yado.pryado.pryadonew.greendao.DaoMaster;
import com.yado.pryado.pryadonew.greendao.DaoSession;


import java.util.ArrayList;
import java.util.List;

public class MyApplication extends Application {
    private static Context context;
    private ApplicationComponent mApplicationComponent;
    private static MyApplication mInstance;
    private static ActivityManager activityManager;

    private MySQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    public static String database_name = "PReado";
    private static RefWatcher refWatcher;
    private int refCount = 0;
    private boolean isBackground;
    private ApplicationLike tinkerApplicationLike;


    @Override
    public void onCreate() {
        super.onCreate();
        // enable cookies
        context = getApplicationContext();
        initApplicationComponent();
        mInstance = this;
        initArouter();
        if (LeakCanary.isInAnalyzerProcess(getInstance())) {
            return;
        }
        refWatcher = LeakCanary.install(getInstance());
        activityManager = ActivityManager.getInstance();
        Utils.init(mInstance);
        //配置ToastUtils的相关的属性
        ToastUtils.setGravity(Gravity.CENTER, 0, (int) (80 * Utils.getApp().getResources().getDisplayMetrics().density + 0.5));
        ToastUtils.setBgColor(getResources().getColor(R.color.white));
        ToastUtils.setMsgColor(getResources().getColor(R.color.black));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SDKInitializer.initialize(mInstance);
                initTinker();
            }
        }, 3000);
        initOnWorkThread();
        registerActivityLifecycleCallbacks();

    }

    private void initTinker() {
        // 我们可以从这里获得Tinker加载过程的信息
        tinkerApplicationLike = TinkerPatchApplicationLike.getTinkerPatchApplicationLike();

        // 初始化TinkerPatch SDK, 更多配置可参照API章节中的,初始化SDK
        TinkerPatch.init(tinkerApplicationLike)
                .reflectPatchLibrary()
                .setPatchRollbackOnScreenOff(true)
                .setPatchRestartOnSrceenOff(true)
                .setFetchPatchIntervalByHours(1);

        // 每隔1个小时(通过setFetchPatchIntervalByHours设置)去访问后台时候有更新,通过handler实现轮训的效果
        TinkerPatch.with().fetchPatchUpdateAndPollWithInterval();
    }

    private void registerActivityLifecycleCallbacks() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityStarted(Activity activity) {
                refCount++;
            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                refCount--;
                isBackground = refCount == 0;
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    public static RefWatcher getRefWatcher() {
        return refWatcher;
    }

    private void initArouter() {
        if (isDebug(mInstance)) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(mInstance); // 尽可能早，推荐在Application中初始化
    }

    /**
     * 非阻塞的，在子线程进行初始化
     */
    private void initOnWorkThread() {
        new Thread() {
            @Override
            public void run() {
                //不与主线程争抢资源
                setDatabase();
                CrashReport.initCrashReport(getApplicationContext(), "e4e17dacb1", false);
            }
        }.start();
    }

    /**
     * 初始化ApplicationComponent
     */
    private void initApplicationComponent() {
        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    private void setDatabase() {
        openHelper = new MySQLiteOpenHelper(this, database_name, null);
        db = openHelper.getWritableDatabase();//获取可写数据库
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public boolean isDebug(Context context) {
        boolean isDebug = context.getApplicationInfo() != null &&
                (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        return isDebug;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }


    public SQLiteDatabase getDb() {
        return db;
    }

    public boolean isBackground() {
        return isBackground;
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }

    public static Context getAppContext() {
        return mInstance.getApplicationContext();
    }

    public static MyApplication getInstance() {
        return mInstance;
    }

    public static Context getContext() {
        return context;
    }

    //检查是否存在此包名的应用程序
    public static boolean isAppInstalled(String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packageInfo = packageManager.getInstalledPackages(0);
        List<String> pName = new ArrayList<String>();
        if (packageInfo != null) {
            for (int i = 0; i < packageInfo.size(); i++) {
                String pn = packageInfo.get(i).packageName;
                pName.add(pn);
            }
        }
        return pName.contains(packageName);
    }

    public static ActivityManager getActivityManager() {
        return activityManager;
    }

}

