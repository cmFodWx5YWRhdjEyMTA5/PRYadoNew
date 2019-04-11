package com.yado.pryado.pryadonew.ui.riskAssessAndDetail;

import com.yado.pryado.pryadonew.base.BaseContract;
import com.yado.pryado.pryadonew.bean.BugList;
import com.yado.pryado.pryadonew.bean.RoomListBean;
import com.yado.pryado.pryadonew.net.INetListener;

/**
 * Created by yuong on 2018/11/15.
 * des:
 */

public interface AssessContract {


    interface View extends BaseContract.BaseView {

    }

    interface Presenter extends BaseContract.BasePresenter<View> {

        /**
         * 获取站室
         */
        void getRoomList();

        /**
         * 获取隐患
         */
        void getBugList(int pid);

        /**
         * 获取隐患详情
         */
        void getBugInfo(int bugId);


    }

    interface Model extends BaseContract.BaseModel {

        /**
         * 获取站室
         */
        void getRoomList(INetListener<Object, Throwable, Object> listener);

        /**
         * 获取隐患
         */
        void getBugList(int pid, INetListener<Object, Throwable, Object> listener);

        /**
         * 获取隐患详情
         */
        void getBugInfo(int bugId, INetListener<Object, Throwable, Object> listener);

    }


}
