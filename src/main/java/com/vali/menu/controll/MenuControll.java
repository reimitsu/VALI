package com.vali.menu.controll;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>メニュー画面コントロール用クラス</p>
 * @author rei mitsu
 */
@Controller
public class MenuControll {

    @RequestMapping("/menu")
    public ModelAndView init(ModelAndView mav) {
        mav.addObject("memo", "現在メンテナンス中です。");
        mav.setViewName("menu/Menu.html");
        return mav;
    }
}
