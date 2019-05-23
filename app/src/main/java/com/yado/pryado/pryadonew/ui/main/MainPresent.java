package com.yado.pryado.pryadonew.ui.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;

import com.yado.pryado.pryadonew.MyApplication;
import com.yado.pryado.pryadonew.base.BaseActivity;
import com.yado.pryado.pryadonew.base.BasePresenter;
import com.yado.pryado.pryadonew.bean.Update;
import com.yado.pryado.pryadonew.net.INetListener;
import com.yado.pryado.pryadonew.util.SharedPrefUtil;
import android.support.v4.content.FileProvider;
import com.blankj.utilcode.util.ToastUtils;

import java.io.File;

import javax.inject.Inject;

import static com.yado.pryado.pryadonew.ui.main.MainActivity.REQUEST_CODE_UNKNOWN_APP;

/**
 * Created by OnexZgj on 2018/9/11:09:37.
 * des:
 */

public class MainPresent extends BasePresenter<MainContract.View, MainModel> implements MainContract.Presenter {

//    @Inject
//    CartoonFragmentModel model;

    /**
     * 注入到Fragment
     */
    @Inject
    public MainPresent() {
    }


    @Override
    public void CheckUpdate(final int versionCode) {
        mModel.CheckUpdate(versionCode, new INetListener<Object, Throwable, Object>() {
            @Override
            public void success(Object o) {
                final Update update = (Update) o;
                if (update.getVersionCode() > versionCode) {
                    if (!SharedPrefUtil.getInstance(MyApplication.getInstance()).getT("isPopUp", false)) {
                        mView.showDownloadDialog(update);
                    }
                }
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
    public void DownLoadApk(String downloadUrl, Handler handler) {
        mModel.DownLoadApk(downloadUrl, handler, new INetListener<Object, Throwable, Object>() {
            @Override
            public void success(Object o) {

            }

            @Override
            public void failed(Throwable throwable) {

            }

            @Override
            public void loading(Object o) {

            }
        });
    }

    public void installApk(File apkfile, BaseActivity activity) {
        ToastUtils.showShort("存放apk路径==" + apkfile.getAbsolutePath());
//        File apkfile = new File(saveFileName);
        if (!apkfile.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//无Activity环境启动时添加；
            Uri apkUri = FileProvider.getUriForFile(activity, "com.yado.pryado.pryadonew.fileprovider", apkfile);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            boolean hasInstallPermission = activity.getPackageManager().canRequestPackageInstalls();
            if (hasInstallPermission) {
                //安装应用
                Uri apkUri = FileProvider.getUriForFile(activity, "com.yado.pryado.pryadonew.fileprovider", apkfile);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            } else {
                //跳转至“安装未知应用”权限界面，引导用户开启权限
                Uri selfPackageUri = Uri.parse("package:" + activity.getPackageName());
                Intent intent1 = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, selfPackageUri);
                activity.startActivityForResult(intent1, REQUEST_CODE_UNKNOWN_APP);
            }
        } else {
            intent.setDataAndType(Uri.fromFile(apkfile), "application/vnd.android.package-archive");

        }
        activity.startActivity(intent);
    }
}
