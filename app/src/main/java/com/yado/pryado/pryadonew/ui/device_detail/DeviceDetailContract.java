package com.yado.pryado.pryadonew.ui.device_detail;

import com.yado.pryado.pryadonew.base.BaseContract;
import com.yado.pryado.pryadonew.net.INetListener;
import com.yado.pryado.pryadonew.ui.widgit.MyProgressDialog;

/**
 * Created by yuong on 2018/11/15.
 * des:
 */

public interface DeviceDetailContract {


    interface View extends BaseContract.BaseView {

    }

    interface Presenter extends BaseContract.BasePresenter<View> {

        /**
         * 获取详情
         */
        void getDetail(String did, MyProgressDialog progressDialog);

        /**
         * 获取详情
         */
        void getDetail2(String did1, String did2);

        /**
         * 获取隐患列表
         */
        void getBugList(String did);

        /**
         * 获取点数据
         */
        void getPointData(String dateTime, String hour, String pid, String did, String tagid);

        /**
         * 上传隐患
         */
        void postNewBug(String pID, int did, String bugLocation, String bugDesc);

        /**
         * 获取编码详情
         * @param code
         */
        void getInfoByCode(String code);


    }

    interface Model extends BaseContract.BaseModel {
        /**
         * 获取详情
         */
        void getDetail(String did1, MyProgressDialog progressDialog, INetListener<Object, Throwable, Object> listener);

        /**
         * 获取详情
         */
        void getDetail2(String did1, String did2, INetListener<Object, Throwable, Object> listener);

        /**
         * 获取隐患列表
         */
        void getBugList(String did, INetListener<Object, Throwable, Object> listener);

        /**
         * 获取点数据
         */
        void getPointData(String dateTime, String hour, String pid, String did, String tagid, INetListener<Object, Throwable, Object> listener);

        /**
         * 上传隐患
         */
        void postNewBug(String pID, int did, String bugLocation, String bugDesc, INetListener<Object, Throwable, Object> listener);

        /**
         * 获取编码详情
         * @param code
         */
        void getInfoByCode(String code, INetListener<Object, Throwable, Object> listener);
    }


}
