package net.poringsoft.imascggallery.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.preference.PreferenceManager;

import net.poringsoft.imascggallery.utils.PSDebug;

/**
 * 設定情報保持クラス
 */
public class EnvOption {
    //定数
    //---------------------------------------------------------
    //通信関連
    public static final String NET_GET_AGENT = "Mozilla/5.0 (Linux; Android; ja-jp;)";
    public static final int NET_GET_TIMEOUT = 20000;
    
    //カード情報
    public static final Point CARD_IMAGE_SIZE = new Point(640, 800);   //カード実サイズ
    
    //設定値
    public static final String KEY_MAIN_LIST_SORT_TYPE = " main_list_sort_type";     //メインリストの並び替え値

    //表示設定
    public static final String KEY_VIEW_SHOW_IDOL_BIRTHDAY = "view_show_idol_birthday"; //誕生日情報の表示
    public static final String KEY_VIEW_SHOW_CARD_PARAM = "view_show_card_param";       //カードパラメーターの表示
    public static final String KEY_VIEW_SHOW_CARD_FRAME = "view_show_card_frame";       //カードフレームの表示

    //共通メソッド
    //---------------------------------------------------------
    /**
     * 指定した文字列を設定ファイルに保存する
     * @param context コンテキスト
     * @param key 保存キー
     * @param value 保存値
     */
    private static void putString(Context context, String key, String value) {
        PSDebug.d("key=" + key + " value=" + value);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        pref.edit().putString(key, value).apply();
    }

    /**
     * 設定ファイルに保存されている文字列を取得する
     * @param context コンテキスト
     * @param key 保存キー
     * @param def 保存されていなかったときのデフォルト値
     * @return 取得したデータ
     */
    private static String getString(Context context, String key, String def) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String result = pref.getString(key, def);
        PSDebug.d("key=" + key + " value=" + result + " def=" + def);
        return result;
    }

    /**
     * 指定した整数値を設定ファイルに保存する
     * @param context コンテキスト
     * @param key 保存キー
     * @param value 保存値
     */
    private static void putInt(Context context, String key, int value) {
        PSDebug.d("key=" + key + " value=" + value);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        pref.edit().putInt(key, value).apply();
    }

    /**
     * 設定ファイルに保存されている整数値を取得する
     * @param context コンテキスト
     * @param key 保存キー
     * @param def 保存されていなかったときのデフォルト値
     * @return 取得したデータ
     */
    private static int getInt(Context context, String key, int def) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        int result = pref.getInt(key, def);
        PSDebug.d("key=" + key + " value=" + result + " def=" + def);
        return result;
    }

    /**
     * 指定した論理値を設定ファイルに保存する
     * @param context コンテキスト
     * @param key 保存キー
     * @param value 保存値
     */
    private static void putBoolean(Context context, String key, boolean value) {
        PSDebug.d("key=" + key + " value=" + value);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        pref.edit().putBoolean(key, value).apply();
    }

    /**
     * 設定ファイルに保存されている論理値を取得する
     * @param context コンテキスト
     * @param key 保存キー
     * @param def 保存されていなかったときのデフォルト値
     * @return 取得したデータ
     */
    private static boolean getBoolean(Context context, String key, boolean def) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        boolean result = pref.getBoolean(key, def);
        PSDebug.d("key=" + key + " value=" + result + " def=" + def);
        return result;
    }
    
    //メイン画面関連
    //-------------------------------------------
    /**
     * メイン画面の並び替え値を保存
     * @param context コンテキスト
     * @param sortType 並び替え値（SqlSelectHelper.SELECT_MAIN_SORT_）
     */
    public static void putMainListSortType(Context context, int sortType) {
        putInt(context, KEY_MAIN_LIST_SORT_TYPE, sortType);
    }

    /**
     * メイン画面の並び替え値を取得
     * @param context コンテキスト
     * @return 並び替え値（SqlSelectHelper.SELECT_MAIN_SORT_）
     */
    public static int getMainListSortType(Context context) {
        return getInt(context, KEY_MAIN_LIST_SORT_TYPE, SqlSelectHelper.SELECT_MAIN_SORT_ROWID_ASC);
    }

    //表示関連
    //-------------------------------------------
    /**
     * アイドル情報一覧で誕生日・星座情報を表示するかどうか
     * @param context コンテキスト
     * @return 表示するときはtrue
     */
    public static boolean getViewShowIdolBirthday(Context context) {
        return getBoolean(context, KEY_VIEW_SHOW_IDOL_BIRTHDAY, false);
    }

    /**
     * カード情報パラメーターを表示するかどうか
     * @param context コンテキスト
     * @return 表示するときはtrue
     */
    public static boolean getViewShowCardParam(Context context) {
        return getBoolean(context, KEY_VIEW_SHOW_CARD_PARAM, false);
    }

    /**
     * カードフレームを表示するかどうか（Sレア以上のときのみ）
     * @param context コンテキスト
     * @return 表示するときはtrue
     */
    public static boolean getViewShowCardFrame(Context context) {
        return getBoolean(context, KEY_VIEW_SHOW_CARD_FRAME, false);
    }



}
