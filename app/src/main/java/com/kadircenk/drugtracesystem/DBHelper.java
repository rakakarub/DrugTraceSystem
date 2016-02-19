package com.kadircenk.drugtracesystem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

import java.nio.ByteBuffer;

public class DBHelper extends SQLiteOpenHelper {

    //DB ismi
    public static final String DB_NAME = "DrugTraceSystem.db";

    //Table isimleri
    public static final String TABLE_NAME_USER = "Users";
    public static final String TABLE_NAME_DRUG = "Drugs";
    public static final String TABLE_NAME_USESDRUG = "UsesDrug";

    //Users tablosu icin field isimleri
    public static final String TABLE_USER_ID = "userID";
    public static final String TABLE_USER_NAME = "userName";
    public static final String TABLE_USER_AGE = "userAge";
    public static final String TABLE_USER_GENDER = "userGender";
    public static final String TABLE_USER_PIC = "userPic";

    //Drugs tablosu icin field isimleri
    public static final String TABLE_DRUG_ID = "drugID";
    public static final String TABLE_DRUG_NAME = "drugName";
    public static final String TABLE_DRUG_SKT = "drugSKT";
    public static final String TABLE_DRUG_PRICE = "drugPrice";

    //UsesDrug tablosu icin field isimleri
    public static final String TABLE_USESDRUG_DRUGID = "drugID";
    public static final String TABLE_USESDRUG_USERID = "userID";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Users tablosu
        db.execSQL(
                "create table "
                        + TABLE_NAME_USER +
                        "("
                        + TABLE_USER_ID + " integer primary key autoincrement,"
                        + TABLE_USER_NAME + " text,"
                        + TABLE_USER_AGE + " integer,"
                        + TABLE_USER_GENDER + " text,"
                        + TABLE_USER_PIC + " blob)"
        );

        //Drugs tablosu
        db.execSQL(
                "create table "
                        + TABLE_NAME_DRUG +
                        "("
                        + TABLE_DRUG_ID + " integer primary key autoincrement,"
                        + TABLE_DRUG_NAME + " text,"
                        + TABLE_DRUG_SKT + " text,"
                        + TABLE_DRUG_PRICE + " real)"
        );

