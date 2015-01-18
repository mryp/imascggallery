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
