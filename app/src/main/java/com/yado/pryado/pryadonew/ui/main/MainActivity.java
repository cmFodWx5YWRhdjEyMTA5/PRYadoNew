package com.yado.pryado.pryadonew.ui.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;

import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ToastUtils;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yado.pryado.pryadonew.MyApplication;
import com.yado.pryado.pryadonew.MyConstants;
import com.yado.pryado.pryadonew.R;
import com.yado.pryado.pryadonew.base.BaseActivity;
import com.yado.pryado.pryadonew.bean.MsgEvent;
import com.yado.pryado.pryadonew.bean.Update;
import com.yado.pryado.pryadonew.net.EadoUrl;
import com.yado.pryado.pryadonew.service.PollingService;
import com.yado.pryado.pryadonew.ui.widgit.SmoothCheckBox;
import com.yado.pryado.pryadonew.util.DialogUtil;
import com.yado.pryado.pryadonew.util.DownloadListener;
import com.yado.pryado.pryadonew.util.DownloadUtils;
import com.yado.pryado.pryadonew.util.ServiceUtil;
import com.yado.pryado.pryadonew.util.SharedPrefUtil;
import com.yado.pryado.pryadonew.util.Util;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;
import me.leolin.shortcutbadger.ShortcutBadger;
import q.rorbin.badgeview.QBadgeView;

// 在支持路由的页面上添加注解(必选)
// 这里的路径需要注意的是至少需要有两级，/xx/xx
@Route(path = "/ui/main/MainActivity")
public class MainActivity extends BaseActivity<MainPresent> implements MainContract.View {

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.navigationView)
    NavigationView navigationView;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.ll_gongdan)
    LinearLayout llGongdan;
    @BindView(R.id.ll_alert)
    LinearLayout llAlert;

    private FrameLayout headerView;
    private TextView tv_username;
    private int versionCode;
    private String versionName;

    private View dialogView;
    private LinearLayout ll_content;
    private TextView tv_update_log;
    private TextView tv_update_time;
    private TextView update_version;
    private TextView current_version;
    private LinearLayout ll_progress;
    private SmoothCheckBox smoothCheckBox;
    private NumberProgressBar numberProgressBar;

    private View compatibleView;
    private RadioGroup rg_Compatible;
    private RadioButton rb_Compatible, rb_Incompatible;

    private QBadgeView badgeView1, badgeView2;

//    private static final int FINISHDOWN = 4;
//    private static final int DOWNERROR = 5;
//    private static final int UPDATEPROGRESS = 7;
//    private WeakReference<String> saveFileName;
//    private String saveFileName;

    //    @SuppressLint("HandlerLeak")
