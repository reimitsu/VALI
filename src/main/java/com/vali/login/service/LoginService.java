package com.vali.login.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.vali.common.ValiConstant;
import com.vali.common.ValiUtility;
import com.vali.common.entity.UserMst;


/**
 * <p>ログインサービスクラス</p>
 * @author rei mitsu
 */
@Service
public class LoginService implements UserDetailsService {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //入力されたユーザIDが空の場合、ログイン失敗
        if(username == null || username.equals("")) {
            ValiUtility.logWrite("VALIER001", null);
            throw new UsernameNotFoundException("");
        }

        //DBからユーザ情報を取得する。
        SqlParameterSource param = new MapSqlParameterSource("USER_ID", username);
        UserMst user = null;
        try {
            user = jdbcTemplate.queryForObject(
                    "SELECT * FROM USER_MST WHERE USER_ID = :USER_ID AND DEL_FLG = '0'",
                    param, new BeanPropertyRowMapper<UserMst>(UserMst.class));
        //ユーザ情報が取得できなかった場合、ログイン失敗
        } catch (EmptyResultDataAccessException e) {
            ValiUtility.logWrite("VALIER002", username);
            throw new UsernameNotFoundException("");
        }
        String password = user.getPassword();
        ArrayList<String> roles = new ArrayList<String>();
        //管理者フラグを参照し権限を付与
        if(ValiConstant.MANAGE_FLG_ADMIN.equals(user.getManageFlg())) {
            roles.add("ROLE_ADMIN");
            roles.add("ROLE_USER");
        } else {
            roles.add("ROLE_USER");
        }
        Collection<? extends GrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new User(username, password, authorities);
    }

}
