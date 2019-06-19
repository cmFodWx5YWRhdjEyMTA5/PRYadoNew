package com.yado.pryado.pryadonew.ui.riskAssessAndDetail;

import com.blankj.utilcode.util.ToastUtils;
import com.yado.pryado.pryadonew.base.BasePresenter;
import com.yado.pryado.pryadonew.bean.BugInfo;
import com.yado.pryado.pryadonew.bean.BugList;
import com.yado.pryado.pryadonew.bean.RoomListBean;
import com.yado.pryado.pryadonew.net.INetListener;

import javax.inject.Inject;

/**
 * Created by OnexZgj on 2018/9/11:09:37.
 * des:
 */

public class AssessPresent extends BasePresenter<AssessContract.View, AssessModel> implements AssessContract.Presenter {

    /**
     * 注入到Fragment
     */
    @Inject
    public AssessPresent() {
    }

    /**
     * 获取站室
     */
    @Override
    public void getRoomList() {
        mModel.getRoomList(new INetListener<Object, Throwable, Object>() {
            @Override
            public void success(Object o) {
                RoomListBean roomListBean = (RoomListBean) o;
                ((AssessActivity)mView).setRoomlist(roomListBean);
            }

            @Override
            public void failed(Throwable throwable) {
            }

            @Override
            public void loading(Object o) {

            }
        });
    }

    /**
     * 获取隐患
     */
    @Override
    public void getBugList(int pid) {
        mModel.getBugList(pid, new INetListener<Object, Throwable, Object>() {
            @Override
            public void success(Object o) {
                BugList bugList = (BugList) o;
                ((AssessActivity)mView).setBuglist(bugList);
            }

            @Override
            public void failed(Throwable throwable) {
            }

            @Override
            public void loading(Object o) {

            }
        });
    }

    /**
     * 获取隐患详情
     */
    @Override
    public void getBugInfo(int bugId) {
        mModel.getBugInfo(bugId, new INetListener<Object, Throwable, Object>() {
            @Override
            public void success(Object o) {
                ((DangerDetailActivity)mView).setBugInfo((BugInfo) o);
            }

            @Override
            public void failed(Throwable throwable) {
            }

            @Override
            public void loading(Object o) {

            }
        });
    }
}
