package net.poringsoft.imascggallery.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

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
}



