package com.yado.pryado.pryadonew;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 *
 * @author lushujie
 * @date 2017/6/26
 */

public class InfraredInfo implements BaseColumns {

    public static final Uri CONTENT_URI = Uri.parse("content://com.yado.camera/infrared");

    /**
     * 表名
     */
    public static final String TABLE_NAME = "infrared";
    /**
     * 图片名称，后缀“.jpg”换成“.raw”为Raw图路径
     */
    public static final String COLUMN_PATH = "Path";
     /**
     * Raw图中的最高温度值
     */
    public static final String COLUMN_MAX_TEMP = "maxTemp";
    /**
     * Raw图中的最低温度值
     */
    public static final String COLUMN_MIN_TEMP = "minTemp";
    /**
     * Raw图中的中心温度值
     */
    public static final String COLUMN_CENTER_TEMP = "centerTemp";
    /**
     * Raw图中的第一个选点温度值
     */
    public static final String COLUMN_TOUCH_TEMP = "touchTemp";

    public static final String JUMP_KEY = "infrared";

    public static final String JUMP_INFO = "get_infrared_info";

    public static final String PREADO_KEY = "cn.com.eado.preado";

    /**
     * 图片路径
     */
    public static final String IMG_PATH = "img_path";

    /**
     * 图片名字
     */
    public static final String IMG_NAME = "img_name";

    public static final String IMG_STORE_PATH = "/preado/infrared/";




}
