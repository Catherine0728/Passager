package com.ky.db;

import com.redbull.log.Logger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 
 * 建一个视频的下载库，正在下载的视频，在这里面会有记录
 * 这里添加了一个isdelete的字段，是用来判断是否选中需要删除，如果是0，就代表不用删除，如果是1，就代表需要删除
 * 
 * 
 * 
 * 在这里为了将所有的下载任务归纳到一起，所以，还需要多建立三个字段
 * 
 * 类别：小说：novel；游戏：game；视频：vedio
 * 
 * 总长度：allSize;下载的长度：downingSize；
 * */
public class VideoDowningDB extends SQLiteOpenHelper {
	public static String TAG = "VideoDownDB";
	public final static String DATABASE_NAME = "VIDEODOWNING.db";

	public final static int DATABASE_VERSION = 1;

	public final static String TABLE_NAME = "Video_Downing";
	public final static String n_Id = "_id";

	public final static String n_URL = "_URL";
	public final static String n_NAME = "_NAME";
	// 是否标记删除，默认为否，否就是0；如果要删除，那就是1；
	public final static String is_Delete = "is_Delete";
	// 这是类别，用于标记这个下载的任务是小说还是游戏还是视频
	public final static String Category = "_category";
	public final static String AllSize = "_allSize";
	public final static String DowningSize = "_DowningSize";

	public VideoDowningDB(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql = "CREATE TABLE " + TABLE_NAME + " (" + n_Id

		+ " INTEGER primary key autoincrement, " + n_URL + " text," + Category
				+ " text," + AllSize + " INTEGER," + DowningSize + " INTEGER,"
				+ is_Delete + " INTEGER," + n_NAME + " text);";

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

	/* 根据存储的进行分别的查询 */
	public Cursor selectCategory(String category) {
		long time = 0;
		String str = "select * from " + TABLE_NAME + " where _category='"
				+ category + "'";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(str, null);
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

	/**
	 * 
	 * 这个查询，可以用来查询是否有要删除的
	 * */
	public Cursor selectISDelete(int isDelete) {
		System.out.println("选中该删除的");
		String str = "select * from " + TABLE_NAME + " where is_Delete='"
				+ isDelete + "'";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(str, null);
		return cursor;

	}

	public long insert(String m_URL, String m_NAME, String isDelete,
			String category, int allsize, int downingsize)

	{
		System.out.println("insert name is=====>" + m_NAME);
		SQLiteDatabase db = this.getWritableDatabase();

		/* ContentValues */

		ContentValues cv = new ContentValues();
		cv.put(is_Delete, isDelete);
		cv.put(n_URL, m_URL);
		cv.put(n_NAME, m_NAME);
		cv.put(Category, category);
		cv.put(AllSize, allsize);
		cv.put(DowningSize, downingsize);
		long row = db.insert(TABLE_NAME, null, cv);

		return row;

	}

	public void delete(String name)

	{

		Logger.d(TAG, "delete name is====》" + name);
		SQLiteDatabase db = this.getWritableDatabase();

		String where = n_NAME + " = ?";

		String[] whereValue = { name };

		db.delete(TABLE_NAME, where, whereValue);

	}

	public void deleteData(int isDelete)

	{

		Logger.d(TAG, "delete isdelete is====》" + isDelete);
		SQLiteDatabase db = this.getWritableDatabase();

		String where = is_Delete + " = ?";

		String[] whereValue = { isDelete + "" };

		db.delete(TABLE_NAME, where, whereValue);

	}

	public void update(String name, String url, int isDelete, String category,
			int allsize, int downingsize)

	{
		System.out.println("iam int the db update");

		SQLiteDatabase db = this.getWritableDatabase();

		String where = n_NAME + " = ?";

		String[] whereValue = { name + "" };

		ContentValues cv = new ContentValues();

		cv.put(n_NAME, name);
		cv.put(n_URL, url);
		cv.put(is_Delete, isDelete);
		cv.put(Category, category);
		cv.put(AllSize, allsize);
		cv.put(DowningSize, downingsize);
		db.update(TABLE_NAME, cv, where, whereValue);

	}

}
