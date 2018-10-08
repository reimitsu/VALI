package com.vali.usermanage.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.vali.common.ValiConstant;
import com.vali.common.ValiUtility;
import com.vali.common.entity.UserMst;

/**
 * ユーザ管理サービスクラス
 * @author rei mitsu
 */
@Service
public class UserManageService {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    PasswordEncoder passwordEncoder;

    /**
     * ユーザ情報検索
     * @param userId ユーザID
     * @param userName ユーザ名
     * @return ユーザ情報リスト
     */
    public List<UserMst> getUserInfo(String userId, String userName) {
        SqlParameterSource param = new MapSqlParameterSource("USER_ID", ValiUtility.escape(userId) + "%")
                .addValue("USER_NAME", "%" + ValiUtility.escape(userName) + "%");
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT USER_ID, USER_NAME, MANAGE_FLG FROM USER_MST WHERE ");
        if(!ValiUtility.isNullorEmpty(userId)) {
            sql.append("USER_ID ILIKE :USER_ID AND ");
        }
        if(!ValiUtility.isNullorEmpty(userName)) {
            sql.append("USER_NAME LIKE :USER_NAME AND ");
        }
        sql.append("DEL_FLG = '0' ORDER BY USER_ID");
        return jdbcTemplate.query(sql.toString(), param, new BeanPropertyRowMapper<UserMst>(UserMst.class));
    }

    /**
     * ユーザ情報登録内容チェック
     * @param addId
     * @param addPass
     * @param checkPass
     * @param addName
     * @param addAuth
     * @return エラーメッセージ
     */
    public String[] addUserInfoCheck(String addId, String addPass, String checkPass, String addName, String addAuth) {
        ArrayList<String> errorLs = new ArrayList<String>();
        if(addId == null || addId.length() < 6 || addId.length() > 15 || !addId.matches("^[a-zA-Z0-9]+$")) {
            errorLs.add("IDの入力値が不正です。");
        }
        if(addPass == null || addPass.length() < 6 || addPass.length() > 20 || !addPass.matches("^[a-zA-Z0-9]+$")) {
            errorLs.add("パスワードの入力値が不正です。");
        }
        if(!addPass.equals(checkPass)) {
            errorLs.add("確認用パスワードが一致しません。");
        }
        if(ValiUtility.isNullorEmpty(addName) || addName.length() > 15) {
            errorLs.add("名前の入力値が不正です。");
        }
        if(addAuth != null) {
            if(!addAuth.equals(ValiConstant.MANAGE_FLG_ADMIN)) {
                errorLs.add("管理者の値が不正です。");
            }
        }
        String[] message = errorLs.toArray(new String[errorLs.size()]);
        return message;
    }

    /**
     * ユーザ情報登録
     * @param addId
     * @param addPass
     * @param addName
     * @param addAuth
     * @return エラーメッセージ
     */
    public String[] addUserInfo(String addId, String addPass, String addName, String addAuth) {
        SqlParameterSource param = new MapSqlParameterSource("USER_ID", addId);
        String sql = "SELECT USER_ID FROM USER_MST WHERE USER_ID = :USER_ID AND DEL_FLG = '0'";
        List<UserMst> user = jdbcTemplate.query(sql, param, new BeanPropertyRowMapper<UserMst>(UserMst.class));
        if(user.size() > 0) {
            String[] message = {"入力されたIDは既に使用されています。"};
            return message;
        }
        addPass = passwordEncoder.encode(addPass);
        if(addAuth == null) {
            addAuth = "0";
        }
        param = new MapSqlParameterSource("USER_ID", addId)
                .addValue("USER_NAME", addName)
                .addValue("PASSWORD", addPass)
                .addValue("MANAGE_FLG", addAuth);
        sql = "INSERT INTO USER_MST "
                + "(USER_ID, USER_NAME, PASSWORD, DEL_FLG, MANAGE_FLG) VALUES "
                + "(:USER_ID, :USER_NAME, :PASSWORD, '0', :MANAGE_FLG) "
                + "ON CONFLICT ON CONSTRAINT USER_MST_PKEY "
                + "DO UPDATE SET USER_NAME = :USER_NAME, PASSWORD = :PASSWORD, DEL_FLG = '0', MANAGE_FLG = :MANAGE_FLG";
        jdbcTemplate.update(sql, param);
        return null;
    }

    /**
     * ユーザ情報更新
     * @param addId
     * @param addPass
     * @param addName
     * @param addAuth
     * @param passwordResetFlg
     */
    public void changeUserInfo(String addId, String addPass, String addName, String addAuth, boolean passwordResetFlg) {
        String sql = "UPDATE USER_MST "
                + "SET USER_NAME = :USER_NAME, MANAGE_FLG = :MANAGE_FLG "
                + "WHERE USER_ID = :USER_ID";
        if(passwordResetFlg) {
            addPass = passwordEncoder.encode(addPass);
            sql = "UPDATE USER_MST "
                    + "SET USER_NAME = :USER_NAME, PASSWORD = :PASSWORD, MANAGE_FLG = :MANAGE_FLG "
                    + "WHERE USER_ID = :USER_ID";
        }
        if(addAuth == null) {
            addAuth = "0";
        }
        SqlParameterSource param = new MapSqlParameterSource("USER_ID", addId)
                .addValue("USER_NAME", addName)
                .addValue("PASSWORD", addPass)
                .addValue("MANAGE_FLG", addAuth);
        jdbcTemplate.update(sql, param);
    }

    /**
     * ユーザ情報削除
     * @param userId
     */
    public void deleteUserInfo(String userId) {
        String sql = "UPDATE USER_MST "
                + "SET DEL_FLG = '1' "
                + "WHERE USER_ID = :USER_ID";
        SqlParameterSource param = new MapSqlParameterSource("USER_ID", userId);
        jdbcTemplate.update(sql, param);
    }
}
