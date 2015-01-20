package net.poringsoft.imascggallery.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import net.poringsoft.imascggallery.utils.PSDebug;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * カードデータ操作クラス
 */
public class SqlDao {
    //定数
    //----------------------------------------------------------------------------
    public static final int RETRY_SQL_CALL = 5;		//リトライ回数
    public static final int WAIT_SLEEP_TIME = 50;		//スリープ時間
    public static final String SPLIT_CHAR = "\t";

    //テーブル定数
    //----------------------------------------------------------------------------
    public static final String IDLE_CARD_TABLE_MAME = "idlecardtbl";
    public static final String IDLE_CARD_COLUMN_ID = "rowid";
    public static final String IDLE_CARD_COLUMN_ALBUM_ID = "m_albumId";
    public static final String IDLE_CARD_COLUMN_ATTRIBUTE = "m_attribute";
    public static final String IDLE_CARD_COLUMN_RARITY = "m_rarity";
    public static final String IDLE_CARD_COLUMN_NAME_PREFIX = "m_namePrefix";
    public static final String IDLE_CARD_COLUMN_NAME = "m_name";
    public static final String IDLE_CARD_COLUMN_NAME_POST = "m_namePost";
    public static final String IDLE_CARD_COLUMN_COST = "m_cost";
    public static final String IDLE_CARD_COLUMN_ATTACK = "m_attack";
    public static final String IDLE_CARD_COLUMN_DEFENSE = "m_defense";
    public static final String IDLE_CARD_COLUMN_MAX_ATTACK = "m_maxAttack";
    public static final String IDLE_CARD_COLUMN_MAX_DEFENSE = "m_maxDefense";
    public static final String IDLE_CARD_COLUMN_MAX_CONFIRMED = "m_maxConfirmed";
    public static final String IDLE_CARD_COLUMN_ATTACK_COSPA = "m_attackCospa";
    public static final String IDLE_CARD_COLUMN_DEFENSE_COSPA = "m_defenseCospa";
    public static final String IDLE_CARD_COLUMN_SKILL_NAME = "m_skillName";
    public static final String IDLE_CARD_COLUMN_TARGET_ATTR = "m_targetAttr";
    public static final String IDLE_CARD_COLUMN_ATTDEF_TYPE = "m_attdefType";
    public static final String IDLE_CARD_COLUMN_SKILL_EFFECT = "m_skillEffect";
    public static final String IDLE_CARD_COLUMN_REMAKS = "m_remarks";
    public static final String IDLE_CARD_COLUMN_IMAGE_HASH = "m_imageHash";
    public static final String[] IDLE_CARD_COLUMNS = {
            IDLE_CARD_COLUMN_ID,
            IDLE_CARD_COLUMN_ALBUM_ID,
            IDLE_CARD_COLUMN_ATTRIBUTE,
            IDLE_CARD_COLUMN_RARITY,
            IDLE_CARD_COLUMN_NAME_PREFIX,
            IDLE_CARD_COLUMN_NAME,
            IDLE_CARD_COLUMN_NAME_POST,
            IDLE_CARD_COLUMN_COST,
            IDLE_CARD_COLUMN_ATTACK,
            IDLE_CARD_COLUMN_DEFENSE,
            IDLE_CARD_COLUMN_MAX_ATTACK,
            IDLE_CARD_COLUMN_MAX_DEFENSE,
            IDLE_CARD_COLUMN_MAX_CONFIRMED,
            IDLE_CARD_COLUMN_ATTACK_COSPA,
            IDLE_CARD_COLUMN_DEFENSE_COSPA,
            IDLE_CARD_COLUMN_SKILL_NAME,
            IDLE_CARD_COLUMN_TARGET_ATTR,
            IDLE_CARD_COLUMN_ATTDEF_TYPE,
            IDLE_CARD_COLUMN_SKILL_EFFECT,
            IDLE_CARD_COLUMN_REMAKS,
            IDLE_CARD_COLUMN_IMAGE_HASH,
    };


