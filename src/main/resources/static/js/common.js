$(function(){
//--- ユーザ管理 ---//
	$('#MAIN').on('blur', 'input[name="USER_ID"]', function() {
		var val = $(this).val().replace(/[Ａ-Ｚａ-ｚ０-９]/g, function(s) {return String.fromCharCode(s.charCodeAt(0) - 0xFEE0);});
		$(this).val(val);
	});
	$('#MAIN').on('click', 'input[name="RESET"]', function() {
		$('input[name="USER_ID"]').val('');
		$('input[name="USER_NAME"]').val('');
	});
	$('#MAIN').on('click', 'input[name="SEARCH"]', function() {
		$('input[name="SEARCH"]').prop('disabled', true);
		search();
	});
	$('#MAIN').on('click', 'input[name="NEW"]', function() {
		$('#MAIN').text('');
		var rowTag = '<div class="row align-items-center oneHeight"><div class="col-12 col-md-4">';
		var endTag = '</div></div>'
		$('#MAIN').append('<div class="oneHeight" id="ERROR"></div>'
				+ '<form action="/addUserData" method="get" id="ADDFORM">'
				+ rowTag + 'ID<input type="text" placeholder="半角英数字6桁以上で入力して下さい" maxlength="15" name="ADDID">' + endTag
				+ rowTag + 'パスワード<input type="password" placeholder="半角英数字6桁以上で入力して下さい" autocomplete="off" maxlength="20" name="ADDPASS">' + endTag
				+ rowTag + '確認用パスワード<input type="password" autocomplete="off" maxlength="20" name="CHECKPASS">' + endTag
				+ rowTag + '名前<input type="text" maxlength="15" name="ADDNAME">' + endTag
				+ rowTag + '<label for="ADMIN">管理者</label><input type="checkbox" name="ADDAUTH" id="ADMIN" value="1">' + endTag
				+ '</form>'
				+ '<div class="row align-items-center oneHeight"><div class="offset-1"><input type="button" name="ADDUSER" value="登録"></div></div>');
	});
	$('#MAIN').on('click', 'input[name="CHANGE"]', function() {
		$('#MAIN').text('');
		var rowTag = '<div class="row align-items-center oneHeight"><div class="col-12 col-md-4">';
		var endTag = '</div></div>'
		var manage = '';
		if($(this).data('manage') == '1') {
			manage =' checked="checked"'
		}
		$('#MAIN').append('<div class="oneHeight" id="ERROR"></div>'
				+ '<form action="/changeUserData" method="get" id="ADDFORM">'
				+ rowTag + 'ID<input type="text" readonly="readonly" value="' + $(this).data('id') + '" name="ADDID">' + endTag
				+ rowTag + '名前<input type="text" maxlength="15" value="' + $(this).data('name') + '" name="ADDNAME">' + endTag
				+ rowTag + '<label for="ADMIN">管理者</label><input type="checkbox" name="ADDAUTH" id="ADMIN" value="1"' + manage + '>' + endTag
				+ '<div class="note" style="border-top: double; padding-top: 10px;">※再設定したい場合のみ入力</div>'
				+ rowTag + 'パスワード<input type="password" placeholder="半角英数字6桁以上で入力して下さい" autocomplete="off" maxlength="20" name="ADDPASS">' + endTag
				+ rowTag + '確認用パスワード<input type="password" autocomplete="off" maxlength="20" name="CHECKPASS">' + endTag
				+ '</form>'
				+ '<div class="row align-items-center oneHeight"><div class="offset-1"><input type="button" name="ADDUSER" value="変更"></div></div>');
	});
	$('#MAIN').on('click', 'input[name="ADDUSER"]', function() {
		if($('input[name="ADDID"]').val().match(/[^a-zA-Z0-9]/) ||
				$('input[name="ADDPASS"]').val().match(/[^a-zA-Z0-9]/)) {
			alert("ID・パスワードには半角英数字のみ使用可能です。");
			return false;
		} else if ($('input[name="ADDID"]').val().length < 6 ||
				($(this).val() == '登録' && $('input[name="ADDPASS"]').val().length < 6) ||
				($(this).val() == '変更' && (0 < $('input[name="ADDPASS"]').val().length && $('input[name="ADDPASS"]').val().length < 6))) {
			alert("ユーザID・パスワードは6桁以上で入力して下さい。");
			return false;
		} else if($('input[name="ADDPASS"]').val() != $('input[name="CHECKPASS"]').val()) {
			alert("確認用パスワードが一致しません。");
			return false;
		} else if($('input[name="ADDNAME"]').val() == '') {
			alert("名前を入力して下さい。");
			return false;
		}
		$('input[name="ADDUSER"]').prop('disabled', true);
		add();
	});
	$('#MAIN').on('click', 'input[name="DELETE"]', function() {
		if(confirm('ユーザを削除してもよろしいですか？')) {
			$.ajax({
				type : 'GET',
				url : '/deleteUserData',
				data : {ID: $(this).data('id')},
				success : function(data) {
					successDelete(data);
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					error(XMLHttpRequest, textStatus, errorThrown);
				}
			});
		}
	});
});
function add() {
	$.ajax({
		type : $('#ADDFORM').prop('method'),
		url : $('#ADDFORM').prop('action'),
		data : $('#ADDFORM').serialize(),
		success : function(data) {
			successAdd(data);
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			error(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}
function search() {
	$.ajax({
		type : $('#SEARCHFORM').prop('method'),
		url : $('#SEARCHFORM').prop('action'),
		data : $('#SEARCHFORM').serialize(),
		dataType : 'json',
		timeout : 100000,
		success : function(data) {
			successSearch(data);
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			error(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}
function successSearch(data) {
	$('#RESULT').text('');
	$('#RESULT').append('<div><input type="button" name="NEW" value="新規登録"></div>');
	if(0 < data.length) {
		$('#RESULT').append('<div class="row contents">'
				+ '<div class="col-3">ID</div>'
				+ '<div class="col-4">名前</div>'
				+ '<div class="col-2">権限</div>'
				+ '<div class="col-3"></div>'
				+ '</div>');
		for (var i = 0; i < data.length; i++) {
			var manage = '';
			if(data[i].manageFlg == '1') {
				manage ='管理者'
			}
			$('#RESULT').append('<div class="row items">'
					+ '<div class="col-3">' + data[i].userId + '</div>'
					+ '<div class="col-4">' + data[i].userName + '</div>'
					+ '<div class="col-2">' + manage + '</div>'
					+ '<div class="col-3 center"><input type="button" name="CHANGE" '
					+ 'data-id="' + data[i].userId + '" data-name="' + data[i].userName + '" data-manage="' + data[i].manageFlg + '" value="変更"> '
					+ '<input type="button" name="DELETE" data-id="' + data[i].userId + '" value="削除"></div>'
					+ '</div>');
		}
	} else {
		$('#RESULT').append('<div>検索結果が0件です。</div>');
	}
	$('input[name="SEARCH"]').prop('disabled', false);
}
function successAdd(data) {
	if(data.length > 0) {
		$('#ERROR').text('');
		for(var i = 0; i < data.length; i++) {
			$('#ERROR').append('<div>' + data[i] + '</div>');
		}
		$('input[name="ADDUSER"]').prop('disabled', false);
	} else {
		$('#MAIN').text('');
		$('#MAIN').append('<div>ユーザを登録しました。</div>');
	}
}
function successDelete(data) {
	$('#MAIN').text('');
	if(data.length > 0) {
		$('#MAIN').append('<div>' + data[0] + '</div>');
	} else {
		$('#MAIN').append('<div>ユーザを削除しました。</div>');
	}
}
function error(XMLHttpRequest, textStatus, errorThrown) {
	console.log('error:' + XMLHttpRequest + ' status:' + textStatus + ' errorThrown:' + errorThrown);
	window.location.href = '/logout';
}
//----------------//
