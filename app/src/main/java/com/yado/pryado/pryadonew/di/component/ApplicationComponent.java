package com.yado.pryado.pryadonew.di.component;

import android.content.Context;

import com.yado.pryado.pryadonew.di.module.ApplicationModule;
import com.yado.pryado.pryadonew.di.scope.ContextLife;
import com.yado.pryado.pryadonew.di.scope.PerApp;

import dagger.Component;


/**
 *ApplicationComponent
 * @author yuong
 * @date 2017/1/19
 */
@PerApp
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    @ContextLife("Application")
    Context getApplication();
}