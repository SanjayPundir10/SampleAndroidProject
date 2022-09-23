package com.atharvakale.facerecognition;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "faceData.db";
    private static final String tableLogs = "tableLogs";
    private static final String id = "id";
    private static final String empId = "empId";
    private static final String empName = "empName";
    private static final String dateTime = "dateTime";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE IF NOT EXISTS " + tableLogs + "(id INTEGER PRIMARY KEY autoincrement,empId TEXT,empName TEXT,dateTime TEXT);";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + tableLogs);
        onCreate(db);
    }

    public void insertData(String id, String name) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd, HH:mm:ss", Locale.getDefault());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(empId, id);
        contentValues.put(empName, name);
        contentValues.put(dateTime, sdf.format(new Date()));
        db.insert(tableLogs, null, contentValues);
        db.close();
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("select * from  TABLE_QUESTIONS", null);
    }

    public Integer getFaceById(String eId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mCursor = db.rawQuery("select count(*) from  tableLogs where empId =?", new String[]{eId});
        mCursor.moveToFirst();
        Integer count = mCursor.getInt(0);
        mCursor.close();
        return count;
    }

    public Integer deleteAllData(String examId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(tableLogs, "ExamId = ?", new String[]{examId});
    }

}