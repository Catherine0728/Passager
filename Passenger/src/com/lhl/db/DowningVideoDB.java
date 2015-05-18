package com.lhl.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 下载文件的数据库
 * 
 * */
public class DowningVideoDB extends SQLiteOpenHelper {
	public String TAG = "DowningVideoDB";

	public final static String DATABASE_NAME = "DOWNING.db";

	public final static int DATABASE_VERSION = 1;

	public final static String TABLE_NAME = "DOWNING";
	public final static String n_Id = "_id";// 自增长ID

	public final static String n_URL = "_URL";// 下载路径
	public final static String n_NAME = "_NAME";// 下载的名字
	public final static String n_Size = "_Size";// 下载文件的大小
	public final static String n_Select = "_Select";// 表示当前的文件是否选中。如果选中既代表需要删除，如果没选中则代表不需要删除

	public DowningVideoDB(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql = "CREATE TABLE " + TABLE_NAME + " (" + n_Id

		+ " INTEGER primary key autoincrement, " + n_URL + " text," + n_Size
				+ " text," +n_Select+" INTEGER, " +n_NAME + " text);";

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
				time = cursor.getColumnIndex(n_URL);
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

	public long insert(String size, String m_URL, String m_NAME,int isSelect)

	{
		System.out.println("iam in the db instert");
		SQLiteDatabase db = this.getWritableDatabase();

		/* ContentValues */

		ContentValues cv = new ContentValues();
		cv.put(n_Size, size);
		cv.put(n_URL, m_URL);
		cv.put(n_NAME, m_NAME);
		 cv.put(n_Select, isSelect);
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

	public void update(String Size, String name, String url,int isSelect)

	{
		System.out.println("iam int the db update");

		SQLiteDatabase db = this.getWritableDatabase();

		String where = n_NAME + " = ?";

		String[] whereValue = { name + "" };

		ContentValues cv = new ContentValues();

		cv.put(n_NAME, name);
		cv.put(n_URL, url);
		cv.put(n_Size, Size);
		cv.put(n_Select, isSelect);
		db.update(TABLE_NAME, cv, where, whereValue);

	}

}
