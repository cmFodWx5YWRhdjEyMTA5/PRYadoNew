package com.yado.pryado.pryadonew.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ToastUtils;
import com.yado.pryado.pryadonew.MyApplication;
import com.yado.pryado.pryadonew.di.component.DaggerFragmentComponent;
import com.yado.pryado.pryadonew.di.component.FragmentComponent;
import com.yado.pryado.pryadonew.di.module.FragmentModule;


import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment<T extends BaseContract.BasePresenter> extends Fragment implements BaseContract.BaseView {

    protected Context context;

    protected View mFragmentView;
    protected Unbinder unbinder;

    public boolean isUIVisible;
    public boolean isViewCreated;
    public boolean isFirstLoad;
    /**
     * 泛型注入每个继承BaseFragment的子Fragment中
     */
    @Nullable
    @Inject
    protected T mPresenter;
    protected FragmentComponent mFragmentComponent;


    /**
     * 该类被系统创建的时候调用
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFragmentComponent();
        context = getActivity();
        if (isRegisterEventBus()) {
            if (!EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this);
            }
        }
        initInjector();
        attachView();
    }

    /**
     * 初始化FragmentComponent
     */
    private void initFragmentComponent() {
        mFragmentComponent = DaggerFragmentComponent.builder()
                .applicationComponent(((MyApplication) getActivity().getApplication()).getApplicationComponent())
                .fragmentModule(new FragmentModule(this))
                .build();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mFragmentView == null) {
            mFragmentView = inflater.inflate(getLayoutId(), container, false);
            initView();
        }
        unbinder = ButterKnife.bind(this, mFragmentView);
        return mFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isViewCreated = true;
        if (!isFirstLoad) {
            lazyLoad();
            isFirstLoad = true;
        }
    }

    /**
     * 是否需要注册EventBus
     * @return
     */
    protected abstract boolean isRegisterEventBus();

    /**
     * 获取布局
     * @return
     */
    public abstract int getLayoutId();

    /**
     * 吃书画注入器
     */
    protected abstract void initInjector();

    /**
     * 初始化View
     */
    public abstract void initView();

    /**
     * 加载数据
     */
    protected abstract void loadData();

    /**
     * 懒加载
     */
    private void lazyLoad() {
        //这里进行双重标记判断,是因为setUserVisibleHint会多次回调,并且会在onCreateView执行前回调,必须确保onCreateView加载完毕且页面可见,才加载数据
        if (isViewCreated && isUIVisible) {
            loadData();
            //数据加载完毕,恢复标记,防止重复加载
            isViewCreated = false;
            isUIVisible = false;
        }
    }

    /**
     * 贴上view
     */
    private void attachView() {
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
    }

    /**
     * 分离view
     */
    private void detachView() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //isVisibleToUser这个boolean值表示:该Fragment的UI 用户是否可见
        if (isVisibleToUser) {
            isUIVisible = true;
            lazyLoad();
        } else {
            isUIVisible = false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isRegisterEventBus()) {
            if (EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().unregister(this);
            }
        }
        context = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        detachView();
        mPresenter = null;
        mFragmentComponent = null;
        mFragmentView = null;
    }
}
