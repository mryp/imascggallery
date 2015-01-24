package net.poringsoft.imascggallery.utils;

/**
 * テキスト行読み込みイベント 
 * Created by mry on 15/01/24.
 */
public interface IReadLineListener {
    /**
     * 読み込んだ行を渡す
     * @param lineText 読み込んだ行文字列
     */
    void onReadLine(String lineText);
}
