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
	
	private Button btnInsert;
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
		
		// 挿入ボタン
		btnInsert = (Button)findViewById(R.id.btn_insert);
		btnInsert.setOnClickListener(this);
		
		// 挿入ボタン
		btnSelect = (Button)findViewById(R.id.btn_select);
		btnSelect.setOnClickListener(this);
		
		editStart = (EditText)findViewById(R.id.edit_start);
		editArrive = (EditText)findViewById(R.id.edit_arrive);
		
		// SampleSQLiteOpenHelperのインスタンス生成
		hlpr = new SampleSQLiteOpenHelper(getApplicationContext());
		// getWritableDatabase()を使用してデータベースを開き、SQLiteDatabaseのインスタンス取得
		db = hlpr.getWritableDatabase();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public void onClick(View v) {
		if (v == btnInsert){
			
			String startStation = editStart.getText().toString();
			String arriveStation = editArrive.getText().toString();
			
			// 空白チェック
			if (startStation.equals("") && arriveStation.equals("")) {
				Toast.makeText(this, "INPUT ERROR", Toast.LENGTH_LONG).show();
				return;
			}
			
			// ContentValuesのインスタンスにデータを格納
			ContentValues values = new ContentValues();
			
			values.put("start_station", startStation);
			values.put("arrive_station", arriveStation);

			// データの挿入
			db.insert("sample_table", null, values);
			
			Toast.makeText(this, "INSERT DB", Toast.LENGTH_LONG).show();
        } else if (v == btnSelect){
        	
        	// テーブルからデータを検索
        	Cursor cursor = db.query(
        			"sample_table", 
        			new String[] {"_id", "start_station", "arrive_station"}, 
        			null, null, null, null, "_id DESC");
        	// 参照先を一番始めに
        	boolean isEof = cursor.moveToFirst();
        	Log.d("log", "" + isEof);
        	// データを取得していく
        	while(isEof) {
        		Log.d("SELECT", "" + 
        				cursor.getString(1) + ", " + // start_station
        				cursor.getString(2));		// arrive_station
        		isEof = cursor.moveToNext();
        	}
        	// 忘れずに閉じる
        	cursor.close();
        }
	} 
}
