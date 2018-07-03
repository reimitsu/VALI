package com.vali.common;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * <p>共通エラーハンドラクラス</p>
 * @author rei mitsu
 */
@ControllerAdvice
public class SystemErrorHandler {

    /**
     * <p>処理されない例外が発生した場合、ログを出力しエラー画面に遷移させる。</p>
     * @return 遷移先画面
     */
    @ExceptionHandler(Exception.class)
    public String handleError(Exception exception) {
        exception.printStackTrace();
        return "Error";
    }
}
