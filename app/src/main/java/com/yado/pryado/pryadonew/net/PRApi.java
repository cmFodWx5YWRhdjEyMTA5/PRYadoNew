package com.yado.pryado.pryadonew.net;

import com.yado.pryado.pryadonew.bean.AlertBean;
import com.yado.pryado.pryadonew.bean.BugDetail;
import com.yado.pryado.pryadonew.bean.BugInfo;
import com.yado.pryado.pryadonew.bean.BugList;
import com.yado.pryado.pryadonew.bean.CatchInitBean;
import com.yado.pryado.pryadonew.bean.ChangePwNean;
import com.yado.pryado.pryadonew.bean.DeviceDetailBean2;
import com.yado.pryado.pryadonew.bean.DeviceInfo;
import com.yado.pryado.pryadonew.bean.DeviceInfoListBean;
import com.yado.pryado.pryadonew.bean.HisData;
import com.yado.pryado.pryadonew.bean.HisData2;
import com.yado.pryado.pryadonew.bean.Order;
import com.yado.pryado.pryadonew.bean.OrderDetail;
import com.yado.pryado.pryadonew.bean.OrderList;
import com.yado.pryado.pryadonew.bean.PostBugResult;
import com.yado.pryado.pryadonew.bean.PullBean;
import com.yado.pryado.pryadonew.bean.RoomDetail;
import com.yado.pryado.pryadonew.bean.RoomListBean;
import com.yado.pryado.pryadonew.bean.RoomStatusBean;
import com.yado.pryado.pryadonew.bean.SaveOrderResult;
import com.yado.pryado.pryadonew.bean.TypeBean;
import com.yado.pryado.pryadonew.bean.UpLoadResult;
import com.yado.pryado.pryadonew.bean.Update;
import com.yado.pryado.pryadonew.bean.VagueHistoryGraphBean;
import com.yado.pryado.pryadonew.bean.VagueRealTimeBean;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by Ulez on 2016/10/9.
 * Email：lcy1532110757@gmail.com
 */
public interface PRApi {
    //http://113.106.90.51:66/FileUpload/AppDeleteFile?filename=magazine-unlock-05-2.3.514-bigpicture_05_51.jpg&module=order&ctype=image
    @FormUrlEncoded
    @POST("/FileUpload/AppDeleteFile")
    Observable<String> deleteFile(@Field("filename") String filename, @Field("module") String module, @Field("ctype") String ctype);

    //files 文件路径, Modules 所属模块（order/bug）, fk_id 所属模块对象的编号, FSource 来源（app/web）, Remark 备注, CommitUser 上传用户
    //http://113.106.90.51:66/FileUpload/MultiUpload
    @Multipart
    @POST("/FileUpload/MultiUpload")
    Observable<UpLoadResult> upload(@Part("description") RequestBody description, @Part MultipartBody multipartBody);

    @FormUrlEncoded
    @POST("/App/AppUserInfo")
////http://113.106.90.51:8008/App/AppUserInfo
    Observable<String> userLogin(@Field("Username") String Username, @Field("UserPassWord") String UserPassWord);

    //http://113.106.90.51:66/app/SaveOrderInfo?OrderID=3&IsQualified=1&CheckInfo=fdsfgfgsdgfdsgdg&Latitude=112&Longtitude=110&Rectification=Rectification
    @FormUrlEncoded
    @POST("/App/SaveOrderInfo")
//IsQualified 是否合格（1是/0否）,CheckInfo 检查情况 ，Latitude 纬度 ，Longtitude 经度,Rectification 整改信息
    Observable<SaveOrderResult> saveOrderInfo(@Field("OrderID") String OrderID, @Field("IsQualified") String IsQualified, @Field("CheckInfo") String CheckInfo, @Field("Latitude") float Latitude, @Field("Longtitude") float Longtitude, @Field("Rectification") String Rectification);

    @FormUrlEncoded
    @POST("/App/AppChangePW")
    Observable<ChangePwNean> changePW(@Field("username") String username, @Field("oldPassword") String oldPassword, @Field("newPassword") String newPassword);

//    @FormUrlEncoded
//    @POST("/App/reportFeedBack")
//    Observable<FeedbackBean> postFeedBack(@Field("FeedbackTitle") String FeedbackTitle, @Field("FeedbackContent") String FeedbackContent);

