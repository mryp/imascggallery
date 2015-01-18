package net.poringsoft.imascggallery;

/**
 * ナビゲーションのヘッダ部データ
 */
public class NaviSectionHeaderData {
    //フィールド
    //------------------------------------------
    private String m_title;

    /**
     * タイトル部文字列
     * @return タイトル
     */
    public String getTitle()
    {
        return m_title;
    }

    /**
     * コンストラクタ
     * @param title タイトル
     */
    public NaviSectionHeaderData(String title) {
        this.m_title = title;
    }
}
