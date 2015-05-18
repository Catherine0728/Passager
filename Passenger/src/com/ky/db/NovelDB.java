package com.ky.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 
 * 建一个小说的下载库，里面有下载中以及以及下载完了的。
 * */
public class NovelDB extends SQLiteOpenHelper {

	private final static String DATABASE_NAME = "Nevol.db";

	private final static int DATABASE_VERSION = 1;

	private final static String TABLE_NAME = "Neovl_Down";
	private final static String n_Id = "_id";
	private final static String n_IsDown = "_isDown";

	private final static String n_ID = "_ID";
	private final static String n_URL = "_URL";
	private final static String n_NAME = "_NAME";

	public NovelDB(Context context, String name, CursorFactory factory,
			int version) {
		super(context, DATABASE_NAME, factory, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql = "CREATE TABLE " + TABLE_NAME + " (" + n_Id

		+ " INTEGER primary key autoincrement, " + n_IsDown + " text," + n_ID
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

	public Cursor select() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db
				.query(TABLE_NAME, null, null, null, null, null, null);
		return cursor;
	}

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

	public long insert(int ID, String m_URL, String m_NAME)

	{
		System.out.println("iam in the db instert");
		SQLiteDatabase db = this.getWritableDatabase();

		/* ContentValues */

		ContentValues cv = new ContentValues();
		cv.put(n_ID, ID);
		cv.put(n_URL, m_URL);
		cv.put(n_NAME, m_NAME);
		long row = db.insert(TABLE_NAME, null, cv);

		return row;

	}

	public void delete(String ID)

	{

		SQLiteDatabase db = this.getWritableDatabase();

		String where = n_ID + " = ?";

		String[] whereValue = { ID };

		db.delete(TABLE_NAME, where, whereValue);

	}

	public void update(int ID, String name, String url, String isDown)

	{
		System.out.println("iam int the db update");

		SQLiteDatabase db = this.getWritableDatabase();

		String where = n_ID + " = ?";

		String[] whereValue = { ID + "" };

		ContentValues cv = new ContentValues();

		cv.put(n_NAME, name);
		cv.put(n_URL, url);
		cv.put(n_IsDown, isDown);
		db.update(TABLE_NAME, cv, where, whereValue);

	}

}
