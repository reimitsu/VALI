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

    /**
     * ユーザ情報を取得する。
     * @param request
     * @param authentication
     * @return
     */
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

    /**
     * ユーザを登録する。
     * @param request
     * @param authentication
     * @return エラーメッセージ
     */
    @RequestMapping(value = "/addUserData", method = RequestMethod.GET)
    @ResponseBody
    public String[] addUserData(HttpServletRequest request ,Authentication authentication) {
        String[] errorMessage = {"権限がありません。"};
        // 管理者権限を持たないユーザによる登録が行われた場合、エラーメッセージを返却。
        if(ValiUtility.AuthCheck(authentication.getAuthorities(), "ROLE_ADMIN")) {
            String addId = (String)request.getParameter("ADDID");
            String addPass = (String)request.getParameter("ADDPASS");
            String checkPass = (String)request.getParameter("CHECKPASS");
            String addName = (String)request.getParameter("ADDNAME");
            String addAuth = (String)request.getParameter("ADDAUTH");
            errorMessage = userManageService.addUserInfoCheck(addId, addPass, checkPass, addName, addAuth);
            if(errorMessage == null || errorMessage.length == 0) {
                errorMessage = userManageService.addUserInfo(addId, addPass, addName, addAuth);
            }
        }
        return errorMessage;
    }

    /**
     * ユーザ情報を変更する。
     * @param request
     * @param authentication
     * @return エラーメッセージ
     */
    @RequestMapping(value = "/changeUserData", method = RequestMethod.GET)
    @ResponseBody
    public String[] changeUserData(HttpServletRequest request ,Authentication authentication) {
        String[] errorMessage = {"権限がありません。"};
        // 管理者権限を持たないユーザによる登録が行われた場合、エラーメッセージを返却。
        if(ValiUtility.AuthCheck(authentication.getAuthorities(), "ROLE_ADMIN")) {
            String addId = (String)request.getParameter("ADDID");
            String addPass = (String)request.getParameter("ADDPASS");
            String checkPass = (String)request.getParameter("CHECKPASS");
            String addName = (String)request.getParameter("ADDNAME");
            String addAuth = (String)request.getParameter("ADDAUTH");
            boolean passwordResetFlg = true;
            if("".equals(addPass) && "".equals(checkPass)) {
                errorMessage = userManageService.addUserInfoCheck(addId, "111111", "111111", addName, addAuth);
                passwordResetFlg = false;
            } else {
                errorMessage = userManageService.addUserInfoCheck(addId, addPass, checkPass, addName, addAuth);
            }
            if(errorMessage == null || errorMessage.length == 0) {
                    userManageService.changeUserInfo(addId, addPass, addName, addAuth, passwordResetFlg);
            }
        }
        return errorMessage;
    }

    /**
     * ユーザ情報を削除する。
     * @param request
     * @param authentication
     * @return エラーメッセージ
     */
    @RequestMapping(value = "/deleteUserData", method = RequestMethod.GET)
    @ResponseBody
    public String[] deleteUserData(HttpServletRequest request ,Authentication authentication) {
        String[] errorMessage = {"権限がありません。"};
        // 管理者権限を持たないユーザによる削除が行われた場合、エラーメッセージを返却。
        if(ValiUtility.AuthCheck(authentication.getAuthorities(), "ROLE_ADMIN")) {
            String userId = (String)request.getParameter("ID");
            userManageService.deleteUserInfo(userId);
        }
        return errorMessage;
    }
}
