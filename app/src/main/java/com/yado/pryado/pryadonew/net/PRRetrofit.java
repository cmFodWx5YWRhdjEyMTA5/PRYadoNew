package com.yado.pryado.pryadonew.net;

import android.content.Context;
import android.util.Log;

import com.yado.pryado.pryadonew.MyApplication;
import com.yado.pryado.pryadonew.MyConstants;
import com.yado.pryado.pryadonew.util.SharedPrefUtil;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class PRRetrofit {

    private static PRRetrofit myRetrofit;
    //    private Retrofit retrofit;
    private PRApi api;

    private PRRetrofit() {
        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)//默认重试一次，若需要重试N次，则要实现拦截器。
                .addInterceptor(new AddCookiesInterceptor())
                .addInterceptor(new GetCookiesInterceptor())
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS).build();
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())////增加返回值为String的支持
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(SharedPrefUtil.getInstance(MyApplication.getInstance()).getString(MyConstants.BASE_URL, EadoUrl.BASE_URL_WEB))
                .build();
        Log.e("tag","baseUrl = "+ SharedPrefUtil.getInstance(MyApplication.getInstance()).getString(MyConstants.BASE_URL, EadoUrl.BASE_URL_WEB));
        //调用create就可以拿到NetApiService的实例，调用实例的方法就能拿到call，call.enqueue即可完成异步的请求。
        api = retrofit.create(PRApi.class);
    }

    public PRRetrofit(String uri) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new AddCookiesInterceptor())
                .addInterceptor(new GetCookiesInterceptor())
                .connectTimeout(10, TimeUnit.SECONDS).build();
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())////增加返回值为String的支持
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(uri)
                .build();
        Log.e("tag","baseUrl1 = "+uri);
        //调用create就可以拿到NetApiService的实例，调用实例的方法就能拿到call，call.enqueue即可完成异步的请求。
        api = retrofit.create(PRApi.class);
    }

    public static PRRetrofit getInstance(Context context) {
        synchronized (context) {
            if (myRetrofit == null) {
                myRetrofit = new PRRetrofit();
            }
            return myRetrofit;
        }
    }

    public PRApi getApi() {
        return api;
    }

    public void reset() {
        if (myRetrofit != null) {
            myRetrofit = null;
        }
    }

    private HashSet<String> cookies = new HashSet<>();

    public class GetCookiesInterceptor implements Interceptor {

        private List<String> headers;

        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            okhttp3.Response originalResponse = chain.proceed(chain.request());

            headers = originalResponse.headers("Set-Cookie");
            if (!headers.isEmpty()) {
                for (String header : originalResponse.headers("Set-Cookie")) {
                    cookies.add(header);
                }
            }
            return originalResponse;
        }
    }

    public class AddCookiesInterceptor implements Interceptor {

        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request.Builder builder = chain.request().newBuilder();

            for (String cookie : cookies) {
                builder.addHeader("Cookie", cookie);
                Log.v("OkHttp", "Adding Header: " + cookie); // This is done so I know which headers are being added; this interceptor is used after the normal logging of OkHttp
            }

            return chain.proceed(builder.build());
        }
    }
}

