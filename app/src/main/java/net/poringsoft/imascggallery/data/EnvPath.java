package net.poringsoft.imascggallery.data;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 環境パス関連クラス
 */
public class EnvPath {
    //定数
    //---------------------------------------------------------
    private static final String NAME_NOMEDIA_FILE = ".nomedia"; 	//画像非表示用ファイル
    private static final String NAME_MAIN_DIR = "imascggallery";    //メインフォルダ名

    private static final String NAME_ALBUM_CSV = "main.csv";        //アルバムデータファイル名
    private static final String NAME_PROFILE_CSV = "profile.csv";   //アイドルプロフィールファイル名
    private static final String NAME_HASH_JSON = "id2hash.json";    //ハッシュリストデータファイル名
    private static final String NAME_UNIT_TEXT = "unit.txt";        //アイドルユニット一覧ファイル名


    //フィールド
    //---------------------------------------------------------
    private static String m_rootDir = "";


    //メソッド
    //---------------------------------------------------------
    /**
     * 初期化
     */
    public static void init() {
        m_rootDir = "";
    }

    /**
     * ルートディレクトリパスを取得する
     * アプリケーションデータはこのフォルダ以下に保存する
     */
    public static String getRootDirPath() {
        if (m_rootDir.equals("")) {
            //TODO: どこかのタイミングで/Android/data/net.poringsoft.imascggalleryに変更する
            File sdcard = Environment.getExternalStorageDirectory();
            if (sdcard == null || sdcard.equals("")) {
                return "";
            }

            m_rootDir = getDirPath(sdcard.getPath() + "/" + NAME_MAIN_DIR);
            createNomeidaFile(m_rootDir);
        }

        return m_rootDir;
    }

    /**
     * NOMEDIAファイルを作成する
     * @param dir 作成するフォルダパス
     */
    private static void createNomeidaFile(String dir) {
        String filePath = dir + NAME_NOMEDIA_FILE;
        File file = new File(filePath);
        if (!file.exists()) {
            try
            {
                //空のファイルを作成する
                file.createNewFile();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * ディレクトリパスを取得する
     * パスに指定したディレクトリが存在しないときは作成する
     * @param dirPath ディレクトリパス
     * @return 作成したディレクトリパス
     */
    private static String getDirPath(String dirPath) {
        if (!dirPath.endsWith("/")) {
            dirPath = dirPath + "/";	//最後は必ず"/"で閉じるようにする
        }

        File file = new File(dirPath);
        if (!file.exists()) {
            file.mkdirs();	//フォルダがないときは作成する
        }

        return dirPath;
    }

    /**
     * カード情報ファイルパスを取得する 
     * @return ファイルパス
     */
    public static String getAlbumFilePath()
    {
        return getRootDirPath() + NAME_ALBUM_CSV;
    }

    /**
     * プロイール情報ファイルパスを取得する 
     * @return ファイルパス
     */
    public static String getProfileFilePath() {
        return getRootDirPath() + NAME_PROFILE_CSV;
    }

    /**
     * カードハッシュ値ファイルパスを取得する 
     * @return ファイルパス
     */
    public static String getHashFilePath()
    {
        return getRootDirPath() + NAME_HASH_JSON;
    }

    /**
     * ユニット一覧ファイルパスを取得する
     * @return ファイルパス
     */
    public static String getUnitListFilePath() {
        return getRootDirPath() + NAME_UNIT_TEXT;
    }

    /**
     * リスト用アイコン画像（100x100ピクセル）の画像URLを取得する 
     * @param hash 画像ハッシュキー
     * @return URL
     */
    public static String getIdleIconImageUrl(String hash)
    {
        return "http://sp.pf-img-a.mbga.jp/12008305/?guid=ON&url=http%3A%2F%2F125.6.169.35%2Fidolmaster%2Fimage_sp%2Fcard%2Fxs%2F"
                + hash + ".jpg";
    }

    /**
     * カード画像表示用画像（640x800）の画像URLを取得する
     * @param cardInfo カード情報
     * @param isWaku 枠有かどうか
     * @return URL
     */
    public static String getIdleCardImageUrl(IdleCardInfo cardInfo, boolean isWaku) {
        if ( ! cardInfo.getRarity().contains("SR")){
            isWaku = true; //レア以下は枠なしにできない
        }
        return getIdleCardImageUrl(cardInfo.getImageHash(), isWaku);
    }

    /**
     * カード画像表示用画像（640x800）の画像URLを取得する
     * @param hash 画像ハッシュキー
     * @param isWaku 枠有かどうか
     * @return URL
     */
    private static String getIdleCardImageUrl(String hash, boolean isWaku)
    {
        if (isWaku) {
            return "http://sp.pf-img-a.mbga.jp/12008305/?guid=ON&url=http%3A%2F%2F125.6.169.35%2Fidolmaster%2Fimage_sp%2Fcard%2Fl%2F"
                    + hash + ".jpg";
        }
        else {
            return "http://sp.pf-img-a.mbga.jp/12008305/?guid=ON&url=http%3A%2F%2F125.6.169.35%2Fidolmaster%2Fimage_sp%2Fcard%2Fl_noframe%2F"
                    + hash + ".jpg";
        }
    }

    /**
     * カード画像の直リンク用画像URLを取得する
     * @param cardInfo カード情報
     * @param isWaku  枠有かどうか
     * @return URL
     */
    public static String getIdleCardImageUrlDirect(IdleCardInfo cardInfo, boolean isWaku)
    {
        if ( ! cardInfo.getRarity().contains("SR")){
            isWaku = true; //レア以下は枠なしにできない
        }
        return getIdleCardImageUrlDirect(cardInfo.getImageHash(), isWaku);
    }

    /**
     * カード画像の直リンク用画像URLを取得する
     * @param hash 画像ハッシュキー
     * @param isWaku  枠有かどうか
     * @return URL
     */
    private static String getIdleCardImageUrlDirect(String hash, boolean isWaku)
    {
        if (isWaku) {
            return "http://125.6.169.35/idolmaster/image_sp/card/l/"
                    + hash + ".jpg";
        }
        else {
            return "http://125.6.169.35/idolmaster/image_sp/card/l_noframe/"
                    + hash + ".jpg";
        }
    }

    /**
     * 相対URLを絶対URLに変換して返す
     * @param baseUrlText ベースとなるURL文字列
     * @param fileUrlText 相対ファイルURL文字列
     * @return 絶対URL文字列
     */
    public static String getAbsoluteUrl(String baseUrlText, String fileUrlText) {
        URL fileUrl = null;
        try {
            URL baseUrl = new URL(baseUrlText);
            fileUrl = new URL(baseUrl, fileUrlText);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if (fileUrl == null) {
            return "";
        }

        return fileUrl.toString();
    }
}
