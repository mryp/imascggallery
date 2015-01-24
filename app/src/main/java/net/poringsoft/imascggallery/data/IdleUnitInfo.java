package net.poringsoft.imascggallery.data;

/**
 * アイドルユニット情報クラス
 * Created by mry on 15/01/21.
 */
public class IdleUnitInfo {

    //フィールド
    //---------------------------------------------------
    private String m_unitName;
    private String m_charName;

    //プロパティ
    //---------------------------------------------------
    /**
     * ユニット名 
     */
    public String getUnitName() {
        return m_unitName;
    }

    /**
     * ユニット名を設定する 
     */
    public void setUnitName(String unitName) {
        m_unitName = unitName;
    }

    /**
     * ユニットに所属するキャラクター名（複数の時はカンマ区切り） 
     */
    public String getCharName() {
        return m_charName;
    }

    /**
     * ユニットに所属するキャラクター名（複数の時はカンマ区切り）
     */
    public void setCharName(String charName) {
        m_charName = charName;
    }
    
    //メソッド
    //---------------------------------------
    /**
     * コンストラクタ
     */
    public IdleUnitInfo() {
    }
}
