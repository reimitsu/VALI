package com.vali.menu.controll;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.vali.common.ValiConstant;

/**
 * メニュー画面コントロール用クラス
 * @author rei mitsu
 */
@Controller
public class MenuControll {

    @RequestMapping("/menu")
    public ModelAndView init(ModelAndView mav, HttpSession session) {
        // セッションからユーザ名を取得する。
        String userName = (String)session.getAttribute("UserName");
        mav.addObject("username", userName);
        mav.addObject("title", ValiConstant.PAGE_TITLE_MENU);
        mav.setViewName("menu/Menu.html");
        return mav;
    }
}
