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
    private int m_ago;
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
    public String getName() {
        return m_name;
    }

    public void setName(String name) {
        this.m_name = name;
    }

    public String getKana() {
        return m_kana;
    }

    public void setKana(String kana) {
        this.m_kana = kana;
    }

    public int getAgo() {
        return m_ago;
    }

    public void setAgo(int ago) {
        this.m_ago = ago;
    }

    public int getHeight() {
        return m_height;
    }

    public void setHeight(int height) {
        this.m_height = height;
    }

    public int getWeight() {
        return m_weight;
    }

    public void setWeight(int weight) {
        this.m_weight = weight;
    }

    public int getBust() {
        return m_bust;
    }

    public void setBust(int bust) {
        this.m_bust = bust;
    }

    public int getWaist() {
        return m_waist;
    }

    public void setWaist(int waist) {
        this.m_waist = waist;
    }

    public int getHip() {
        return m_hip;
    }

    public void setHip(int hip) {
        this.m_hip = hip;
    }

    public String getBirthday() {
        return m_birthday;
    }

    public void setBirthday(String birthday) {
        this.m_birthday = birthday;
    }

    public String getConstellation() {
        return m_constellation;
    }

    public void setConstellation(String constellation) {
        this.m_constellation = constellation;
    }

    public String getBloodType() {
        return m_bloodType;
    }

    public void setBloodType(String bloodType) {
        this.m_bloodType = bloodType;
    }

    public String getHand() {
        return m_hand;
    }

    public void setHand(String hand) {
        this.m_hand = hand;
    }

    public String getHome() {
        return m_home;
    }

    public void setHome(String home) {
        this.m_home = home;
    }

    public String getHobby() {
        return m_hobby;
    }

    public void setHobby(String hobby) {
        this.m_hobby = hobby;
    }

    public String getImageHash() {
        return m_imageHash;
    }

    public void setImageHash(String imageHash) {
        this.m_imageHash = imageHash;
    }
    
    
    //メソッド
    //-------------------------------------------
    public IdleProfileInfo() {

    }
    
    public String toDebugString() {
        return "name=" + m_name + "(" + m_kana + ")" + " hash=" + m_imageHash;
    }
}
