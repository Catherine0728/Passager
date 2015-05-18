package com.ky.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 
 * this is a database to record the time of the last watch
 * 
 * @author Catherine.Brain
 * */
public class RecordVedioPlay extends SQLiteOpenHelper {

	private final static String DATABASE_NAME = "RecordVideo.db";

	private final static int DATABASE_VERSION = 1;

	private final static String TABLE_NAME = "VideoPlay";
	public final static String VIDEO_ID = "vide0_id";

	public final static String VIDEO_NAME = "video_name";

	public final static String VIDEO_TIME = "video_time";

	public RecordVedioPlay(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql = "CREATE TABLE " + TABLE_NAME + " (" + VIDEO_ID

		+ " INTEGER primary key autoincrement, " + VIDEO_NAME + " text,"
				+ VIDEO_TIME + " long);";

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
		String str = "select * from " + TABLE_NAME + " where video_name='"
				+ name + "'";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(str, null);
		if (cursor != null && cursor.getCount() > 0) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				time = cursor.getLong(cursor.getColumnIndex(VIDEO_TIME));
			}
			return time;
		} else {
			return 0;
		}

	}

	public long insert(String videoName, long videoTime)

	{
		System.out.println("iam in the db instert");
		SQLiteDatabase db = this.getWritableDatabase();

		/* ContentValues */

		ContentValues cv = new ContentValues();
		cv.put(VIDEO_NAME, videoName);
		cv.put(VIDEO_TIME, videoTime);

		long row = db.insert(TABLE_NAME, null, cv);

		return row;

	}

	public void delete(String videoName)

	{

		SQLiteDatabase db = this.getWritableDatabase();

		String where = VIDEO_NAME + " = ?";

		String[] whereValue = { videoName };

		db.delete(TABLE_NAME, where, whereValue);

	}

	public void update(String videoName, long videoTime)

	{
		System.out.println("iam int the db update");

		SQLiteDatabase db = this.getWritableDatabase();

		String where = VIDEO_NAME + " = ?";

		String[] whereValue = { videoName };

		ContentValues cv = new ContentValues();

		cv.put(VIDEO_TIME, videoTime);
		cv.put(VIDEO_NAME, videoName);
		db.update(TABLE_NAME, cv, where, whereValue);

	}

}
