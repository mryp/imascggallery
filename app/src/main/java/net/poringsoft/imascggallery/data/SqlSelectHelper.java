package net.poringsoft.imascggallery.data;

import android.database.DatabaseUtils;

import net.poringsoft.imascggallery.utils.KanamojiCharUtils;

import java.util.List;

/**
 * SQL検索・並び替え用文字列生成ヘルパークラス 
 * Created by mry on 15/01/20.
 */
public class SqlSelectHelper {
    //定数
    //---------------------------------------------------
    //検索コマンド
    public static final String CMD_ALL = "ALL";             //すべて表示
    public static final String CMD_BOOKMARK = "BOOKMARK";   //お気に入り
    public static final String CMD_NAME_LIST = "NAMELIST";  //名前列挙

    //並び替え種別
    public static final int SELECT_MAIN_SORT_ROWID_ASC = 1;
    public static final int SELECT_MAIN_SORT_NAME_ASC = 2;
    public static final int SELECT_MAIN_SORT_KANA_ASC = 3;
    public static final int SELECT_MAIN_SORT_AGE_ASC = 4;
    public static final int SELECT_MAIN_SORT_AGE_DESC = 5;
    public static final int SELECT_MAIN_SORT_HEIGHT_ASC = 6;
    public static final int SELECT_MAIN_SORT_HEIGHT_DESC = 7;
    public static final int SELECT_MAIN_SORT_WEIGHT_ASC = 8;
    public static final int SELECT_MAIN_SORT_WEIGHT_DESC = 9;
    public static final int SELECT_MAIN_SORT_BUST_ASC = 10;
    public static final int SELECT_MAIN_SORT_BUST_DESC = 11;
    public static final int SELECT_MAIN_SORT_WAIST_ASC = 12;
    public static final int SELECT_MAIN_SORT_WAIST_DESC = 13;
    public static final int SELECT_MAIN_SORT_HIP_ASC = 14;
    public static final int SELECT_MAIN_SORT_HIP_DESC = 15;

    //メソッド
    //---------------------------------------------------
    /**
     * アイドルプロフィール情報を検索する文字列を生成する
     * @param searchText 検索文字列（コマンドはSELECT_MAIN_SORT_〜を使用する）
     * @return DB検索用文字列
     */
    public static String createSelectIldeProfile(String searchText){
        String[] command = searchText.split(":");
        if (command.length < 2)
        {
            return createIdleProfileNameSelect(searchText);
        }

        String key = command[0];
        String value = command[1];
        String select = "";
        if (key.equals(CMD_ALL))
        {
            select = "";    //すべて表示するため検索条件なし
        }
        else if (key.equals(CMD_NAME_LIST))
        {
            String[] nameList = value.split(",");

            StringBuilder sb = new StringBuilder();
            for (String name : nameList) {
                sb.append(SqlDao.IDLE_PROFILE_COLUMN_NAME);
                sb.append(" LIKE ");
                sb.append(DatabaseUtils.sqlEscapeString("%"+name+"%"));
                sb.append(" OR ");
            }
            
            select = sb.toString();
            if (select.endsWith(" OR ")) {
                select = select.substring(0, select.length() - 4);
            }
        }

        return select;
    }

    /**
     * 指定した検索分がブックマーク検索かどうか
     * @param searchText 検索分
     * @return ブックマーク検索のときはtrue
     */
    public static boolean isBookmarkCommand(String searchText) {
        return searchText.startsWith(CMD_BOOKMARK);
    }

    /**
     * 名前リストからアイドルプロフィールを検索するときの名前リストコマンドに変換して返す
     * @param nameList 名前リスト
     * @return 名前列挙コマンド
     */
    public static String createCommandNameList(List<String> nameList) {
        StringBuilder sb = new StringBuilder();
        for (String name : nameList) {
            sb.append(name);
            sb.append(",");
        }
        String commnad = sb.toString();
        if (commnad.endsWith(",")) {
            commnad = commnad.substring(0, commnad.length() - 1);
        }

        return CMD_NAME_LIST + ":" + commnad;
    }

