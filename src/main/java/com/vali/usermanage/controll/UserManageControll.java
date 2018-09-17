package com.vali.usermanage.controll;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vali.common.ValiUtility;
import com.vali.common.entity.UserMst;
import com.vali.usermanage.service.UserManageService;

/**
 * ユーザ管理画面コントロール用クラス
 * @author rei mitsu
 */
@Controller
public class UserManageControll {

    @Autowired
    private UserManageService userManageService;

    @RequestMapping(value = "/getUserData", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<UserMst>> getUserData(HttpServletRequest request ,Authentication authentication) {
        // 管理者権限を持たないユーザによる検索が行われた場合、システムエラーとする。
        if(ValiUtility.AuthCheck(authentication.getAuthorities(), "ROLE_ADMIN")) {
            String userId = (String)request.getParameter("USER_ID");
            String userName = (String)request.getParameter("USER_NAME");
            List<UserMst> user = userManageService.getUserInfo(userId, userName);
            return ResponseEntity.ok(user);
        }
        return null;
    }
}
