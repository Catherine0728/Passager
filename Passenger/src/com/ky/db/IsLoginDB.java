package com.ky.db;

import com.redbull.log.Logger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 
 * this is a db to check the user is login.....
 * 
 * @author Catherine.Brain
 * 
 *         当用户点击弹幕的时候，然后，就去数据库里面判断当前是否有用户已经登录，如果没有就弹出一个框，代表需要登录，才可以发布消息
 * */
public class IsLoginDB extends SQLiteOpenHelper {
	String TAG = "GetInterCutDB";
	private final static String DATABASE_NAME = "ISLOGIN.db";

	private final static int DATABASE_VERSION = 1;

	private final static String TABLE_NAME = "is_Login";
	public final String u_ID = "u_id";// 这是数据库的ID，然后依次递增
	public final String u_UserName = "u_username";// 用户名
	public final String u_ISLogin = "is_login";// 是否登录
	public final String u_Phone = "u_phone";// 用户手机号
	public final String u_email = "u_email";// 用户邮箱
	public final String u_user_id = "u_user_id";// 用户的id
	public final String u_pass = "u_pass";// 用户密码
	public final String u_key = "u_key";// 密钥
	public final String u_Point = "u_Point";// 密钥

	public IsLoginDB(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql = "CREATE TABLE " + TABLE_NAME + " (" + u_ID

		+ " INTEGER primary key autoincrement, " + u_UserName + " text, "
				+ u_ISLogin + " text, " + u_email + " text, " + u_pass
				+ " text, " + u_user_id + " text, " + u_key + " text, "
				+ u_Point + " INTEGER, " + u_Phone + " text);";

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
		Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null,
				null, null);
		return cursor;
	}

	/**
	 * 
	 * select the sql
	 * */
	public long select(String userId) {
		long time = 0;
		String str = "select * from " + TABLE_NAME + " where u_user_id='"
				+ userId + "'";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(str, null);
		if (cursor != null && cursor.getCount() > 0) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				time = cursor.getLong(cursor.getColumnIndex(u_user_id));
			}
			return time;
		} else {
			return 0;
		}

	}

	public Cursor selectOfIs(String userName) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, null, userName, null, null, null,
				null, null);
		return cursor;
	}

	public Cursor selectUserId(String userId) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null,
				userId, null);
		return cursor;
	}

	// /**
	// *
	// * select the sql
	// * */
	// public long select(int mID) {
	// long time = 0;
	// String str = "select * from " + TABLE_NAME;
	// // + " where _id='" + mID
	// // + "'";
	// SQLiteDatabase db = this.getReadableDatabase();
	// Cursor cursor = db.rawQuery(str, null);
	// if (cursor != null && cursor.getCount() > 0) {
	// for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
	// .moveToNext()) {
	// // time = cursor.getLong(cursor.getColumnIndex(mID));
	// }
	// return time;
	// } else {
	// return 0;
	// }
	//
	// }

	/**
	 * 
	 * insert the sql
	 * */
	public long insert(String UserId, String userName, String Phonenumber,
			String password, String isLogin, String key, int point)

	{
		Logger.d(TAG, "insert");
		SQLiteDatabase db = this.getWritableDatabase();

		/* ContentValues */

		ContentValues cv = new ContentValues();
		cv.put(u_UserName, userName);
		cv.put(u_Phone, Phonenumber);
		cv.put(u_pass, password);
		cv.put(u_ISLogin, isLogin);
		cv.put(u_user_id, UserId);
		cv.put(u_key, key);
		cv.put(u_Point, point);
		Logger.d("insert", userName + "_" + Phonenumber + "-" + password
				+ "---" + UserId + "--------" + key);
		long row = db.insert(TABLE_NAME, null, cv);

		return row;

	}

	/**
	 * 
	 * delete the sql
	 * */
	public void delete(String userName)

	{

		SQLiteDatabase db = this.getWritableDatabase();

		String where = u_UserName + " = ?";

		String[] whereValue = { userName };

		db.delete(TABLE_NAME, where, whereValue);

	}

	public void deleteUserID(String userId)

	{

		SQLiteDatabase db = this.getWritableDatabase();

		String where = u_user_id + " = ?";

		String[] whereValue = { userId };

		db.delete(TABLE_NAME, where, whereValue);

	}

	/**
	 * 
	 * update the sql
	 * */
	public void update(String UserId, String userName, String Phonenumber,
			String password, String isLogin, String key, int point)

	{

		SQLiteDatabase db = this.getWritableDatabase();

		String where = u_user_id + " = ?";

		String[] whereValue = { "" + UserId };

		ContentValues cv = new ContentValues();
		cv.put(u_UserName, userName);
		cv.put(u_Phone, Phonenumber);
		cv.put(u_pass, password);
		cv.put(u_ISLogin, isLogin);
		cv.put(u_key, key);
		cv.put(u_Point, point);
		Logger.d("update", u_Phone + "_" + password + "_" + Phonenumber + "_"
				+ userName + "——" + isLogin + "---" + u_UserName + "----"
				+ u_user_id + "--------" + key);
		db.update(TABLE_NAME, cv, where, whereValue);

	}
}
