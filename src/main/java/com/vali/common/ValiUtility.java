package com.vali.common;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.vali.login.service.LoginService;

/**
 * <p>共通機能実装用クラス</p>
 * @author rei mitsu
 */
public class ValiUtility implements AutoCloseable {

    @Override
    public void close() throws IOException {
        throw new IOException();
    }

    /**
     * <p>プロパティファイル参照用メソッド</p>
     * @param filePath プロパティファイルパス
     * @param keyName プロパティキー名
     * @return 取得値
     */
    public static String readProperties(String filePath, String keyName) {
        Properties prop = new Properties();
        try (FileInputStream fis = new FileInputStream(filePath);
                InputStreamReader isr = new InputStreamReader(fis, ValiConstant.CHAR_SET_UTF_8);
                BufferedReader br = new BufferedReader(isr);){
            prop.load(br);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop.getProperty(keyName);
    }

    /**
     * <p>ログ出力用メソッド</p>
     * @param errorCd エラーメッセージコード
     * @param arg 埋め込みメッセージ(無しの場合にはNULLを指定)
     */
    public static void logWrite(String errorCd, String arg) {
        Log log = LogFactory.getLog(LoginService.class);
        String propValue = "";
        Properties prop = new Properties();
        try (FileInputStream fis = new FileInputStream(ValiConstant.LOG_PROP_FILE);
                InputStreamReader isr = new InputStreamReader(fis, ValiConstant.CHAR_SET_UTF_8);
                BufferedReader br = new BufferedReader(isr);){
            prop.load(br);
            if(arg == null) {
                propValue = prop.getProperty(ValiConstant.LOG_PROP_FILE_KEY + errorCd);
            } else {
                propValue = MessageFormat.format(prop.getProperty(ValiConstant.LOG_PROP_FILE_KEY + errorCd), arg);
            }
            log.error(propValue);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
