-----------------------------------------
-- ユーザマスタテーブル作成用SQL
-- rei mitsu
-----------------------------------------

CREATE
	TABLE user_mst (
		user_id VARCHAR(10) PRIMARY KEY,
		user_name VARCHAR(10) NOT NULL,
		password VARCHAR(85) NOT NULL,
		del_flg VARCHAR(1) NOT NULL DEFAULT '0',
		manage_flg VARCHAR(1) NOT NULL DEFAULT '0'
	);