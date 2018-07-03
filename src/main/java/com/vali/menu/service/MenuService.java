package com.vali.menu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

import com.vali.common.entity.UserMst;

/**
 * <p>メニュー画面用サービスクラス</p>
 * @author rei mitsu
 */
@Service
public class MenuService {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    /**
     * <p>DBからユーザ情報を取得する。</p>
     * @param userId ログインユーザID
     * @return ユーザ名
     */
    public String getUsername(String userId) {
        SqlParameterSource param = new MapSqlParameterSource("USER_ID", userId);
        UserMst user = jdbcTemplate.queryForObject(
                "SELECT * FROM USER_MST WHERE USER_ID = :USER_ID AND DEL_FLG = '0'",
                param, new BeanPropertyRowMapper<UserMst>(UserMst.class));

        return user.getUserName();
    }

}