        //UsesDrug tablosu
        db.execSQL(
                "create table "
                        + TABLE_NAME_USESDRUG +
                        "("
                        + TABLE_USESDRUG_USERID + " integer default 0 on delete set default,"
                        + TABLE_USESDRUG_DRUGID + " integer on delete cascade,"
                        + "primary key (" + TABLE_USESDRUG_USERID + ", " + TABLE_USESDRUG_DRUGID + ") "
                        + "foreign key (" + TABLE_USESDRUG_USERID + ") "
                        + "references " + TABLE_NAME_USER + "(" + TABLE_USER_ID + ") "
                        + "foreign key (" + TABLE_USESDRUG_DRUGID + ") "
                        + "references " + TABLE_NAME_DRUG + "(" + TABLE_DRUG_ID + ") " + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_NAME_USER);
        db.execSQL("drop table if exists " + TABLE_NAME_DRUG);
        db.execSQL("drop table if exists " + TABLE_NAME_USESDRUG);
        onCreate(db);
    }

    public int insertUser(String userName, int userAge, String userGender, Bitmap userPic) {
        //Convert Bitmap to byte[] to insert into DB
        int pic_bytes = userPic.getByteCount();
        ByteBuffer buffer = ByteBuffer.allocate(pic_bytes); //Create a new buffer
        userPic.copyPixelsToBuffer(buffer); //Move the byte data to the buffer
        byte[] pic_array = buffer.array(); //Get the underlying array containing the data.

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_USER_NAME, userName);
        contentValues.put(TABLE_USER_AGE, userAge);
        contentValues.put(TABLE_USER_GENDER, userGender);
        contentValues.put(TABLE_USER_PIC, pic_array);
        return (int) db.insert(TABLE_NAME_USER, null, contentValues); // inserted row'un _id'sini dönüyor
    }

    public int insertDrug(String drugName, String drugSKT, float drugPrice) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_DRUG_NAME, drugName);
        contentValues.put(TABLE_DRUG_SKT, drugSKT);
        contentValues.put(TABLE_DRUG_PRICE, drugPrice);
        return (int) db.insert(TABLE_NAME_DRUG, null, contentValues); // inserted row'un _id'sini dönüyor
    }

    public int insertUsesDrug(int userID, int drugID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_USESDRUG_USERID, userID);
        contentValues.put(TABLE_USESDRUG_DRUGID, drugID);
        return (int) db.insert(TABLE_NAME_USESDRUG, null, contentValues); // inserted row'un _id'sini dönüyor
    }

    public Cursor getUser(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("select * from " + TABLE_NAME_USER + " where " + TABLE_USER_ID + "=" + id, null);
    }

    public Cursor getDrug(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("select * from " + TABLE_NAME_DRUG + " where " + TABLE_DRUG_ID + "=" + id, null);
    }

    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("select * from " + TABLE_NAME_USER, null);
    }

    public Cursor getAllDrugs() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("select * from " + TABLE_NAME_DRUG, null);
    }

    public int getNumberOfRowsUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME_USER);
    }

    public int getNumberOfRowsDrugs() {
        SQLiteDatabase db = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME_DRUG);
    }

    public boolean updateUser(Integer userID, String userName, int userAge, String userGender, Bitmap userPic) {
        //Convert Bitmap to byte[] to insert into DB
        int pic_bytes = userPic.getByteCount();
        ByteBuffer buffer = ByteBuffer.allocate(pic_bytes); //Create a new buffer
        userPic.copyPixelsToBuffer(buffer); //Move the byte data to the buffer
        byte[] pic_array = buffer.array(); //Get the underlying array containing the data.

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_USER_NAME, userName);
        contentValues.put(TABLE_USER_AGE, userAge);
        contentValues.put(TABLE_USER_GENDER, userGender);
        contentValues.put(TABLE_USER_PIC, pic_array);
        db.update(TABLE_NAME_USER, contentValues, TABLE_USER_ID + " = ? ", new String[]{Integer.toString(userID)});
        return true;
    }

    public boolean updateDrug(Integer drugID, String drugName, String drugSKT, float drugPrice) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_DRUG_NAME, drugName);
        contentValues.put(TABLE_DRUG_SKT, drugSKT);
        contentValues.put(TABLE_DRUG_PRICE, drugPrice);
        db.update(TABLE_NAME_DRUG, contentValues, TABLE_DRUG_ID + " = ? ", new String[]{Integer.toString(drugID)});
        return true;
    }

    public boolean updateUsesDrugSetDrug(Integer reqUserID, Integer setDrugID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_USESDRUG_USERID, reqUserID);
        contentValues.put(TABLE_USESDRUG_DRUGID, setDrugID);
        db.update(TABLE_NAME_USESDRUG, contentValues, TABLE_USESDRUG_USERID + " = ? ", new String[]{Integer.toString(reqUserID)});
        return true;
    }

    public boolean updateUsesDrugSetUser(Integer setUserID, Integer reqDrugID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_USESDRUG_USERID, setUserID);
        contentValues.put(TABLE_USESDRUG_DRUGID, reqDrugID);
        db.update(TABLE_NAME_USESDRUG, contentValues, TABLE_USESDRUG_DRUGID + " = ? ", new String[]{Integer.toString(setUserID)});
        return true;
    }

    public Integer deleteUser(Integer userID) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME_USER, "userID = ? ", new String[]{Integer.toString(userID)});
    }

    public Integer deleteDrug(Integer drugID) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME_DRUG, "drugID = ? ", new String[]{Integer.toString(drugID)});
    }

    public Integer deleteUsesDrug(Integer userID, Integer drugID) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME_USESDRUG, "userID = ? and drugID = ?", new String[]{Integer.toString(userID), Integer.toString(drugID)});
    }
}
