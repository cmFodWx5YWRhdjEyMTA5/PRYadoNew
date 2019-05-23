package com.yado.pryado.pryadonew.ui.alert;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnDismissListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yado.pryado.pryadonew.R;
import com.yado.pryado.pryadonew.base.BaseActivity;
import com.yado.pryado.pryadonew.base.BaseFragment;
import com.yado.pryado.pryadonew.bean.RoomListBean;
import com.yado.pryado.pryadonew.ui.adapter.MyPagerAdapter;
import com.yado.pryado.pryadonew.ui.adapter.RoomsSpinnerAdapter;
import com.yado.pryado.pryadonew.ui.widgit.PopupWindow.CommonPopupWindow;
import com.yado.pryado.pryadonew.ui.widgit.ZoomOutPageTransformer;
import com.yado.pryado.pryadonew.util.DateUtils;
import com.yado.pryado.pryadonew.util.ScreenUtil;


import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

@Route(path = "/ui/alert/AlertActivity")
public class AlertActivity extends BaseActivity<AlertPresent> implements AlertContract.View {

    @BindView(R.id.tv_pre)
    TextView tvPre;
    @BindView(R.id.tv_shouye)
    LinearLayout tvShouye;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.id_vp)
    ViewPager idVp;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.vs_spinner)
    ViewStub vsSpinner;

    private static final int TYPE_DAY = 0;
    private static final int TYPE_WEEK = 1;
    private static final int TYPE_MONTH = 2;
    private static final int TYPE_ALL = 3;

    //    private String[] mItems;    //站室名称数组
    private WeakReference<String[]> mItems;   //站室名称数组


    private List<RoomListBean.RowsEntity> rooms = new ArrayList<>();

    private List<BaseFragment> fragments;
    private List<String> titles;
    private AlertFragment fragmentDay;
    private AlertFragment fragmentWeek;
    private AlertFragment fragmentMonth;
    private AlertFragment fragmentAll;
    private MyPagerAdapter mAdapter;

    private int pid = -1;
    private TimePickerView pvTime;
    private Calendar date;
    private Calendar endDate;
    private Calendar startDate;
    private SimpleDateFormat formatter_year;
    private SimpleDateFormat formatter_mouth;
    private SimpleDateFormat formatter_day;
    private int switchDate;
    private boolean isSure;

    private LinearLayout llRooms;
    private TextView tvRoomName;
    private ImageView ivRooms;
    private CommonPopupWindow popupWindow;
    private View popView;
    private RecyclerView rv_rooms;
    private TextView room_name;
    @Inject
    RoomsSpinnerAdapter adapter;


    @Override
    public int inflateContentView() {
        return R.layout.activity_alert;
    }

    @Override
    protected void initInjector() {
        mActivityComponent.inject(this);
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void initData() {
        name.setText(getResources().getString(R.string.baojin));
        llRooms = vsSpinner.inflate().findViewById(R.id.ll_rooms);
        tvRoomName = llRooms.findViewById(R.id.tv_room_name);
        ivRooms = llRooms.findViewById(R.id.iv_rooms);
        initFragments();
        initPopView();
        idVp.setAdapter(mAdapter);
        idVp.setOffscreenPageLimit(3);
        idVp.setPageTransformer(true, new ZoomOutPageTransformer());
        idVp.setCurrentItem(0);
        tabLayout.setupWithViewPager(idVp);
        initDate();
        initRangeDate();
        assert mPresenter != null;
        mPresenter.getRoomList();
        initListener();
    }

    private void initDate() {
        formatter_year = new SimpleDateFormat("yyyy ");
        formatter_mouth = new SimpleDateFormat("MM ");
        formatter_day = new SimpleDateFormat("dd ");
        date = Calendar.getInstance();
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String year_str = formatter_year.format(curDate);
        int year_int = (int) Double.parseDouble(year_str);
        String momth_str = formatter_mouth.format(curDate);
        int momth_int = (int) Double.parseDouble(momth_str);

        String day_str = formatter_day.format(curDate);
        int day_int = (int) Double.parseDouble(day_str);
        date.set(year_int, momth_int - 1, day_int - 1);
    }

    private View.OnClickListener mTabOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            int position = (int) v.getTag();
            if (position == 3 && tabLayout.getTabAt(position).isSelected()) {
                isSure = false;
                //时间选择器
                if (pvTime == null) {
                    pvTime = new TimePickerBuilder(mContext, new OnTimeSelectListener() {
                        @Override
                        public void onTimeSelect(Date date, View v) {
                            if (date.compareTo(DateUtils.getNowDate()) > 0) {
                                ToastUtils.showShort("选择的日期不能大于今天！");
                                return;
                            }
                            switchDate++;
                            String year_str = formatter_year.format(date);
                            int year_int = (int) Double.parseDouble(year_str);

                            String momth_str = formatter_mouth.format(date);
                            int momth_int = (int) Double.parseDouble(momth_str);

                            String day_str = formatter_day.format(date);
                            int day_int = (int) Double.parseDouble(day_str);
                            if (momth_int == 1) {
                                momth_int = 13;
                                year_int = year_int - 1;
                            }
                            ((AlertActivity) mActivityComponent.getActivity()).date.set(year_int, momth_int - 1, day_int);
                            if (switchDate % 2 == 1) {
                                fragmentAll.setStartDate(DateUtils.dateToStr(date));
                                isSure = true;

                            } else {
                                fragmentAll.setEndDate(DateUtils.dateToStr(date));
                                isSure = false;
                            }
                        }
                    })
                            .setRangDate(startDate, endDate)
                            .build();
                }
                pvTime.setDate(((AlertActivity) mActivityComponent.getActivity()).date);
                pvTime.setTitleText("开始时间");
                pvTime.show();
                pvTime.setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss(Object o) {
                        if (isSure) {
                            pvTime.setDate(((AlertActivity) mActivityComponent.getActivity()).date);
                            pvTime.setTitleText("结束时间");
                            pvTime.show();
                        }
                    }
                });
            } else {
                TabLayout.Tab tab = tabLayout.getTabAt(position);
                if (tab != null) {
                    tab.select();
                }
            }
        }
    };

    private void initRangeDate() {
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String year_str = formatter_year.format(curDate);
        int year_int = (int) Double.parseDouble(year_str);


        String momth_str = formatter_mouth.format(curDate);
        int momth_int = (int) Double.parseDouble(momth_str);

        String day_str = formatter_day.format(curDate);
        int day_int = (int) Double.parseDouble(day_str);

        startDate = Calendar.getInstance();
        startDate.set(2013, 1, 1);//设置起始年份
        endDate = Calendar.getInstance();
        endDate.set(year_int, momth_int, day_int);//设置结束年份
    }

    private void initListener() {
        tvShouye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        llRooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popupWindow == null) {
                    initPopWindow();
                }
                popupWindow.showAsDropDown(view);
                Animation rotate = new RotateAnimation(0f, 180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                LinearInterpolator lin = new LinearInterpolator();
                rotate.setInterpolator(lin); //设置插值器
                rotate.setDuration(200);//设置动画持续周期
                rotate.setFillAfter(true);//动画执行完后是否停留在执行完的状态
                ivRooms.startAnimation(rotate);
            }
        });

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab == null) {
                return;
            }
            //这里使用到反射，拿到Tab对象后获取Class
            Class c = tab.getClass();
            try {
                //Filed “字段、属性”的意思,c.getDeclaredField 获取私有属性。
                //"view"是Tab的私有属性名称(可查看TabLayout源码),类型是 TabView,TabLayout私有内部类。
                Field field = c.getDeclaredField("view");
                //值为 true 则指示反射的对象在使用时应该取消 Java 语言访问检查。值为 false 则指示反射的对象应该实施 Java 语言访问检查。
                //如果不这样会报如下错误
                // java.lang.IllegalAccessException:
                //Class com.test.accessible.Main
                //can not access
                //a member of class com.test.accessible.AccessibleTest
                //with modifiers "private"
                field.setAccessible(true);
                final View view = (View) field.get(tab);
                if (view == null) {
                    return;
                }
                view.setTag(i);
                view.setOnClickListener(mTabOnClickListener);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getByPid(pid);
    }


    private void initFragments() {
        fragments = new ArrayList<>();
        fragmentDay = AlertFragment.newInstance(TYPE_DAY);
        fragmentWeek = AlertFragment.newInstance(TYPE_WEEK);
        fragmentMonth = AlertFragment.newInstance(TYPE_MONTH);
        fragmentAll = AlertFragment.newInstance(TYPE_ALL);
        fragments.add(fragmentDay);
        fragments.add(fragmentWeek);
        fragments.add(fragmentMonth);
        fragments.add(fragmentAll);
        titles = new ArrayList<>();
        titles.add("今日");
        titles.add("本周");
        titles.add("本月");
//        titles.add("全部"); 隐藏该功能 改为选择日期筛选
        titles.add("自定义");
        mAdapter = new MyPagerAdapter(getSupportFragmentManager(), fragments, titles);
    }

    private void getByPid(int pid) {
        reGet(fragmentDay, pid);
        reGet(fragmentWeek, pid);
        reGet(fragmentMonth, pid);
        reGet(fragmentAll, pid);
    }

    private void reGet(AlertFragment fragment, int pid) {
        if (fragment != null) {
            fragment.onRefresh(pid);
        }
    }

    @Override
    protected boolean isRegisterEventBus() {
        return false;
    }

    @Override
    protected boolean isNeedInject() {
        return false;
    }

    public void setRooms(List<RoomListBean.RowsEntity> rooms) {
        if (rooms.size() > 0) {
            RoomListBean.RowsEntity rowsEntity = new RoomListBean.RowsEntity();
            rowsEntity.setName("全部站点");
            rowsEntity.setPID(0);
            this.rooms.add(rowsEntity);
            this.rooms.addAll(rooms);
            llRooms.setVisibility(View.VISIBLE);
            tvRoomName.setText(this.rooms.get(0).getName());
            pid = this.rooms.get(0).getPID();
            adapter.setNewData( this.rooms);
        } else {
            llRooms.setVisibility(View.GONE);
        }

    }

    private void initPopWindow() {
        if (popupWindow == null) {
            popupWindow = new CommonPopupWindow.Builder(this)
                    .setView(popView)
                    .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtil.getScreenHeight(mContext) * 2 / 3)
                    .setAnimationStyle(R.style.AnimDown)
                    .setOutsideTouchable(true)
                    .create();
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    Animation rotate = new RotateAnimation(180f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    LinearInterpolator lin = new LinearInterpolator();
                    rotate.setInterpolator(lin); //设置插值器
                    rotate.setDuration(200);//设置动画持续周期
                    rotate.setFillAfter(true);//动画执行完后是否停留在执行完的状态
                    ivRooms.startAnimation(rotate);
                }
            });
        }

    }

    private void initPopView() {
        popView = LayoutInflater.from(mContext).inflate(R.layout.popup_item2, null);
        rv_rooms = popView.findViewById(R.id.rv_devices);
        room_name = popView.findViewById(R.id.room_name);
        room_name.setText("站室列表");
        room_name.setBackgroundColor(Color.parseColor("#C8484848"));
        rv_rooms.setLayoutManager(new LinearLayoutManager(mContext));
        //添加Android自带的分割线
        rv_rooms.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rv_rooms.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                RoomListBean.RowsEntity rowsEntity = (RoomListBean.RowsEntity) adapter.getData().get(position);
                if (pid != rowsEntity.getPID()) {
                    pid = rowsEntity.getPID();
                    getByPid(pid);
                    tvRoomName.setText(rowsEntity.getName());
                }
                popupWindow.dismiss();

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (rooms != null) {
            rooms.clear();
            rooms = null;
        }
        if (fragments != null) {
            fragments.clear();
            fragments = null;
        }
        if (titles != null) {
            titles.clear();
            titles = null;
        }
    }
}
