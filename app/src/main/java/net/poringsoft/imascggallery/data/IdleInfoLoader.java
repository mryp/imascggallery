package net.poringsoft.imascggallery.data;

import android.content.Context;

import net.poringsoft.imascggallery.utils.IReadLineListener;
import net.poringsoft.imascggallery.utils.KanamojiCharUtils;
import net.poringsoft.imascggallery.utils.PSDebug;
import net.poringsoft.imascggallery.utils.PSUtils;
import net.poringsoft.imascggallery.utils.TextLineReader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * アイドル情報ファイルからデータの読み込みを行うクラス
 * Created by mry on 15/01/19.
 */
public class IdleInfoLoader {
    //フィールド
    //----------------------------------------------------
    private Context m_context;
    
    //メソッド
    //----------------------------------------------------
    /**
     * コンストラクタ
     * @param context コンテキスト
     */
    public IdleInfoLoader(Context context) {
        m_context = context;
    }

    /**
     * 各種ファイルからデータを読み込む
     * @param mainCsvPath アイドルカード情報ファイル
     * @param profileCsvPath プロフィール情報ファイル
     * @param hashJsonPath カード画像ハッシュ値ファイル
     * @param unitListPath ユニット一覧ファイル（必須ではない）
     * @return 正常時はtrue
     */
    public boolean loadFile(String mainCsvPath, String profileCsvPath, String hashJsonPath, String unitListPath) {
        Map<Integer, String> hashTable = readImageHashList(hashJsonPath);
        if (hashTable == null || hashTable.size() == 0) {
            return false;
        }

        ArrayList<IdleCardInfo> infoList = readIdleCardList(mainCsvPath, hashTable);
        if (infoList == null || infoList.size() == 0) {
            return false;
        }
        
        ArrayList<IdleProfileInfo> profileList = readIdleProfileList(profileCsvPath, infoList);
        if (profileList == null || profileList.size() == 0) {
            return false;
        }

        SqlAccessManager sqlManager = new SqlAccessManager(m_context);
        sqlManager.insertIdleCardInfoList(infoList);
        sqlManager.insertIdleProfileInfoList(profileList);

        //ユニット一覧はオプション
        ArrayList<IdleUnitInfo> unitList = readUnitList(unitListPath);
        if (unitList != null && unitList.size() > 0) {
            sqlManager.insertIdleUnitInfoList(unitList);
        }
        return true;
    }

    /**
     * カードハッシュ値ファイルからアルバムID・ハッシュテーブルを作成して返す 
     * @param filePath ハッシュ値ファイル
     * @return key=アルバムID,value=ハッシュ値のテーブルデータ
     */
    private Map<Integer, String> readImageHashList(String filePath) {
        Map<Integer, String> infoTable = new HashMap<>();
        JSONObject hashJson = readHashJson(filePath);
        if (hashJson == null) {
            return infoTable;
        }
        
        Iterator<String> iter = hashJson.keys();
        while (iter.hasNext()) {
            String albumId = iter.next();
            String hashText;
            try {
                hashText = hashJson.getJSONObject(albumId).getString("hash");
            } catch (JSONException e) {
                hashText = "";
            }

            Integer albumIdInteger = PSUtils.tryParseInt(albumId, 0);
            if (!infoTable.containsKey(albumIdInteger)) {
                infoTable.put(albumIdInteger, hashText);
            }
        }
        
        return infoTable;
    }

    /**
     * ファイルからJSONオブジェクトを作成して返す 
     * @param filePath テキストファイル
     * @return JSONオブジェクト
     */
    private JSONObject readHashJson(String filePath) {
        JSONObject resultObject = null;
        InputStream input = null;
        try {
            input = new FileInputStream(filePath);
            int size = input.available();
            byte[] buffer = new byte[size];
            input.read(buffer);

            // Json読み込み
            String json = new String(buffer);
            resultObject = new JSONObject(json);
        }
        catch (Exception e) {
            resultObject = null;
        }
        finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return resultObject;
    }

