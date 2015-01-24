package net.poringsoft.imascggallery.data;

/**
 * アイドルカード情報クラス
 * Created by mry on 15/01/18.
 */
public class IdleCardInfo {
    //フィールド
    //---------------------------------------------------
    private int m_albumId = 0;
    private String m_attribute = "";
    private String m_rarity = "";
    private String m_namePrefix = "";
    private String m_name = "";
    private String m_namePost = "";
    private int m_cost = 0;
    private int m_attack = 0;
    private int m_defense = 0;
    private int m_maxAttack = 0;
    private int m_maxDefense = 0;
    private String m_maxConfirmed = "";
    private double m_attackCospa = 0;
    private double m_defenseCospa = 0;
    private String m_skillName = "";
    private String m_targetAttr = "";
    private String m_attdefType = "";
    private String m_skillEffect = "";
    private String m_remarks = "";
    private String m_imageHash = "";


    //プロパティ
    //---------------------------------------------------
    /**
     * アルバムID（例：1503002）
     */
    public int getAlbumId() {
        return m_albumId;
    }

    /**
     * 属性（例：Cu）
     */
    public String getAttribute() {
        return m_attribute;
    }

    /**
     * レア（例：SR+）
     */
    public String getRarity() {
        return m_rarity;
    }

    /**
     * 称号（例：[ﾊﾛｳｨﾝぷちﾃﾞﾋﾞﾙ]）
     */
    public String getNamePrefix() {
        return m_namePrefix;
    }

    /**
     * 名前（例：双葉杏） 
     */
    public String getName() {
        return m_name;
    }

    /**
     * +付きかどうか（から文字　or +） 
     */
    public String getNamePost() {
        return m_namePost;
    }

    /**
     * コスト（例：17）
     */
    public int getCost() {
        return m_cost;
    }

    /**
     * アルバム攻（例：5280） 
     */
    public int getAttack() {
        return m_attack;
    }

    /**
     * アルバム守（例：3840） 
     */
    public int getDefense() {
        return m_defense;
    }

    /**
     * MAX攻（例：17490） 
     */
    public int getMaxAttack() {
        return m_maxAttack;
    }

    /**
     * MAX守（例：12720）
     */
    public int getMaxDefense() {
        return m_maxDefense;
    }

    /**
     * MAX確認（例：○）
     */
    public String getMaxConfirmed() {
        return m_maxConfirmed;
    }

    /**
     * MAX攻/コスト（例：1,028.8）
     */
    public double getAttackCospa() {
        return m_attackCospa;
    }

    /**
     * MAX守/コスト（例：748.2）
     */
    public double getDefenseCospa() {
        return m_defenseCospa;
    }

    /**
     * 特技（例：飴くれ）
     */
    public String getSkillName() {
        return m_skillName;
    }

    /**
     * 対象属性（例：キュート）
     */
    public String getTargetAttr() {
        return m_targetAttr;
    }

    /**
     * 攻守（例：攻）
     */
    public String getAttdefType() {
        return m_attdefType;
    }

    /**
     * 効果（例：特大～極大アップ）
     */
    public String getSkillEffect() {
        return m_skillEffect;
    }

    /**
     * 備考（例：「ﾊﾛｳｨﾝﾊﾟｰﾃｨｰ」ｶﾞﾁｬ）
     */
    public String getRemarks() {
        return m_remarks;
    }

    /**
     * 画像ハッシュ値
     */
    public String getImageHash() {
        return m_imageHash;
    }

    /**
     * アルバムID
     */
    public void setAlbumId(int albumId) {
        m_albumId = albumId;
    }

    /**
     * 属性
     */
    public void setAttribute(String attribute) {
        m_attribute = attribute;
    }

    /**
     * レア
     */
    public void setRarity(String rarity) {
        m_rarity = rarity;
    }

    /**
     * 称号
     */
    public void setNamePrefix(String namePrefix) {
        m_namePrefix = namePrefix;
    }

    /**
     * 名前 
     */
    public void setName(String name) {
        m_name = name;
    }

    /**
     * ＋付きかどうか 
     */
    public void setNamePost(String namePost) {
        m_namePost = namePost;
    }

    /**
     * コスト 
     */
    public void setCost(int cost) {
        m_cost = cost;
    }

    /**
     * アルバム攻 
     */
    public void setAttack(int attack) {
        m_attack = attack;
    }

    /**
     * アルバム守 
     */
    public void setDefense(int defense) {
        m_defense = defense;
    }

    /**
     * MAX攻 
     */
    public void setMaxAttack(int maxAttack) {
        m_maxAttack = maxAttack;
    }

    /**
     * MAX守 
     */
    public void setMaxDefense(int maxDefense) {
        m_maxDefense = maxDefense;
    }

    /**
     * MAX確認 
     */
    public void setMaxConfirmed(String maxConfirmed) {
        m_maxConfirmed = maxConfirmed;
    }

    /**
     * MAX攻/コスト 
     */
    public void setAttackCospa(double attackCospa) {
        m_attackCospa = attackCospa;
    }

    /**
     * MAX守/コスト
     */
    public void setDefenseCospa(double defenseCospa) {
        m_defenseCospa = defenseCospa;
    }

    /**
     * 特技 
     */
    public void setSkillName(String skillName) {
        m_skillName = skillName;
    }

    /**
     * 対象属性
     */
    public void setTargetAttr(String targetAttr) {
        m_targetAttr = targetAttr;
    }

    /**
     * 攻守タイプ
     */
    public void setAttdefType(String attdefType) {
        m_attdefType = attdefType;
    }

    /**
     * 効果 
     */
    public void setSkillEffect(String skillEffect) {
        m_skillEffect = skillEffect;
    }

    /**
     * 備考 
     */
    public void setRemarks(String remarks) {
        m_remarks = remarks;
    }

    /**
     * 画像ハッシュ値 
     */
    public void setImageHash(String imageHash) {
        m_imageHash = imageHash;
    }
    
    //メソッド
    //---------------------------------------------------
    /**
     * コンストラクタ 
     */
    public IdleCardInfo() {
    }

    /**
     * デバッグ用表示文字列
     * @return デバッグ用文字列
     */
    public String toDebugString() {
        return "id=" + m_albumId + " hash=" + m_imageHash + " name=" + m_namePrefix + m_name + m_namePost;
    }
}
