package net.poringsoft.imascggallery.data;

/**
 * アイドル情報・プロフィール保持クラス
 * Created by mry on 15/01/18.
 */
public class IdleInfo {
    
    public String getName() {
        return "名前";
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
