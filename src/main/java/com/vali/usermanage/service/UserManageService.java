package com.vali.usermanage.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

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

    /**
     * ユーザ情報検索SQL作成
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
        sql.append("DEL_FLG = '0'");
        return jdbcTemplate.query(sql.toString(), param, new BeanPropertyRowMapper<UserMst>(UserMst.class));
    }

}
