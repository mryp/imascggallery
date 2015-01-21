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

    /**
     * ユニット名 
     * @return
     */
    public String getUnitName() {
        return m_unitName;
    }

    /**
     * ユニット名を設定する 
     * @param unitName
     */
    public void setUnitName(String unitName) {
        m_unitName = unitName;
    }

    /**
     * ユニットに所属するキャラクター名（複数の時はカンマ区切り） 
     * @return
     */
    public String getCharName() {
        return m_charName;
    }

    /**
     * ユニットに所属するキャラクター名（複数の時はカンマ区切り）
     * @param charName
     */
    public void setCharName(String charName) {
        m_charName = charName;
    }
    
    //メソッド
    //---------------------------------------
    public IdleUnitInfo() {
    }
}
