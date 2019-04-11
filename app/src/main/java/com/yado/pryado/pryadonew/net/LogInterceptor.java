package com.yado.pryado.pryadonew.net;

import com.yado.pryado.pryadonew.MyApplication;
import com.yado.pryado.pryadonew.MyConstants;
import com.yado.pryado.pryadonew.util.SharedPrefUtil;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Created by Ulez on 2016/11/21.
 * Email：lcy1532110757@gmail.com
 */
public class LogInterceptor implements Interceptor {
    public static final String TAG = "LogInterceptor.java";

    public LogInterceptor() {

    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Response originalResponse = chain.proceed(originalRequest);//原始接口结果
        if (originalResponse.code() == 500) {
            originalResponse.body().close();
            Request loginRequest = getLoginRequest();//获取登陆的Request
            Response loginResponse = chain.proceed(loginRequest);//执行登陆，获取新的SessionId
            if (loginResponse.isSuccessful()) {//登陆成功
                loginResponse.body().close();//释放登陆成功的资源
                return chain.proceed(originalRequest);//重新执行
            }
        } else {
            return originalResponse;
        }
        //the request url
        String url = originalRequest.url().toString();
        //the request method
        String method = originalRequest.method();
        long t1 = System.nanoTime();
//        Log.e(TAG, String.format(Locale.getDefault(), "Sending %s request [url = %s]", method, url));
        //the request body
        RequestBody requestBody = originalRequest.body();
        if (requestBody != null) {
            StringBuilder sb = new StringBuilder("Request Body [");
            okio.Buffer buffer = new okio.Buffer();
            requestBody.writeTo(buffer);
            Charset charset = Charset.forName("UTF-8");
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(charset);
            }
            if (isPlaintext(buffer)) {
                sb.append(buffer.readString(charset));
                sb.append(" (Content-Type = ").append(contentType.toString()).append(",")
                        .append(requestBody.contentLength()).append("-byte body)");
            } else {
                sb.append(" (Content-Type = ").append(contentType.toString())
                        .append(",binary ").append(requestBody.contentLength()).append("-byte body omitted)");
            }
            sb.append("]");
//            Log.e(TAG, String.format(Locale.getDefault(), "%s %s", method, sb.toString()));
        }
        Response response = chain.proceed(originalRequest);
        long t2 = System.nanoTime();
        //the response time
//        KLog.e(TAG, String.format(Locale.getDefault(), "Received response for [url = %s] in %.1fms", url, (t2 - t1) / 1e6d));
        //the response state
//        Log.e(TAG, String.format(Locale.CHINA, "Received response is %s ,message[%s],code[%d]", response.isSuccessful() ? "success" : "fail", response.message(), response.code()));
        //the response data
        ResponseBody body = response.body();

        BufferedSource source = body.source();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        Buffer buffer = source.buffer();
        Charset charset = Charset.defaultCharset();
        MediaType contentType = body.contentType();
        if (contentType != null) {
            charset = contentType.charset(charset);
        }
        String bodyString = buffer.clone().readString(charset);
//        Log.e(TAG, String.format("Received response json string [%s]", bodyString));
//        isFirst=false;
        return response;
    }

    private Request getLoginRequest() {
        return new Request.Builder()
                .url(SharedPrefUtil.getInstance(MyApplication.getInstance()).getString(MyConstants.BASE_URL, EadoUrl.BASE_URL_WEB)+"/App/AppUserInfo")//http://113.106.90.51:8008/App/AppUserInfo
                .post(new FormBody.Builder()
                        .add(MyConstants.USERNAME, SharedPrefUtil.getInstance(MyApplication.getInstance()).getT(MyConstants.USERNAME, ""))
                        .add(MyConstants.PWD, SharedPrefUtil.getInstance(MyApplication.getInstance()).getT(MyConstants.PWD, ""))
                        .build())
                .build();
    }

    static boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }
}