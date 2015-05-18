package com.ky.db;

import com.redbull.log.Logger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 
 * this is a db to cache the some info of the inter_out
 * 
 * @author Catherine.Brain
 * 
 *         接受用户的提示信息
 * */
public class GetInterCutDB extends SQLiteOpenHelper {
	String TAG = "GetInterCutDB";
	private final static String DATABASE_NAME = "MESSAGE.db";

	private final static int DATABASE_VERSION = 1;

	private final static String TABLE_NAME = "get_Mess";
	public final static String ID = "_id";// 这是数据库的ID，然后依次递增
	public final static String M_CONTINUE = "m_con";// 内容
	// public final static String IS_START = "is_start";// 是否启动提醒
	public final static String TIME = "time";// 提醒时间

	public GetInterCutDB(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql = "CREATE TABLE " + TABLE_NAME + " (" + ID

		+ " INTEGER primary key autoincrement, " + M_CONTINUE + " text, "
				+ TIME + " text);";

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

	/**
	 * select the sql
	 * 
	 * */
	public Cursor select() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db
				.query(TABLE_NAME, null, null, null, null, null, null);
		return cursor;
	}

	public Cursor selectOfIs(int isStart) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db
				.query(TABLE_NAME, null, null, null, null, null, null);
		return cursor;
	}

	/**
	 * 
	 * select the sql
	 * */
	public long select(int mID) {
		long time = 0;
		String str = "select * from " + TABLE_NAME;
	
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(str, null);
		if (cursor != null && cursor.getCount() > 0) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				// time = cursor.getLong(cursor.getColumnIndex(mID));
			}
			return time;
		} else {
			return 0;
		}

	}

	/**
	 * 
	 * insert the sql
	 * */
	public long insert(String mCon, String time)

	{
		Logger.d(TAG, "insert");
		SQLiteDatabase db = this.getWritableDatabase();

		/* ContentValues */

		ContentValues cv = new ContentValues();
//		cv.put(ID, 1);
		cv.put(M_CONTINUE, mCon);
		cv.put(TIME, time);
		Logger.d("insert", "_" + mCon + "-" + time + "_mid----" + ID);
		long row = db.insert(TABLE_NAME, null, cv);

		return row;

	}

	/**
	 * 
	 * delete the sql
	 * */
	public void delete(String content)

	{
		System.out.println("the delete id is===>" + content);
		SQLiteDatabase db = this.getWritableDatabase();

		String where = M_CONTINUE + " = ?";

		String[] whereValue = { content };

		db.delete(TABLE_NAME, where, whereValue);

	}

//	/**
//	 * 
//	 * update the sql
//	 * */
//	public void update(int mId, String mCon, String time)
//
//	{
//
//		SQLiteDatabase db = this.getWritableDatabase();
//
//		String where = ID + " = ?";
//
//		String[] whereValue = { "" + mId };
//
//		ContentValues cv = new ContentValues();
//
//		// cv.put(IS_START, isStart);
//		cv.put(M_CONTINUE, mCon);
//		cv.put(TIME, time);
//		Logger.d("update", mId + "_" + mCon + "_" + time);
//		db.update(TABLE_NAME, cv, where, whereValue);
//
//	}

}
