package com.yado.pryado.pryadonew;

import android.graphics.Color;
import android.net.Uri;

public class MyConstants {
    public static final String INFRARED_PACKAGE_NAME = "com.yado.camera";
    public static String TB_ALERT = "tb_alert";
    public static String ALERT_DETAIL = "alert_detail";
    public static String TB_NOTICE = "tb_notice";
    public static String TB_NOTICE_DETAIL = "tb_notice_detail";
    public static String RING = "ring";
    public static String RING2 = "ring2";
    public static String CURSOR_POSITION = "cursor_position";
    public static final int TYPE_MOBILE = 1;
    public static final int TYPE_WIFI = 2;
    public static final int TYPE_ALL_NETWORK = 3;
    public static final String NETWORK_SETTING = "NetworkSetting";

    public static final String USERNAME = "Username";
    public static final String PWD = "UserPassWord";
    public static final String IS_REMEMBER = "is_remember";

    public static final String BASE_URL = "base_url";

    public static final String FROM_LOGIN = "from_login";

    public static int[] array = new int[]{
            Color.parseColor("#d08d03"),
            Color.parseColor("#027a98"),
            Color.parseColor("#71a904"),
            Color.parseColor("#f92e74"),
    };

    public static int[] COLORS = new int[]{
            Color.parseColor("#669900"),
            Color.parseColor("#fec34a"),
            Color.parseColor("#FFF2AD21"),
            Color.parseColor("#c50e1b"),
            Color.parseColor("#ababab")
    };

    public static final String IsFirst = "isfirst";

    public static final String ADD_NORMAL_PHOTO = "add_normal_photo";
    public static final String KEY_TAKE_PHOTO = "picker_result";//和第三方包对应
    public static final String INFRARED_PHOTO = "infrared_photo";

    public static final String AUTHORITY = "com.yado.camera";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/infrared");

    public static final String FROM_MAIN = "FROM_MAIN";

    public static final String counts_upcoming = "counts_upcoming";
    public static final String alert_num = "alert_num";


    public static final int ORDER_TYPE_AWAIT = 0;
    public static final int ORDER_TYPE_ACCEPTET = 1;
    public static final int ORDER_TYPE_COMPLETE = 2;
    public static final String ORDER = "order";


    /*网络错误*/
    public static final int NETWORD_ERROR = 0x1;
    /*fastjson错误*/
    public static final int JSON_ERROR = 0x3;
    /*未知错误*/
    public static final int UNKNOWN_ERROR = 0x4;

    public static final int CONNECT_ERROR = 0x7;

    public static final int ORDER_ERROR = 0x8;

    public static final int AUTH_ERROR = 0x9;

    public static final int ERROR404 = 0x10;


    public static String HASORDERS = "hasOrders";
    public static final String HASROOM = "HASROOM";
    public static final String DangerType = "DangerType";
    public static final String PID = "pid";
    public static final String DID = "did";
    public static final String FROM_SCAN = "fromScan";
    public static final String ENCODE = "Encode";

    public static final String INNER_WJ = "内圈 危急";
    public static final String INNER_YZ = "内圈 严重";
    public static final String INNER_YB = "内圈 一般";
    public static final String INNER_ZC = "内圈 正常";

    public static final String OUT_WJ = "外环 危急";
    public static final String OUT_YZ = "外环 严重";
    public static final String OUT_YB = "外环 一般";
    public static final String OUT_YQR = "外环 已确认";
    public static final String OUT_ZC = "外环 正常";

    public static final String DANGER_WJ = "危急 危急";
    public static final String DANGER_YQR = "危急 已确认";
    public static final String DANGER_ZC = "危急 正常";

    public static final String YANZHONG_WJ = "严重 危急";
    public static final String YANZHONG_YZ = "严重 严重";
    public static final String YANZHONG_YQR = "严重 已确认";
    public static final String YANZHONG_ZC = "严重 正常";

    public static final String YIBAN_WJ = "一般 危急";
    public static final String YIBAN_YZ = "一般 严重";
    public static final String YIBAN_YB = "一般 一般";
    public static final String YIBAN_YQR = "一般 已确认";
    public static final String YIBAN_ZC = "一般 正常";

    public static final String normal_wj = "正常 危急";
    public static final String normal_yz = "正常 严重";
    public static final String normal_yb = "正常 一般";
    public static final String normal_yqr = "正常 已确认";
    public static final String normal_zc = "正常 正常";

    public static final String task_time = "task_time";

    public static final String INFRARED_ACTION = "SendURIFromEado";
    public static final String SCREEN_ACTION = "LockScreenMsgReceiver";
    public static final String NOTICE_CANCEL_ACTION = "notice_cancel";

    //Arouter
    public static final String CHOICE_VOICE = "/ui/ChoiceVoiceActivity";
    public static final String LOGIN = "/ui/login/LoginActivity";
    public static final String DEVICE_DETAIL = "/ui/device_detail/DeviceDetailActivity";
    public static final String WEB_VIEW = "/ui/WebViewActivity";
    public static final String DANGER_DETAIL = "/ui/riskAssessAndDetail/DangerDetailActivity";
    public static final String TASK = "/ui/task/TaskActivity";
    public static final String MY_TODO = "/ui/todo/MyTodoActivity";
    public static final String HIS_ORDER = "/ui/hisOrder/HisOrderActivity";
    public static final String DEVICE_DETAIL2 = "/ui/device_detail/DeviceDetailActivity2";
    public static final String ROOM_DETAIL = "/ui/roomDetail/RoomDetailActivity";
    public static final String ASSESS = "/ui/riskAssessAndDetail/AssessActivity";
    public static final String REPORT = "/ui/report/ReportActivity";
    public static final String RECEIVE_SETTING = "/ui/ReceiveSettingActivity";
    public static final String NETWORK_SETTING1 = "/ui/NetworkSettingActivity";
    public static final String FEEDBACK = "/ui/FeedbackActivity";
    public static final String ABOUT = "/ui/AboutActivity";
    public static final String PASSWORD_SETTING = "/ui/mod_pwd/PasswordSettingActivity";
    public static final String ALERT = "/ui/alert/AlertActivity";
    public static final String DANGER = "/ui/DangerActivity";
    public static final String CEWEN = "/ui/temp_monitor/TempMonitorActivity";
    public static final String MONITOR = "/ui/monitor/MonitorActivity";
    public static final String AFFAIR = "/ui/affair/AffairActivity";
    public static final String FORM = "/ui/FormActivity";
    public static final String CAPTUCRE = "/zxing/activity/CaptureActivity";
    public static final String MAIN = "/ui/main/MainActivity";





}
