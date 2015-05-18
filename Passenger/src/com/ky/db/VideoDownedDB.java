package com.ky.db;

import org.xml.sax.Parser;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 
 * 建一个视频的下载库，下载完了的视频，在这里面会有记录
 * */
public class VideoDownedDB extends SQLiteOpenHelper {

	public final static String DATABASE_NAME = "VIDEODOWNED.db";

	public final static int DATABASE_VERSION = 1;

	public final static String TABLE_NAME = "Video_Downed";
	public final static String n_Id = "_id";

	public final static String n_URL = "_URL";
	public final static String n_NAME = "_NAME";
	public final static String is_Delete = "_isDelete";

	public VideoDownedDB(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "CREATE TABLE " + TABLE_NAME + " (" + n_Id

		+ " INTEGER primary key autoincrement, " + n_URL + " text," + is_Delete
				+ " INTEGER," + n_NAME + " text);";

		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
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

	/**
	 * 
	 * 这个查询，可以用来查询是否有要删除的
	 * */
	public Cursor selectISDelete(String isDelete) {
		System.out.println("选中该删除的");
		String str = "select * from " + TABLE_NAME + " where _isDelete='"
				+ isDelete + "'";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(str, null);
		return cursor;

	}

	public long select(String name) {
		long time = 0;
		String str = "select * from " + TABLE_NAME + " where _URL='" + name
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

	public long insert(String m_URL, String m_NAME, int isDelete)

	{
		System.out.println("insert name is====>" + n_NAME);
		SQLiteDatabase db = this.getWritableDatabase();

		/* ContentValues */

		ContentValues cv = new ContentValues();
		cv.put(is_Delete, isDelete);
		cv.put(n_URL, m_URL);
		cv.put(n_NAME, m_NAME);
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

	// 根据可编辑来看，是否删除
	public void deleteData(int isDelete)

	{

		SQLiteDatabase db = this.getWritableDatabase();

		String where = is_Delete + " = ?";

		String[] whereValue = { "" + isDelete };

		db.delete(TABLE_NAME, where, whereValue);

	}

	public void update(String name, String url, int isDelete)

	{
		System.out.println("iam int the db update");

		SQLiteDatabase db = this.getWritableDatabase();

		String where = n_NAME + " = ?";

		String[] whereValue = { name + "" };

		ContentValues cv = new ContentValues();

		cv.put(n_NAME, name);
		cv.put(n_URL, url);
		cv.put(is_Delete, isDelete);
		db.update(TABLE_NAME, cv, where, whereValue);

	}

}
