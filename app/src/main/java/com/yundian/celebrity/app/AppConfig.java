package com.yundian.celebrity.app;

import android.os.Environment;

public class AppConfig {

    /**
     * The constant DEBUG_TAG.
     */
    public static final String DEBUG_TAG = "logger";// LogCat的标记
   /* 自动更新配置*/
    public static String API_FIRE_TOKEN = "";
    public static String APP_FIRE_ID = "";
    //appid
    public static String APP_ID = "";

    public static String QI_NIU_PIC_ADRESS = "http://ouim6qew1.bkt.clouddn.com/";
//    public static String QI_NIU_PIC_ADRESS = "http://ouim6qew1.bkt.clouddn.com/";
    public static String HUANAN_QI_NIU_PIC_ADRESS ="http://ouim6qew1.bkt.clouddn.com/";//华南
    public static long AREA_ID = 0;
    public static String AREA = "";
    public static long ISP_ID = 0;
    public static String ISP = "";
    public static final String TOKEN = "MqF35-H32j1PH8igh-am7aEkduP511g-5-F7j47Z:0gzBOkhm3KsFGbGk2HdKfA4jZp4=:eyJzY29wZSI6InNob3J0LXZpZGVvIiwiZGVhZGxpbmUiOjE2NTA3MTExMDcsInVwaG9zdHMiOlsiaHR0cDovL3VwLXoyLnFpbml1LmNvbSIsImh0dHA6Ly91cGxvYWQtejIucWluaXUuY29tIiwiLUggdXAtejIucWluaXUuY29tIGh0dHA6Ly8xODMuNjAuMjE0LjE5OCJdfQ==";
    public static final String VIDEO_STORAGE_DIR = Environment.getExternalStorageDirectory() + "/ShortVideo";
    public static final String TRIM_STORAGE_DIR = Environment.getExternalStorageDirectory() + "/ShortVideo";
}
