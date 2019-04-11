package com.yado.pryado.pryadonew.ui.device_detail;

import com.blankj.utilcode.util.ToastUtils;
import com.yado.pryado.pryadonew.base.BasePresenter;
import com.yado.pryado.pryadonew.bean.AlertBean;
import com.yado.pryado.pryadonew.bean.BugList;
import com.yado.pryado.pryadonew.bean.DeviceDetailBean2;
import com.yado.pryado.pryadonew.bean.DeviceInfo;
import com.yado.pryado.pryadonew.bean.HisData;
import com.yado.pryado.pryadonew.bean.HisData2;
import com.yado.pryado.pryadonew.bean.PostBugResult;
import com.yado.pryado.pryadonew.bean.RoomListBean;
import com.yado.pryado.pryadonew.net.INetListener;
import com.yado.pryado.pryadonew.ui.alert.AlertActivity;
import com.yado.pryado.pryadonew.ui.alert.AlertFragment;
import com.yado.pryado.pryadonew.ui.device_detail.account_info.AccountInfoFragment;
import com.yado.pryado.pryadonew.ui.device_detail.danger.DangerFragment;
import com.yado.pryado.pryadonew.ui.device_detail.real_time_data.RealtimeDataFragment;
import com.yado.pryado.pryadonew.ui.widgit.MyProgressDialog;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by OnexZgj on 2018/9/11:09:37.
 * des:
 */

public class DeviceDetailPresent extends BasePresenter<DeviceDetailContract.View, DeviceDetailModel> implements DeviceDetailContract.Presenter {

    /**
     * 注入到Fragment
     */
    @Inject
    public DeviceDetailPresent() {
    }

    @Override
    public void getDetail(String did, MyProgressDialog progressDialog) {
        mModel.getDetail(did,progressDialog, new INetListener<Object, Throwable, Object>() {
            @Override
            public void success(Object o) {
                DeviceDetailBean2 detailBean = (DeviceDetailBean2) o;
                ((DeviceDetailActivity)mView).setDeviceDetail(detailBean);
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
    public void getDetail2(String did1, String did2) {
        mModel.getDetail2(did1, did2, new INetListener<Object, Throwable, Object>() {
            @Override
            public void success(Object o) {
                List<DeviceDetailBean2.RealTimeParamsBean> realTimeParamsBeans = (List<DeviceDetailBean2.RealTimeParamsBean>) o;
                ((DeviceDetailActivity2)mView).setDataList(realTimeParamsBeans);
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
    public void getBugList(String did) {
        mModel.getBugList(did, new INetListener<Object, Throwable, Object>() {
            @Override
            public void success(Object o) {
                BugList bugList = (BugList) o;
                ((AccountInfoFragment) mView).setBuglist(bugList);
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
    public void getPointData(String dateTime, String hour, String pid, String did, String tagid) {
        mModel.getPointData(dateTime, hour, pid, did, tagid, new INetListener<Object, Throwable, Object>() {
            @Override
            public void success(Object o) {
                HisData2 hisData2 = (HisData2) o;
                ((RealtimeDataFragment) mView).setHisData(hisData2);
            }

            @Override
            public void failed(Throwable throwable) {
                ((RealtimeDataFragment) mView).showNoDataView();
            }

            @Override
            public void loading(Object o) {

            }
        });
    }

    @Override
    public void postNewBug(String pID, int did, String bugLocation, String bugDesc) {
        mModel.postNewBug(pID, did, bugLocation, bugDesc, new INetListener<Object, Throwable, Object>() {
            @Override
            public void success(Object o) {
                PostBugResult postBugResult = (PostBugResult) o;
                ((DangerFragment)mView).setPostBugResult(postBugResult);
            }

            @Override
            public void failed(Throwable throwable) {
//                ToastUtils.showShort(throwable.getMessage());
            }

            @Override
            public void loading(Object o) {

            }
        });
    }

    @Override
    public void getInfoByCode(String code) {
        mModel.getInfoByCode(code, new INetListener<Object, Throwable, Object>() {
            @Override
            public void success(Object o) {
                DeviceInfo deviceInfo = (DeviceInfo) o;
                ((DangerFragment)mView).setDeviceInfo(deviceInfo);
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
