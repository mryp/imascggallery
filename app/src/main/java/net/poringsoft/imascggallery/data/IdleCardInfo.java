package net.poringsoft.imascggallery.data;

/**
 * アイドル情報・プロフィール保持クラス
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
     * @return
     */
    public int getAlbumId() {
        return m_albumId;
    }

    /**
     * 属性（例：Cu）
     * @return
     */
    public String getAttribute() {
        return m_attribute;
    }

    /**
     * レア（例：SR+） 
     * @return
     */
    public String getRarity() {
        return m_rarity;
    }

    /**
     * 称号（例：[ﾊﾛｳｨﾝぷちﾃﾞﾋﾞﾙ]）
     * @return
     */
    public String getNamePrefix() {
        return m_namePrefix;
    }

    /**
     * 名前（例：双葉杏） 
     * @return
     */
    public String getName() {
        return m_name;
    }

    /**
     * +付きかどうか（から文字　or +） 
     * @return
     */
    public String getNamePost() {
        return m_namePost;
    }

    /**
     * コスト（例：17）
     * @return
     */
    public int getCost() {
        return m_cost;
    }

    /**
     * アルバム攻（例：5280） 
     * @return
     */
    public int getAttack() {
        return m_attack;
    }

    /**
     * アルバム守（例：3840） 
     * @return
     */
    public int getDefense() {
        return m_defense;
    }

    /**
     * MAX攻（例：17490） 
     * @return
     */
    public int getMaxAttack() {
        return m_maxAttack;
    }

    /**
     * MAX守（例：12720）
     * @return
     */
    public int getMaxDefense() {
        return m_maxDefense;
    }

    /**
     * MAX確認（例：○） 
     * @return
     */
    public String getMaxConfirmed() {
        return m_maxConfirmed;
    }

    /**
     * MAX攻/コスト（例：1,028.8）
     * @return
     */
    public double getAttackCospa() {
        return m_attackCospa;
    }

    /**
     * MAX守/コスト（例：748.2） 
     * @return
     */
    public double getDefenseCospa() {
        return m_defenseCospa;
    }

    /**
     * 特技（例：飴くれ） 
     * @return
     */
    public String getSkillName() {
        return m_skillName;
    }

    /**
     * 対象属性（例：キュート） 
     * @return
     */
    public String getTargetAttr() {
        return m_targetAttr;
    }

    /**
     * 攻守（例：攻） 
     * @return
     */
    public String getAttdefType() {
        return m_attdefType;
    }

    /**
     * 効果（例：特大～極大アップ） 
     * @return
     */
    public String getSkillEffect() {
        return m_skillEffect;
    }

    /**
     * 備考（例：「ﾊﾛｳｨﾝﾊﾟｰﾃｨｰ」ｶﾞﾁｬ） 
     * @return
     */
    public String getRemarks() {
        return m_remarks;
    }

    /**
     * 画像ハッシュ値
     * @return
     */
    public String getImageHash() {
        return m_imageHash;
    }


    public void setAlbumId(int albumId) {
        m_albumId = albumId;
    }

    public void setAttribute(String attribute) {
        m_attribute = attribute;
    }

    public void setRarity(String rarity) {
        m_rarity = rarity;
    }

    public void setNamePrefix(String namePrefix) {
        m_namePrefix = namePrefix;
    }

    public void setName(String name) {
        m_name = name;
    }

    public void setNamePost(String namePost) {
        m_namePost = namePost;
    }

    public void setCost(int cost) {
        m_cost = cost;
    }

    public void setAttack(int attack) {
        m_attack = attack;
    }

    public void setDefense(int defense) {
        m_defense = defense;
    }

    public void setMaxAttack(int maxAttack) {
        m_maxAttack = maxAttack;
    }

    public void setMaxDefense(int maxDefense) {
        m_maxDefense = maxDefense;
    }

    public void setMaxConfirmed(String maxConfirmed) {
        m_maxConfirmed = maxConfirmed;
    }

    public void setAttackCospa(double attackCospa) {
        m_attackCospa = attackCospa;
    }

    public void setDefenseCospa(double defenseCospa) {
        m_defenseCospa = defenseCospa;
    }

    public void setSkillName(String skillName) {
        m_skillName = skillName;
    }

    public void setTargetAttr(String targetAttr) {
        m_targetAttr = targetAttr;
    }

    public void setAttdefType(String attdefType) {
        m_attdefType = attdefType;
    }

    public void setSkillEffect(String skillEffect) {
        m_skillEffect = skillEffect;
    }

    public void setRemarks(String remarks) {
        m_remarks = remarks;
    }

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
    
    public String toDebugString() {
        return "id=" + m_albumId + " hash=" + m_imageHash + " name=" + m_namePrefix + m_name + m_namePost;
    }
    
    public int getColor() {
        return 0xFFF04228;
    }
    
    public String getCardUrl() {
        return "";
    }
    
    public String getIconUrl() {
        //TODO: ハッシュキーは仮
        return EnvPath.getIdleIconImageUrl("519e953aad9d655e2d34f86a8d0ec5b8");
    }
}
