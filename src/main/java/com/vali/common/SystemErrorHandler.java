package com.vali.common;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 共通エラーハンドラクラス
 * @author rei mitsu
 */
@ControllerAdvice
public class SystemErrorHandler {

    /**
     * 処理されない例外が発生した場合、ログを出力しエラー画面に遷移させる。
     * @return 遷移先画面
     */
    @ExceptionHandler(Exception.class)
    public String errorHandle(Exception exception) {
        if(!(exception instanceof ValiError)) {
            exception.printStackTrace();
        }
        return "Error.html";
    }
}
