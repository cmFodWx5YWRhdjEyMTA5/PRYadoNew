package com.yado.pryado.pryadonew.ui.riskAssessAndDetail;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.yado.pryado.pryadonew.MyConstants;
import com.yado.pryado.pryadonew.R;
import com.yado.pryado.pryadonew.base.BaseActivity;
import com.yado.pryado.pryadonew.bean.BugList;
import com.yado.pryado.pryadonew.bean.RoomListBean;
import com.yado.pryado.pryadonew.ui.adapter.DangerAdapter;
import com.yado.pryado.pryadonew.ui.widgit.SimplePaddingDecoration;
import com.yado.pryado.pryadonew.util.NetworkChecker;
import com.yado.pryado.pryadonew.util.Util;

import org.angmarch.views.NiceSpinner;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = "/ui/riskAssessAndDetail/AssessActivity")
public class AssessActivity extends BaseActivity<AssessPresent> implements AssessContract.View {
    @BindView(R.id.tv_pre)
    TextView tvPre;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.sp_choice_room)
    NiceSpinner spChoiceRoom;
    @BindView(R.id.pb_scanning)
    NumberProgressBar pbScanning;
    @BindView(R.id.tv_scanning)
    TextView tvScanning;
    @BindView(R.id.fl_progress)
    FrameLayout flProgress;
    @BindView(R.id.tv_find)
    TextView tvFind;
    @BindView(R.id.tv_danger_num)
    TextView tvDangerNum;
    @BindView(R.id.cb_get_list)
    Button cbGetList;
    @BindView(R.id.rv_dangers)
    RecyclerView rvDangers;
    @BindView(R.id.ll_danger)
    LinearLayout llDanger;
    @BindView(R.id.btn_assess1)
    Button btnAssess1;
//    @BindView(R.id.pd_name_spinner)
//    NiceSpinner pdNameSpinner;

    private AccessTask thread;

    private WeakReference<String[]> mItems; //站室名称数组
