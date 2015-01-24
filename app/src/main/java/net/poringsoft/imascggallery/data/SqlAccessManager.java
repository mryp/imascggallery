package net.poringsoft.imascggallery.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;
import java.util.List;

/**
 * DBアクセス管理クラス
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
    
    //アイドルカード情報関連
    //---------------------------------------------
    /**
     * アイドルカード情報をDBに保存する
     * 現在設定されているデータはすべて削除される
     * @param infoList アイドルカード情報一覧
     */
    public void insertIdleCardInfoList(List<IdleCardInfo> infoList)
    {
        SqlDao dao = open(OPEN_MODE_READWRITE);
        if (dao == null)
        {
            return;
        }

        dao.beginTransaction();
        try
        {
            //一旦全削除
            dao.deleteIdleCardInfoAll();
            
            //すべて追加
            for (IdleCardInfo info : infoList)
            {
                dao.insertIdleCardInfo(info);
            }

            dao.setTransactionSuccessful();
        }
        finally
        {
            dao.endTransaction();
            dao.Close();
        }
    }

    /**
     * アイドルカード情報を取得する
     * @param searchText 検索文字列
     * @return カード情報一覧
     */
    public List<IdleCardInfo> selectIdleCardInfo(String searchText)
    {
        SqlDao dao = open(OPEN_MODE_READONLY);
        if (dao == null)
        {
            return new ArrayList<>();
        }

        List<IdleCardInfo> infoList;
        dao.beginTransaction();
        try
        {
            infoList = dao.selectIdleInfoSearchText(searchText);
            dao.setTransactionSuccessful();
        }
        finally
        {
            dao.endTransaction();
            dao.Close();
        }

        return infoList;
    }


    //アイドルプロフィール関連
    //---------------------------------------------
    /**
     * アイドルプロフィール情報をDBに保存する
     * 現在保存されている情報はすべて削除される 
     * @param infoList プロフィール情報リスト
     */
    public void insertIdleProfileInfoList(List<IdleProfileInfo> infoList)
    {
        SqlDao dao = open(OPEN_MODE_READWRITE);
        if (dao == null)
        {
            return;
        }

        dao.beginTransaction();
        try
        {
            //一旦全削除
            dao.deleteIdleProfileInfoAll();

            //すべて追加
            for (IdleProfileInfo info : infoList)
            {
                dao.insertIdleProfileInfo(info);
            }

            dao.setTransactionSuccessful();
        }
        finally
        {
            dao.endTransaction();
            dao.Close();
        }
    }

    /**
     * プロフィール情報一覧を取得する
     * @param searchText 検索文字列
     * @return プロフィール情報一覧
     */
    public List<IdleProfileInfo> selectIdleProfileInfo(String searchText)
    {
        SqlDao dao = open(OPEN_MODE_READONLY);
        if (dao == null)
        {
            return new ArrayList<>();
        }

        List<IdleProfileInfo> infoList;
        dao.beginTransaction();
        try
        {
            int sortType = EnvOption.getMainListSortType(m_context);
            infoList = dao.selectIdleProfileInfoSearchText(searchText, sortType);
            dao.setTransactionSuccessful();
        }
        finally
        {
            dao.endTransaction();
            dao.Close();
        }

        return infoList;
    }

    //アイドルユニット関連
    //---------------------------------------------
    /**
     * アイドルユニット情報をDBに保存する
     * 現在保存されている情報はすべて削除される 
     * @param infoList アイドルユニット情報一覧
     */
    public void insertIdleUnitInfoList(List<IdleUnitInfo> infoList)
    {
        SqlDao dao = open(OPEN_MODE_READWRITE);
        if (dao == null)
        {
            return;
        }

        dao.beginTransaction();
        try
        {
            //一旦全削除
            dao.deleteIdleUnitInfoAll();

            //すべて追加
            for (IdleUnitInfo info : infoList)
            {
                dao.insertIdleUnitInfo(info);
            }

            dao.setTransactionSuccessful();
        }
        finally
        {
            dao.endTransaction();
            dao.Close();
        }
    }

    /**
     * アイドルユニット一覧情報を取得する
     * @return ユニット一覧情報
     */
    public List<IdleUnitInfo> selectIdleUnitInfo()
    {
        SqlDao dao = open(OPEN_MODE_READONLY);
        if (dao == null)
        {
            return new ArrayList<>();
        }

        List<IdleUnitInfo> infoList;
        dao.beginTransaction();
        try
        {
            infoList = dao.selectIdleUnitInfoAll();
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
