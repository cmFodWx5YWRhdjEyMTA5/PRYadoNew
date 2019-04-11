package com.yado.pryado.pryadonew.di.module;

import android.content.Context;

import com.yado.pryado.pryadonew.MyApplication;
import com.yado.pryado.pryadonew.di.scope.ContextLife;
import com.yado.pryado.pryadonew.di.scope.PerApp;

import dagger.Module;
import dagger.Provides;


/**
 * ApplicationModule
 * Created yuong
 */
@Module
public class ApplicationModule {
    private MyApplication mApplication;

    public ApplicationModule(MyApplication application) {
        mApplication = application;
    }

    @Provides
    @PerApp
    @ContextLife("Application")
    public Context provideApplicationContext() {
        return mApplication.getApplicationContext();
    }
}
