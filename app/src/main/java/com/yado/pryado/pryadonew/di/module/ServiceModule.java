package com.yado.pryado.pryadonew.di.module;

import android.app.Service;
import android.content.Context;


import com.yado.pryado.pryadonew.di.scope.ContextLife;
import com.yado.pryado.pryadonew.di.scope.PerService;

import dagger.Module;
import dagger.Provides;

/**
 * ServiceModule
 * Created yuong
 */
@Module
public class ServiceModule {
    private Service mService;

    public ServiceModule(Service service) {
        mService = service;
    }

    @Provides
    @PerService
    @ContextLife("Service")
    public Context ProvideServiceContext() {
        return mService;
    }
}
