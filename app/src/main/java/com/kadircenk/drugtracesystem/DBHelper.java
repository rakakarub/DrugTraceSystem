package com.kadircenk.drugtracesystem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by kadircenk on 17.02.2016.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "DrugTraceSystem.db";
    public static final String DB_TABLE_NAME = "all_data";
    public static final String DB_COLUMN_ID = "id";
    public static final String DB_COLUMN_USER = "user";
    public static final String DB_COLUMN_ILAC_ISMI = "ilac_ismi";
    public static final String DB_COLUMN_ILAC_SKT = "ilac_skt";
    public static final String DB_COLUMN_ILAC_FIYAT = "ilac_fiyat";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table all_data" +
                        "(id integer primary key autoincrement, user text, ilac_ismi text, ilac_skt text, ilac_fiyat text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS all_data");
        onCreate(db);
    }

    public int insertData(String user, String ilac_ismi, String ilac_skt, String ilac_fiyat) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("user", user);
        contentValues.put("ilac_ismi", ilac_ismi);
        contentValues.put("ilac_skt", ilac_skt);
        contentValues.put("ilac_fiyat", ilac_fiyat);
        return (int) db.insert("all_data", null, contentValues); // inserted row'un _id'sini dönüyor
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("select * from all_data where id=" + id + "", null);
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("select * from all_data", null);
    }

    public int getNumberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, DB_TABLE_NAME);
    }

    public boolean updateData(Integer id, String user, String ilac_ismi, String ilac_skt, String ilac_fiyat) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("user", user);
        contentValues.put("ilac_ismi", ilac_ismi);
        contentValues.put("ilac_skt", ilac_skt);
        contentValues.put("ilac_fiyat", ilac_fiyat);
        db.update("all_data", contentValues, "id = ? ", new String[]{Integer.toString(id)});
        return true;
    }

    public Integer deleteData(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("all_data",
                "id = ? ",
                new String[]{Integer.toString(id)});
    }

    public ArrayList<String> getAllUser() {
        ArrayList<String> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from all_data", null);

        while (res.moveToNext())
            array_list.add(res.getString(res.getColumnIndex(DB_COLUMN_USER)));

        return array_list;
    }

    public ArrayList<String> getAllIlacName() {
        ArrayList<String> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from all_data", null);

        while (res.moveToNext())
            array_list.add(res.getString(res.getColumnIndex(DB_COLUMN_ILAC_ISMI)));

        return array_list;
    }

    public ArrayList<String> getAllIlacSKT() {
        ArrayList<String> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from all_data", null);

        while (res.moveToNext())
            array_list.add(res.getString(res.getColumnIndex(DB_COLUMN_ILAC_SKT)));

        return array_list;
    }

    public ArrayList<String> getAllIlacFiyat() {
        ArrayList<String> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from all_data", null);

        while (res.moveToNext())
            array_list.add(res.getString(res.getColumnIndex(DB_COLUMN_ILAC_FIYAT)));

        return array_list;
    }
}
