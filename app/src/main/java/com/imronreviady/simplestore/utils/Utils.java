package com.imronreviady.simplestore.utils;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.imronreviady.simplestore.Config;
import com.imronreviady.simplestore.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.core.app.ActivityCompat;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by Panacea-Soft on 7/15/15.
 * Contact Email : teamps.is.cool@gmail.com
 */


public class Utils {

    private static Typeface fromAsset;
    private static SpannableString spannableString;
    private static Fonts currentTypeface;

//    public static final String NOTI_EXISTS_TO_SHOW = "is_noti_exists_to_show";
//    public static final String NOTI_MSG = "noti_msg";
//    public static final String NOTI_SETTING = "noti_setting";
//    public static final String NOTI_TOKEN = "noti_token";
//
//    public static final int RESULT_LOAD_IMAGE = 1;
//    public static final int RESULT_OK = -1;

//    public static final String USER_NO_USER = "nologinuser";

//    public static final String START_OF_OFFSET = "0"; // Please don't change!
//
//    public static final String PLATFORM = "android"; // Please don't change!
//
//    private static Boolean cacheUnicode = null;

//    public static String IMAGE_TYPE_PRODUCT = "product";
//    public static String FILTERING_typeName = "product";
//    public static String FILTERING_typeName_CAT = "category";
//    public static String FILTERING_CATEGORY_typeName = "category";
//    public static String IMAGE_TYPE_ABOUT = "about";

    /*Configuration Counts*/

//    public static String CONFIG_COLLECTION_COUNT = "collection_count";
//    public static String CONFIG_HOME_PRODUCT_COUNT = "home_product_count";
//    public static String CONFIG_PRODUCT_COUNT = "product_count";
//    public static String CONFIG_HOME_CATEGORY_COUNT = "home_category_count";
//    public static String CONFIG_LIST_CATEGORY_COUNT = "list_category_count";
//    public static String CONFIG_NOTI_LIST_COUNT = "noti_list_count";
//    public static String CONFIG_COMMENT_COUNT = "comment_count";


    /*Configuration Counts*/

    /*For ProductParameterHolder*/

//    public static String active = "1";
//    public static String FILTERING_INACTIVE = "";
//    public static String FILTERING_TRENDING = "touch_count";
//    public static String FILTERING_FEATURE = "featured_date";
//    //public static String latest = "addedDate";
//    public static String price = "originalPrice";
//    public static String FILTERING_ASC = "FILTERING_ASC";
//    public static String FILTERING_DESC = Constants.FILTERING_DESC;

    /*For ProductParameterHolder*/

    /*PayPal*/
//    public static int REQUEST_CODE__PAYPAL = 1444;

    public static void hideFirstFab(final View v) {
        v.setVisibility(View.GONE);
        v.setTranslationY(v.getHeight());
        v.setAlpha(0f);
    }

    public static boolean twistFab(final View v, boolean rotate) {
        v.animate().setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                    }
                })
                .rotation(rotate ? 165f : 0f);
        return rotate;
    }

    public static void showFab(final View v) {
        v.setVisibility(View.VISIBLE);
        v.setAlpha(0f);
        v.setTranslationY(v.getHeight());
        v.animate()
                .setDuration(300)
                .translationY(0)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                    }
                })
                .alpha(1f)
                .start();
    }

    public static void hideFab(final View v) {
        v.setVisibility(View.VISIBLE);
        v.setAlpha(1f);
        v.setTranslationY(0);
        v.animate()
                .setDuration(300)
                .translationY(v.getHeight())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        v.setVisibility(View.GONE);
                        super.onAnimationEnd(animation);
                    }
                }).alpha(0f)
                .start();
    }

    private static final NavigableMap<Long, String> suffixes = new TreeMap<>();

    static {
        suffixes.put(1_000L, "k");
        suffixes.put(1_000_000L, "M");
        suffixes.put(1_000_000_000L, "G");
        suffixes.put(1_000_000_000_000L, "T");
        suffixes.put(1_000_000_000_000_000L, "P");
        suffixes.put(1_000_000_000_000_000_000L, "E");
    }

    public static String numberFormat(long value) {
        //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
        if (value == Long.MIN_VALUE) return numberFormat(Long.MIN_VALUE + 1);
        if (value < 0) return "-" + numberFormat(-value);
        if (value < 1000) return Long.toString(value); //deal with easy case

        Map.Entry<Long, String> e = suffixes.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        long truncated = value / (divideBy / 10); //the number part of the output times 10
        boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
        return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
    }

    public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    public static String priceFormat(Float amount) {
        return NumberFormat.getNumberInstance(Locale.US).format(round(amount, 2));
    }

    public static String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static boolean toggleUpDownWithAnimation(View view) {
        if (view.getRotation() == 0) {
            view.animate().setDuration(150).rotation(180);
            return true;
        } else {
            view.animate().setDuration(150).rotation(0);
            return false;
        }
    }

