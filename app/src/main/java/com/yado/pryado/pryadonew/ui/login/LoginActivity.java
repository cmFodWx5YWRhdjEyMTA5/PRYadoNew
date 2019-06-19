package com.yado.pryado.pryadonew.ui.login;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yado.pryado.pryadonew.MyApplication;
import com.yado.pryado.pryadonew.MyConstants;
import com.yado.pryado.pryadonew.R;
import com.yado.pryado.pryadonew.base.BaseActivity;
import com.yado.pryado.pryadonew.net.PRRetrofit;
import com.yado.pryado.pryadonew.ui.widgit.MyClearEditText;
import com.yado.pryado.pryadonew.ui.widgit.MyProgressDialog;
import com.yado.pryado.pryadonew.ui.widgit.SmoothCheckBox;
import com.yado.pryado.pryadonew.util.SharedPrefUtil;

import org.angmarch.views.NiceSpinner;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import io.reactivex.functions.Consumer;

// 在支持路由的页面上添加注解(必选)
// 这里的路径需要注意的是至少需要有两级，/xx/xx
@Route(path = "/ui/login/LoginActivity")
public class LoginActivity extends BaseActivity<LoginPresent> implements LoginContract.View {

    @BindView(R.id.user_name)
    MyClearEditText userName;
    @BindView(R.id.user_password)
    MyClearEditText userPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.tv_yado)
    TextView tvYado;
    @BindView(R.id.smoothCheckBox)
    SmoothCheckBox smoothCheckBox;
    @BindView(R.id.tv_change_ip)
    TextView tv_change_ip;
    @BindView(R.id.iv_logo)
    ImageView ivLogo;

    private NiftyDialogBuilder dialogBuilder;

    private MyProgressDialog progressDialog;
    private WeakReference<String> username;
    private WeakReference<String> password;
    private WeakReference<String> title;
    private WeakReference<Boolean> isLogin;
    private boolean isIP = true;
    private long showTime = 0;
    private NiceSpinner ip_spinner;
    private WeakReference<String[]> mItems;
    private List<String> mIps;
    private View dialogView;

    private TextView tv_change;
    private EditText et_ip1;
    private EditText et_ip2;
    private EditText et_ip3;
    private EditText et_ip4;
    private EditText et_ip5;
    private EditText et_domain;
    private LinearLayout ll_ip;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermissions();
    }

    /**
     * 申请权限
     */
    @SuppressLint("CheckResult")
    private void requestPermissions() {
        RxPermissions rxPermission = new RxPermissions((Activity) mContext);
        rxPermission
                .requestEach(Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.READ_SMS,
                        Manifest.permission.DISABLE_KEYGUARD,
                        Manifest.permission.WAKE_LOCK)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            // 用户已经同意该权限
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时。还会提示请求权限的对话框
                        } else {
                            // 用户拒绝了该权限，而且选中『不再询问』
                        }
                    }
                });

    }

    /**
     * 加载布局
     * @return
     */
    @Override
    public int inflateContentView() {
        return R.layout.activity_login;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        AssetManager mgr = getAssets();
        Typeface tf = Typeface.createFromAsset(mgr, "fonts/FZHaiCKJW.TTF");
        tvYado.setTypeface(tf);
        boolean isRemember = SharedPrefUtil.getInstance(MyApplication.getInstance()).getBoolean(MyConstants.IS_REMEMBER, false);
        if (isRemember) {
            smoothCheckBox.setChecked(true);
            userName.setText(SharedPrefUtil.getInstance(MyApplication.getInstance()).getString(MyConstants.USERNAME, ""));
            userPassword.setText(SharedPrefUtil.getInstance(MyApplication.getInstance()).getString(MyConstants.PWD, ""));
        }
        initListener();
    }

    /**
     * 初始化监听事件
     */
    private void initListener() {
        ivLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((System.currentTimeMillis() - showTime) > 2000) {
                    showTime = System.currentTimeMillis();
                } else {
                    tv_change_ip.setVisibility(View.VISIBLE);
                }

            }
        });
        tv_change_ip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showIPDialog();
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(userName.getText().toString().trim())) {
                    userName.setShakeAnimation();
                } else if (TextUtils.isEmpty(userPassword.getText().toString().trim())) {
                    userPassword.setShakeAnimation();
                } else {
                    username = new WeakReference<>(userName.getText().toString().trim());
                    password = new WeakReference<>(userPassword.getText().toString().trim());
                    isLogin = new WeakReference<>(true);
//                    isLogin = true;
                    assert mPresenter != null;
                    mPresenter.checkLogin(username.get(), password.get());
//                    mPresenter.login(username.get(), password.get());//如果使用旧版请用这个登录
                }
            }
        });
    }

    /**
     * 显示设置IP Dialog
     */
    private void showIPDialog() {
        if (dialogBuilder == null) {
            dialogBuilder = NiftyDialogBuilder.getInstance(mContext);
            dialogBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    tv_change_ip.setVisibility(View.GONE);
                }
            });
        }
        initDialogView();
        setIpSpinner();
        dialogBuilder
                .withTitle(getDialogTitle())                               //标题.withTitle(null)  no title
                .withTitleColor("#FFFFFF")                               //def  标题颜色
                .withDividerColor("#11000000")                           //def
                .withMessage(null)                 //.withMessage(null)  no Msg   内容
                .withMessageColor("#FFCFCFCF")                          //def  | withMessageColor(int resid)   内容颜色
                .withDialogColor("#AF444D50")                            //def  | withDialogColor(int resid)   dialog框颜色
                .withIcon(mContext.getResources().getDrawable(R.drawable.ic_logo2))  //标题栏图标
                .withDuration(700)                                      //def      动画持续时间
                .withEffect(Effectstype.Sidefill)                                     //def Effectstype.Slidetop  动画模式
                .withButton1Text("取消")                                  //def gone     按钮文字
                .withButton2Text("确定")                              //def gone
                .isCancelableOnTouchOutside(false)                       //def    | isCancelable(true)是否支持点击dialog框外关闭dialog
                .setCustomView(dialogView, mContext)     //.setCustomView(View or ResId,context)    自定义布局
                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                    }
                })
                .setButton2Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String ip;
                        if (isIP) {
                            if (TextUtils.isEmpty(et_ip1.getText().toString().trim()) || TextUtils.isEmpty(et_ip2.getText().toString().trim()) || TextUtils.isEmpty(et_ip3.getText().toString().trim())
                                    || TextUtils.isEmpty(et_ip4.getText().toString().trim()) || TextUtils.isEmpty(et_ip5.getText().toString().trim())) {
                                ToastUtils.showShort(R.string.ip_error);
                                return;
                            }
                            ip = "http://" + et_ip1.getText().toString().trim() + "." + et_ip2.getText().toString().trim() + "." + et_ip3.getText().toString().trim()
                                    + "." + et_ip4.getText().toString().trim() + ":" + et_ip5.getText().toString().trim();
                        } else {
                            if (TextUtils.isEmpty(et_domain.getText().toString().trim())) {
                                ToastUtils.showShort(R.string.ip_error);
                                return;
                            }

                            ip = "http://" + et_domain.getText().toString().trim();
                            if (!Patterns.WEB_URL.matcher(ip).matches()) {
                                ToastUtils.showShort(R.string.ip_error);
                                return;
                            }

                        }

                        SharedPrefUtil.getInstance(MyApplication.getInstance()).saveObject(MyConstants.BASE_URL, ip);
                        if (mIps == null) {
                            mIps = new ArrayList<>();
                        }
                        if (mIps.contains(ip.substring(7))) {
                            mIps.remove(ip.substring(7));
                        }
                        mIps.add(ip.substring(7));
                        SharedPrefUtil.getInstance(MyApplication.getInstance()).setIpList("ip", mIps);
                        PRRetrofit.getInstance(MyApplication.getInstance()).reset();
                        dialogBuilder.dismiss();
                    }
                })
                .show();    //展示
        tv_change.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                if (isIP) {
                    if (dialogBuilder != null) {
                        title = new WeakReference<>("设置域名");
                        dialogBuilder.withTitle(title.get());
                    }
                    et_domain.setVisibility(View.VISIBLE);
                    ll_ip.setVisibility(View.GONE);
                    et_domain.setHint("yw.eado.com.cn");

                } else {
                    if (dialogBuilder != null) {
                        title = new WeakReference<>("设置端口IP");
                        dialogBuilder.withTitle(title.get());
                    }
//                    tv_change.setText("去设置域名");
                    ll_ip.setVisibility(View.VISIBLE);
                    et_domain.setVisibility(View.GONE);
                    et_ip1.setHint("121");
                    et_ip2.setHint("201");
                    et_ip3.setHint("108");
                    et_ip4.setHint("27");
                    et_ip5.setHint("8008");
                }
                isIP = !isIP;
            }
        });
        ip_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                if (pos > 0) {
//                    String ip = mIps.get(pos - 1);
                    String ip = mIps.get(mIps.size() - pos);
                    final String[] split = ip.split("\\.");
                    if (isIP) {
                        if (split.length >= 4) {
                            et_ip1.setText(split[0]);
                            et_ip2.setText(split[1]);
                            et_ip3.setText(split[2]);
                            et_ip4.setText(split[3].split(":")[0]);
                            et_ip5.setText(split[3].split(":")[1]);
                        }
                    } else {
                        et_domain.setText(ip);

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    /**
     * 初始化IP 对话框 View
     */
    private void initDialogView() {
        if (dialogView == null) {
            dialogView = LayoutInflater.from(this).inflate(R.layout.activity_set_ip, null);
            tv_change = dialogView.findViewById(R.id.tv_change);
            et_ip1 = dialogView.findViewById(R.id.ip_edit_text1);
            et_ip2 = dialogView.findViewById(R.id.ip_edit_text2);
            et_ip3 = dialogView.findViewById(R.id.ip_edit_text3);
            et_ip4 = dialogView.findViewById(R.id.ip_edit_text4);
            et_ip5 = dialogView.findViewById(R.id.ip_edit_text5);
            et_domain = dialogView.findViewById(R.id.et_domain);
            ll_ip = dialogView.findViewById(R.id.ll_ip);
            ip_spinner = dialogView.findViewById(R.id.ip_spinner);
            String ip = SharedPrefUtil.getInstance(MyApplication.getInstance()).getT(MyConstants.BASE_URL, "");
            if (ip.length() > 0) {
                et_domain.setText(ip.substring(7));
                String[] split = ip.substring(7).split("\\.");
                if (split.length >= 4) {
                    et_ip1.setText(split[0]);
                    et_ip2.setText(split[1]);
                    et_ip3.setText(split[2]);
                    et_ip4.setText(split[3].split(":")[0]);
                    et_ip5.setText(split[3].split(":")[1]);
                }
            }

        }
        ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(SizeUtils.dp2px(15), SizeUtils.dp2px(10), SizeUtils.dp2px(15), 0);
        dialogView.setLayoutParams(layoutParams);
    }

    /**
     * 设置 IP 下拉框
     */
    private void setIpSpinner() {
        mIps = SharedPrefUtil.getInstance(MyApplication.getInstance()).getIpList("ip");
        mItems = new WeakReference<>(new String[mIps.size() + 1]);
        mItems.get()[0] = "";
        for (int i = 1; i < mIps.size() + 1; i++) {
            mItems.get()[i] = mIps.get(mIps.size() - i);
        }

        List<String> dataset = new LinkedList<>(Arrays.asList(mItems.get()));
        if (dataset.size() > 0) {
            ip_spinner.attachDataSource(dataset);
        }
    }

    /**
     * 获取IP 对话框 标题
     * @return
     */
    private String getDialogTitle() {
        if (isIP) {
            return "设置端口IP";
        } else {
            return "设置域名";
        }
    }

    /**
     * 是否注册EventBus
     * @return
     */
    @Override
    protected boolean isRegisterEventBus() {
        return false;
    }

    /**
     * 是否需要注入 arouter
     * @return
     */
    @Override
    protected boolean isNeedInject() {
        return false;
    }

    /**
     * 注入View
     */
    @Override
    protected void initInjector() {
        mActivityComponent.inject((LoginActivity) mContext);
    }

    /**
     * 跳转到MainActivity
     */
    @Override
    public void goToMain() {
        SharedPrefUtil.getInstance(MyApplication.getInstance()).saveObject(MyConstants.USERNAME, username.get());
        SharedPrefUtil.getInstance(MyApplication.getInstance()).saveObject(MyConstants.PWD, password.get());
        SharedPrefUtil.getInstance(MyApplication.getInstance()).saveObject(MyConstants.IS_REMEMBER, smoothCheckBox.isChecked());

        // 2. 跳转并携带参数
        ARouter.getInstance().build(MyConstants.MAIN)
                .withBoolean(MyConstants.FROM_LOGIN, true)
                .navigation();
        finish();
    }

    /**
     * 取消登录
     */
    public void cancelLogin() {
        assert mPresenter != null;
        mPresenter.cancelLogin();
    }

    /**
     * 显示登录对话框
     */
    @Override
    public void showLoadingDialog() {
        if (progressDialog == null) {
            progressDialog = new MyProgressDialog(mContext, R.style.MyProgressDialog);
        }
        progressDialog.setText("登录中...");
        progressDialog.show();
        progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (isLogin.get() && progressDialog != null && progressDialog.isShowing()) {
                        cancelLogin();
                        ToastUtils.showShort("取消登录！");
                        isLogin = new WeakReference<>(false);
                    }
                }
                return false;
            }
        });

    }

    /**
     * 隐藏对话框
     */
    @Override
    public void hideLoadingDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    /**
     * 显示确认登录提示框
     * @param username
     * @param password
     */
    @Override
    public void showCheckDialog(final String username, final String password) {
        final NiftyDialogBuilder dialogBuilder1 = NiftyDialogBuilder.getInstance(mContext);
        dialogBuilder1
                .withTitle("提示")                               //标题.withTitle(null)  no title
                .withTitleColor("#FFFFFF")                               //def  标题颜色
                .withDividerColor("#11000000")                           //def
                .withMessage("该账号已经登录或异常退出，是否要继续登录？")                 //.withMessage(null)  no Msg   内容
                .withMessageColor("#FFCFCFCF")                          //def  | withMessageColor(int resid)   内容颜色
                .withDialogColor("#AF444D50")                            //def  | withDialogColor(int resid)   dialog框颜色
                .withIcon(mContext.getResources().getDrawable(R.drawable.ic_logo2))  //标题栏图标
                .withDuration(700)                                      //def      动画持续时间
                .withEffect(Effectstype.SlideBottom)                                     //def Effectstype.Slidetop  动画模式
                .withButton1Text("取消")                                  //def gone     按钮文字
                .withButton2Text("确定")                              //def gone
                .isCancelableOnTouchOutside(false)                       //def    | isCancelable(true)是否支持点击dialog框外关闭dialog
                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder1.dismiss();
                        hideLoadingDialog();
                    }
                })
                .setButton2Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        assert mPresenter != null;
                        mPresenter.login(username, password);
                        dialogBuilder1.dismiss();
                    }
                })
                .show();    //展示
    }

    @Override
    protected void onDestroy() {
        if (mIps != null) {
            mIps.clear();
        }
        super.onDestroy();
    }
}
