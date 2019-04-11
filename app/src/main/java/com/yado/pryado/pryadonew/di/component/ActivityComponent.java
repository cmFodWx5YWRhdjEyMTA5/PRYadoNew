package com.yado.pryado.pryadonew.di.component;

import android.app.Activity;
import android.content.Context;


import com.yado.pryado.pryadonew.di.module.ActivityModule;
import com.yado.pryado.pryadonew.di.scope.ContextLife;
import com.yado.pryado.pryadonew.di.scope.PerActivity;
import com.yado.pryado.pryadonew.ui.affair.AffairActivity;
import com.yado.pryado.pryadonew.ui.alert.AlertActivity;
import com.yado.pryado.pryadonew.ui.device_detail.DeviceDetailActivity;
import com.yado.pryado.pryadonew.ui.device_detail.DeviceDetailActivity2;
import com.yado.pryado.pryadonew.ui.hisOrder.HisOrderActivity;
import com.yado.pryado.pryadonew.ui.login.LoginActivity;
import com.yado.pryado.pryadonew.ui.main.MainActivity;
import com.yado.pryado.pryadonew.ui.mod_pwd.PasswordSettingActivity;
import com.yado.pryado.pryadonew.ui.monitor.MonitorActivity;
import com.yado.pryado.pryadonew.ui.report.ReportActivity;
import com.yado.pryado.pryadonew.ui.riskAssessAndDetail.AssessActivity;
import com.yado.pryado.pryadonew.ui.riskAssessAndDetail.DangerDetailActivity;
import com.yado.pryado.pryadonew.ui.roomDetail.RoomDetailActivity;
import com.yado.pryado.pryadonew.ui.task.TaskActivity;
import com.yado.pryado.pryadonew.ui.temp_monitor.TempMonitorActivity;

import dagger.Component;

/**
 * ActivityComponent
 * @author yuong
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    @ContextLife("Activity")
    Context getActivityContext();

    @ContextLife("Application")
    Context getApplicationContext();

    Activity getActivity();

    void inject(LoginActivity activity);

    void inject(MainActivity activity);

    void inject(AlertActivity activity);

    void inject(AssessActivity activity);

    void inject(DangerDetailActivity activity);

    void inject(ReportActivity reportActivity);

    void inject(AffairActivity affairActivity);

    void inject(HisOrderActivity hisOrderActivity);

    void inject(TaskActivity taskActivity);

    void inject(MonitorActivity monitorActivity);

    void inject(RoomDetailActivity roomDetailActivity);

    void inject(DeviceDetailActivity deviceDetailActivity);

    void inject(DeviceDetailActivity2 deviceDetailActivity2);

    void inject(PasswordSettingActivity passwordSettingActivity);

    void inject(TempMonitorActivity tempMonitorActivity);
}
