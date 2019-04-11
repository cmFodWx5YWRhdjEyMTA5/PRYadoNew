package com.yado.pryado.pryadonew.ui.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.yado.pryado.pryadonew.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class MyPagerAdapter extends FragmentPagerAdapter {

    private List<BaseFragment> mfragmentList;
    private List<String> titles;

    public MyPagerAdapter(FragmentManager fm, List<BaseFragment> fragmentList, List<String> titles) {
        super(fm);
        this.mfragmentList = fragmentList;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return mfragmentList.get(position);
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

    @Override
    public int getCount() {
        return mfragmentList.size();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//            super.destroyItem(container, position, object);
    }
}
