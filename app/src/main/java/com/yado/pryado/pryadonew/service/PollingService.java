package com.yado.pryado.pryadonew.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ToastUtils;
import com.yado.pryado.pryadonew.ActivityManager;
import com.yado.pryado.pryadonew.MyApplication;
import com.yado.pryado.pryadonew.MyConstants;
import com.yado.pryado.pryadonew.R;
import com.yado.pryado.pryadonew.base.BaseActivity;
import com.yado.pryado.pryadonew.bean.ListmapBean;
import com.yado.pryado.pryadonew.bean.OrderList;
import com.yado.pryado.pryadonew.bean.PullBean;
import com.yado.pryado.pryadonew.bean.StatusBean;
import com.yado.pryado.pryadonew.net.PRApi;
import com.yado.pryado.pryadonew.net.PRRetrofit;
import com.yado.pryado.pryadonew.receiver.LockScreenReceiver;
import com.yado.pryado.pryadonew.subscriber.DefaultDisposablePoolImpl;
import com.yado.pryado.pryadonew.subscriber.PRSubscriber;
import com.yado.pryado.pryadonew.ui.DialogActivity;
import com.yado.pryado.pryadonew.ui.alert.AlertActivity;
import com.yado.pryado.pryadonew.ui.login.LoginActivity;
import com.yado.pryado.pryadonew.ui.todo.MyTodoActivity;
import com.yado.pryado.pryadonew.util.SharedPrefUtil;
import com.yado.pryado.pryadonew.util.UserLogoutUtil;
import com.yado.pryado.pryadonew.util.Util;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;

public class PollingService extends Service {

    private static final String TAG = "PollingService";
    public static String CURSOR_POSITION = "cursor_position";
    public static final String ALARM_CHANNEL = "alarm";
    public static final String NEW_ORDER_CHANNEL = "new_order";

