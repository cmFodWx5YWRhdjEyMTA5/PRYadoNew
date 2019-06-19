package com.yado.pryado.pryadonew.util;

import android.util.Log;

import com.yado.pryado.pryadonew.MyApplication;
import com.yado.pryado.pryadonew.MyConstants;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UserLogoutUtil {


    //发送图片到后台
    public static void logout(final boolean isExit) {
        //请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    Log.e("上传", "开始");

                    // 0.相信证书

                    // 1. 获取访问地址URL
                    URL url = new URL("http://192.168.1.187:85/App/AppLogout");
                    // 2. 创建HttpURLConnection对象
                    connection = (HttpURLConnection) url.openConnection();
                    // 3. 设置请求参数等
                    // 请求方式
                    connection.setRequestMethod("POST");
                    // 超时时间
                    connection.setConnectTimeout(30000);
                    connection.setReadTimeout(30000);
                    // 设置是否输出
                    connection.setDoOutput(true);
                    // 设置是否读入
                    connection.setDoInput(true);
                    // 设置是否使用缓存
                    connection.setUseCaches(false);
                    // 设置此 HttpURLConnection 实例是否应该自动执行 HTTP 重定向
                    connection.setInstanceFollowRedirects(true);
                    // 设置使用标准编码格式编码参数的名-值对
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");//使用的是表单请求类型
                    // 连接
                    connection.connect();
                    // 4. 处理输入输出
                    // 写入参数到请求中

                    String params = "username" + "=" + SharedPrefUtil.getInstance(MyApplication.getContext()).getString(MyConstants.USERNAME, "") + "&" +
                            "UserPassWord" + "=" + SharedPrefUtil.getInstance(MyApplication.getContext()).getString(MyConstants.PWD, "") ;
                    Log.e("上传", "请求参数：" + params);

                    OutputStream out = connection.getOutputStream();
                    out.write(params.getBytes());
                    out.flush();
                    out.close();
                    // 从连接中读取响应信息
                    String msg = "";
                    int code = connection.getResponseCode();
                    if (code == 200) {
                        Log.e("上传", "成功");
                    }
                    Log.e("上传", "请求结果：" + msg);
                } catch (Exception e) {
                    //loading
                    Log.e("上传", "异常：" + e);
                } finally {
                    // 5. 断开连接
                    if (connection != null) {
                        connection.disconnect();
                    }

                }
            }
        }).start();


    }
}