    //fragment提交隐患，有DID，隐患类型为设备
    //http://113.106.90.51:66/app/SaveBugInfo? ?PID=1&BugDesc=WTF&DID =5
    @FormUrlEncoded
    @POST("/App/SaveBugInfo")
    Observable<PostBugResult> postNewBug(@Field("PID") String PID, @Field("DID") int DID, @Field("BugLocation") String BugLocation, @Field("BugDesc") String BugDesc);

    //无DID的提交隐患，隐患类型为环境
    //http://113.106.90.51:66/app/SaveBugInfo? ?PID=1&BugDesc=WTF
    @FormUrlEncoded
    @POST("/App/SaveBugInfo")
    Observable<PostBugResult> postNewBug(@Field("PID") int PID, @Field("BugLocation") String BugLocation, @Field("BugDesc") String BugDesc);

    // App/ scanEadoCode 提交编码；
    @FormUrlEncoded
    @POST("/App/scanEadoCode")
    Observable<DeviceInfo> getInfoByCode(@Field("EadoCode") String EadoCode);

    @GET("/app/getPDRList")
    Observable<List<RoomListBean>> getRdList();

    @GET("/App/PROrder")
//工单；
    Observable<List<Order>> getPROrder();

    @GET("/App/PReado")
//配电房和设备列表信息；
    Observable<List<CatchInitBean>> getPReado();

    @GET("/App/AppUpData.Json")
    Observable<Update> CheckUpdate();

    //http://113.106.90.51:8008/app/getPDRList
    @GET("/app/getPDRList")
    Observable<RoomListBean> getPDRList();

    @GET("/App/AppUpData.Json")
    Observable<Update> getUpdateInfo();

    //http://113.106.90.51:8004/App/getNewAlarm?lastAlarmTime=2016/8/2 19:05:12
    @GET("/App/getNewAlarm")
    Observable<PullBean> getAlertPull(@Query("lastAlarmTime") String lastAlarmTime);

    // http://113.106.90.51:8004/App/getPAlarmState?pagesize=20&type=1&pageindex=1
//    @GET("/App/getPAlarmState")
//    Observable<List<AlertListBean>> getAlertList(@Query("pagesize") int pagesize, @Query("type") int type, @Query("pageindex") int pageindex);

    // http://113.106.90.51:66/App/GetAlarmDate?rows=20&type=3&page=1&pid=0
    @GET("/App/GetAlarmDate")
    Observable<AlertBean> getAlertList(@Query("rows") int rows, @Query("type") int type, @Query("page") int page, @Query("pid") int pid);

    @GET("/App/GetAlarmData_1")
    Observable<AlertBean> getAlertList_1(@Query("rows") int rows, @Query("page") int page, @Query("StartDate") String startDate, @Query("EndDate") String endDate, @Query("pid") int pid);

    //http://113.106.90.51:66/App/PointHisDataJson?AlarmTime=2017/3/23 17:27:46&pid=1&did=5&tagid=0
    @GET("/App/PointHisDataJson")
    Observable<HisData> getPointHisData(@Query("AlarmTime") String AlarmTime, @Query("pid") String pid, @Query("did") String did, @Query("tagid") String tagid);


    //PointHisDataJson(string DateTime, , int hour =12, int pid = 0, int did = 0, int tagid = 0)
    //http://113.106.90.51:66/App/PointDataJson?DateTime=2017/5/2 11:27:46&hour=12&pid=105&did=469&tagid=12284
    @GET("/App/PointDataJson")
    Observable<HisData2> getPointData(@Query("DateTime") String DateTime, @Query("hours") String hours, @Query("pid") String pid, @Query("did") String did, @Query("tagid") String tagid);


    //  http://113.106.90.51:8008/App/PDRInfo?DID=812
//    @GET("/App/PDRInfo")
//    Observable<DeviceDetailBean> getDetail(@Query("DID") String DID);