//    public Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case DOWNERROR:
//                    ToastUtils.showShort("下载失败...");
//                    break;
//                case UPDATEPROGRESS:
//                    numberProgressBar.setProgress((int) msg.obj);
//                    if ((int) msg.obj == 100 && dialogBuilder != null) {
//                        dialogBuilder.dismiss();
//                        dialogBuilder = null;
//                        ll_progress.setVisibility(View.GONE);
//                        ll_content.setVisibility(View.VISIBLE);
//                        numberProgressBar.setProgress(0);
//                    }
//                    break;
//                case FINISHDOWN://下载完成；安装apk
//                    saveFileName = (String) msg.obj;
//                    installApk(saveFileName);
//                    break;
//                default:
//                    break;
//            }
//        }
//    };
//    private static class MyHandler extends Handler {
//
//        WeakReference<MainActivity> weakReference;
//
//        MyHandler(MainActivity mainActivity) {
//            this.weakReference = new WeakReference<>(mainActivity);
//
//        }
//
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case DOWNERROR:
//                    ToastUtils.showShort("下载失败...");
//                    weakReference.get().dialogBuilder.dismiss();
//                    weakReference.get().dialogBuilder = null;
//                    break;
//                case UPDATEPROGRESS:
//                    weakReference.get().numberProgressBar.setProgress((int) msg.obj);
//                    if ((int) msg.obj == 100 && weakReference.get().dialogBuilder != null) {
//                        weakReference.get().dialogBuilder.dismiss();
//                        weakReference.get().ll_progress.setVisibility(View.GONE);
//                        weakReference.get().ll_content.setVisibility(View.VISIBLE);
//                        weakReference.get().numberProgressBar.setProgress(0);
//                    }
//                    break;
//                case FINISHDOWN://下载完成；安装apk
//                    weakReference.get().saveFileName = new WeakReference<>((String) msg.obj);
//                    weakReference.get().installApk(weakReference.get().saveFileName.get());
//                    break;
//                default:
//                    break;
//            }
//
//        }
//    }

    //    private MyHandler handler = new MyHandler(MainActivity.this);
    public static final int REQUEST_CODE_UNKNOWN_APP = 100;


    private NiftyDialogBuilder dialogBuilder;
    private NiftyDialogBuilder dialogBuilder1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermissions();
        if (!ServiceUtil.isServiceRun(PollingService.class.getName())){
            startService(new Intent(mContext, PollingService.class));
        }
        //实例化角标
        badgeView1 = new QBadgeView(mActivityComponent.getActivityContext());
        badgeView2 = new QBadgeView(mActivityComponent.getActivityContext());

        EventBus.getDefault().register(this);
    }

    /**
     * 检查权限
     */
    @SuppressLint("CheckResult")
    private void requestPermissions() {
        RxPermissions rxPermission = new RxPermissions((Activity) mContext);
        rxPermission
                .requestEach(Manifest.permission.CAMERA,
                        Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            // 用户已经同意该权限
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时。还会提示请求权限的对话框
                        } else {
                            // 用户拒绝了该权限，而且选中『不再询问』
                        }
                    }
                });
    }

    /**
     * 加载布局
     * @return
     */
    @Override
    public int inflateContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Integer integer = SharedPrefUtil.getInstance(MyApplication.getInstance()).getT(MyConstants.counts_upcoming, 0);
        Integer integer2 = SharedPrefUtil.getInstance(MyApplication.getInstance()).getT(MyConstants.alert_num, 0);
        if (integer > 0) {

            badgeView1.bindTarget(llGongdan).setBadgeNumber(integer);
            ShortcutBadger.applyCount(MyApplication.getInstance(), integer);
        } else {
            badgeView1.hide(false);
            ShortcutBadger.removeCount(MyApplication.getInstance());
        }
        if (integer2 > 0) {
            badgeView2.bindTarget(llAlert).setBadgeNumber(integer2);
        } else {
            badgeView2.hide(false);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMessage(MsgEvent message) {
        Log.e("tag", "Subscribe: "+ message.getAlarmNum());
//        SharedPrefUtil.getInstance(MyApplication.getInstance()).saveObject(MyConstants.counts_upcoming, message.getGongdanNum());
        SharedPrefUtil.getInstance(MyApplication.getInstance()).saveObject(MyConstants.alert_num, message.getAlarmNum());
        if (message.getAlarmNum() > 0) {
            badgeView2.bindTarget(llAlert).setBadgeNumber(message.getAlarmNum());
        } else {
            badgeView2.hide(false);
        }
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        //得到头布局
        headerView = (FrameLayout) navigationView.getHeaderView(0);
        tv_username = headerView.findViewById(R.id.tv_username);
        navigationView.setItemIconTintList(null);
        tv_username.setText(SharedPrefUtil.getInstance(MyApplication.getInstance()).getT(MyConstants.USERNAME, "unknown"));
        userName.setText(SharedPrefUtil.getInstance(MyApplication.getInstance()).getT(MyConstants.USERNAME, "unknown"));
        initDialogView();
        initCompatibleView();
        //获取当前版本号和版本名
        versionCode = Util.getVersionCode(mContext);
        versionName = Util.getVersionName(mContext);
        assert mPresenter != null;
        mPresenter.CheckUpdate(versionCode);
        initListener();
        initDownload();
    }

    /**
     * 初始化 文件下载工具类
     */
    private void initDownload() {
        DownloadUtils.getInstance().initDownload(EadoUrl.DEFAULT_SAVE_FILE_PATH);
        DownloadUtils.getInstance().setListener(new DownloadListener() {
            @Override
            public void startDownload() {

            }

            @Override
            public void stopDownload() {
            }

            @Override
            public void finishDownload() {
                mPresenter.installApk(DownloadUtils.getInstance().downloadFile, (BaseActivity) mContext);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (dialogBuilder != null) {
                            dialogBuilder.dismiss();
                            ll_progress.setVisibility(View.GONE);
                            ll_content.setVisibility(View.VISIBLE);
                            numberProgressBar.setProgress(0);
                        }
                    }
                });

            }

            @Override
            public void downloadProgress(final long progress) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        numberProgressBar.setProgress((int) progress);
                    }
                });

            }
        });
    }

    /**
     * 初始化监听事件
     */
    private void initListener() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (!Util.isDoubleClick()) {
                    switch (menuItem.getItemId()) {
                        case R.id.receive_setting:  //接收消息设定
                            ARouter.getInstance().build(MyConstants.RECEIVE_SETTING).navigation();
                            break;
                        case R.id.net_setting:  //网络设定
                            ARouter.getInstance().build(MyConstants.NETWORK_SETTING1).navigation();
                            break;
                        case R.id.feedBack: //意见反馈
                            ARouter.getInstance().build(MyConstants.FEEDBACK).navigation();
                            break;
                        case R.id.version_up:   //检查更新
                            SharedPrefUtil.getInstance(MyApplication.getInstance()).saveObject("isPopUp", false);
                            assert mPresenter != null;
                            mPresenter.CheckUpdate(versionCode);
                            break;
                        case R.id.compatibility_mode:   //兼容模式
                            showCompatibleDialog();
                            break;
                        case R.id.recovery: //恢复默认数据
                            ToastUtils.showShort(getResources().getString(R.string.restoring));
                            restoreData();
                            break;
                        case R.id.pwd_setting:  //密码设定
                            ARouter.getInstance().build(MyConstants.PASSWORD_SETTING).navigation();
                            break;
                        case R.id.switch_user:  //切换用户
                            DialogUtil.getInstance(mContext).showDialog(getResources().getString(R.string.switchuser), getResources().getString(R.string.switch_user_confirm),
                                    getResources().getString(R.string.no), getResources().getString(R.string.yes), Effectstype.SlideBottom);
                            break;
                        case R.id.about:
                            ARouter.getInstance().build(MyConstants.ABOUT).navigation();
                            break;
                        case R.id.exit: //退出
                            DialogUtil.getInstance(mContext).showDialog(getResources().getString(R.string.exit_app), getResources().getString(R.string.exit_confirm),
                                    getResources().getString(R.string.exit_all), getResources().getString(R.string.background_process), Effectstype.Sidefill);
                            break;
                        default:
                            break;
                    }
                    drawerLayout.closeDrawers();
                }
                return false;
            }
        });
    }

    /**
     * 恢复默认数据
     */
    private void restoreData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String name = SharedPrefUtil.getInstance(MyApplication.getInstance()).getT(MyConstants.USERNAME, "");
                String pwd = SharedPrefUtil.getInstance(MyApplication.getInstance()).getT(MyConstants.PWD, "");
                String last_time = SharedPrefUtil.getInstance(MyApplication.getInstance()).getT("LastAlarmTime", "2016/01/05 04:26:32.959");
                String last_time2 = SharedPrefUtil.getInstance(MyApplication.getInstance()).getT("LastAlarmTime2", "2016/01/05 04:26:32.959");

                SharedPrefUtil.getInstance(MyApplication.getInstance()).removeAll();

                SharedPrefUtil.getInstance(MyApplication.getInstance()).saveObject(MyConstants.USERNAME, name);
                SharedPrefUtil.getInstance(MyApplication.getInstance()).saveObject(MyConstants.PWD, pwd);
                SharedPrefUtil.getInstance(MyApplication.getInstance()).saveObject("LastAlarmTime", last_time);
                SharedPrefUtil.getInstance(MyApplication.getInstance()).saveObject("LastAlarmTime2", last_time2);
                SharedPrefUtil.getInstance(MyApplication.getInstance()).remove("ip");
                ToastUtils.showShort(getResources().getString(R.string.restored));
            }
        }, 2000);
    }

    /**
     * 显示兼容模式对话框
     */
    private void showCompatibleDialog() {
        if (dialogBuilder1 == null) {
            dialogBuilder1 = NiftyDialogBuilder.getInstance(mActivityComponent.getActivityContext());
        }
        if (rb_Compatible != null) {
            if (SharedPrefUtil.getInstance(MyApplication.getInstance()).getT("Compatibility", false)) {
                rb_Compatible.setChecked(true);
                rb_Incompatible.setChecked(false);
            } else {
                rb_Incompatible.setChecked(true);
                rb_Compatible.setChecked(false);
            }
        }
        dialogBuilder1
                .withTitle("兼容模式")
                .withTitleColor("#FFFFFF")
                .withDividerColor("#11000000")
                .withMessage(null)
                .withMessageColor("#FFCFCFCF")
                .withDialogColor("#AF444D50")
                .withIcon(mContext.getResources().getDrawable(R.drawable.ic_logo2))
                .withDuration(700)
                .withEffect(Effectstype.SlideBottom)
                .withButton1Text("取消")
                .withButton2Text("确定")
                .isCancelableOnTouchOutside(false)
                .setCustomView(compatibleView, mActivityComponent.getActivityContext())
                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder1.dismiss();
                    }
                })
                .setButton2Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isCompatible;
                        isCompatible = rg_Compatible.getCheckedRadioButtonId() == rb_Compatible.getId();
                        SharedPrefUtil.getInstance(MyApplication.getInstance()).saveObject("Compatibility", isCompatible);
                        dialogBuilder1.dismiss();
                    }
                })
                .show();    //展示
    }

    /**
     * 注入View
     */
    @Override
    protected void initInjector() {
        mActivityComponent.inject((MainActivity) mContext);
    }

    /**
     * 是否需要注册 EventBus
     * @return
     */
    @Override
    protected boolean isRegisterEventBus() {
        return false;
    }

    /**
     * 是否需要注入Arouter
     * @return
     */
    @Override
    protected boolean isNeedInject() {
        return false;
    }

    /**
     * 返回键监听
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            PackageManager pm = getPackageManager();
            ResolveInfo homeInfo = pm.resolveActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME), 0);
            ActivityInfo ai = homeInfo.activityInfo;
            Intent startIntent = new Intent(Intent.ACTION_MAIN);
            startIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            startIntent.setComponent(new ComponentName(ai.packageName, ai.name));
            startActivitySafely(startIntent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 保存状态
     * @param intent
     */
    private void startActivitySafely(Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException | SecurityException e) {
            Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick({R.id.ll_alert, R.id.ll_yinhuan, R.id.ll_cewen, R.id.ll_jiance, R.id.ll_gongdan, R.id.ll_buzhuo, R.id.iv_set, R.id.ll_shaoma})
    public void onViewClicked(View view) {
        if (!Util.isDoubleClick()) {
            switch (view.getId()) {
                case R.id.ll_alert: //报警页面
                    ARouter.getInstance().build(MyConstants.ALERT).navigation();
                    break;
                case R.id.ll_yinhuan://隐患页面
                    ARouter.getInstance().build(MyConstants.DANGER).navigation();
                    break;
                case R.id.ll_cewen://非介入式界面
                    ARouter.getInstance().build(MyConstants.CEWEN).navigation();
                    break;
                case R.id.ll_jiance://监测页面
                    ARouter.getInstance().build(MyConstants.MONITOR).navigation();
                    break;
                case R.id.ll_gongdan://工单页面
                    ARouter.getInstance().build(MyConstants.AFFAIR).navigation();
                    break;
                case R.id.ll_buzhuo://捕捉页面
                    ARouter.getInstance().build(MyConstants.REPORT)
                            .withBoolean(MyConstants.FROM_MAIN, true)
                            .navigation();
                    break;
//                case R.id.ll_baobiao:
//                    ARouter.getInstance().build(MyConstants.FORM).navigation();
//                    break;
                case R.id.iv_set://设置界面
                    drawerLayout.openDrawer(Gravity.START);
                    break;
                case R.id.ll_shaoma://扫码页面
                    ARouter.getInstance().build(MyConstants.CAPTUCRE).navigation();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 显示下载 对话框
     * @param update
     */
    @Override
    public void showDownloadDialog(final Update update) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final String updateLog = update.getUpdateLog();
                String[] split = updateLog.split("；");
                StringBuilder builder = new StringBuilder().append("更新内容:\n");
                for (String aSplit : split) {
                    builder.append(aSplit).append("\n");
                }
                builder.deleteCharAt(builder.length() - 1);
                tv_update_log.setText(builder.toString());
                tv_update_time.setText(update.getUpdateTime());
                current_version.setText("当前版本：" + versionName);
                update_version.setText("最新版本：" + update.getVersionName());
                if (dialogBuilder == null) {
                    dialogBuilder = NiftyDialogBuilder.getInstance(mContext);

                }
                dialogBuilder
                        .withTitle("发现新版本")
                        .withTitleColor("#FFFFFF")
                        .withDividerColor("#11000000")
                        .withMessage(null)
                        .withMessageColor("#FFCFCFCF")
                        .withDialogColor("#AF444D50")
                        .withIcon(mContext.getResources().getDrawable(R.drawable.ic_logo2))
                        .withDuration(700)
                        .withEffect(Effectstype.SlideBottom)
                        .withButton1Text(getResources().getString(R.string.cancel))
                        .withButton2Text(getString(R.string.update))
                        .isCancelableOnTouchOutside(false)
                        .setCustomView(dialogView, mContext)
                        .setButton1Click(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogBuilder.dismiss();
                                DownloadUtils.getInstance().stopDownload();
                            }
                        })
                        .setButton2Click(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ll_content.setVisibility(View.GONE);
                                ll_progress.setVisibility(View.VISIBLE);
                                DownloadUtils.getInstance().startDownload(update.getDownloadUrl());
                                dialogBuilder.setButton2Click(null);
                            }
                        })
                        .show();    //展示
                dialogBuilder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                        return keyCode == KeyEvent.KEYCODE_BACK;
                    }
                });
                dialogBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        ll_content.setVisibility(View.VISIBLE);
                        ll_progress.setVisibility(View.GONE);
                        SharedPrefUtil.getInstance(MyApplication.getInstance()).saveObject("isPopUp", smoothCheckBox.isChecked());
                    }
                });
            }
        });

    }

    /**
     * 初始化 兼容模式对话框 View
     */
    private void initCompatibleView() {
        compatibleView = LayoutInflater.from(mActivityComponent.getActivityContext()).inflate(R.layout.compatibility_mode, null);
        rb_Compatible = compatibleView.findViewById(R.id.rb_Compatible);
        rg_Compatible = compatibleView.findViewById(R.id.rg_Compatible);
        rb_Incompatible = compatibleView.findViewById(R.id.rb_Incompatible);

    }

    /**
     * 初始化下载对话框 View
     */
    private void initDialogView() {
        dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_update, null);
        tv_update_log = dialogView.findViewById(R.id.tv_update_log);
        tv_update_time = dialogView.findViewById(R.id.tv_update_time);
        current_version = dialogView.findViewById(R.id.current_version);
        update_version = dialogView.findViewById(R.id.update_version);
        ll_content = dialogView.findViewById(R.id.ll_content);
        ll_progress = dialogView.findViewById(R.id.ll_progress);
        smoothCheckBox = dialogView.findViewById(R.id.smoothCheckBox);
        numberProgressBar = dialogView.findViewById(R.id.numberProgressBar);
        numberProgressBar.setMax(100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_UNKNOWN_APP) {
            mPresenter.installApk(DownloadUtils.getInstance().downloadFile, (BaseActivity) mContext);
        }
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(mContext, PollingService.class));
        DialogUtil.getInstance(mContext).reset();
        DownloadUtils.getInstance().setListener(null);
        DownloadUtils.getInstance().reset();
//        handler.removeCallbacksAndMessages(null);
        super.onDestroy();

        EventBus.getDefault().unregister(this);
    }
}
