package com.example.sql_test;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * SQLiteOpenHelperのサブクラスSampleSQLiteOpenHelperの実装
 */
class SampleSQLiteOpenHelper extends SQLiteOpenHelper {
	
	// コンストラクタ
	public SampleSQLiteOpenHelper(Context c) {
		super(c, "sqlite_sample.db", null, 1);
	}
	
	/**
	 * データベースファイル初回使用時に実行される処理
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		// テーブル作成のクエリを発行
		db.execSQL(
			"create table routes ( " +
			"_id integer primary key autoincrement, " +
			"start_station text, " +
			"arrive_station text, " +
			"resisted_route integer" + 
			");");
		
		for (int i = 1; i <= 3; i++) {
			db.execSQL("insert into routes values("+i+", NULL, NULL, 0);");
		}
	}
	
	/**
	 * データベースのバージョンアップ時に実行される処理
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// テーブルの破棄と再作成
		db.execSQL("drop table routes;");
		onCreate(db);
	}
}