//    private String[] mItems;    //站室名称数组

    private ArrayList<RoomListBean.RowsEntity> rooms;
    private int selectPos = 0;

    int lastCount = -1;

    private List<BugList.ListmapBean> danger_list;


    @Inject
    DangerAdapter adapter;

    private MyHandler handler = new MyHandler(AssessActivity.this);

    private static class MyHandler extends Handler {

        WeakReference<AssessActivity> weakReference;

        MyHandler(AssessActivity assessActivity) {
            this.weakReference = new WeakReference<>(assessActivity);

        }

        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int size = weakReference.get().danger_list.size();
            if (size > 0) {
                int i = msg.what;
                if (weakReference.get().pbScanning != null) {
                    weakReference.get().pbScanning.setProgress(i);
                }
//                double j = 100.0 / size;
                int pos = msg.arg1;
                if (pos + 1 != weakReference.get().lastCount) {
                    weakReference.get().lastCount = pos + 1;
                    if (pos + 1 >= 1) {
                        weakReference.get().tvDangerNum.setTextColor(Color.RED);
                    } else {
                        weakReference.get().tvDangerNum.setTextColor(Color.GREEN);
                    }
                    weakReference.get().tvDangerNum.setText(weakReference.get().adapter.getData().size() + 1 + "");
                    BugList.ListmapBean bug = weakReference.get().danger_list.get(pos);
                    weakReference.get().adapter.addData(bug);//
                    weakReference.get().tvScanning.setText("正在诊断:" + bug.getBugDesc());
                    switch (bug.getBugLevel()) {
                        case "重大":
                            weakReference.get().tvScanning.setTextColor(Color.RED);
                            break;
                        case "紧急":
                            weakReference.get().tvScanning.setTextColor(Color.YELLOW);
                            break;
                        default:
                            weakReference.get().tvScanning.setTextColor(Color.BLACK);
                            break;
                    }
                    if (pos == weakReference.get().danger_list.size() - 1) {
                        weakReference.get().flProgress.setVisibility(View.GONE);
                        weakReference.get().tvFind.setText("共发现");
                        weakReference.get().cbGetList.setVisibility(View.VISIBLE);
                        weakReference.get().btnAssess1.setVisibility(View.GONE);
                    }
                }
            } else {
                weakReference.get().tvDangerNum.setTextColor(Color.GREEN);
                weakReference.get().tvDangerNum.setText(0 + "");
                weakReference.get().flProgress.setVisibility(View.GONE);
                weakReference.get().tvFind.setText("共发现");
                weakReference.get().cbGetList.setVisibility(View.VISIBLE);
                weakReference.get().btnAssess1.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int inflateContentView() {
        return R.layout.activity_assess;
    }

    @Override
    protected void initInjector() {
        mActivityComponent.inject(this);

    }

    @Override
    protected void initData() {
//        pdNameSpinner.setVisibility(View.GONE);
        rvDangers.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        rvDangers.addItemDecoration(new SimplePaddingDecoration(mContext));
        rvDangers.setAdapter(adapter);
        name.setText("隐患评估");
        tvPre.setText("返回");
        assert mPresenter != null;
        mPresenter.getRoomList();
        cbGetList.setVisibility(View.GONE);
        llDanger.setVisibility(View.GONE);
        thread = new AccessTask();

        initLinstener();
    }

    private void initLinstener() {
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                BugList.ListmapBean bug = (BugList.ListmapBean) adapter.getData().get(position);
//                Intent intent = new Intent(mContext, DangerDetailActivity.class);
//                intent.putExtra("bug", bug);
//                startActivity(intent);
                if (!Util.isDoubleClick()) {
                    ARouter.getInstance().build(MyConstants.DANGER_DETAIL)
                            .withParcelable("bug", bug)
                            .navigation();
                }


            }
        });

        spChoiceRoom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectPos = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    protected boolean isRegisterEventBus() {
        return false;
    }

    @Override
    protected boolean isNeedInject() {
        return false;
    }

    @OnClick({R.id.tv_shouye, R.id.btn_assess1, R.id.cb_get_list})
    public void onViewClicked(View view) {
        NetworkChecker checker = new NetworkChecker(this);
        switch (view.getId()) {
            case R.id.tv_shouye:
                finish();
                break;
            case R.id.cb_get_list:
                if (!checker.isConnected()) {
                    ToastUtils.showShort(R.string.net_error);
                    return;
                }
                clearData();
                assert mPresenter != null;
                mPresenter.getBugList(deliver(selectPos));
                btnAssess1.setVisibility(View.VISIBLE);
                cbGetList.setVisibility(View.GONE);
                break;
            case R.id.btn_assess1:
                if (!checker.isConnected()) {
                    ToastUtils.showShort(R.string.net_error);
                    return;
                }
                if ("重新诊断".contentEquals(btnAssess1.getText().toString().trim())) {
                    thread.stop = true;
                    clearData();
                    btnAssess1.setText("停止诊断");
                    assert mPresenter != null;
                    mPresenter.getBugList(deliver(selectPos));
                    return;
                }
                if ("停止诊断".contentEquals(btnAssess1.getText().toString().trim())) {
//                    if (thread.isAlive()) {
                    llDanger.setVisibility(View.GONE);
                    btnAssess1.setVisibility(View.VISIBLE);
                    thread.stop = true;
                    btnAssess1.setGravity(Gravity.CENTER);
//                    }
                    clearData();
                    btnAssess1.setText("重新诊断");
                    return;
                }
//                if (thread.isAlive()) {
//                    llDanger.setVisibility(View.GONE);
//                    btnAssess1.setVisibility(View.VISIBLE);
//                    thread.stop = true;
//                    btnAssess1.setText("重新诊断");
//                    btnAssess1.setGravity(Gravity.CENTER);
//                }
                else {
                    btnAssess1.setText("停止诊断");
                    btnAssess1.setVisibility(View.VISIBLE);
                    assert mPresenter != null;
                    mPresenter.getBugList(deliver(selectPos));
                }
                break;
            default:
                break;
        }
    }

    private int deliver(int selectPos) {
        if (selectPos == 0) {
            return 0;
        } else {
            return rooms.get(selectPos - 1).getPID();
        }
    }

    private void clearData() {
        thread.stop = true;
        lastCount = -1;
        adapter.getData().clear();
        adapter.notifyDataSetChanged();
        tvDangerNum.setText(0 + "");
        tvDangerNum.setTextColor(Color.GREEN);
    }

    public void setRoomlist(RoomListBean roomListBean) {
        rooms = roomListBean.getRows();
        mItems = new WeakReference<>(new String[rooms.size() + 1]);
        mItems.get()[0] = getString(R.string.all_rooms);
        for (int i = 1; i < rooms.size() + 1; i++) {
            mItems.get()[i] = rooms.get(i - 1).getName();
        }
        List<String> dataset = new LinkedList<>(Arrays.asList(mItems.get()));
        spChoiceRoom.attachDataSource(dataset);
        btnAssess1.setEnabled(mItems.get().length > 0);
    }

    public void setBuglist(BugList bugList) {
        danger_list = bugList.getListmap();
        pbScanning.setMax(100);
        startAssess();
    }

    /**
     * 启动分线程
     */
    private void startAssess() {
        if (danger_list == null || danger_list.size() <= 0) {
            flProgress.setVisibility(View.GONE);
            tvFind.setText("共发现");
            cbGetList.setVisibility(View.VISIBLE);
            btnAssess1.setVisibility(View.GONE);
        }
        if (thread != null) {
            thread.stop = true;
        }
        llDanger.setVisibility(View.VISIBLE);
        flProgress.setVisibility(View.VISIBLE);
        tvFind.setText("已发现");
        thread = new AccessTask();
        thread.start();
    }

    class AccessTask extends Thread {
        volatile boolean stop = false;

        @Override
        public void run() {
            while (!stop && danger_list != null && danger_list.size() > 0) {
                for (int i = 0; i < danger_list.size(); i++) {
                    if (stop) {
                        break;
                    }
                    SystemClock.sleep(50);
                    if (handler != null) {
                        Message message = Message.obtain();
                        message.what = (int) ((float) i / (float) danger_list.size() * 100f);
                        message.arg1 = i;
//                        handler.sendEmptyMessage((int) ((float)i / (float) danger_list.size() * 100f));
                        handler.sendMessage(message);
                    }
                }
                stop = true;
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (thread != null && thread.isAlive()) {
            thread.interrupt();
            thread = null;
        }
        handler.removeCallbacksAndMessages(null);
        handler = null;
        if (rooms != null) {
            rooms.clear();
            rooms = null;
        }
        if (danger_list != null) {
            danger_list.clear();
            danger_list = null;
        }
        super.onDestroy();
    }
}
