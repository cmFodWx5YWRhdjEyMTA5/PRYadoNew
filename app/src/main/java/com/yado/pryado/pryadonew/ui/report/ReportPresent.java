package com.yado.pryado.pryadonew.ui.report;

import com.blankj.utilcode.util.ToastUtils;
import com.yado.pryado.pryadonew.base.BasePresenter;
import com.yado.pryado.pryadonew.bean.BugInfo;
import com.yado.pryado.pryadonew.bean.BugList;
import com.yado.pryado.pryadonew.bean.PostBugResult;
import com.yado.pryado.pryadonew.bean.RoomListBean;
import com.yado.pryado.pryadonew.net.INetListener;
import com.yado.pryado.pryadonew.ui.riskAssessAndDetail.AssessActivity;
import com.yado.pryado.pryadonew.ui.riskAssessAndDetail.DangerDetailActivity;

import javax.inject.Inject;

/**
 * Created by OnexZgj on 2018/9/11:09:37.
 * des:
 */

public class ReportPresent extends BasePresenter<ReportContract.View, ReportModel> implements ReportContract.Presenter {

    /**
     * 注入到Fragment
     */
    @Inject
    public ReportPresent() {
    }

    @Override
    public void getRoomList() {
        mModel.getRoomList(new INetListener<Object, Throwable, Object>() {
            @Override
            public void success(Object o) {
                RoomListBean roomListBean = (RoomListBean) o;
                ((ReportActivity) mView).setRoomlist(roomListBean);
            }

            @Override
            public void failed(Throwable throwable) {
            }

            @Override
            public void loading(Object o) {

            }
        });
    }

    @Override
    public void postNewBug(int pid, String bugLocation, String bugDesc) {
        mModel.postNewBug(pid, bugLocation, bugDesc, new INetListener<Object, Throwable, Object>() {
            @Override
            public void success(Object o) {
                PostBugResult postBugResult = (PostBugResult) o;
                int resultCode = postBugResult.getResultCode();
                if (resultCode == 1) {
                    ((ReportActivity) mView).uploadFile(postBugResult.getBugid() + "");
                } else {
                    ToastUtils.showShort("提交失败!");
                }
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