    public static final String IDLE_PROFILE_TABLE_MAME = "idleprofiletbl";
    public static final String IDLE_PROFILE_COLUMN_ID = "rowid";
    public static final String IDLE_PROFILE_COLUMN_NAME = "m_name";
    public static final String IDLE_PROFILE_COLUMN_KANA = "m_kana";
    public static final String IDLE_PROFILE_COLUMN_AGO = "m_ago";
    public static final String IDLE_PROFILE_COLUMN_HEIGHT = "m_height";
    public static final String IDLE_PROFILE_COLUMN_WEIGHT = "m_weight";
    public static final String IDLE_PROFILE_COLUMN_BUST = "m_bust";
    public static final String IDLE_PROFILE_COLUMN_WAIST = "m_waist";
    public static final String IDLE_PROFILE_COLUMN_HIP = "m_hip";
    public static final String IDLE_PROFILE_COLUMN_BIRTHDAY = "m_birthday";
    public static final String IDLE_PROFILE_COLUMN_COLSTELLATION = "m_constellation";
    public static final String IDLE_PROFILE_COLUMN_BLOODTYPE = "m_bloodType";
    public static final String IDLE_PROFILE_COLUMN_HAND = "m_hand";
    public static final String IDLE_PROFILE_COLUMN_HOME = "m_home";
    public static final String IDLE_PROFILE_COLUMN_HOBBY = "m_hobby";
    public static final String IDLE_PROFILE_COLUMN_IMAGE_HASH = "m_imageHash";
    public static final String[] IDLE_PROFILE_COLUMNS = {
            IDLE_PROFILE_COLUMN_ID,
            IDLE_PROFILE_COLUMN_NAME,
            IDLE_PROFILE_COLUMN_KANA,
            IDLE_PROFILE_COLUMN_AGO,
            IDLE_PROFILE_COLUMN_HEIGHT,
            IDLE_PROFILE_COLUMN_WEIGHT,
            IDLE_PROFILE_COLUMN_BUST,
            IDLE_PROFILE_COLUMN_WAIST,
            IDLE_PROFILE_COLUMN_HIP,
            IDLE_PROFILE_COLUMN_BIRTHDAY,
            IDLE_PROFILE_COLUMN_COLSTELLATION,
            IDLE_PROFILE_COLUMN_BLOODTYPE,
            IDLE_PROFILE_COLUMN_HAND,
            IDLE_PROFILE_COLUMN_HOME,
            IDLE_PROFILE_COLUMN_HOBBY,
            IDLE_PROFILE_COLUMN_IMAGE_HASH,
    };
    
    
    //フィールド
    //----------------------------------------------------------------------------
    private SQLiteDatabase m_db = null;		//データベースオブジェクト


    //初期化終了処理関連メソッド
    //----------------------------------------------------------------------------
    /**
     * コンストラクタ
     */
    public SqlDao(SQLiteDatabase db)
    {
        m_db = db;
    }

    /**
     * DBクローズ
     */
    public void Close()
    {
        m_db.close();
    }


    //DBアクセスヘルパーメソッド
    //----------------------------------------------------------------------------
    /**
     * トランザクション開始（複数操作を同時に指定する場合はこれを使用する）
     * 正常処理時はsetTransSuccessfulをよび、正常・失敗にかかわらず処理終了後は必ずendTransactionを呼び出すこと
     * 例：
     * 	db.beginTransaction();
     *  try {
     *    ...
     *    db.setTransSuccessful();
     *  } finally {
     *    db.endTrans();
     *  }
     */
    public void beginTransaction()
    {
        m_db.beginTransaction();
    }

    /**
     * トランザクション中に成功したとき
     */
    public void setTransactionSuccessful()
    {
        m_db.setTransactionSuccessful();
    }

    /**
     * トランザクション終了
     */
    public void endTransaction()
    {
        m_db.endTransaction();
    }

    /**
     * DBへデータを挿入する
     */
    private long dbInsert(String table, String nullColumnHack, ContentValues values, int retry)
    {
        long id;
        try
        {
            id = m_db.insert(table, nullColumnHack, values);
        }
        catch (SQLiteException e)
        {
            if (retry == 0)
            {
                return 0;	//あきらめる
            }

            //ちょっとだけ待つ
            try {
                Thread.sleep(WAIT_SLEEP_TIME);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
                return 0;
            }

            //リトライする
            return dbInsert(table, nullColumnHack, values, retry-1);
        }

        return id;
    }

