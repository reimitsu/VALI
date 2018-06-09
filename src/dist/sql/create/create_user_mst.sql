-----------------------------------------
-- ユーザマスタテーブル作成用SQL
-- rei mitsu
-----------------------------------------

CREATE
	TABLE user_mst (
		user_id VARCHAR(15) PRIMARY KEY,
		user_name VARCHAR(15) NOT NULL,
		password VARCHAR(60) NOT NULL,
		del_flg VARCHAR(1) NOT NULL
	);