    private Notification mNotification;
    private NotificationManager mManager;
    private boolean flag = true;
    private int seconds = 20 * 1000;
    private int notificationID = 0;
    private static String TB_ALERT = "tb_alert";
    private PRApi prApi;
    private DefaultDisposablePoolImpl disposablePool;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initNotifiManager();
        prApi = PRRetrofit.getInstance(MyApplication.getInstance()).getApi();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        KLog.e(TAG,"onStartCommand");
//        startPolling();
        new PollingThread().start();
        return super.onStartCommand(intent, flags, startId);
    }

    @SuppressLint({"NewApi", "WrongConstant"})
    private void initNotifiManager() {
        mManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        NotificationChannel chan1 = new NotificationChannel(ALARM_CHANNEL,
//                getString(R.string.alarm), NotificationManager.IMPORTANCE_DEFAULT);
//        mManager.createNotificationChannel(chan1);
//
//        NotificationChannel chan2 = new NotificationChannel(NEW_ORDER_CHANNEL,
//                getString(R.string.new_order), NotificationManager.IMPORTANCE_HIGH);
//        mManager.createNotificationChannel(chan2);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mManager.createNotificationChannels(Arrays.asList(
                    new NotificationChannel(ALARM_CHANNEL,
                            getString(R.string.alarm), NotificationManager.IMPORTANCE_DEFAULT),
                    new NotificationChannel(NEW_ORDER_CHANNEL,
                            getString(R.string.new_order), NotificationManager.IMPORTANCE_HIGH)
            ));
        }

    }

    public void addSubscription(Disposable d) {
        if (disposablePool == null) {
            disposablePool = new DefaultDisposablePoolImpl();
        }
        disposablePool.addDisposable(d);
    }


    /**
     * 建立一级通道的通知
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification.Builder getNotification1(String body, PendingIntent pendingIntent) {
        return new Notification.Builder(getApplicationContext(), ALARM_CHANNEL)
                .setContentTitle(getString(R.string.alarm))
                .setContentText(body)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_logo)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true);
    }

    /**
     * 建立二级通道的通知
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification.Builder getNotification2(String body, PendingIntent pendingIntent) {
        return new Notification.Builder(getApplicationContext(), NEW_ORDER_CHANNEL)
                .setContentTitle(getString(R.string.new_order))
                .setContentText(body)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_logo)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true);
    }


    //弹出Notification
    @SuppressLint("NewApi")
    private void showNotification(String message, Class<?> cls, int type) {
        notificationID++;
        Intent i = new Intent(this, cls);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, 0);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {

            Notification.Builder builder = new Notification.Builder(this)
                    .setAutoCancel(true)
                    .setContentTitle(getString(R.string.notice))
                    .setContentText(message)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.ic_logo)
                    .setWhen(System.currentTimeMillis())
                    .setOngoing(true);
            mNotification = builder.build();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder builder = null;
            if (type == 1) {
                builder = getNotification1(message, pendingIntent);
            } else {
                builder = getNotification2(message, pendingIntent);

            }
            //分组（可选）
            //groupId要唯一
//            String groupId = "group" + notificationID;
//            NotificationChannelGroup group = new NotificationChannelGroup(groupId, message);
//
//            //创建group
//            mManager.createNotificationChannelGroup(group);
//
//            //channelId要唯一
//            String channelId = "yado_channel" + notificationID;
//
//            NotificationChannel adChannel = new NotificationChannel(channelId,
//                    message, NotificationManager.IMPORTANCE_DEFAULT);
//            //补充channel的含义（可选）
//            adChannel.setDescription(message);
//            //将渠道添加进组（先创建组才能添加）
//            adChannel.setGroup(groupId);
//            //创建channel
//            mManager.createNotificationChannel(adChannel);
//
//            //创建通知时，标记你的渠道id
//            Notification.Builder builder = new Notification.Builder(MyApplication.getInstance(), channelId)
//                    .setContentText(message)
//                    .setAutoCancel(true)
//                    .setContentTitle(getString(R.string.notice))
//                    .setContentText(message)
//                    .setContentIntent(pendingIntent)
//                    .setSmallIcon(R.drawable.ic_logo)
//                    .setWhen(System.currentTimeMillis())
//                    .setOngoing(true);
            mNotification = builder.build();

        }
        mNotification.tickerText = message;
//        mNotification.defaults |= Notification.DEFAULT_SOUND;
        RingtoneManager rm = new RingtoneManager(this);
        rm.setType(RingtoneManager.TYPE_NOTIFICATION);
        Cursor cursor = rm.getCursor();

//        rm.setType(RingtoneManager.TYPE_NOTIFICATION);
//        KLog.e("get=" + SharedPrefUtil.getInstance(MyApplication.getContext()).getString(CURSOR_POSITION, RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_NOTIFICATION).toString()));
        if (SharedPrefUtil.getInstance(MyApplication.getInstance()).getT(TB_ALERT, true)) {
            mNotification.sound = Uri.parse(SharedPrefUtil.getInstance(MyApplication.getContext()).getString(CURSOR_POSITION, RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_NOTIFICATION).toString()));
        } else {
            mNotification.sound = null;
        }
        mNotification.flags = Notification.FLAG_AUTO_CANCEL;
        mManager.notify(notificationID, mNotification);
        LockScreenReceiver.type = notificationID;
        sendBroadcast(new Intent().setAction(MyConstants.SCREEN_ACTION)); //发送广播
        cursor.close();
    }

    private void startPolling() {
        //获取我的待办数目;
        getAwaitOrder();
        if (SharedPrefUtil.getInstance(MyApplication.getInstance()).getT(TB_ALERT, true)) {
            if (TextUtils.isEmpty(SharedPrefUtil.getInstance(MyApplication.getInstance()).getT(MyConstants.USERNAME, ""))) {
                return;
            }
            String last_time = SharedPrefUtil.getInstance(MyApplication.getInstance()).getT("LastAlarmTime", "2016/01/05 04:26:32.959");
            Log.e(TAG, last_time);
            getAlarm(last_time);
        }
        getNetPhoneMac();
    }

    private void getNetPhoneMac() {
        prApi.getPhoneMac(SharedPrefUtil.getInstance(MyApplication.getInstance()).getT(MyConstants.USERNAME, "unknown"),
                SharedPrefUtil.getInstance(MyApplication.getInstance()).getT(MyConstants.PWD, "unknown"))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new PRSubscriber<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        super.onSubscribe(d);
                        addSubscription(d);
                    }

                    @Override
                    public void onNext(String s) {
                        Log.e("getPhoneMac", s);
                        if (!s.equals(Util.getIMEI(MyApplication.getInstance().getApplicationContext()))) {
                            Handler handlerThree = new Handler(Looper.getMainLooper());
                            handlerThree.post(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtils.showShort("该账户已在其他设备登录！");

                                }
                            });
                            handlerThree.postDelayed(new Runnable() {
                                @Override
                                public void run() {
//                                    SystemClock.sleep(1000);
                                    ARouter.getInstance().build(MyConstants.LOGIN).navigation();
                                    stopSelf();
                                    MyApplication.getActivityManager().popAllExcludeLogin();
                                }
                            }, 1000);

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                });
    }

    private void getAlarm(String last_time) {
        Log.i("getAlarm", "getAlarm=" + last_time);
        prApi.getAlertPull(last_time)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new PRSubscriber<PullBean>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        super.onSubscribe(d);
                        addSubscription(d);
                    }

                    @Override
                    public void onError(Throwable e) {//HttpException，MalformedJsonException
                        super.onError(e);
                        Log.e(TAG, "onError,getAlarm" + e.toString());
                    }

                    @Override
                    public void onNext(PullBean bean) {
                        if (bean.getAlarmCount() > 0) {
                            SharedPrefUtil.getInstance(MyApplication.getInstance()).saveObject("LastAlarmTime", bean.getLastAlarmTime());
                            showNotification(getString(R.string.has_new_alert), AlertActivity.class, 1);
//                            createNotificationChannel();
                            EventBus.getDefault().post(new StatusBean(getString(R.string.has_new_alert)));
                        } else {
//                            KLog.e(TAG, "无报警");
                        }
                    }
                });
    }

    /**
     * 获取待办工单
     */
    private void getAwaitOrder() {
        Log.i("getAwaitOrder", "getAwaitOrder");
        Observable
                .zip(prApi.getOrderList(SharedPrefUtil.getInstance(MyApplication.getInstance()).getT(MyConstants.USERNAME, "unknown"), MyConstants.ORDER_TYPE_AWAIT, ""),
                        prApi.getOrderList(SharedPrefUtil.getInstance(MyApplication.getInstance()).getT(MyConstants.USERNAME, "unknown"), MyConstants.ORDER_TYPE_ACCEPTET, ""), new BiFunction<OrderList, OrderList, List<ListmapBean>>() {

                            private StatusBean statusBean;

                            @Override
                            public List<ListmapBean> apply(OrderList orderList, OrderList orderList2) {
                                List<ListmapBean> result = new ArrayList<>();
                                if (orderList != null) {
                                    result.addAll(orderList.getListmap());
                                }
                                if (orderList.getListmap() != null && orderList.getListmap().size() > 0) {//待接收的
                                    String nowTime = orderList.getListmap().get(0).getCreateDate();
                                    String savedTime = SharedPrefUtil.getInstance(MyApplication.getInstance()).getT(MyConstants.task_time, "");
//                                    KLog.e(nowTime+"----------"+savedTime);
                                    if (!savedTime.equals(nowTime)) {
                                        SharedPrefUtil.getInstance(MyApplication.getInstance()).saveObject(MyConstants.counts_upcoming, orderList.getListmap().size() + orderList2.getListmap().size());
//                                        KLog.e(orderList.getListmap().size() + orderList2.getListmap().size());
                                        SharedPrefUtil.getInstance(MyApplication.getInstance()).saveObject(MyConstants.task_time, nowTime);
                                        showNotification(getString(R.string.has_new_task), MyTodoActivity.class, 2);

                                        if (!MyApplication.getInstance().isBackground()) { //应用在前台弹出对话框
                                            Intent intent = new Intent();
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            intent.setClass(getApplicationContext(), DialogActivity.class);
                                            startActivity(intent);
                                        }

                                        statusBean = new StatusBean(getString(R.string.has_new_task));
                                        statusBean.setAlarmCount(orderList.getListmap().size() + orderList2.getListmap().size());
                                        EventBus.getDefault().post(statusBean);
                                    }
                                }
                                if (orderList2 != null) {
                                    result.addAll(orderList2.getListmap());
                                }
                                return result;
                            }
                        })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new PRSubscriber<List<ListmapBean>>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        super.onSubscribe(d);
                        addSubscription(d);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        Log.e(TAG, "onError,getAwaitOrder-" + e.toString());
                    }

                    @Override
                    public void onNext(List<ListmapBean> orderList) {
                        SharedPrefUtil.getInstance(MyApplication.getInstance()).saveObject(MyConstants.counts_upcoming, orderList.size());
                    }
                });
    }

    class PollingThread extends Thread {
        @Override
        public void run() {
            while (flag) {
                startPolling();
                SystemClock.sleep(seconds);
            }
        }
    }

    @Override
    public void onDestroy() {
        this.flag = false;
        super.onDestroy();
        prApi = null;
        Log.e("userLogout", "onDestroy");
        PRRetrofit.getInstance(MyApplication.getInstance()).reset();
        if (disposablePool != null) {
            disposablePool.clearPool();
        }
        disposablePool = null;

    }

}
