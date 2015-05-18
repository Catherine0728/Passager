package com.ky.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 
 * 建一个游戏下载的数据库 ，正在下载的视频，在这里面会有记录
 * */
public class GameDowningDB extends SQLiteOpenHelper {
	public String TAG = "GameDowningDB";

	public final static String DATABASE_NAME = "GAMEDOWNING.db";

	public final static int DATABASE_VERSION = 1;

	public final static String TABLE_NAME = "Game_Downing";
	public final static String n_Id = "_id";

	public final static String n_URL = "_URL";
	public final static String n_NAME = "_NAME";
	public final static String n_Size = "_Size";

	public GameDowningDB(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql = "CREATE TABLE " + TABLE_NAME + " (" + n_Id

		+ " INTEGER primary key autoincrement, " + n_URL + " text," + n_Size
				+ " text," + n_NAME + " text);";

		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
		// to update the database
		db.execSQL(sql);

		onCreate(db);
	}

	public Cursor select() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db
				.query(TABLE_NAME, null, null, null, null, null, null);
		return cursor;
	}

	public long select(String name) {
		long time = 0;
		String str = "select * from " + TABLE_NAME + " where _NAME='" + name
				+ "'";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(str, null);
		if (cursor != null && cursor.getCount() > 0) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				time =cursor.getColumnIndex(n_URL);
			}
			return time;
		} else {
			return 0;
		}

	}

	public Cursor selectISDown(String isDown) {
		long time = 0;
		String str = "select * from " + TABLE_NAME + " where _isDown='"
				+ isDown + "'";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(str, null);
		return cursor;
		// if (cursor != null && cursor.getCount() > 0) {
		// for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
		// .moveToNext()) {
		// time = cursor.getLong(cursor.getColumnIndex(n_IsDown));
		// }
		// return time;
		// } else {
		// return 0;
		// }

	}

	public long insert(String size, String m_URL, String m_NAME)

	{
		System.out.println("iam in the db instert");
		SQLiteDatabase db = this.getWritableDatabase();

		/* ContentValues */

		ContentValues cv = new ContentValues();
		cv.put(n_Size, size);
		cv.put(n_URL, m_URL);
		cv.put(n_NAME, m_NAME);
		// cv.put(n_IsDown, isDown);
		long row = db.insert(TABLE_NAME, null, cv);

		return row;

	}

	public void delete(String name)

	{

		SQLiteDatabase db = this.getWritableDatabase();

		String where = n_NAME + " = ?";

		String[] whereValue = { name };

		db.delete(TABLE_NAME, where, whereValue);

	}

	public void update(String Size, String name, String url)

	{
		System.out.println("iam int the db update");

		SQLiteDatabase db = this.getWritableDatabase();

		String where = n_NAME + " = ?";

		String[] whereValue = { name + "" };

		ContentValues cv = new ContentValues();

		cv.put(n_NAME, name);
		cv.put(n_URL, url);
		cv.put(n_Size, Size);
		db.update(TABLE_NAME, cv, where, whereValue);

	}

}
