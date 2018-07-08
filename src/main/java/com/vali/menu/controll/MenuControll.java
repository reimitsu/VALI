package com.vali.menu.controll;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.vali.menu.service.MenuService;

/**
 * <p>メニュー画面コントロール用クラス</p>
 * @author rei mitsu
 */
@Controller
public class MenuControll {

    @Autowired
    MenuService menuService;

    @RequestMapping("/menu")
    public ModelAndView init(ModelAndView mav, Principal principal) {
        //ログインIDからユーザ名を取得する。
        String userName = menuService.getUsername(principal.getName());
        mav.addObject("username", userName);
        mav.addObject("memo", "現在メンテナンス中です。");
        mav.setViewName("menu/Menu.html");
        return mav;
    }
}
