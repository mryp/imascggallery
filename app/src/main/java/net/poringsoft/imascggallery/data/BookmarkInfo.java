package net.poringsoft.imascggallery.data;

import java.util.Date;

/**
 * アイドルブックマーク情報
 */
public class BookmarkInfo {
    //フィールド
    //---------------------------------------------------
    private String m_name;
    private int m_index;
    private long m_uptime;


    //プロパティ
    //---------------------------------------------------
    /**
     * アイドル名
     */
    public String getName() {
        return m_name;
    }

    /**
     * アイドル名
     */
    public void setName(String name) {
        m_name = name;
    }

    /**
     * 更新日時
     */
    public long getUptime() {
        return m_uptime;
    }

    /**
     * 更新日時
     */
    public void setUptime(long uptime) {
        m_uptime = uptime;
    }

    /**
     * 並び順
     */
    public int getIndex() {
        return m_index;
    }

    /**
     * 並び順
     */
    public void setIndex(int index) {
        m_index = index;
    }


    //メソッド
    //---------------------------------------
    /**
     * コンストラクタ
     */
    public BookmarkInfo() {
    }

    /**
     * コンストラクタ
     * @param name アイドル名
     */
    public BookmarkInfo(String name) {
        m_name = name;
        m_index = 0;
        m_uptime = new Date().getTime();
    }
}
