package net.poringsoft.imascggallery.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * DBアクセス用ヘルパークラス
 */
public class SqlAccessHelper extends SQLiteOpenHelper {
    //定数
    //-------------------------------------------------------
    public static final String DATABASE_NAME = "cgg.db";	//データベース名
    public static final int DATABASE_VERSION = 1;			//テーブルバージョン（更新時は数値を1つあげる）

    //テーブル定義
    //-------------------------------------------------------
    /**
     * お気に入りテーブル
     */
    private static final String CREATE_SQL_FAVORITE_DATA = "create table favorite"
            + "("
            + "rowid integer primary key autoincrement, "
            + "m_name text,"
            + "m_index integer,"
            + "m_uptime integer"
            + ")";

    /**
     * アイドルカード情報データ
     */
    private static final String CREATE_SQL_IDLE_CARD_DATA = "create table idlecardtbl"
            + "("
            + "rowid integer primary key autoincrement, "
            + "m_albumId integer,"
            + "m_attribute text,"
            + "m_rarity text,"
            + "m_namePrefix text,"
            + "m_name text,"
            + "m_namePost text,"
            + "m_cost integer,"
            + "m_attack integer,"
            + "m_defense integer,"
            + "m_maxAttack integer,"
            + "m_maxDefense integer,"
            + "m_maxConfirmed text,"
            + "m_attackCospa real,"
            + "m_defenseCospa real,"
            + "m_skillName text,"
            + "m_targetAttr text,"
            + "m_attdefType text,"
            + "m_skillEffect text,"
            + "m_remarks text,"
            + "m_imageHash text"
            + ")";

    /**
     * アイドルプロフィール情報
     */
    private static final String CREATE_SQL_IDLE_PROFILE_DATA = "create table idleprofiletbl"
            + "("
            + "rowid integer primary key autoincrement, "
            + "m_name text,"
            + "m_kana text,"
            + "m_ago integer,"
            + "m_height integer,"
            + "m_weight integer,"
            + "m_bust integer,"
            + "m_waist integer,"
            + "m_hip integer,"
            + "m_birthday text,"
            + "m_constellation text,"
            + "m_bloodType text,"
            + "m_hand text,"
            + "m_home text,"
            + "m_hobby text,"
            + "m_imageHash text"
            + ")";

    /**
     * ユニット情報
     */
    private static final String CREATE_SQL_IDLE_UNIT_DATA = "create table idleunittbl"
            + "("
            + "rowid integer primary key autoincrement, "
            + "m_unitName text,"
            + "m_charName text"
            + ")";

    //メソッド
    //-------------------------------------------------------
    /**
     * コンストラクタ
     */
    public SqlAccessHelper(Context context)
    {
        super(context, EnvPath.getRootDirPath() + DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * DBテーブル作成メソッド
     */
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.beginTransaction();
        try
        {
            createTableVer1(db);
            db.setTransactionSuccessful();
        }
        finally
        {
            db.endTransaction();
        }
    }

    /**
     * version 1のテーブルを作成する
     * @param db DB
     */
    private void createTableVer1(SQLiteDatabase db)
    {
        db.execSQL(CREATE_SQL_FAVORITE_DATA);
        db.execSQL(CREATE_SQL_IDLE_CARD_DATA);
        db.execSQL(CREATE_SQL_IDLE_PROFILE_DATA);
        db.execSQL(CREATE_SQL_IDLE_UNIT_DATA);
    }

    /**
     * DBテーブル更新メソッド
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.beginTransaction();
        try
        {
            /*
            if (oldVersion == 1)
            {
                createTableVer2(db);
            }
            */
            db.setTransactionSuccessful();
        }
        finally
        {
            db.endTransaction();
        }
    }
}
