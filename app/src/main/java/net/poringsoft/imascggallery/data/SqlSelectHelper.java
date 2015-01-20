package net.poringsoft.imascggallery.data;

import android.database.DatabaseUtils;

import net.poringsoft.imascggallery.utils.KanamojiCharUtils;

/**
 * Created by mry on 15/01/20.
 */
public class SqlSelectHelper {
    //検索コマンド
    public static final String CMD_ALL = "ALL";  //すべて表示
    public static final String CMD_NAME_LIST = "NAMELIST";  //名前列挙

    public static String createSelectIldeProfile(String searchText){
        String[] command = searchText.split(":");
        if (command.length < 2)
        {
            return createNameSelect(searchText);
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

    private static String createNameSelect(String searchText) {
        String kanaSearchText = KanamojiCharUtils.zenkakuHiraganaToZenkakuKatakana(searchText);

        //部分一致検索
        String nameQuery = DatabaseUtils.sqlEscapeString("%"+searchText+"%");
        String kanaQuery = DatabaseUtils.sqlEscapeString("%"+kanaSearchText+"%");

        StringBuilder select = new StringBuilder();
        select.append(SqlDao.IDLE_PROFILE_COLUMN_NAME);
        select.append(" LIKE ");
        select.append(nameQuery);
        select.append(" OR ");
        select.append(SqlDao.IDLE_PROFILE_COLUMN_KANA);
        select.append(" LIKE ");
        select.append(kanaQuery);

        return select.toString();
    }

}
