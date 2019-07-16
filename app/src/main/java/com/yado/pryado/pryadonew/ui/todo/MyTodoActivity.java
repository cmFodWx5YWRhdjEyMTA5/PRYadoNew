package com.yado.pryado.pryadonew.ui.todo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.yado.pryado.pryadonew.MyConstants;
import com.yado.pryado.pryadonew.R;
import com.yado.pryado.pryadonew.base.BaseActivity;
import com.yado.pryado.pryadonew.base.BaseFragment;
import com.yado.pryado.pryadonew.ui.widgit.ZoomOutPageTransformer;

import org.angmarch.views.NiceSpinner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = "/ui/todo/MyTodoActivity")
public class MyTodoActivity extends BaseActivity {

    @BindView(R.id.tv_pre)
    TextView tvPre;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.id_vp)
    ViewPager idVp;

    private List<BaseFragment> fragments;

    private FragmentPagerAdapter mAdapter;
    private TaskListFragment fragment1;//日常巡检
    private TaskListFragment fragment2;//应急抢修
    private List<String> titles = new ArrayList<>();

    /**
     * 加载布局
     * @return
     */
    @Override
    public int inflateContentView() {
        return R.layout.activity_my_todo_actovity;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        initFragments();
        tvPre.setText(getResources().getString(R.string.back));
        name.setText(getResources().getString(R.string.upcoming));
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent().setAction(MyConstants.NOTICE_CANCEL_ACTION)); //发送广播,取消通知栏的通知
    }

    /**
     * 初始化fragment
     */
    private void initFragments() {
        fragments = new ArrayList<>();
        fragment1 = TaskListFragment.newInstance(getString(R.string.richangxunjian));
        fragment2 = TaskListFragment.newInstance(getString(R.string.urgent));
        fragments.add(fragment1);
        fragments.add(fragment2);
        titles.add(getString(R.string.richangxunjian));
        titles.add(getString(R.string.urgent));
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return titles.size();
            }

            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                String title = titles.get(position);
                if (title == null) {
                    title = "";
                }
                return title;
            }
        };
        idVp.setAdapter(mAdapter);
        idVp.setOffscreenPageLimit(2);
        idVp.setPageTransformer(true, new ZoomOutPageTransformer());
        tabLayout.setupWithViewPager(idVp);
    }

    /**
     * 是否需要注册 EventBus
     * @return
     */
    @Override
    protected boolean isRegisterEventBus() {
        return false;
    }

    /**
     * 是否需要注入Arouter
     * @return
     */
    @Override
    protected boolean isNeedInject() {
        return false;
    }


    @OnClick(R.id.tv_shouye)
    public void onViewClicked() {
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
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
