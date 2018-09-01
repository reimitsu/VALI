package com.vali.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ユーザマスタエンティティクラス
 * @author rei mitsu
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMst {
    private String userId;
    private String userName;
    private String password;
    private String delFlg;
    private String manageFlg;
}
