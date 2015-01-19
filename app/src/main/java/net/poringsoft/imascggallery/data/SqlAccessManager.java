package net.poringsoft.imascggallery.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by mry on 15/01/18.
 */
public class SqlAccessManager {
    //定数
    //-------------------------------------------------------
    public static final int OPEN_MODE_READONLY = 0;			//読み取り専用
    public static final int OPEN_MODE_READWRITE = 1;		//読み書き

    //読み込み時リトライ回数
    private static final int RETRY_SQL_CALL = 10;

    //フィールド
    //-------------------------------------------------------
    private Context m_context = null;

    //メソッド
    //-------------------------------------------------------
    /**
     * コンストラクタ
     */
    public SqlAccessManager(Context context)
    {
        this.m_context = context;
    }


    //ユーティリティメソッド
    //-------------------------------------------------------
    /**
     * DBオープン
     * @param mode オープンモード（OPEN_MODE_～）
     * @return オープンしたDB（失敗した場合はnul）
     */
    private SqlDao open(int mode)
    {
        SqlAccessHelper helper = new SqlAccessHelper(m_context);
        SQLiteDatabase db = null;
        switch (mode)
        {
            case OPEN_MODE_READONLY:
                db = getReadableDatabase(helper, RETRY_SQL_CALL);
                break;
            case OPEN_MODE_READWRITE:
                db = getWritableDatabase(helper, RETRY_SQL_CALL);
                break;
        }

        if (db == null)
        {
            return null;
        }
        return new SqlDao(db);
    }

    /**
     * 読み込み用DBを生成する
     * @param helper DBヘルパー
     * @param retry リトライ数
     * @return オープンした読み込み用DB
     */
    private SQLiteDatabase getReadableDatabase(SqlAccessHelper helper, int retry)
    {
        SQLiteDatabase db;
        try
        {
            db = helper.getReadableDatabase();
        }
        catch (SQLiteException e)
        {
            if (retry == 0)
            {
                return null;	//あきらめる
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            return getReadableDatabase(helper, retry - 1);
        }

        return db;
    }

    /**
     * 書き込み用DBを生成する
     * @param helper DBヘルパー
     * @param retry リトライ回数
     * @return オープンした書き込み可能DB
     */
    private SQLiteDatabase getWritableDatabase(SqlAccessHelper helper, int retry)
    {
        SQLiteDatabase db;
        try
        {
            db = helper.getWritableDatabase();
        }
        catch (SQLiteException e)
        {
            if (retry == 0)
            {
                return null;	//あきらめる
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            return getWritableDatabase(helper, retry - 1);
        }

        return db;
    }
    
    //アイドル情報関連
    //---------------------------------------------

    public List<IdleCardInfo> selectIdleInfo(String searchText)
    {
        SqlDao dao = open(OPEN_MODE_READONLY);
        if (dao == null)
        {
            return new ArrayList<IdleCardInfo>();
        }

        List<IdleCardInfo> infoList;
        dao.beginTransaction();
        try
        {
            infoList = new ArrayList<>();

            //TODO: とりあえずテストのため適当に追加
            infoList.add(new IdleCardInfo());
            infoList.add(new IdleCardInfo());
            infoList.add(new IdleCardInfo());
            infoList.add(new IdleCardInfo());
            infoList.add(new IdleCardInfo());

            dao.setTransactionSuccessful();
        }
        finally
        {
            dao.endTransaction();
            dao.Close();
        }

        return infoList;
    }
    
    
}
