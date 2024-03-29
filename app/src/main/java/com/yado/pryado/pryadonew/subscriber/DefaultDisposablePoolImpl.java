package com.yado.pryado.pryadonew.subscriber;


import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Description :实现了连接丢弃
 *
 * @date 2018/6/19  13:45
 */
public class DefaultDisposablePoolImpl implements IDisposablePool {
    private CompositeDisposable mDisposable;

    //添加一个请求
    @Override
    public void addDisposable(Disposable disposable) {
        if (mDisposable == null) {
            mDisposable = new CompositeDisposable(disposable);
        } else {
            mDisposable.add(disposable);
        }
    }

    //删除一个请求
    @Override
    public void deleteDisposable(Disposable disposable) {
        mDisposable.delete(disposable);
    }

    //清除请求
    @Override
    public void clearPool() {
        if (mDisposable != null) {
            mDisposable.clear();
            mDisposable = null;
        }
    }
}