    //  http://113.106.90.51:8008/App/getDeviceState?DID=812
    @GET("/App/getDeviceState")
    Observable<DeviceDetailBean2> getDetail2(@Query("DID") String DID);


    //http://113.106.90.51:66/App/LoadBugInfo?BugID=200
    @GET("/App/LoadBugInfo")
    Observable<BugInfo> getBugInfo(@Query("BugID") int BugID);

    //http://113.106.90.51:8004/App/getPDRState?pid=104
    @GET("/App/getPDRState")
    Observable<RoomStatusBean> getRoomDetail(@Query("pid") int pid);


    //http://113.106.90.51:66/App/getPDRState?pid=1&ver=1
    @GET("/App/getPDRState")
    Observable<RoomDetail> getRoomDetail(@Query("pid") int pid, @Query("ver") int ver);

    //http://113.106.90.51:66/app/OrderInfoData?UserName=罗春友&&OrderState=0&&OrderType=日常巡检//未完成工单；UserName工单的用户，OrderType工单类型（日常巡检/应急抢修），OrderState工单状态（-1全部，0待接收，1已受理，2已完成）；
    @GET("/app/OrderInfoData")
    Observable<OrderList> getOrderList(@Query("UserName") String UserName, @Query("OrderState") int OrderState, @Query("OrderType") String OrderType);

    //http://113.106.90.51:66/app/OrderInfoData?PID=105
    @GET("/app/OrderInfoData")
    Observable<OrderList> getOrderList(@Query("PID") String PID, @Query("OrderState") int OrderState);

    //http://113.106.90.51:66/app/LoadOrderInfo?OrderID=3
    @GET("/app/LoadOrderInfo")
//工单详情
    Observable<OrderDetail> getOrderDetail(@Query("OrderID") int OrderID);

    //http://113.106.90.51:66/app/BugInfoData?PID=0
    @GET("/app/BugInfoData")
//隐患列表
    Observable<BugList> getBugList(@Query("PID") int PID);

    //http://113.106.90.51:66/app/BugInfoData2?DID=0
    @GET("/app/BugInfoData")
//隐患列表
    Observable<BugList> getBugList(@Query("DID") String DID);

    //http://113.106.90.51:66/app/LoadBugInfo?BugID=6
    @GET("/app/LoadBugInfo")
//隐患详情
    Observable<BugDetail> getBugDetail(@Query("BugID") int BugID);

    @GET("/app/loadVagueRealTime")
    Observable<VagueRealTimeBean> loadVagueRealTime(@Query("TagID") int tagId, @Query("DID") int did, @Query("PID") int pid);

    @GET("/App/loadVagueHistoryGraph")
    Observable<List<VagueHistoryGraphBean>> loadVagueHistoryGraph(@Query("TagID") int tagId, @Query("StartDate") String startDate, @Query("EndDate") String endDate);

    @GET("/App/loadVagueAlarm")
    Observable<AlertBean> loadVagueAlarm(@Query("PID") int pid, @Query("AlarmConfirm") String alarmConfirm, @Query("StartDate") String startDate, @Query("EndDate") String endDate);

    @GET("/Home/GetFJRCount?")
    Observable<TypeBean> getGraphType(@Query("pid") int pid);

    @GET("/App/loadMonitorHistoryGraph")
    Observable<List<VagueHistoryGraphBean>> loadMonitorHistoryGraph(@Query("PID") int pid, @Query("TagID") int TagID, @Query("StartDate") String StartDate, @Query("EndDate") String EndDate);

    @GET("/NonIntrusive/GetCabinetList")
    Observable<ResponseBody> getStatusData(@Query("pid") int pid, @Query("did") int did, @Query("type") int type);

    @GET("/PDRInfo/GetMaxTempInfo")
    Observable<ResponseBody> getMaxDiv(@Query("pid") int pid);

    //获取配电房所有的设备列表
    @GET("/App/DeviceInfoList")
    Observable<DeviceInfoListBean> getDeviceInfoList(@Query("pid") int pid, @Query("pagesize") int pagesize, @Query("pageindex") int pageindex);
}
