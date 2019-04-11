package com.yado.pryado.pryadonew.ui.monitor;

import android.os.Parcelable;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ToastUtils;
import com.yado.pryado.pryadonew.MyConstants;
import com.yado.pryado.pryadonew.base.BasePresenter;
import com.yado.pryado.pryadonew.bean.AlertBean;
import com.yado.pryado.pryadonew.bean.HisData;
import com.yado.pryado.pryadonew.bean.RoomListBean;
import com.yado.pryado.pryadonew.bean.TypeBean;
import com.yado.pryado.pryadonew.net.INetListener;
import com.yado.pryado.pryadonew.ui.alert.AlertActivity;
import com.yado.pryado.pryadonew.ui.alert.AlertFragment;
import com.yado.pryado.pryadonew.ui.temp_monitor.TempMonitorPlanFragment;
import com.yado.pryado.pryadonew.util.Util;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by OnexZgj on 2018/9/11:09:37.
 * des:
 */

public class MonitorPresent extends BasePresenter<MonitorContract.View, MonitorModel> implements MonitorContract.Presenter {

    /**
     * 注入到Fragment
     */
    @Inject
    public MonitorPresent() {
    }


    @Override
    public void getRoomList() {
        mModel.getRoomList(new INetListener<Object, Throwable, Object>() {
            @Override
            public void success(Object o) {
                RoomListBean roomListBean = (RoomListBean) o;
                if (roomListBean == null) {
                    return;
                }
                ((MonitorActivity) mView).setRooms(roomListBean.getRows());

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
    public void getGraphType(int pid, final RoomListBean.RowsEntity room, final List<RoomListBean.RowsEntity> rooms, final int position) {
        mModel.getGraphType(pid, new INetListener<Object, Throwable, Object>() {
            @Override
            public void success(Object o) {
                TypeBean typeBean = (TypeBean) o;
//                mView.setGraph((TypeBean) o);
                ARouter.getInstance().build(MyConstants.ROOM_DETAIL)
                        .withParcelable("room", room)
                        .withSerializable("TypeBean", typeBean)
                        .withParcelableArrayList("room_list", (ArrayList<? extends Parcelable>) rooms)
                        .withInt("position", position)
                        .navigation();
            }

            @Override
            public void failed(Throwable throwable) {
                ARouter.getInstance().build(MyConstants.ROOM_DETAIL)
                        .withParcelable("room", room)
                        .withParcelableArrayList("room_list", (ArrayList<? extends Parcelable>) rooms)
                        .withInt("position", position)
                        .navigation();
            }

            @Override
            public void loading(Object o) {

            }
        });
    }
}
