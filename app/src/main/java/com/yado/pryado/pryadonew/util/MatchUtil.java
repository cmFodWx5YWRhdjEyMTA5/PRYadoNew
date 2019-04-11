package com.yado.pryado.pryadonew.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ulez on 2017/7/27.
 * Email：lcy1532110757@gmail.com
 */


public class MatchUtil {
    public static String matchChanel(String webUrl) {
        Pattern pattern = Pattern.compile("drv=\\d*");
        Matcher matcher = pattern.matcher(webUrl);
        matcher.find();
        String sss = matcher.group(0);
        return sss.substring(sss.indexOf("drv=") + 4, sss.length());
    }
}