    /**
     * アイドルプロフィールの名前検索条件を作成する
     * @param searchText 検索文字列
     * @return DB検索文字列
     */
    private static String createIdleProfileNameSelect(String searchText) {
        String kanaSearchText = KanamojiCharUtils.zenkakuHiraganaToZenkakuKatakana(searchText);

        //部分一致検索
        String nameQuery = DatabaseUtils.sqlEscapeString(searchText);
        String kanaQuery = DatabaseUtils.sqlEscapeString("%"+kanaSearchText+"%");

        return SqlDao.IDLE_PROFILE_COLUMN_NAME
                + " LIKE "
                + nameQuery
                + " OR "
                + SqlDao.IDLE_PROFILE_COLUMN_KANA
                + " LIKE "
                + kanaQuery;
    }

    /**
     * アイドルプロフィール情報並び替え条件を作成する
     * @param sortType 並び替え値（SELECT_MAIN_SORT_〜）
     * @return 並び替え条件文字列
     */
    public static String createOrderIdleProfile(int sortType) {
        String order;
        switch (sortType) {
            case SELECT_MAIN_SORT_ROWID_ASC:
                order = SqlDao.IDLE_PROFILE_COLUMN_ID + " ASC";
                break;
            case SELECT_MAIN_SORT_NAME_ASC:
                order = SqlDao.IDLE_PROFILE_COLUMN_NAME + " ASC";
                break;
            case SELECT_MAIN_SORT_KANA_ASC:
                order = SqlDao.IDLE_PROFILE_COLUMN_KANA + " ASC";
                break;
            case SELECT_MAIN_SORT_AGE_ASC:
                order = SqlDao.IDLE_PROFILE_COLUMN_AGO + " ASC";
                break;
            case SELECT_MAIN_SORT_AGE_DESC:
                order = SqlDao.IDLE_PROFILE_COLUMN_AGO + " DESC";
                break;
            case SELECT_MAIN_SORT_HEIGHT_ASC:
                order = SqlDao.IDLE_PROFILE_COLUMN_HEIGHT + " ASC";
                break;
            case SELECT_MAIN_SORT_HEIGHT_DESC:
                order = SqlDao.IDLE_PROFILE_COLUMN_HEIGHT + " DESC";
                break;
            case SELECT_MAIN_SORT_WEIGHT_ASC:
                order = SqlDao.IDLE_PROFILE_COLUMN_WEIGHT + " ASC";
                break;
            case SELECT_MAIN_SORT_WEIGHT_DESC:
                order = SqlDao.IDLE_PROFILE_COLUMN_WEIGHT + " DESC";
                break;
            case SELECT_MAIN_SORT_BUST_ASC:
                order = SqlDao.IDLE_PROFILE_COLUMN_BUST + " ASC";
                break;
            case SELECT_MAIN_SORT_BUST_DESC:
                order = SqlDao.IDLE_PROFILE_COLUMN_BUST + " DESC";
                break;
            case SELECT_MAIN_SORT_WAIST_ASC:
                order = SqlDao.IDLE_PROFILE_COLUMN_WAIST + " ASC";
                break;
            case SELECT_MAIN_SORT_WAIST_DESC:
                order = SqlDao.IDLE_PROFILE_COLUMN_WAIST + " DESC";
                break;
            case SELECT_MAIN_SORT_HIP_ASC:
                order = SqlDao.IDLE_PROFILE_COLUMN_HIP + " ASC";
                break;
            case SELECT_MAIN_SORT_HIP_DESC:
                order = SqlDao.IDLE_PROFILE_COLUMN_HIP + " DESC";
                break;
            default:
                order = SqlDao.IDLE_PROFILE_COLUMN_ID + " ASC";
                break;
        }

        return order;
    }

    /**
     * アイドル名からアイドルカード情報を検索するセレクト文を生成する
     * @param searchText アイドル名
     * @return セレクト文
     */
    public static String createSelectIldeCard(String searchText){
        return SqlDao.IDLE_CARD_COLUMN_NAME + "=" + DatabaseUtils.sqlEscapeString(searchText);
    }

    /**
     * アルバムIDからアイドルカード情報を検索するセレクト文を生成する
     * @param albumId アルバムID
     * @return セレクト文
     */
    public static String createSelectIldeCardFromAlbumId(int albumId) {
        return SqlDao.IDLE_CARD_COLUMN_ALBUM_ID + "=" + String.valueOf(albumId);
    }

    /**
     * アイドル名からブックマーク登録情報を検索するセレクト分を生成する
     * @param searchText アイドル名
     * @return セレクト分
     */
    public static String createBookmarkNameSelect(String searchText) {
        return SqlDao.BOOKMARK_COLUMN_NAME + "=" + DatabaseUtils.sqlEscapeString(searchText);
    }
}
