package com.vali.login.controll;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/*
 * <p>ログイン画面コントロール用クラス</p>
 * @author rei mitsu
 */

@Controller
public class LoginControll {

    @RequestMapping("/")
    public ModelAndView init(ModelAndView mav) {
        mav.addObject("memo", "現在メンテナンス中です。");
        mav.setViewName("login/Login.html");
        return mav;
    }
}
