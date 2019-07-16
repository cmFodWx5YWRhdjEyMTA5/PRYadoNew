package com.yado.pryado.pryadonew.ui.affair;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.yado.pryado.pryadonew.MyApplication;
import com.yado.pryado.pryadonew.MyConstants;
import com.yado.pryado.pryadonew.R;
import com.yado.pryado.pryadonew.base.BaseActivity;
import com.yado.pryado.pryadonew.bean.StatusBean;
import com.yado.pryado.pryadonew.util.BitmapUtil;
import com.yado.pryado.pryadonew.util.SharedPrefUtil;
import com.yado.pryado.pryadonew.util.Util;

import org.angmarch.views.NiceSpinner;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import q.rorbin.badgeview.QBadgeView;

@Route(path = "/ui/affair/AffairActivity")
public class AffairActivity extends BaseActivity<AffairPresent> implements AffairContract.View {

    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.ll_upcoming)
    LinearLayout llUpcoming;

    private int count;
    private QBadgeView badgeView;

    /**
     * 获取布局
     * @return
     */
    @Override
    public int inflateContentView() {
        return R.layout.activity_affair;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        name.setText(getResources().getString(R.string.order));
        badgeView = new QBadgeView(mContext);
        assert mPresenter != null;
        mPresenter.getCounts();
    }

    /**
     * 注入View
     */
    @Override
    protected void initInjector() {
        mActivityComponent.inject(this);
    }

    /**
     * 是否需要注册EventBus
     * @return
     */
    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    /**
     * 是否需要注入Arouter
     * @return
     */
    @Override
    protected boolean isNeedInject() {
        return false;
    }

    @OnClick({R.id.tv_shouye, R.id.ll_upcoming, R.id.ll_his_order})
    public void onViewClicked(View view) {
        if (!Util.isDoubleClick()) {
            switch (view.getId()) {
                case R.id.tv_shouye:
                    finish();
                    break;
                case R.id.ll_upcoming:
                    ARouter.getInstance().build(MyConstants.MY_TODO).navigation();
                    break;
                case R.id.ll_his_order:
//                startActivity(new Intent(AffairActivity.this, HisOrderActivity.class));
                    ARouter.getInstance().build(MyConstants.HIS_ORDER).navigation();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //获取角标
        if (badgeView != null) {
            badgeView.bindTarget(llUpcoming).setBadgeNumber(SharedPrefUtil.getInstance(MyApplication.getInstance()).getT(MyConstants.counts_upcoming, 0));
        }
    }

    @Override
    public int getCounts() {
        return count;
    }

    @Override
    public void setCounts(int count) {
        this.count = count;
        assert mPresenter != null;
        mPresenter.setCounts(this.count);
    }

    @Subscribe
    public void onMessageEvent(StatusBean change) {
        if (getString(R.string.has_new_task).equals(change.getStatus())) {
            assert mPresenter != null;
            mPresenter.getCounts();
        }
    }

    /**
     * 设置角标
     * @param count
     */
    @Override
    public void setBadgeView(int count) {
//      badgeView.bindTarget(llUpcoming).setBadgeNumber(0);
        badgeView.bindTarget(llUpcoming).setBadgeNumber(count);
    }

}
