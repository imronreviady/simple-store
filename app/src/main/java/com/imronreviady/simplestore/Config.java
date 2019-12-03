package com.imronreviady.simplestore;

/**
 * Created by Panacea-Soft on 7/15/15.
 * Contact Email : teamps.is.cool@gmail.com
 */

public class Config {

    //AppVersion
    public static String APP_VERSION = "1.4";

    /* APP Setting */
    public static boolean IS_DEVELOPMENT = true; // set false, your app is production

    /* API Related */

    public static final String APP_API_URL = "http://www.panacea-soft.com/ps-store-admin/index.php/";

    public static final String APP_IMAGES_URL = "http://www.panacea-soft.com/ps-store-admin/uploads/";

    public static final String APP_IMAGES_THUMB_URL = "http://www.panacea-soft.com/ps-store-admin/uploads/thumbnail/";

    public static final String YOUTUBE_IMAGE_BASE_URL = "https://img.youtube.com/vi/%s/0.jpg";

    public static final String API_KEY = "teampsisthebest"; // If you change here, you need to update in server.

    public static final String LANGUAGE_EN = "en";
    public static final String LANGUAGE_AR = "ar";
    public static final String LANGUAGE_ES = "es";
    public static final String DEFAULT_LANGUAGE = LANGUAGE_EN;

    /* Loading Limit Count Setting */
    public static final int API_SERVICE_CACHE_LIMIT = 5; // Minutes Cache

    public static final int TRANSACTION_COUNT = 10;
    public static final int TRANSACTION_ORDER_COUNT = 10;

    public static int RATING_COUNT = 30;

    public static int LOAD_FROM_DB = 10;//not increase

    public static int PRODUCT_COUNT = 30;

    public static int LIST_CATEGORY_COUNT = 30;

    public static int LIST_NEW_FEED_COUNT = 30;

    public static int NOTI_LIST_COUNT = 30;

    public static int COMMENT_COUNT = 30;

    public static int COLLECTION_PRODUCT_LIST_LIMIT = 30;


    //region playstore

    public static String PLAYSTORE_MARKET_URL_FIX = "market://details?id=";
    public static String PLAYSTORE_HTTP_URL_FIX = "http://play.google.com/store/apps/details?id=";


    //endregion

    public static final String DECIMAL_PLACES_FORMAT = ",###.00";

    //region attributeSymbols

    public static String ATTRIBUT_SEPERATOR = "#";

    //Image Cache and Loading
    public static int IMAGE_CACHE_LIMIT = 250; // Mb
    public static boolean PRE_LOAD_FULL_IMAGE = true;

    public static final Boolean SHOW_ADMOB = true;

    //GDPR
    public static String CONSENTSTATUS_PERSONALIZED = "PERSONALIZED";
    public static String CONSENTSTATUS_NON_PERSONALIZED = "NON_PERSONALIZED";
    public static String CONSENTSTATUS_UNKNOWN = "UNKNOWN";

    public static String CONSENTSTATUS_CURRENT_STATUS = "UNKNOWN";
    public static String CONSENTSTATUS_IS_READY_KEY = "CONSENTSTATUS_IS_READY";

    public static String POLICY_URL = "http://www.panacea-soft.com/policy/policy.html";
}
