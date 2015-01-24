package net.poringsoft.imascggallery.data;

/**
 * アイドルプロフィール情報クラス
 * 名前,フリガナ,年齢,身長,体重,バスト,ウェスト,ヒップ,誕生日,星座,血液型,利き手,出身地,趣味
 * 例： 島村卯月,しまむらうづき,17,159,45,83,59,87,4月24日,牡牛座,O型,右,東京,友達と長電話
 * Created by mry on 15/01/20.
 */
public class IdleProfileInfo {
    //フィールド
    //---------------------------------------------------
    private String m_name;
    private String m_kana;
    private int m_age;
    private int m_height;
    private int m_weight;
    private int m_bust;
    private int m_waist;
    private int m_hip;
    private String m_birthday;          //誕生日
    private String m_constellation;     //星座
    private String m_bloodType;         //血液型
    private String m_hand;              //利き手
    private String m_home;              //出身地
    private String m_hobby;             //趣味
    private String m_imageHash = "";

    //プロパティ
    //---------------------------------------------------
    /**
     * 名前
     */
    public String getName() {
        return m_name;
    }

    /**
     * 名前 
     */
    public void setName(String name) {
        this.m_name = name;
    }

    /**
     * 読み仮名 
     */
    public String getKana() {
        return m_kana;
    }

    /**
     * 読み仮名 
     */
    public void setKana(String kana) {
        this.m_kana = kana;
    }

    /**
     * 年齢 
     */
    public int getAge() {
        return m_age;
    }

    /**
     * 年齢 
     */
    public void setAge(int age) {
        this.m_age = age;
    }

    /**
     * 身長 
     */
    public int getHeight() {
        return m_height;
    }

    /**
     * 身長 
     */
    public void setHeight(int height) {
        this.m_height = height;
    }

    /**
     * 体重 
     */
    public int getWeight() {
        return m_weight;
    }

    /**
     * 体重 
     */
    public void setWeight(int weight) {
        this.m_weight = weight;
    }

    /**
     * バスト 
     */
    public int getBust() {
        return m_bust;
    }

    /**
     * バスト 
     */
    public void setBust(int bust) {
        this.m_bust = bust;
    }

    /**
     * ウエスト 
     */
    public int getWaist() {
        return m_waist;
    }

    /**
     * ウエスト 
     */
    public void setWaist(int waist) {
        this.m_waist = waist;
    }

    /**
     * ヒップ 
     */
    public int getHip() {
        return m_hip;
    }

    /**
     * ヒップ 
     */
    public void setHip(int hip) {
        this.m_hip = hip;
    }

    /**
     * 誕生日 
     */
    public String getBirthday() {
        return m_birthday;
    }

    /**
     * 誕生日 
     */
    public void setBirthday(String birthday) {
        this.m_birthday = birthday;
    }

    /**
     * 星座 
     */
    public String getConstellation() {
        return m_constellation;
    }

    /**
     * 星座 
     */
    public void setConstellation(String constellation) {
        this.m_constellation = constellation;
    }

    /**
     * 血液型 
     */
    public String getBloodType() {
        return m_bloodType;
    }

    /**
     * 血液型 
     */
    public void setBloodType(String bloodType) {
        this.m_bloodType = bloodType;
    }

    /**
     * 利き手 
     */
    public String getHand() {
        return m_hand;
    }

    /**
     * 利き手 
     */
    public void setHand(String hand) {
        this.m_hand = hand;
    }

    /**
     * 出身地 
     */
    public String getHome() {
        return m_home;
    }

    /**
     * 出身地 
     */
    public void setHome(String home) {
        this.m_home = home;
    }

    /**
     * 趣味 
     */
    public String getHobby() {
        return m_hobby;
    }

    /**
     * 趣味 
     */
    public void setHobby(String hobby) {
        this.m_hobby = hobby;
    }

    /**
     * 画像ハッシュ値 
     */
    public String getImageHash() {
        return m_imageHash;
    }

    /**
     * 画像ハッシュ値
     */
    public void setImageHash(String imageHash) {
        this.m_imageHash = imageHash;
    }
    
    
    //メソッド
    //-------------------------------------------
    /**
     * コンストラクタ
     */
    public IdleProfileInfo() {

    }

    /**
     * デバッグ用文字列
     * @return デバッグ文字列
     */
    public String toDebugString() {
        return "name=" + m_name + "(" + m_kana + ")" + " hash=" + m_imageHash;
    }
}
