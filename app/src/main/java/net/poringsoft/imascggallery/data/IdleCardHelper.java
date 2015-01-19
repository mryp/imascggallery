package net.poringsoft.imascggallery.data;

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

/**
 * Created by mry on 15/01/19.
 */
public class IdleCardHelper {
    
    
    public IdleCardHelper() {
        
        
    }
    
    public boolean loadFile(String mainCsvPath, String hashJsonPath) {
        JSONObject hashJson = readHashJson(hashJsonPath);
        if (hashJson == null) {
            return false;
        }

        ArrayList<IdleCardInfo> infoList = readIdleCardList(mainCsvPath, hashJson);
        if (infoList == null) {
            return false;
        }
        
        return true;
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
    
    private ArrayList<IdleCardInfo> readIdleCardList(String filePath, JSONObject hashJson) {
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
                String[] items = line.split(",");
                if (items.length < 20) {
                    continue;
                }
                int albumId = PSUtils.tryParseInt(items[1], 0);
                if (albumId == 0) {
                    continue;
                }
                
                String hashText = "";
                try {
                    hashText = hashJson.getJSONObject(String.valueOf(albumId)).getString("hash");
                } catch (JSONException e) {
                    hashText = "";
                }

                IdleCardInfo info = new IdleCardInfo();
                info.setAlbumId(PSUtils.tryParseInt(items[1], 0));
                info.setAttribute(items[2]);
                info.setRarity(items[3]);
                info.setNamePrefix(items[4]);
                info.setName(items[5]);
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
                info.setImageHash(hashText);

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
}
