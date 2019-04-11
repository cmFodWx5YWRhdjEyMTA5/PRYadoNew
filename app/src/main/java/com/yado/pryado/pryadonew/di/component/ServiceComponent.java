package com.yado.pryado.pryadonew.di.component;

import android.content.Context;

import com.yado.pryado.pryadonew.di.module.ServiceModule;
import com.yado.pryado.pryadonew.di.scope.ContextLife;
import com.yado.pryado.pryadonew.di.scope.PerService;

import dagger.Component;


/**
 * ServiceComponent 暂时没有用到
 * Created yuong
 */
@PerService
@Component(dependencies = ApplicationComponent.class, modules = ServiceModule.class)
public interface ServiceComponent {
    @ContextLife("Service")
    Context getServiceContext();

    @ContextLife("Application")
    Context getApplicationContext();
}