    /**
     * アイドルカード情報をファイルから読み込んで返す 
     * @param filePath アイドルカード情報ファイル
     * @param hashTable 画像ハッシュ値テーブル
     * @return アイドルカード情報リスト
     */
    private ArrayList<IdleCardInfo> readIdleCardList(String filePath, final Map<Integer, String> hashTable) {
        final ArrayList<IdleCardInfo> infoList = new ArrayList<>();
        TextLineReader reader = new TextLineReader();
        boolean ret = reader.readLine(filePath, new IReadLineListener() {
            @Override
            public void onReadLine(String lineText) {
                String[] items = (lineText + ", ").split(",");  //備考がないデータのために1つ多めに追加する
                if (items.length < 20) {
                    return;
                }
                int albumId = PSUtils.tryParseInt(items[1], 0);
                if (albumId == 0) {
                    return;
                }

                IdleCardInfo info = new IdleCardInfo();
                info.setAlbumId(PSUtils.tryParseInt(items[1], 0));
                info.setAttribute(items[2]);
                info.setRarity(items[3]);
                info.setNamePrefix(items[4]);
                info.setName(KanamojiCharUtils.hankakuKatakanaToZenkakuKatakana(items[5]));
                info.setNamePost(items[6]);

                info.setCost(PSUtils.tryParseInt(items[7], 0));
                info.setAttack(PSUtils.tryParseInt(items[8], 0));
                info.setDefense(PSUtils.tryParseInt(items[9], 0));
                info.setMaxAttack(PSUtils.tryParseInt(items[10], 0));
                info.setMaxDefense(PSUtils.tryParseInt(items[11], 0));

                info.setMaxConfirmed(items[12]);
                info.setAttackCospa(PSUtils.tryParseDouble(items[13], 0.0));
                info.setDefenseCospa(PSUtils.tryParseDouble(items[14], 0.0));

                info.setSkillName(items[15]);
                info.setTargetAttr(items[16]);
                info.setAttdefType(items[17]);
                info.setSkillEffect(items[18]);
                info.setRemarks(items[19]);

                if (hashTable.containsKey(info.getAlbumId())) {
                    info.setImageHash(hashTable.get(info.getAlbumId()));
                }

                PSDebug.d(info.toDebugString());
                infoList.add(info);
            }
        });
        
        if (!ret) {
            return null;
        }
        return infoList;
    }

    /**
     * アイドルプロフィール情報をファイルから読み込んで返す 
     * @param filePath プロフィールファイル
     * @param idleCardList アイドルカード情報リスト
     * @return プロフィール情報リスト
     */
    private ArrayList<IdleProfileInfo> readIdleProfileList(String filePath, final ArrayList<IdleCardInfo> idleCardList) {
        final ArrayList<IdleProfileInfo> profileList = new ArrayList<>();
        TextLineReader reader = new TextLineReader();
        boolean ret = reader.readLine(filePath, new IReadLineListener() {
            @Override
            public void onReadLine(String lineText) {
                String[] items = lineText.split(",");
                if (items.length < 14) {
                    return;
                }
                if (items[0].equals("名前")) {
                    return;   //列名説明なので省略
                }

                IdleProfileInfo info = new IdleProfileInfo();

                info.setName(KanamojiCharUtils.hankakuKatakanaToZenkakuKatakana(items[0]));
                info.setKana(items[1]);
                info.setAge(PSUtils.tryParseInt(items[2], 0));
                info.setHeight(PSUtils.tryParseInt(items[3], 0));
                info.setWeight(PSUtils.tryParseInt(items[4], 0));
                info.setBust(PSUtils.tryParseInt(items[5], 0));
                info.setWaist(PSUtils.tryParseInt(items[6], 0));
                info.setHip(PSUtils.tryParseInt(items[7], 0));
                info.setBirthday(items[8]);
                info.setConstellation(items[9]);
                info.setBloodType(items[10]);
                info.setHand(items[11]);
                info.setHome(items[12]);
                info.setHobby(items[13]);
                for (IdleCardInfo cardInfo : idleCardList) {
                    if (cardInfo.getName().equals(info.getName())) {
                        info.setImageHash(cardInfo.getImageHash());
                        break;
                    }
                }

                PSDebug.d(info.toDebugString());
                profileList.add(info);
            }
        });
        if (!ret) {
            return null;
        }
        
        return profileList;
    }

    /**
     * ユニット一覧ファイルを読み込んで返す
     * @param filePath ユニット一覧ファイル
     * @return ユニット一覧情報リスト
     */
    private ArrayList<IdleUnitInfo> readUnitList(String filePath) {
        final ArrayList<IdleUnitInfo> infoList = new ArrayList<>();
        TextLineReader reader = new TextLineReader();
        boolean ret = reader.readLine(filePath, new IReadLineListener() {
            @Override
            public void onReadLine(String lineText) {
                String[] items = lineText.split("=");
                if (items.length < 2) {
                    return;
                }

                IdleUnitInfo info = new IdleUnitInfo();
                info.setUnitName(items[0]);
                info.setCharName(items[1]);
                infoList.add(info);
            }
        });
        if (!ret) {
            return null;
        }
        return infoList;
    }
}
