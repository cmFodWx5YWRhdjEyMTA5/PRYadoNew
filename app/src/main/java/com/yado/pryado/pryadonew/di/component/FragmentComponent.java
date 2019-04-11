package com.yado.pryado.pryadonew.di.component;

import android.app.Activity;
import android.content.Context;


import com.yado.pryado.pryadonew.di.module.FragmentModule;
import com.yado.pryado.pryadonew.di.scope.ContextLife;
import com.yado.pryado.pryadonew.di.scope.PerFragment;
import com.yado.pryado.pryadonew.ui.alert.AlertFragment;
import com.yado.pryado.pryadonew.ui.device_detail.account_info.AccountInfoFragment;
import com.yado.pryado.pryadonew.ui.device_detail.danger.DangerFragment;
import com.yado.pryado.pryadonew.ui.device_detail.real_time_data.RealtimeDataFragment;
import com.yado.pryado.pryadonew.ui.temp_monitor.TempMonitorAssessFragment;
import com.yado.pryado.pryadonew.ui.temp_monitor.TempMonitorPlanFragment;
import com.yado.pryado.pryadonew.ui.todo.TaskListFragment;

import dagger.Component;

/**
 * FragmentComponent  提供Fragment注入
 * Created by yuong
 * 使用的是Dagger2的方法和参数
 */
@PerFragment
@Component(dependencies = ApplicationComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {
    @ContextLife("Activity")
    Context getActivityContext();

    @ContextLife("Application")
    Context getApplicationContext();

    Activity getActivity();

    void inject(AlertFragment fragment);

    void inject(TaskListFragment taskListFragment);

    void inject(RealtimeDataFragment realtimeDataFragment);

    void inject(AccountInfoFragment accountInfoFragment);

    void inject(DangerFragment dangerFragment);

    void inject(TempMonitorAssessFragment tempMonitorAssessFragment);

    void inject(TempMonitorPlanFragment tempMonitorPlanFragment);

}
