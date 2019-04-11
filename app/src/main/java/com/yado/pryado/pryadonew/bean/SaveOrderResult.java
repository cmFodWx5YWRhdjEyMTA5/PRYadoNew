package com.yado.pryado.pryadonew.bean;

/**
 * Created by eado on 2017/1/17.
 */

public class SaveOrderResult {


//"resultCode": 1//1成功，2，失败
//    "result":”成功”
    /**
     * OrderID : 3
     * resultCode : 1
     * result : 成功
     */

    private int OrderID;
    private int resultCode;
    private String result;

    public int getOrderID() {
        return OrderID;
    }

    public void setOrderID(int OrderID) {
        this.OrderID = OrderID;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
