package com.yado.pryado.pryadonew.exception;

import android.util.Log;

import com.google.gson.JsonParseException;
import com.google.gson.stream.MalformedJsonException;
import com.yado.pryado.pryadonew.MyApplication;
import com.yado.pryado.pryadonew.MyConstants;
import com.yado.pryado.pryadonew.R;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import retrofit2.HttpException;

public class ApiExceptionFactory {

    private final static String TAG = "ApiExceptionFactory";
    public static ApiException getApiException(Throwable e) {
        Log.e(TAG, e.getClass().getName());
        ApiException apiException = new ApiException(e);
        String msg;
        int code;
        if (e instanceof ConnectException) {
            msg = MyApplication.getInstance().getString(R.string.server_connect_error);
            code = MyConstants.NETWORD_ERROR;
        } else if (e instanceof JsonParseException) {
            msg = MyApplication.getContext().getString(R.string.json_error);
            code = MyConstants.JSON_ERROR;
        } else if (e instanceof SocketTimeoutException) {
            msg = MyApplication.getContext().getString(R.string.timeout_error);
            code = MyConstants.CONNECT_ERROR;
        } else if (e instanceof ErrorOrderException) {
            msg = MyApplication.getContext().getString(R.string.order_error);
            code = MyConstants.ORDER_ERROR;
        } else if (e instanceof MalformedJsonException) {
            msg = MyApplication.getContext().getString(R.string.auth_error);
            code = MyConstants.AUTH_ERROR;
        } else if (e instanceof HttpException) {
            msg = MyApplication.getContext().getString(R.string.error404);
            code = MyConstants.ERROR404;
        } else {
            msg = MyApplication.getContext().getString(R.string.unknow_error);
            code = MyConstants.UNKNOWN_ERROR;
        }
        apiException.setCode(code);
        apiException.setDisplayMessage(msg);
        return apiException;
    }
}
