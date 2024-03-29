package com.yado.pryado.pryadonew.subscriber;


import io.reactivex.disposables.Disposable;

/**
 * Description : 连接池
 */

public interface IDisposablePool {

    /**
     * Created by XQ Yang on 2017/9/6  17:15.
     * Description : 连接池
     */
    void addDisposable(Disposable disposable);

    void deleteDisposable(Disposable disposable);

    /**
     * 丢弃连接 在view销毁时调用
     */
    void clearPool();
}