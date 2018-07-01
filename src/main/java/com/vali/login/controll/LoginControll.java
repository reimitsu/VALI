package com.vali.login.controll;

import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.vali.common.ValiMessage;

/**
 * <p>ログイン画面コントロール用クラス</p>
 * @author rei mitsu
 */
@Controller
public class LoginControll {

    @RequestMapping("/")
    public ModelAndView init(@ModelAttribute("USER_ID") String userId,
            @RequestAttribute(name = WebAttributes.AUTHENTICATION_EXCEPTION, required = false) Exception exception,
            ModelAndView mav) {
        mav.addObject("USER_ID", userId);
        //ログイン認証失敗時、画面にメッセージを出力
        if(exception != null) {
            mav.addObject("message", ValiMessage.LOGIN_FAILED_MSG);
        }
        mav.setViewName("login/Login.html");
        return mav;
    }
}
