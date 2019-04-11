package com.yado.pryado.pryadonew.bean;

import java.util.LinkedHashMap;

/**
 * Created by Ulez on 2017/5/17.
 * Email：lcy1532110757@gmail.com
 */


public class HisData {

    /**
     * HisDevData : {"2017/3/23 1:27:46":16.12,"2017/3/23 3:27:46":16.12,"2017/3/23 5:27:46":16.12,"2017/3/23 7:27:46":16.12,"2017/3/23 9:27:46":16.12,"2017/3/23 11:27:46":16.12,"2017/3/23 13:27:46":16.12,"2017/3/23 15:27:46":16.12,"2017/3/23 17:27:46":16.12}
     * HisEnvData : {"2017/3/23 1:27:46":16.12,"2017/3/23 3:27:46":16.12,"2017/3/23 5:27:46":16.12,"2017/3/23 7:27:46":16.12,"2017/3/23 9:27:46":16.12,"2017/3/23 11:27:46":16.12,"2017/3/23 13:27:46":16.12,"2017/3/23 15:27:46":16.12,"2017/3/23 17:27:46":16.12}
     */

    private LinkedHashMap<String,String> HisDevData;
    private LinkedHashMap<String,String> HisEnvData;

    public LinkedHashMap<String, String> getHisDevData() {
        return HisDevData;
    }

    public void setHisDevData(LinkedHashMap<String, String> hisDevData) {
        HisDevData = hisDevData;
    }

    public LinkedHashMap<String, String> getHisEnvData() {
        return HisEnvData;
    }

    public void setHisEnvData(LinkedHashMap<String, String> hisEnvData) {
        HisEnvData = hisEnvData;
    }
}