//    public static AppLanguage appLanguage = new AppLanguage();
//
//
//    public static int getDrawableInt(Context context, String name) {
//        return context.getResources().getIdentifier(name, "drawable", context.getPackageName());
//    }
//
//    public static void setImageToImageView(Context context, ImageView imageView, int drawable) {
//        RequestOptions requestOptions = new RequestOptions()
//                .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
//                .skipMemoryCache(true);
//
//        Glide.with(context)
//                .load(drawable)
//                .apply(requestOptions)
//                .transition(DrawableTransitionOptions.withCrossFade())
//                .into(imageView);
//    }

    public static String checkUserId(String loginUserId) {
        if (loginUserId.trim().equals("")) {
            loginUserId = Constants.USER_NO_USER;
        }
        return loginUserId;
    }

    public static int getToolbarHeight(Context context) {
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[]{R.attr.actionBarSize});
        int toolbarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        return toolbarHeight;
    }

    @SuppressLint("RestrictedApi")
    public static void removeShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                //item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            Log.e("ERROR NO SUCH FIELD", "Unable to get shift mode field");
        } catch (IllegalAccessException e) {
            Log.e("ERROR ILLEGAL ALG", "Unable to change value of shift mode");
        }
    }

    public static void setMargins(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

    public static int pxToDp(Context context, int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public static Point getScreenSize(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        try {
            display.getSize(size);
        } catch (NoSuchMethodError e) {
            // For lower than api 11
            size.x = display.getWidth();
            size.y = display.getHeight();
        }
        return size;
    }

    public static boolean isAndroid_5_0() {
        String version = Build.VERSION.RELEASE;
        if (version != null && !version.equals("")) {
            String[] versionDetail = version.split("\\.");
            Log.d("TEAMPS", "0 : " + versionDetail[0] + " 1 : " + versionDetail[1]);
            if (versionDetail[0].equals("5")) {
                if (versionDetail[1].equals("0") || versionDetail[1].equals("00")) {
                    return true;
                }
            }
        }

        return false;
    }

    public static void psLog(String log) {
        if (Config.IS_DEVELOPMENT) {
            Log.d("TEAMPS", log);
        }
    }

    public static boolean isGooglePlayServicesOK(Activity activity) {

        final int GPS_ERRORDIALOG_REQUEST = 9001;

        int isAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);

        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (GooglePlayServicesUtil.isUserRecoverableError(isAvailable)) {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(isAvailable, activity, GPS_ERRORDIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(activity, "Can't connect to Google Play services", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public static int getScreenHeight(Activity activity) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.heightPixels;
    }

    public static int getScreenWidth(Activity activity) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.widthPixels;
    }

    public static boolean isEmailFormatValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public static void saveBitmapImage(Context context, Bitmap b, String picName) {
        FileOutputStream fos;
        try {
            fos = context.openFileOutput(picName, Context.MODE_APPEND);
            b.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d("TEAMPS", "file not found");
            e.printStackTrace();
        } catch (IOException e) {
            Log.d("TEAMPS", "io exception");
            e.printStackTrace();
        }

    }

    public static Bitmap loadBitmapImage(Context context, String picName) {
        Bitmap b = null;
        FileInputStream fis;
        try {
            fis = context.openFileInput(picName);
            b = BitmapFactory.decodeStream(fis);
            fis.close();

        } catch (FileNotFoundException e) {
            Log.d("TEAMPS", "file not found");
            e.printStackTrace();
        } catch (IOException e) {
            Log.d("TEAMPS", "io exception");
            e.printStackTrace();
        }
        return b;
    }

    public static Typeface getTypeFace(Context context, Fonts fonts) {

        if (currentTypeface == fonts) {
            if (fromAsset == null) {
                if (fonts == Fonts.NOTO_SANS) {
                    fromAsset = Typeface.createFromAsset(context.getAssets(), "fonts/NotoSans-Regular.ttf");
                } else if (fonts == Fonts.ROBOTO) {
//                    fromAsset = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
                    fromAsset = Typeface.createFromAsset(context.getAssets(), "fonts/Product-Sans-Regular.ttf");
                } else if (fonts == Fonts.ROBOTO_MEDIUM) {
                    fromAsset = Typeface.createFromAsset(context.getAssets(), "fonts/Product-Sans-Bold.ttf");
                } else if (fonts == Fonts.ROBOTO_LIGHT) {
                    fromAsset = Typeface.createFromAsset(context.getAssets(), "fonts/Product-Sans-Regular.ttf");
                } else if (fonts == Fonts.MM_FONT) {
                    fromAsset = Typeface.createFromAsset(context.getAssets(), "fonts/mymm.ttf");
                }

            }
        } else {
            if (fonts == Fonts.NOTO_SANS) {
                fromAsset = Typeface.createFromAsset(context.getAssets(), "fonts/NotoSans-Regular.ttf");
            } else if (fonts == Fonts.ROBOTO) {
//                fromAsset = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
                fromAsset = Typeface.createFromAsset(context.getAssets(), "fonts/Product-Sans-Regular.ttf");
            } else if (fonts == Fonts.ROBOTO_MEDIUM) {
                fromAsset = Typeface.createFromAsset(context.getAssets(), "fonts/Product-Sans-Bold.ttf");
            } else if (fonts == Fonts.ROBOTO_LIGHT) {
                fromAsset = Typeface.createFromAsset(context.getAssets(), "fonts/Product-Sans-Regular.ttf");
            } else if (fonts == Fonts.MM_FONT) {
                fromAsset = Typeface.createFromAsset(context.getAssets(), "fonts/mymm.ttf");
            } else {
                fromAsset = Typeface.createFromAsset(context.getAssets(), "fonts/Product-Sans-Regular.ttf");
            }

            //fromAsset = Typeface.createFromAsset(activity.getAssets(), "fonts/Roboto-Italic.ttf");
            currentTypeface = fonts;
        }
        return fromAsset;
    }

    public static SpannableString getSpannableString(Context context, String str, Fonts font) {
        spannableString = new SpannableString(str);
        spannableString.setSpan(new PSTypefaceSpan("", Utils.getTypeFace(context, font)), 0, spannableString.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    public enum Fonts {
        ROBOTO,
        NOTO_SANS,
        ROBOTO_LIGHT,
        ROBOTO_MEDIUM,
        MM_FONT
    }

    public enum LoadingDirection {
        top,
        bottom,
        none
    }

    public static Bitmap getUnRotatedImage(String imagePath, Bitmap rotatedBitmap) {
        int rotate = 0;
        try {
            File imageFile = new File(imagePath);
            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        Matrix matrix = new Matrix();
        matrix.postRotate(rotate);
        return Bitmap.createBitmap(rotatedBitmap, 0, 0, rotatedBitmap.getWidth(), rotatedBitmap.getHeight(), matrix,
                true);
    }

    public static int getLineNumber() {
        return Thread.currentThread().getStackTrace()[4].getLineNumber();
    }

    public static String getClassName(Object obj) {
        return "" + ((Object) obj).getClass();
    }

    public static void psErrorLog(String log, Object obj) {
        try {
            Log.d("TEAMPS", log);
            Log.d("TEAMPS", "Line : " + getLineNumber());
            Log.d("TEAMPS", "Class : " + getClassName(obj));
        } catch (Exception ee) {
            Log.d("TEAMPS", "Error in psErrorLog");
        }
    }

    public static void psErrorLog(String log, Exception e) {
        try {
            StackTraceElement l = e.getStackTrace()[0];
            Log.d("TEAMPS", log);
            Log.d("TEAMPS", "Line : " + l.getLineNumber());
            Log.d("TEAMPS", "Method : " + l.getMethodName());
            Log.d("TEAMPS", "Class : " + l.getClassName());
        } catch (Exception ee) {
            Log.d("TEAMPS", "Error in psErrorLogE");
        }

    }


    public static void unbindDrawables(View view) {
        try {
            if (view.getBackground() != null) {
                view.getBackground().setCallback(null);
            }
            if (view instanceof ViewGroup) {
                for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                    unbindDrawables(((ViewGroup) view).getChildAt(i));
                }

                if (!(view instanceof AdapterView)) {
                    ((ViewGroup) view).removeAllViews();
                }
            }
        } catch (Exception e) {
            Utils.psErrorLog("Error in Unbind", e);
        }
    }

    public static boolean isStoragePermissionGranted(Activity activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (activity.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Utils.psLog("Permission is granted");
                return true;
            } else {
                Utils.psLog("Permission is revoked");
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Utils.psLog("Permission is granted");
            return true;
        }
    }

    // Sleep Me
    public static void sleepMe(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Utils.psErrorLog("InterruptedException", e);
        } catch (Exception e) {
            Utils.psErrorLog("Exception", e);
        }
    }


    public static void hideKeyboard(Activity activity) {
        try {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);

            if (imm != null) {
                if (activity.getCurrentFocus() != null) {
                    imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
                }
            }
        } catch (Exception e) {
            Utils.psErrorLog("Error in hide keyboard.", e);
        }
    }

    //Ad
//    public static void initInterstitialAd(Context context, String adKey) {
//        //load ad
//        AdRequest adRequest = new AdRequest.Builder().build();
//
//        InterstitialAd interstitial;
//        // Prepare the Interstitial Ad
//        interstitial = new InterstitialAd(context);
//
//        // Insert the Ad Unit ID
//        interstitial.setAdUnitId(adKey);
//
//        interstitial.loadAd(adRequest);
//
//        // Prepare an Interstitial Ad Listener
//        interstitial.setAdListener(new AdListener() {
//            public void onAdLoaded() {
//                // Call displayInterstitial() function
//                displayInterstitial(interstitial);
//            }
//        });
//    }

//    public static void displayInterstitial(InterstitialAd interstitial) {
//        // If Ads are loaded, show Interstitial else show nothing.
//        if (interstitial.isLoaded()) {
//            interstitial.show();
//        }
//    }

    public static boolean toggleUporDown(View v) {
        if (v.getRotation() == 0) {
            v.animate().setDuration(150).rotation(180);
            return true;
        } else {
            v.animate().setDuration(150).rotation(0);
            return false;
        }
    }

//    public static void setConfigCountToShared(int value, SharedPreferences pref, String name) {
//
//        if (name.equals(CONFIG_COLLECTION_COUNT)) {
//            SharedPreferences.Editor editor = pref.edit();
//            editor.putInt(CONFIG_COLLECTION_COUNT, value);
//            editor.apply();
//        } else if (name.equals(CONFIG_HOME_PRODUCT_COUNT)) {
//            SharedPreferences.Editor editor = pref.edit();
//            editor.putInt(CONFIG_HOME_PRODUCT_COUNT, value);
//            editor.apply();
//        } else if (name.equals(CONFIG_PRODUCT_COUNT)) {
//            SharedPreferences.Editor editor = pref.edit();
//            editor.putInt(CONFIG_PRODUCT_COUNT, value);
//            editor.apply();
//        } else if (name.equals(CONFIG_HOME_CATEGORY_COUNT)) {
//            SharedPreferences.Editor editor = pref.edit();
//            editor.putInt(CONFIG_HOME_CATEGORY_COUNT, value);
//            editor.apply();
//        } else if (name.equals(CONFIG_LIST_CATEGORY_COUNT)) {
//            SharedPreferences.Editor editor = pref.edit();
//            editor.putInt(CONFIG_LIST_CATEGORY_COUNT, value);
//            editor.apply();
//        } else if (name.equals(CONFIG_NOTI_LIST_COUNT)) {
//            SharedPreferences.Editor editor = pref.edit();
//            editor.putInt(CONFIG_NOTI_LIST_COUNT, value);
//            editor.apply();
//        } else if (name.equals(CONFIG_COMMENT_COUNT)) {
//            SharedPreferences.Editor editor = pref.edit();
//            editor.putInt(CONFIG_COMMENT_COUNT, value);
//            editor.apply();
//        }
//
//    }


    public static void setDatesToShared(String start_date, String end_date, SharedPreferences pref) {

        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Constants.SHOP_START_DATE, start_date);
        editor.putString(Constants.SHOP_END_DATE, end_date);
        editor.apply();

    }

    public static String format(double value) {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        DecimalFormat df = (DecimalFormat) nf;
        df.applyPattern(Config.DECIMAL_PLACES_FORMAT);
        return df.format(value);

    }


//    public static int getLimitCount(SharedPreferences preferences, String name) {
//
//        if (name.equals(Constants.CONFIG_COLLECTION_COUNT)) {
//
//            if (Config.COLLECTION_PRODUCT_LIST_LIMIT_SERVER == 0) {
//
//                Config.COLLECTION_PRODUCT_LIST_LIMIT_SERVER = preferences.getInt(Constants.CONFIG_COLLECTION_COUNT, Config.DEFAULT_COUNT);
//
//                if (Config.COLLECTION_PRODUCT_LIST_LIMIT_SERVER > 0) {
//
//                    return Config.COLLECTION_PRODUCT_LIST_LIMIT_SERVER;
//
//                } else {
//
//                    return Config.COLLECTION_PRODUCT_LIST_LIMIT;
//
//                }
//
//            } else {
//
//                return Config.COLLECTION_PRODUCT_LIST_LIMIT_SERVER;
//
//            }
//
//        } else if (name.equals(Constants.CONFIG_HOME_PRODUCT_COUNT)) {
//
//            if (Config.HOME_PRODUCT_COUNT_SERVER == 0) {
//
//                Config.HOME_PRODUCT_COUNT_SERVER = preferences.getInt(Constants.CONFIG_HOME_PRODUCT_COUNT, Config.DEFAULT_COUNT);
//
//                if (Config.HOME_PRODUCT_COUNT_SERVER > 0) {
//
//                    return Config.HOME_PRODUCT_COUNT_SERVER;
//
//                } else {
//
//                    return Config.HOME_PRODUCT_COUNT;
//
//                }
//            } else {
//                return Config.HOME_PRODUCT_COUNT_SERVER;
//            }
//
//        } else if (name.equals(Constants.CONFIG_PRODUCT_COUNT)) {
//
//            if (Config.PRODUCT_COUNT_SERVER == 0) {
//
//                Config.PRODUCT_COUNT_SERVER = preferences.getInt(Constants.CONFIG_PRODUCT_COUNT, Config.DEFAULT_COUNT);
//
//                if (Config.PRODUCT_COUNT_SERVER > 0) {
//
//                    return Config.PRODUCT_COUNT_SERVER;
//
//                } else {
//
//                    return Config.PRODUCT_COUNT;
//
//                }
//            } else {
//
//                return Config.PRODUCT_COUNT_SERVER;
//
//            }
//        } else if (name.equals(Constants.CONFIG_HOME_CATEGORY_COUNT)) {
//
//            if (Config.HOME_CATEGORY_COUNT_SERVER == 0) {
//
//                Config.HOME_CATEGORY_COUNT_SERVER = preferences.getInt(Constants.CONFIG_HOME_CATEGORY_COUNT, Config.DEFAULT_COUNT);
//
//                if (Config.HOME_CATEGORY_COUNT_SERVER > 0) {
//
//                    return Config.HOME_CATEGORY_COUNT_SERVER;
//
//                } else {
//
//                    return Config.HOME_CATEGORY_COUNT;
//
//                }
//            } else {
//
//                return Config.HOME_CATEGORY_COUNT_SERVER;
//
//            }
//
//        } else if (name.equals(Constants.CONFIG_LIST_CATEGORY_COUNT)) {
//
//            if (Config.LIST_CATEGORY_COUNT_SERVER == 0) {
//
//                Config.LIST_CATEGORY_COUNT_SERVER = preferences.getInt(Constants.CONFIG_LIST_CATEGORY_COUNT, Config.DEFAULT_COUNT);
//
//                if (Config.LIST_CATEGORY_COUNT_SERVER > 0) {
//
//                    return Config.LIST_CATEGORY_COUNT_SERVER;
//
//                } else {
//
//                    return Config.LIST_CATEGORY_COUNT;
//
//                }
//            } else {
//
//                return Config.LIST_CATEGORY_COUNT_SERVER;
//
//            }
//        } else if (name.equals(Constants.CONFIG_NOTI_LIST_COUNT)) {
//
//            if (Config.NOTI_LIST_COUNT_SERVER == 0) {
//
//                Config.NOTI_LIST_COUNT_SERVER = preferences.getInt(Constants.CONFIG_NOTI_LIST_COUNT, Config.DEFAULT_COUNT);
//
//                if (Config.NOTI_LIST_COUNT_SERVER > 0) {
//
//                    return Config.NOTI_LIST_COUNT_SERVER;
//
//                } else {
//
//                    return Config.NOTI_LIST_COUNT;
//
//                }
//
//            } else {
//
//                return Config.NOTI_LIST_COUNT_SERVER;
//
//            }
//
//        } else if (name.equals(Constants.CONFIG_COMMENT_COUNT)) {
//
//            if (Config.COMMENT_COUNT_SERVER == 0) {
//
//                Config.COMMENT_COUNT_SERVER = preferences.getInt(Constants.CONFIG_COMMENT_COUNT, Config.DEFAULT_COUNT);
//
//                if (Config.COMMENT_COUNT_SERVER > 0) {
//
//                    return Config.COMMENT_COUNT_SERVER;
//
//                } else {
//
//                    return Config.COMMENT_COUNT;
//
//                }
//
//            } else {
//
//                return Config.COMMENT_COUNT_SERVER;
//
//            }
//
//        } else {
//
//            return Config.DEFAULT_COUNT;
//
//        }
//
//    }
}
