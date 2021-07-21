//同じ日付で2回目以降ならローディング画面非表示の設定

var splash_tt = $.cookie('accessdate'); //キーが入っていれば年月日を取得
var myD = new Date();//日付データを取得
var myYear = String(myD.getFullYear());//年
var myMonth = String(myD.getMonth() + 1);//月
var myDate = String(myD.getDate());//日

if (splash_tt != myYear + myMonth + myDate) {//cookieデータとアクセスした日付を比較↓
        //テキストのカウントアップの設定
			var bar = new ProgressBar.Line(splash_text, {//id名を指定
			strokeWidth: 0,//進捗ゲージの太さ
			duration: 1000,//時間指定(1000＝1秒)
			trailWidth: 0,//線の太さ
			text: {//テキストの形状を直接指定
			style: {//天地中央に配置
				position:'absolute',
				left:'50%',
				top:'50%',
				padding:'0',
				margin:'0',
				transform:'translate(-50%,-50%)',
				'font-size':'1.2rem',
				color:'#fff',
			},
			autoStyleContainer: false //自動付与のスタイルを切る
		},
		step: function(state, bar) {
			bar.setText(Math.round(bar.value() * 100) + ' %'); //テキストの数値
		}
	});

	//アニメーションスタート
	bar.animate(1.0, function () {//バーを描画する割合を指定します 1.0 なら100%まで描画します
		$("#splash").delay(500).fadeOut(800);//アニメーションが終わったら#splashエリアをフェードアウト
	});

	$.cookie('accessdate', myYear + myMonth + myDate); //accessdateキーで年月日を記録
}else {
    $("#splash").css("display", "none");//同日2回目のアクセスでローディング画面非表示
}



