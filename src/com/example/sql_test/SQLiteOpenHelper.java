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
			"create table sample_table ( " +
			"_id integer primary key autoincrement, " +
			"start_station text not null, " +
			"arrive_station text not null " +
			");");
	}
	
	/**
	 * データベースのバージョンアップ時に実行される処理
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// テーブルの破棄と再作成
		db.execSQL("drop table sample_table;");
		onCreate(db);
	}
}