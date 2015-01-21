package net.poringsoft.imascggallery.data;

import android.content.Context;

import net.poringsoft.imascggallery.utils.KanamojiCharUtils;
import net.poringsoft.imascggallery.utils.PSDebug;
import net.poringsoft.imascggallery.utils.PSUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * アイドル情報ファイルからデータの読み込みを行うクラス
 * Created by mry on 15/01/19.
 */
public class IdleInfoLoader {
    private Context m_context;
    
    public IdleInfoLoader(Context context) {
        m_context = context;
    }
    
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
    
    private ArrayList<IdleCardInfo> readIdleCardList(String filePath, Map<Integer, String> hashTable) {
        ArrayList<IdleCardInfo> infoList = new ArrayList<>();
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferReader = null;
        try {
            // CSVファイルの読み込み
            inputStream = new FileInputStream(filePath);
            inputStreamReader = new InputStreamReader(inputStream);
            bufferReader = new BufferedReader(inputStreamReader);
            String line = "";
            while ((line = bufferReader.readLine()) != null) {
                String[] items = (line + ", ").split(",");  //備考がないデータのために1つ多めに追加する
                if (items.length < 20) {
                    continue;
                }
                int albumId = PSUtils.tryParseInt(items[1], 0);
                if (albumId == 0) {
                    continue;
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
        
        return infoList;
    }
    
    private ArrayList<IdleProfileInfo> readIdleProfileList(String filePath, ArrayList<IdleCardInfo> idleCardList) {
        ArrayList<IdleProfileInfo> profileList = new ArrayList<>();
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferReader = null;
        try {
            // CSVファイルの読み込み
            inputStream = new FileInputStream(filePath);
            inputStreamReader = new InputStreamReader(inputStream);
            bufferReader = new BufferedReader(inputStreamReader);
            String line = "";
            while ((line = bufferReader.readLine()) != null) {
                String[] items = line.split(",");
                if (items.length < 14) {
                    continue;
                }
                if (items[0].equals("名前")) {
                    continue;   //列名説明なので省略
                }
                
                IdleProfileInfo info = new IdleProfileInfo();

                info.setName(KanamojiCharUtils.hankakuKatakanaToZenkakuKatakana(items[0]));
                info.setKana(items[1]);
                info.setAgo(PSUtils.tryParseInt(items[2], 0));
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
        
        return profileList;
    }


    private ArrayList<IdleUnitInfo> readUnitList(String filePath) {
        ArrayList<IdleUnitInfo> infoList = new ArrayList<>();
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferReader = null;
        try {
            // CSVファイルの読み込み
            inputStream = new FileInputStream(filePath);
            inputStreamReader = new InputStreamReader(inputStream);
            bufferReader = new BufferedReader(inputStreamReader);
            String line = "";
            while ((line = bufferReader.readLine()) != null) {
                String[] items = line.split("=");
                if (items.length < 2) {
                    continue;
                }
                
                IdleUnitInfo info = new IdleUnitInfo();
                info.setUnitName(items[0]);
                info.setCharName(items[1]);
                infoList.add(info);
            }

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
        
        return infoList;
    }
}
