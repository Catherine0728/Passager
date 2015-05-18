package com.ky.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 
 * 这是游戏已下载的数据库，已经下载好的数据在这里会有记录。
 * */
public class GameDownedDB extends SQLiteOpenHelper {
	public String TAG = "GameDownedDB";

	public final static String DATABASE_NAME = "GAMENDOWNED.db";

	public final static int DATABASE_VERSION = 1;

	public final static String TABLE_NAME = "Game_Downed";

	public final static String n_Id = "_id";
	// 游戏是否下载完成，这里下载完成了用1来表示
	public final static String n_IsDown = "_isDown";

	public final static String n_Size = "_Size";
	// 游戏的连接，这里主要是指游戏下载在本地的地址
	public final static String n_URL = "_URL";
	// 游戏的名字
	public final static String n_NAME = "_NAME";

	public GameDownedDB(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql = "CREATE TABLE " + TABLE_NAME + " (" + n_Id

		+ " INTEGER primary key autoincrement, " + n_IsDown + " text," + n_Size
				+ " long," + n_URL + " text," + n_NAME + " text);";

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

	// 查询所有已经下载的数据
	public Cursor select() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db
				.query(TABLE_NAME, null, null, null, null, null, null);
		return cursor;
	}

	// 根据是否已经下载完成来获得当前已经下载好的数据的大小
	public long select(int isDown) {
		long time = 0;
		String str = "select * from " + TABLE_NAME + " where n_IsDown='"
				+ isDown + "'";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(str, null);
		if (cursor != null && cursor.getCount() > 0) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				time = cursor.getLong(cursor.getColumnIndex(n_IsDown));
			}
			return time;
		} else {
			return 0;
		}

	}

	public long select(String fileName) {
		long time = 0;
		String str = "select * from " + TABLE_NAME + " where _NAME='"
				+ fileName + "'";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(str, null);
		if (cursor != null && cursor.getCount() > 0) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				time = cursor.getColumnIndex(n_NAME);
			}

			return time;
		} else {
			return 0;
		}

	}

	public long insert(String Size, String m_URL, String m_NAME)

	{
		System.out.println("iam in the db instert");
		SQLiteDatabase db = this.getWritableDatabase();

		/* ContentValues */

		ContentValues cv = new ContentValues();
		cv.put(n_Size, Size);
		cv.put(n_URL, m_URL);
		cv.put(n_NAME, m_NAME);
		long row = db.insert(TABLE_NAME, null, cv);

		return row;

	}

	// public void delete(String ID)
	//
	// {
	//
	// SQLiteDatabase db = this.getWritableDatabase();
	//
	// String where = n_ID + " = ?";
	//
	// String[] whereValue = { ID };
	//
	// db.delete(TABLE_NAME, where, whereValue);
	//
	// }

	public void update(String Size, String name, String url, String isDown)

	{
		System.out.println("iam int the db update");

		SQLiteDatabase db = this.getWritableDatabase();

		String where = n_Size + " = ?";

		String[] whereValue = { Size };

		ContentValues cv = new ContentValues();

		cv.put(n_NAME, name);
		cv.put(n_URL, url);
		cv.put(n_IsDown, isDown);
		db.update(TABLE_NAME, cv, where, whereValue);

	}

}
