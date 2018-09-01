package com.vali.common;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.vali.login.service.LoginService;

/**
 * 共通機能用クラス
 * @author rei mitsu
 */
public class ValiUtility implements AutoCloseable {

    @Override
    public void close() throws IOException {
        throw new IOException();
    }

    /**
     * プロパティファイル参照用メソッド
     * @param filePath プロパティファイルパス
     * @param keyName プロパティキー名
     * @return 取得値
     */
    public static String readProperties(String filePath, String keyName) {
        Properties prop = new Properties();
        try (FileInputStream fis = new FileInputStream(filePath);
                InputStreamReader isr = new InputStreamReader(fis, ValiConstant.CHAR_SET_UTF8);
                BufferedReader br = new BufferedReader(isr);){
            prop.load(br);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ValiError();
        }
        return prop.getProperty(keyName);
    }

    /**
     * ログ出力用メソッド
     * @param errorCd エラーメッセージコード
     * @param arg 埋め込みメッセージ(無しの場合にはNULLを指定)
     */
    public static void logWrite(String errorCd, String arg) {
        Log log = LogFactory.getLog(LoginService.class);
        String propValue = "";
        Properties prop = new Properties();
        try (FileInputStream fis = new FileInputStream(ValiConstant.LOG_PROP_FILE);
                InputStreamReader isr = new InputStreamReader(fis, ValiConstant.CHAR_SET_UTF8);
                BufferedReader br = new BufferedReader(isr);){
            prop.load(br);
            if(arg == null) {
                propValue = prop.getProperty(ValiConstant.LOG_PROP_FILE_KEY + errorCd);
            } else {
                propValue = MessageFormat.format(prop.getProperty(ValiConstant.LOG_PROP_FILE_KEY + errorCd), arg);
            }
            log.error(propValue);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ValiError();
        }
    }
}
