package com.yado.pryado.pryadonew.ui.mod_pwd;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.NumberKeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ToastUtils;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.yado.pryado.pryadonew.MyApplication;
import com.yado.pryado.pryadonew.MyConstants;
import com.yado.pryado.pryadonew.R;
import com.yado.pryado.pryadonew.base.BaseActivity;
import com.yado.pryado.pryadonew.bean.ChangePwNean;
import com.yado.pryado.pryadonew.ui.widgit.MyClearEditText;
import com.yado.pryado.pryadonew.util.SharedPrefUtil;

import org.angmarch.views.NiceSpinner;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = "/ui/mod_pwd/PasswordSettingActivity")
public class PasswordSettingActivity extends BaseActivity<PasswordSettingPresent> implements PasswordSettingContract.View {

    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.et_old_password)
    MyClearEditText etOldPassword;
    @BindView(R.id.et_new_password)
    MyClearEditText etNewPassword;
    @BindView(R.id.et_new_password_confirm)
    MyClearEditText etNewPasswordConfirm;
//    @BindView(R.id.pd_name_spinner)
//    NiceSpinner pdNameSpinner;
    @BindView(R.id.btn_determine)
    Button btnDetermine;
    private String newPassword;

    @Override
    public int inflateContentView() {
        return R.layout.activity_password_setting;
    }

    @Override
    protected void initData() {
//        pdNameSpinner.setVisibility(View.GONE);
        name.setText("修改密码");
        initEvent();
    }

    @Override
    protected boolean isRegisterEventBus() {
        return false;
    }

    @Override
    protected void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    protected boolean isNeedInject() {
        return false;
    }

    private void initEvent() {
        final char[] sumChar = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                ',', '.', '+', '-', '*', '/'};
        //规定密码输入的字符
        etOldPassword.setKeyListener(new NumberKeyListener() {
            @Override
            protected char[] getAcceptedChars() {
                return sumChar;

            }

            @Override
            public int getInputType() {
                return 0x8888;
            }
        });

        etNewPassword.setKeyListener(new NumberKeyListener() {
            @Override
            protected char[] getAcceptedChars() {

                return sumChar;
            }

            @Override
            public int getInputType() {
                return 0x8888;
            }
        });

        etNewPasswordConfirm.setKeyListener(new NumberKeyListener() {
            @Override
            protected char[] getAcceptedChars() {

                return sumChar;

            }

            @Override
            public int getInputType() {
                return 0x8888;
            }
        });

    }


    @OnClick({R.id.tv_shouye, R.id.btn_determine})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_shouye:
                finish();
                break;
            case R.id.btn_determine:
                showChangePWDDiaolog();
                break;
            default:
                break;
        }
    }

    private void showChangePWDDiaolog() {
        final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(mContext);
        dialogBuilder
                .withTitle("密码修改")                               //标题.withTitle(null)  no title
                .withTitleColor("#FFFFFF")                               //def  标题颜色
                .withDividerColor("#11000000")                           //def
                .withMessage("是否要修改密码？")                 //.withMessage(null)  no Msg   内容
                .withMessageColor("#FFCFCFCF")                          //def  | withMessageColor(int resid)   内容颜色
                .withDialogColor("#AF444D50")                            //def  | withDialogColor(int resid)   dialog框颜色
                .withIcon(mContext.getResources().getDrawable(R.drawable.ic_logo2))  //标题栏图标
                .withDuration(700)                                      //def      动画持续时间
                .withEffect(Effectstype.SlideBottom)                                     //def Effectstype.Slidetop  动画模式
                .withButton1Text("否")                                  //def gone     按钮文字
                .withButton2Text("是")                              //def gone
                .isCancelableOnTouchOutside(true)                       //def    | isCancelable(true)是否支持点击dialog框外关闭dialog
//                .setCustomView(R.layout.custom_view, mContext)     //.setCustomView(View or ResId,context)    自定义布局
                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();

                    }
                })
                .setButton2Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                        changePwd();
                    }
                })
                .show();    //展示
    }

    private void changePwd() {
       String oldPassword = etOldPassword.getText().toString().trim();
//        String oldPassword = etOldPassword.getText().toString();
        newPassword = etNewPassword.getText().toString().trim();
        String newPasswordConfirm = etNewPasswordConfirm.getText().toString().trim();
//        String newPasswordConfirm = etNewPasswordConfirm.getText().toString();
        WeakReference<String> username = new WeakReference<>(SharedPrefUtil.getInstance(MyApplication.getInstance()).getT(MyConstants.USERNAME, ""));
//        String username = SharedPrefUtil.getInstance(mContext).getT(MyConstants.USERNAME, "");

        if ("".equals(oldPassword) || "".equals(newPassword) || "".equals(newPasswordConfirm)) {
            if ("".equals(oldPassword)) {
                etOldPassword.setShakeAnimation();
            } else if ("".equals(newPassword)) {
                etNewPassword.setShakeAnimation();
            } else {
                etNewPasswordConfirm.setShakeAnimation();
            }
            ToastUtils.showShort("密码不可为空！");
        } else if (oldPassword.equals(newPassword)) {
            ToastUtils.showShort("密码相同，未做修改！");
        } else if (!newPassword.equals(newPasswordConfirm)) {
            ToastUtils.showShort("请重新确认您的密码！");
        } else {
            assert mPresenter != null;
            mPresenter.changePW(username.get(), oldPassword, newPassword);
        }
    }

    @Override
    public void setChangePwdBean(ChangePwNean changePwdBean) {
        int code = changePwdBean.getCode();
        String msg = changePwdBean.getMessage();
        if (!TextUtils.isEmpty(msg) && msg.contains("成功")) {
            ToastUtils.showShort(msg);
            SharedPrefUtil.getInstance(MyApplication.getInstance()).saveObject(MyConstants.PWD, newPassword);
            ARouter.getInstance().build(MyConstants.LOGIN).navigation();
            finish();
        }
        if (code == 101) {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    finish();
                }
            }, 1000);
        }
    }

}
