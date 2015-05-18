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
 *         用户一个暂时来接受用户的弹幕信息的数据库
 * */
public class GetBarrageDB extends SQLiteOpenHelper {
	String TAG = "GetInterCutDB";
	private final static String DATABASE_NAME = "BARRAGE.db";

	private final static int DATABASE_VERSION = 1;

	private final static String TABLE_NAME = "get_Barrage";
	public final static String ID = "_id";// 这是数据库的ID，然后依次递增
	public final static String M_CONTINUE = "m_con";// 内容
	public final static String M_TIME = "m_time";// 发表的内容

	public GetBarrageDB(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql = "CREATE TABLE " + TABLE_NAME + " (" + ID

		+ " INTEGER primary key autoincrement, " + M_TIME + " text, "
				+ M_CONTINUE + " text);";

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

	/**
	 * 
	 * select the sql
	 * */
	public long select(int mID) {
		long time = 0;
		String str = "select * from " + TABLE_NAME;
		// + " where _id='" + mID
		// + "'";
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
		cv.put(M_CONTINUE, mCon);
		cv.put(M_TIME, time);
		long row = db.insert(TABLE_NAME, null, cv);
		System.out.println("the insert mcon is===>" + mCon + "the time is====>"
				+ time);

		return row;

	}

	/**
	 * 
	 * delete the sql
	 * */
	public void delete(String mId)

	{

		SQLiteDatabase db = this.getWritableDatabase();

		String where = ID + " = ?";

		String[] whereValue = { mId };

		db.delete(TABLE_NAME, where, whereValue);

	}

	// /**
	// *
	// * UPDATE THE SQL
	// * */
	// PUBLIC VOID UPDATE(STRING MCON, STRING TIME)
	//
	// {
	//
	// SQLITEDATABASE DB = THIS.GETWRITABLEDATABASE();
	//
	// STRING WHERE = ID + " = ?";
	//
	// STRING[] WHEREVALUE = { "" + MID };
	//
	// CONTENTVALUES CV = NEW CONTENTVALUES();
	//
	// CV.PUT(IS_START, ISSTART);
	// CV.PUT(M_CONTINUE, MCON);
	// CV.PUT(TIME, TIME);
	// LOGGER.D("UPDATE", MID + "_" + ISSTART + "_" + MCON + "_" + TIME);
	// DB.UPDATE(TABLE_NAME, CV, WHERE, WHEREVALUE);
	//
	// }
	//
}