    /**
     * DB更新
     */
    private long dbUpdate(String table, ContentValues values, String whereClause, String[] whereArgs, int retry)
    {
        int ret;
        try
        {
            ret = m_db.update(table, values, whereClause, whereArgs);
        }
        catch (SQLiteException e)
        {
            if (retry == 0)
            {
                return 0;	//あきらめる
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            return dbUpdate(table, values, whereClause, whereArgs, retry-1);
        }

        return ret;
    }

    /**
     * DB削除呼び出し
     */
    private int dbDelete(String table, String whereClause, String[] whereArgs, int retry)
    {
        int ret;
        try
        {
            ret = m_db.delete(table, whereClause, whereArgs);
        }
        catch (SQLiteException e)
        {
            if (retry == 0)
            {
                return 0;	//あきらめる
            }
            try {
                Thread.sleep(WAIT_SLEEP_TIME);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            return dbDelete(table, whereClause, whereArgs, retry-1);
        }

        return ret;
    }

    /**
     * bool値をSQLで保存するint値に変換する
     */
    public int ConvBoolToSqlInt(boolean b)
    {
        return b ? 1 : 0;
    }

    /**
     * SQLで保存されているint値をbool値に変換する
     */
    public boolean ConvSqlIntToBool(int i)
    {
        return i != 0;
    }

    /**
     * 現在の日時を取得する
     * @return 日時
     */
    public long getNowTime()
    {
        return (new Date()).getTime();
    }

    
    //アイドルカード情報
    //----------------------------------------------------------------------------
    public long insertIdleCardInfo(IdleCardInfo info) {
        return dbInsert(IDLE_CARD_TABLE_MAME, null, getNewIdleCardInfoValues(info), RETRY_SQL_CALL);
    }

    public void deleteIdleCardInfoAll() {
        dbDelete(IDLE_CARD_TABLE_MAME, null, null, RETRY_SQL_CALL);
    }

    private ContentValues getNewIdleCardInfoValues(IdleCardInfo info) {
        ContentValues values = new ContentValues();

        values.put(IDLE_CARD_COLUMN_ALBUM_ID, info.getAlbumId());
        values.put(IDLE_CARD_COLUMN_ATTRIBUTE, info.getAttribute());
        values.put(IDLE_CARD_COLUMN_RARITY, info.getRarity());
        values.put(IDLE_CARD_COLUMN_NAME_PREFIX, info.getNamePrefix());
        values.put(IDLE_CARD_COLUMN_NAME, info.getName());
        values.put(IDLE_CARD_COLUMN_NAME_POST, info.getNamePost());
        values.put(IDLE_CARD_COLUMN_COST, info.getCost());
        values.put(IDLE_CARD_COLUMN_ATTACK, info.getAttack());
        values.put(IDLE_CARD_COLUMN_DEFENSE, info.getDefense());
        values.put(IDLE_CARD_COLUMN_MAX_ATTACK, info.getMaxAttack());
        values.put(IDLE_CARD_COLUMN_MAX_DEFENSE, info.getMaxDefense());
        values.put(IDLE_CARD_COLUMN_MAX_CONFIRMED, info.getMaxConfirmed());
        values.put(IDLE_CARD_COLUMN_ATTACK_COSPA, info.getAttackCospa());
        values.put(IDLE_CARD_COLUMN_DEFENSE_COSPA, info.getDefenseCospa());
        values.put(IDLE_CARD_COLUMN_SKILL_NAME, info.getSkillName());
        values.put(IDLE_CARD_COLUMN_TARGET_ATTR, info.getTargetAttr());
        values.put(IDLE_CARD_COLUMN_ATTDEF_TYPE, info.getAttdefType());
        values.put(IDLE_CARD_COLUMN_SKILL_EFFECT, info.getSkillEffect());
        values.put(IDLE_CARD_COLUMN_REMAKS, info.getRemarks());
        values.put(IDLE_CARD_COLUMN_IMAGE_HASH, info.getImageHash());
        
        return values;
    }

    private IdleCardInfo createIdleCardInfo(Cursor cursor)
    {
        IdleCardInfo cardInfo = new IdleCardInfo();
        int id = cursor.getInt(0);
        PSDebug.d("id=" + id);

        cardInfo.setAlbumId(cursor.getInt(cursor.getColumnIndex(IDLE_CARD_COLUMN_ALBUM_ID)));
        cardInfo.setAttribute(cursor.getString(cursor.getColumnIndex(IDLE_CARD_COLUMN_ATTRIBUTE)));
        cardInfo.setRarity(cursor.getString(cursor.getColumnIndex(IDLE_CARD_COLUMN_RARITY)));
        cardInfo.setNamePrefix(cursor.getString(cursor.getColumnIndex(IDLE_CARD_COLUMN_NAME_PREFIX)));
        cardInfo.setName(cursor.getString(cursor.getColumnIndex(IDLE_CARD_COLUMN_NAME)));
        cardInfo.setNamePost(cursor.getString(cursor.getColumnIndex(IDLE_CARD_COLUMN_NAME_POST)));
        cardInfo.setCost(cursor.getInt(cursor.getColumnIndex(IDLE_CARD_COLUMN_COST)));
        cardInfo.setAttack(cursor.getInt(cursor.getColumnIndex(IDLE_CARD_COLUMN_ATTACK)));
        cardInfo.setDefense(cursor.getInt(cursor.getColumnIndex(IDLE_CARD_COLUMN_DEFENSE)));
        cardInfo.setMaxAttack(cursor.getInt(cursor.getColumnIndex(IDLE_CARD_COLUMN_MAX_ATTACK)));
        cardInfo.setMaxDefense(cursor.getInt(cursor.getColumnIndex(IDLE_CARD_COLUMN_MAX_DEFENSE)));
        cardInfo.setMaxConfirmed(cursor.getString(cursor.getColumnIndex(IDLE_CARD_COLUMN_MAX_CONFIRMED)));
        cardInfo.setAttackCospa(cursor.getDouble(cursor.getColumnIndex(IDLE_CARD_COLUMN_ATTACK_COSPA)));
        cardInfo.setDefenseCospa(cursor.getDouble(cursor.getColumnIndex(IDLE_CARD_COLUMN_DEFENSE_COSPA)));
        cardInfo.setSkillName(cursor.getString(cursor.getColumnIndex(IDLE_CARD_COLUMN_SKILL_NAME)));
        cardInfo.setTargetAttr(cursor.getString(cursor.getColumnIndex(IDLE_CARD_COLUMN_TARGET_ATTR)));
        cardInfo.setAttdefType(cursor.getString(cursor.getColumnIndex(IDLE_CARD_COLUMN_ATTDEF_TYPE)));
        cardInfo.setSkillEffect(cursor.getString(cursor.getColumnIndex(IDLE_CARD_COLUMN_SKILL_EFFECT)));
        cardInfo.setRemarks(cursor.getString(cursor.getColumnIndex(IDLE_CARD_COLUMN_REMAKS)));
        cardInfo.setImageHash(cursor.getString(cursor.getColumnIndex(IDLE_CARD_COLUMN_IMAGE_HASH)));

        return cardInfo;
    }

    public List<IdleCardInfo> selectIdleCardInfo(String select, String order, String limit)
    {
        PSDebug.d("select=" + select);
        PSDebug.d("order=" + order);
        ArrayList<IdleCardInfo> list = new ArrayList<IdleCardInfo>();
        Cursor cursor = m_db.query(IDLE_CARD_TABLE_MAME, IDLE_CARD_COLUMNS, select, null, null, null, order, limit);
        while (cursor.moveToNext())
        {
            list.add(createIdleCardInfo(cursor));
        }
        cursor.close();

        return list;
    }

    //アイドルプロフィール情報
    //----------------------------------------------------------------------------
    public long insertIdleProfileInfo(IdleProfileInfo info) {
        return dbInsert(IDLE_PROFILE_TABLE_MAME, null, getNewIdleProfileInfoValues(info), RETRY_SQL_CALL);
    }

    public void deleteIdleProfileInfoAll() {
        dbDelete(IDLE_PROFILE_TABLE_MAME, null, null, RETRY_SQL_CALL);
    }

    private ContentValues getNewIdleProfileInfoValues(IdleProfileInfo info) {
        ContentValues values = new ContentValues();

        values.put(IDLE_PROFILE_COLUMN_NAME, info.getName());
        values.put(IDLE_PROFILE_COLUMN_KANA, info.getKana());
        values.put(IDLE_PROFILE_COLUMN_AGO, info.getAgo());
        values.put(IDLE_PROFILE_COLUMN_HEIGHT, info.getHeight());
        values.put(IDLE_PROFILE_COLUMN_WEIGHT, info.getWeight());
        values.put(IDLE_PROFILE_COLUMN_BUST, info.getBust());
        values.put(IDLE_PROFILE_COLUMN_WAIST, info.getWaist());
        values.put(IDLE_PROFILE_COLUMN_HIP, info.getHip());
        values.put(IDLE_PROFILE_COLUMN_BIRTHDAY, info.getBirthday());
        values.put(IDLE_PROFILE_COLUMN_COLSTELLATION, info.getConstellation());
        values.put(IDLE_PROFILE_COLUMN_BLOODTYPE, info.getBloodType());
        values.put(IDLE_PROFILE_COLUMN_HAND, info.getHand());
        values.put(IDLE_PROFILE_COLUMN_HOME, info.getHome());
        values.put(IDLE_PROFILE_COLUMN_HOBBY, info.getHobby());
        values.put(IDLE_PROFILE_COLUMN_IMAGE_HASH, info.getImageHash());

        return values;
    }

    private IdleProfileInfo createIdleProfileInfo(Cursor cursor) {
        IdleProfileInfo info = new IdleProfileInfo();
        int id = cursor.getInt(0);
        PSDebug.d("id=" + id);

        info.setName(cursor.getString(cursor.getColumnIndex(IDLE_PROFILE_COLUMN_NAME)));
        info.setKana(cursor.getString(cursor.getColumnIndex(IDLE_PROFILE_COLUMN_KANA)));
        info.setAgo(cursor.getInt(cursor.getColumnIndex(IDLE_PROFILE_COLUMN_AGO)));
        info.setHeight(cursor.getInt(cursor.getColumnIndex(IDLE_PROFILE_COLUMN_HEIGHT)));
        info.setWeight(cursor.getInt(cursor.getColumnIndex(IDLE_PROFILE_COLUMN_WEIGHT)));
        info.setBust(cursor.getInt(cursor.getColumnIndex(IDLE_PROFILE_COLUMN_BUST)));
        info.setWaist(cursor.getInt(cursor.getColumnIndex(IDLE_PROFILE_COLUMN_WAIST)));
        info.setHip(cursor.getInt(cursor.getColumnIndex(IDLE_PROFILE_COLUMN_HIP)));
        info.setBirthday(cursor.getString(cursor.getColumnIndex(IDLE_PROFILE_COLUMN_BIRTHDAY)));
        info.setConstellation(cursor.getString(cursor.getColumnIndex(IDLE_PROFILE_COLUMN_COLSTELLATION)));
        info.setBloodType(cursor.getString(cursor.getColumnIndex(IDLE_PROFILE_COLUMN_BLOODTYPE)));
        info.setHand(cursor.getString(cursor.getColumnIndex(IDLE_PROFILE_COLUMN_HAND)));
        info.setHome(cursor.getString(cursor.getColumnIndex(IDLE_PROFILE_COLUMN_HOME)));
        info.setHobby(cursor.getString(cursor.getColumnIndex(IDLE_PROFILE_COLUMN_HOBBY)));
        info.setImageHash(cursor.getString(cursor.getColumnIndex(IDLE_PROFILE_COLUMN_IMAGE_HASH)));

        return info;
    }
    
    public List<IdleProfileInfo> selectIdleProfileInfoSearchText(String text) {
        String select = SqlSelectHelper.createSelectIldeProfile(text);
        return selectIdleProfileInfo(select, null, null);
    }

    public List<IdleProfileInfo> selectIdleProfileInfoAll() {
        return selectIdleProfileInfo(null, null, null);
    }
    
    public List<IdleProfileInfo> selectIdleProfileInfo(String select, String order, String limit) {
        PSDebug.d("select=" + select);
        PSDebug.d("order=" + order);
        ArrayList<IdleProfileInfo> list = new ArrayList<IdleProfileInfo>();
        Cursor cursor = m_db.query(IDLE_PROFILE_TABLE_MAME, IDLE_PROFILE_COLUMNS, select, null, null, null, order, limit);
        while (cursor.moveToNext())
        {
            list.add(createIdleProfileInfo(cursor));
        }
        cursor.close();

        return list;
    }

}



