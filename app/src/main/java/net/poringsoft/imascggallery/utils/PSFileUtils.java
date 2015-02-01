package net.poringsoft.imascggallery.utils;

import net.poringsoft.imascggallery.data.EnvOption;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * ファイル関連ユーティリティクラス
 */
public class PSFileUtils {
    /**
     * バッファサイズ
     */
    private static final int BUFF_SIZE = 1024*20;

    /**
     * URLのファイルを保存する
     * @param url ダウンロード先ファイルのURL
     * @param outputFilePath 保存先パス
     * @return 保存パス
     */
    public static String downloadFile(final String url, final String outputFilePath)
    {
        HttpGet request = new HttpGet(url);
        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setSoTimeout(params, EnvOption.NET_GET_TIMEOUT);
        HttpConnectionParams.setConnectionTimeout(params, EnvOption.NET_GET_TIMEOUT);
        DefaultHttpClient httpClient = new DefaultHttpClient(params);

        String result = null;
        try {
            result = httpClient.execute(request, new ResponseHandler<String>() {
                @Override
                public String handleResponse(HttpResponse response) throws IOException {
                    switch (response.getStatusLine().getStatusCode()) {
                        case HttpStatus.SC_OK:
                            //ファイルに保存
                            InputStream is = response.getEntity().getContent();
                            saveFile(is, outputFilePath);
                            return outputFilePath;
                        case HttpStatus.SC_NOT_FOUND:
                            throw new RuntimeException("データなしエラー");
                        default:
                            throw new RuntimeException("通信エラー code=" + response.getStatusLine().getStatusCode());
                    }
                }
            });
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }

        return result;
    }

    /**
     * 入力ストリームからファイルに保存する
     * @param in 入力ストリーム
     * @param filePath 保存ファイルパス
     * @throws IOException
     */
    private static void saveFile(InputStream in, String filePath) throws IOException {
        FileOutputStream out = new FileOutputStream(filePath, false);
        //int writeSize = 0;
        byte[] bytes = new byte[BUFF_SIZE];
        while(true) {
            int readSize = in.read(bytes);
            if (readSize <= 0) {
                break;
            }
            //writeSize += readSize;
            out.write(bytes, 0, readSize);
        }

        out.close();
    }

    /**
     * ZIPファイルを展開する
     * フォルダ構成はすべて無視しフラットな状態で保存を行う
     * @param zipFile ZIPファイルパス
     * @param outputDir 展開先フォルダ
     * @return 展開したファイルリスト
     */
    public static List<String> decodeZipFile(String zipFile, String outputDir) {
        if (!outputDir.endsWith("/")) {
            outputDir = outputDir + "/";	//最後は必ず"/"で閉じるようにする
        }

        List<String> outputList = new ArrayList<>();
        ZipFile zip = null;
        try {
            zip = new ZipFile(zipFile);
            for (Enumeration<? extends ZipEntry> e = zip.entries(); e.hasMoreElements();) {
                ZipEntry entry = e.nextElement();
                if (entry.isDirectory()) {
                    continue;
                }

                File outputFile = new File(outputDir + getFileNameFromPath(entry.getName()));
                if (outputFile.exists()) {
                    outputFile.delete();
                }

                InputStream is = zip.getInputStream(entry);
                saveFile(is, outputFile.getPath());
                is.close();
                outputList.add(outputFile.getPath());
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            if (zip != null) {
                try {
                    zip.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return outputList;
    }

    /**
     * ファイルパスからディレクトリ部分を削除しファイル名部分のみにして返す
     * @param path ファイルパス
     * @return ファイル名
     */
    private static String getFileNameFromPath(String path) {
        int pos = path.lastIndexOf('/');
        if (pos == -1) {
            return path;
        }
        if ((pos + 1) >= path.length()) {
            return path;
        }

        return path.substring(pos + 1, path.length());
    }
}
