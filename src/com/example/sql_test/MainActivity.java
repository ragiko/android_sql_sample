package com.example.sql_test;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

// 参考URL http://tomcky.hatenadiary.jp/entry/2013/08/25/235502

public class MainActivity extends Activity implements OnClickListener {

	private Button btnRoute1;
	private Button btnRoute2;
	private Button btnRoute3;
	private Button btnSelect;

	private EditText editStart;
	private EditText editArrive;

	// db
	SampleSQLiteOpenHelper hlpr;
	SQLiteDatabase db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// ルートボタン
		btnRoute1 = (Button) findViewById(R.id.btn_route1);
		btnRoute2 = (Button) findViewById(R.id.btn_route2);
		btnRoute3 = (Button) findViewById(R.id.btn_route3);
		btnRoute1.setOnClickListener(this);
		btnRoute2.setOnClickListener(this);
		btnRoute3.setOnClickListener(this);

		// SELECTボタン
		btnSelect = (Button) findViewById(R.id.btn_select);
		btnSelect.setOnClickListener(this);

		editStart = (EditText) findViewById(R.id.edit_start);
		editArrive = (EditText) findViewById(R.id.edit_arrive);

		// SampleSQLiteOpenHelperのインスタンス生成
		hlpr = new SampleSQLiteOpenHelper(getApplicationContext());
		// getWritableDatabase()を使用してデータベースを開き、SQLiteDatabaseのインスタンス取得
		db = hlpr.getWritableDatabase();
		
		// 全てのRouteButtonにテキストを割り振る
		setTextToRouteButtons();

	}

	/*
	 * 全てのRouteButtonにテキストを割り振る
	 * ex-> テキストは "ルート1" か "出発地 到着地"
	 */
	private void setTextToRouteButtons() {
		// ボタンの初期TEXTを変更
		Cursor cursor = db.query("routes", new String[] { "_id",
				"start_station", "arrive_station", "resisted_route" }, null,
				null, null, null, "_id ASC");
		boolean isEof = cursor.moveToFirst();
		Button[] buttons = { btnRoute1, btnRoute2, btnRoute3 };
		int cursorCnt = 0;

		// データを取得していく
		while (isEof) {
			Boolean resistedRoute = (cursor.getInt(3)) == 1 ? true : false;

			if (resistedRoute) {
				buttons[cursorCnt].setText(cursor.getString(1) + /* start_station */
				" " + cursor.getString(2) /* arrive_station */);
			} else {
				buttons[cursorCnt].setText("ルート" + (cursorCnt + 1));
			}

			isEof = cursor.moveToNext();
			cursorCnt++;
		}
		// 忘れずに閉じる
		cursor.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		if (v == btnRoute1 || v == btnRoute2 || v == btnRoute3) {

			Button[] buttons = { btnRoute1, btnRoute2, btnRoute3 };

			for (int i = 1; i <= buttons.length; i++) {
				if (v == buttons[i - 1]) {

					// テーブルからデータを検索
					Cursor cursor = db.query("routes",
							new String[] { "_id", "start_station",
									"arrive_station", "resisted_route" },
							"_id = " + i, // where
							null, null, null, "_id DESC");
					cursor.moveToFirst(); // カーソルを先頭にする

					Boolean resistedRoute = (cursor.getInt(3)) == 1 ? true
							: false;
					String startStationFromDB = cursor.getString(1);
					String arriveStationFromBD = cursor.getString(2);

					// 忘れずに閉じる
					cursor.close();

					if (resistedRoute) { /* エディターに値入力 */

						Log.d("T", "");

						editStart.setText(startStationFromDB); // start_station
						editArrive.setText(arriveStationFromBD); // arrive_station

					} else { /* 目的地をDB保存 */

						Log.d("F", "");

						String startStation = editStart.getText().toString();
						String arriveStation = editArrive.getText().toString();

						// 空白チェック
						if (startStation.equals("") || arriveStation.equals("")) {
							Toast.makeText(this, "INPUT ERROR",
									Toast.LENGTH_SHORT).show();
							return;
						}

						// ContentValuesのインスタンスにデータを格納
						ContentValues values = new ContentValues();

						values.put("start_station", startStation);
						values.put("arrive_station", arriveStation);
						values.put("resisted_route", 1);

						// 更新
						db.update("routes", values, "_id = " + i, null);
						buttons[i - 1].setText(startStation + " "
								+ arriveStation);

						Toast.makeText(this, "UPDATE DB", Toast.LENGTH_SHORT)
								.show();
					}
				}
			}

		} else if (v == btnSelect) {
			// delete registed_route
			ContentValues values = new ContentValues();
			values.put("resisted_route", 0);
			db.update("routes", values, null, null);
			
			// 全てのRouteButtonにテキストを割り振る
			setTextToRouteButtons();
		}
	}
}
