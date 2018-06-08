package com.vali.login.controll;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginControll {

    @RequestMapping("/login")
    public String login() {
        return "ログイン画面は現在未実装です。";
    }
}
