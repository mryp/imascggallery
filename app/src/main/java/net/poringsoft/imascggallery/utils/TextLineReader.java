package net.poringsoft.imascggallery.utils;

import net.poringsoft.imascggallery.data.IdleUnitInfo;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * テキスト行読み込みクラス
 * Created by mry o 15/01/24.
 */
public class TextLineReader {
    /**
     * テキストファイルから行ごとに読み込みデータをイベントとして返す
     * @param filePath ファイルパス(UTF-8ファイル)
     * @param readLineListener 行読み込みイベント
     * @return すべて読み込み完了した時はtrue
     */
    public boolean readLine(String filePath, IReadLineListener readLineListener) {
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferReader = null;
        boolean result = false;
        try {
            inputStream = new FileInputStream(filePath);
            inputStreamReader = new InputStreamReader(inputStream);
            bufferReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferReader.readLine()) != null) {
                readLineListener.onReadLine(line);
            }
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
                if (bufferReader != null) {
                    bufferReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }
}
